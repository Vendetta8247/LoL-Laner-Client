package ua.com.vendetta8247.lollaner;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendListFragment extends Fragment {

    RecyclerView rv;
    MyRecViewAdapter adapter;
    CardView addMySummonerButton;
    DBHelper mydb;
    String summonerName;
    String summonerId;

    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) getActivity().findViewById(R.id.recview);
        adapter = new MyRecViewAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        mydb = new DBHelper(getActivity());

        addMySummonerButton = (CardView) getActivity().findViewById(R.id.addMySummonerBtn);
        addMySummonerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Summoner name");


                final EditText input = new EditText(getActivity());


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        summonerName = input.getText().toString();
                        try {
                           summonerId = new SummonerIdGetter(summonerName).execute().get();
                            mydb.insertSummoner(summonerName, summonerId);
                            adapter.notifyDataSetChanged();
                            adapter.notifyItemInserted(adapter.getItemCount()-1);
                            addItems();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
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



addItems();


    }

    public void addItems()
    {
        ArrayList<String> summoners = mydb.getAllSummoners();
        adapter.clearList();
        for(String s : summoners)
            adapter.addItem(new PlayerCard(s));
    }


    class SummonerIdGetter extends AsyncTask<Void, Void, String>
    {
        URL url;
        JSONObject dataSummoner;
        String summonerName;
        String summonerId;

        public SummonerIdGetter(String summonerName)
        {
            this.summonerName = summonerName;
            if(this.summonerName.contains(" "))
                this.summonerName = summonerName.replace(" ", "");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                url = new URL("https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/" + summonerName + "?api_key=RGAPI-75D59888-2CBE-4ADD-82AA-8774239BAA60");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            dataSummoner = new JSONObject(json.toString());
                summonerId = dataSummoner.getJSONObject(summonerName.toLowerCase()).get("id").toString();


        } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
            return summonerId;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
        }
    }



    class MyRecViewAdapter extends RecyclerView.Adapter<MyViewHolder>
    {

        private List<PlayerCard> cardList;

        public MyRecViewAdapter()
        {
            cardList = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView;
            MyViewHolder holder;


                itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.player_card, parent, false);
                holder = new MyViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PlayerCard card = cardList.get(position);
            holder.summonerName.setText(card.summonerName);
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        public void addItem(PlayerCard card)
        {
            cardList.add(card);
            notifyItemInserted(cardList.size()-1);
        }

        public void clearList()
        {
            cardList.clear();
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView summonerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            summonerName = (TextView) itemView.findViewById(R.id.summoner_name);
            ImageView profilePic=(ImageView)itemView.findViewById(R.id.profile_icon);
            Bitmap imageBitmap= BitmapFactory.decodeResource(getResources(),  R.drawable.jhin);
            Bitmap croppedBitmap=Bitmap.createBitmap(imageBitmap,25,25,310,310);

            Bitmap end = getRoundedCornerBitmap(croppedBitmap, 5000);
            end = addBorderToRoundedBitmap(end, 5000, 15, 0xffb99f42);



            RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), croppedBitmap);

            roundedBitmapDrawable.setCornerRadius(5000.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            profilePic.setImageBitmap(end);
        }
    }

    protected Bitmap addBorderToRoundedBitmap(Bitmap srcBitmap, int cornerRadius, int borderWidth, int borderColor){
        borderWidth = borderWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth,
                srcBitmap.getHeight() + borderWidth,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                borderWidth/2,
                borderWidth/2,
                dstBitmap.getWidth() - borderWidth/2,
                dstBitmap.getHeight() - borderWidth/2
        );
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,paint);

        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        srcBitmap.recycle();
        return dstBitmap;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    class PlayerCard
    {
        String summonerName;

        public PlayerCard(String summonerName)
        {
            this.summonerName = summonerName;
        }

    }
}
