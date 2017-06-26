package ua.com.vendetta8247.lollaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FriendListActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    RecyclerView recyclerViewFriends;
    FriendListAdapter friendsAdapter;
    ImageView mySummonerIcon;
    TextView mySummonerName, currentlyPlaying;
    TextView friendsHeader;

    ImageView drawerButton;
    ImageView addFriendButton;
    DrawerLayout drawer;

    StaticDataLoader dataLoader;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        database = new DBHelper(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerButton = (ImageView) findViewById(R.id.drawer_button);

        mySummonerIcon = (ImageView) findViewById(R.id.mySummonerIcon);
        mySummonerName = (TextView) findViewById(R.id.mySummonerName);
        currentlyPlaying = (TextView) findViewById(R.id.currentlyPlaying);

        friendsHeader = (TextView) findViewById(R.id.friendsHeader);

        addFriendButton = (ImageView) findViewById(R.id.addfriendButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CurrentGameActivity.class);
                Cursor mySummoner = database.getMySummoner();
                System.out.println("MY SUMMONER COUNT " + mySummoner.getCount());
                mySummoner.moveToFirst();
                intent.putExtra("summonerId", mySummoner.getString(mySummoner.getColumnIndex("id")));
                startActivity(intent);
            }
        };

        mySummonerName.setOnClickListener(listener);
        mySummonerIcon.setOnClickListener(listener);
        currentlyPlaying.setOnClickListener(listener);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                builder.setTitle( Html.fromHtml("<font color='#f0e6d2'>Summoner name</font>"));
                final LinearLayout ll = new LinearLayout(v.getContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                final EditText input = new EditText(v.getContext());
                TableLayout.LayoutParams params = new TableLayout.LayoutParams();
                params.setMargins(50, 45, 50, 25);
                input.setLayoutParams(params);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(0xffeae0cd);
                ll.addView(input);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new AsyncSummonerInfoLoader(URLEncoder.encode(input.getText().toString(), "UTF-8").replace("+", "%20"), false).execute();
                            System.out.println(URLEncoder.encode(input.getText().toString(), "UTF-8").replace("+", "%20"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Typeface beaufort = Typeface.createFromAsset(getAssets(), "beaufortforlol-bold.otf");
        friendsHeader.setTypeface(beaufort);

        dataLoader = new StaticDataLoader();

        prefs = getSharedPreferences("ua.com.vendetta8247.lollaner", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            System.out.println("FIRST RUN!!!");
            prefs.edit().putBoolean("firstrun", false).commit();
        }
        dataLoader.loadVersion(prefs, database, getApplicationContext());


        System.out.println("version = " +  prefs.getString("version", "0"));


        recyclerViewFriends = (RecyclerView) findViewById(R.id.recViewFriends);

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        friendsAdapter = new FriendListAdapter();
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriends.setAdapter(friendsAdapter);

        Cursor summoners = database.getAllSummoners();
        System.out.println("SUMMONERS COUNT + " + summoners.getCount());
        if(summoners.getCount()>0) {
            summoners.moveToFirst();
            while (true) {

                Bitmap imageBitmap= BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + "/" + summoners.getString(summoners.getColumnIndex("icon")) + ".png");
                Bitmap croppedBitmap=Bitmap.createBitmap(imageBitmap,0,0,128,128);

                Bitmap end = Utils.getRoundedCornerBitmap(croppedBitmap, 5000);
                end = Utils.addBorderToRoundedBitmap(end, 5000, 5, 0xffb99f42);



                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), end);

                roundedBitmapDrawable.setCornerRadius(5000.0f);
                roundedBitmapDrawable.setAntiAlias(true);

                friendsAdapter.addCard(new FriendListCard(roundedBitmapDrawable, summoners.getString(summoners.getColumnIndex("name"))));
                friendsAdapter.notifyDataSetChanged();
                System.out.println(summoners.getString(summoners.getColumnIndex("name")) + "  " + summoners.getInt(summoners.getColumnIndex("status")));

                if(summoners.isLast())
                    break;
                else
                summoners.moveToNext();
            }
        }
        Cursor mySummoner = database.getMySummoner();
        mySummoner.moveToFirst();
        if(mySummoner.getCount()>0)
        mySummonerName.setText(mySummoner.getString(summoners.getColumnIndex("name")));

    }


    class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder>
    {
        List<FriendListCard> cardList;

        public FriendListAdapter()
        {
            cardList = new ArrayList<>();
        }


        public void addCard(FriendListCard card)
        {
            cardList.add(card);
        }

        public void editCard(int position, FriendListCard card)
        {
            cardList.get(position).image = card.image;
            cardList.get(position).summonerName = card.summonerName;
        }

        public void removeAt(int position) {

            cardList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }

        @Override
        public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.summoner_card, parent, false);
            return new FriendListViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(FriendListViewHolder holder, int position) {

            FriendListCard card = cardList.get(position);

            holder.summonerName.setText(card.summonerName);
            holder.summonerImage.setImageDrawable(card.image);
            holder.summonerNameString = card.summonerName;
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }


        class FriendListViewHolder extends RecyclerView.ViewHolder
        {
            protected ImageView summonerImage;
            protected TextView summonerName;
            protected String summonerNameString;

            int status;

            public FriendListViewHolder(final View itemView) {
                super(itemView);
                summonerImage = (ImageView) itemView.findViewById(R.id.summonerIcon);
                summonerName = (TextView) itemView.findViewById(R.id.summonerName);
                System.out.println("STATUS ON CONSTRUCTOR " + status);

                ImageView edit = (ImageView) itemView.findViewById(R.id.buttonEdit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                        builder.setTitle( Html.fromHtml("<font color='#f0e6d2'>Summoner name</font>"));
                        final LinearLayout ll = new LinearLayout(v.getContext());
                        ll.setOrientation(LinearLayout.VERTICAL);
                        final EditText input = new EditText(v.getContext());

                        TableLayout.LayoutParams params = new TableLayout.LayoutParams();
                        params.setMargins(50, 45, 50, 25);
                        input.setLayoutParams(params);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        input.setTextColor(0xffeae0cd);
                        ll.addView(input);
                        builder.setView(ll);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AsyncSummonerInfoLoader(input.getText().toString(), false).execute();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }
                });

                ImageView delete = (ImageView) itemView.findViewById(R.id.buttonDelete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(getAdapterPosition());
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), CurrentGameActivity.class);
                            Cursor summoner = database.getSummonerByName(summonerNameString);
                            System.out.println("MY SUMMONER COUNT " + summoner.getCount());
                            summoner.moveToFirst();
                            intent.putExtra("summonerId", summoner.getString(summoner.getColumnIndex("id")));
                        long lastUpdated = summoner.getLong(summoner.getColumnIndex("lastedited"));
                        long unixTime = System.currentTimeMillis() / 1000L;
                        if(unixTime-lastUpdated>60) {
                            new AsyncSummonerInfoLoader(summoner.getString(summoner.getColumnIndex(DBHelper.SUMMONER_COLUMN_NAME)), true).execute();

                        }
                        System.out.println("DIFF" + (unixTime-lastUpdated));
                        System.out.println(lastUpdated);
                            startActivity(intent);
                    }
                });
            }
        }
    }



    class AsyncSummonerInfoLoader extends AsyncTask<Void, Void, Void>
    {
        boolean force = false;
        String summonerName;
        boolean dataCorrect = false;
        JSONObject data;
        String nameToShow="", idToShow="";
        long lastEdited;
        int icon;
        int accId;

        public AsyncSummonerInfoLoader(String summonerName, boolean force)
        {
            this.summonerName = summonerName;
            this.force = force;
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url;
                if(force)
                url = new URL(CurrentGameActivity.SITENAME + "/summoner/by-name/" + summonerName + "/?force=true");
                else
                    url = new URL(CurrentGameActivity.SITENAME + "/summoner/by-name/" + summonerName);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());
                if(!data.has("name")||!data.has("id")) {
                    dataCorrect = false;
                    System.out.println("DATA INCORRECT 1");
                }
                nameToShow = data.getString("name");
                idToShow = data.getString("id");
                icon = data.getInt("icon");
                accId = data.getInt("accountid");
                lastEdited = data.getLong("lastedited");
                dataCorrect = true;

            }
            catch (IOException ex)
            {

            }
            catch(JSONException ex)
            {
                ex.printStackTrace();
                dataCorrect = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(dataCorrect) {
                Bitmap imageBitmap= BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + "/" + icon + ".png");
                Bitmap croppedBitmap=Bitmap.createBitmap(imageBitmap,0,0,128,128);

                Bitmap end = Utils.getRoundedCornerBitmap(croppedBitmap, 5000);
                end = Utils.addBorderToRoundedBitmap(end, 5000, 5, 0xffb99f42);



                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), end);

                roundedBitmapDrawable.setCornerRadius(5000.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                //profilePic.setImageBitmap(end);

                Drawable d = roundedBitmapDrawable;

                if(!force)

                {
                    friendsAdapter.addCard(new FriendListCard(d, nameToShow));
                    friendsAdapter.notifyDataSetChanged();
                }
                    database.insertSummoner(nameToShow, idToShow, 2, accId, icon, lastEdited);

            }
            else
                System.out.println("DATA INCORRECT 2");
        }
    }

    class AsyncGameChecker extends AsyncTask<Void, Void, Void>
    {

        JSONObject data;
        String summonerId;
        boolean playing = false;

        public AsyncGameChecker(String summonerId)
        {
            this.summonerId = summonerId;
            System.out.println("Async constructor ");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                System.out.println("Async doinBack");
                URL url = new URL(CurrentGameActivity.SITENAME  + "/current-game/" +  summonerId);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());
                if(data.has("participants"))
                {
                    System.out.println("no games");
                    playing = true;
                }

            }
            catch (IOException ex)
            {

            }
            catch(JSONException ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(playing)
            {
                Intent intent = new Intent(getApplicationContext(), CurrentGameActivity.class);
                intent.putExtra("summonerId", summonerId);
                startActivity(intent);
            }

        }
    }

}
