package ua.com.vendetta8247.lollaner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FriendListActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    RecyclerView recyclerViewMySummoner, recyclerViewFriends;
    FriendListAdapter mySummonerAdapter, friendsAdapter;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        database = new DBHelper(this);

        prefs = getSharedPreferences("ua.com.vendetta8247.lollaner", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            System.out.println("FIRST RUN!!!");
            new StaticDataLoader(database).execute();
            prefs.edit().putBoolean("firstrun", false).commit();
        }



        recyclerViewMySummoner = (RecyclerView) findViewById(R.id.recViewMySummoner);
        recyclerViewFriends = (RecyclerView) findViewById(R.id.recViewFriends);

        mySummonerAdapter = new FriendListAdapter();
        recyclerViewMySummoner.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMySummoner.setAdapter(mySummonerAdapter);
        mySummonerAdapter.addCard(new FriendListCard(getResources().getDrawable(R.drawable.plus), "Add Summoner", 0));

        friendsAdapter = new FriendListAdapter();
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriends.setAdapter(friendsAdapter);
        friendsAdapter.addCard(new FriendListCard(getResources().getDrawable(R.drawable.plus), "Add Summoner", 0));

        Cursor mySummoner = database.getMySummoner();
        System.out.println("MY SUMMONER COUNT " + mySummoner.getCount());
        if(mySummoner.getCount() > 0)
        {
            mySummoner.moveToFirst();
            mySummonerAdapter.editCard(0,new FriendListCard(getResources().getDrawable(R.drawable.aatrox), mySummoner.getString(mySummoner.getColumnIndex("name")), 1));
            new AsyncGameChecker(mySummoner.getString(mySummoner.getColumnIndex("id"))).execute();
        }
    }


    class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder>
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
            cardList.get(position).status = card.status;
            cardList.get(position).image = card.image;
            cardList.get(position).summonerName = card.summonerName;
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

            holder.status = card.status;

        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }
    }

    class FriendListViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView summonerImage;
        protected TextView summonerName;

        int status;

        public FriendListViewHolder(View itemView) {
            super(itemView);
            summonerImage = (ImageView) itemView.findViewById(R.id.summonerIcon);
            summonerName = (TextView) itemView.findViewById(R.id.summonerName);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status == 0)
                        {
                            //int inputSelection = 0;
                            //final CharSequence[] items = { " HDMI IN ", " AV IN" };
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                            builder.setTitle( Html.fromHtml("<font color='#f0e6d2'>Summoner name</font>"));

//                            builder.setSingleChoiceItems(items,inputSelection,
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int item) {
//                                            //inputSelection = item;
//                                        }
//                                    });
//

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
                                    new AsyncSummonerInfoLoader(input.getText().toString(),0).execute();

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
                        else if (status == 1) {
                            Intent intent = new Intent(v.getContext(), CurrentGameActivity.class);
                            Cursor mySummoner = database.getMySummoner();
                            System.out.println("MY SUMMONER COUNT " + mySummoner.getCount());
                                mySummoner.moveToFirst();
                            intent.putExtra("summonerId", mySummoner.getString(mySummoner.getColumnIndex("id")));
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(v.getContext(), CurrentGameActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        }
    }

    class AsyncSummonerInfoLoader extends AsyncTask<Void, Void, Void>
    {
        String summonerName;
        boolean dataCorrect = false;
        JSONObject data;
        String nameToShow="", idToShow="";
        int status = -1;

        public AsyncSummonerInfoLoader(String summonerName, int status)
        {
            this.summonerName = summonerName;
            this.status = status;
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(CurrentGameActivity.SITENAME + "/summoner/by-name/" + summonerName);
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
                dataCorrect = true;

            }
            catch (IOException ex)
            {

            }
            catch(JSONException ex)
            {
                dataCorrect = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(dataCorrect) {
                if (status == 0) {


                    Bitmap imageBitmap= BitmapFactory.decodeResource(getResources(),  R.drawable.ahri);
                    Bitmap croppedBitmap=Bitmap.createBitmap(imageBitmap,25,25,310,310);

                    Bitmap end = Utils.getRoundedCornerBitmap(croppedBitmap, 5000);
                    end = Utils.addBorderToRoundedBitmap(end, 5000, 15, 0xffb99f42);



                    RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), end);

                    roundedBitmapDrawable.setCornerRadius(5000.0f);
                    roundedBitmapDrawable.setAntiAlias(true);
                    //profilePic.setImageBitmap(end);

                    Drawable d = roundedBitmapDrawable;
                    mySummonerAdapter.editCard(0, new FriendListCard(d, nameToShow, 1));
                    database.insertSummoner(nameToShow, idToShow, 1);
                    mySummonerAdapter.notifyDataSetChanged();
                } else
                    friendsAdapter.editCard(0, new FriendListCard(getResources().getDrawable(R.drawable.jhin), nameToShow, 2));
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
