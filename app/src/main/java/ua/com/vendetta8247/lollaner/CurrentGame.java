package ua.com.vendetta8247.lollaner;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ua.com.vendetta8247.lollaner.DBHelper.CHAMPIONS_COLUMN_CHAMPION;


public class CurrentGame extends Fragment {
    TextView tv, tv2;
    DBHelper mydb;
    List<JSONObject> team1, team2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View v = inflater.inflate(R.layout.fragment_current_game, container, false);

        mydb = new DBHelper(container.getContext());
        return inflater.inflate(R.layout.fragment_current_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = (TextView) getActivity().findViewById(R.id.team1);
        tv2 = (TextView) getActivity().findViewById(R.id.team2);
        new InitLoader().execute();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    class InitLoader extends AsyncTask<Void, Void, Void>
    {
        JSONArray dataChampionGG;
        JSONObject dataRiot;
        JSONObject currentGameData;
        String team1str="", team2str="";



        @Override
        protected Void doInBackground(Void... params) {
            URL url;

            List<ImageView> summonerImages = new ArrayList<>();
            try {
// champion.gg data
                url = new URL("http://api.champion.gg/champion?api_key=9500ef4bb169271b0763c3075be49d85");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                dataChampionGG = new JSONArray(json.toString());

//riot data

                url = new URL("https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion?api_key=RGAPI-75D59888-2CBE-4ADD-82AA-8774239BAA60");
                connection = (HttpURLConnection)url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                json = new StringBuffer(1024);
                tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                dataRiot = new JSONObject(json.toString());

//current game data for OloloUpyachka
                team1 = new ArrayList<>();
                team2 = new ArrayList<>();

                url = new URL("https://euw.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/EUW1/51469520?api_key=RGAPI-75D59888-2CBE-4ADD-82AA-8774239BAA60");
                connection = (HttpURLConnection)url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                json = new StringBuffer(1024);
                tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();




                currentGameData = new JSONObject(json.toString());

                JSONArray participants = currentGameData.getJSONArray("participants");



                for(int i = 0; i<participants.length(); i++) {
                    if (participants.getJSONObject(i).get("teamId").toString().equals("100")) {


                        //String uri = "drawable/" + Champion.findNameByID(champions, Integer.parseInt(array.getJSONObject(i).get("championId").toString())).toLowerCase();
                        //int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        //final Drawable image = getResources().getDrawable(imageResource);

                        team1.add(participants.getJSONObject(i));
                        System.out.println();

                        //team1Text += array.getJSONObject(i).get("summonerName").toString() + " " + SummonerSpell.findNameByID(summonerSpells, Integer.parseInt(array.getJSONObject(i).get("spell1Id").toString())) + " " + SummonerSpell.findNameByID(summonerSpells, Integer.parseInt(array.getJSONObject(i).get("spell2Id").toString())) + "\n";
                    } else {
                        team2.add(participants.getJSONObject(i));
                    }
                }

                for(int i = 0; i<team1.size(); i++)
                {
                    try {
                        String champId = team1.get(i).get("championId").toString();
                        ArrayList<String> lst = mydb.getChampionById(Integer.parseInt(champId));

                        for(String str : lst) {
                            team1str = team1str + str +"\n";

                        }
                        // tv.setText(tv.getText() + "\n" + );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for(int i = 0; i<team2.size(); i++)
                {
                    try {
                        String champId = team2.get(i).get("championId").toString();
                        ArrayList<String> lst = mydb.getChampionById(Integer.parseInt(champId));

                        for(String str : lst) {
                            team2str = team2str + str + "\n" ;
                        }
                        // tv.setText(tv.getText() + "\n" + );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mydb.clearTable();
                try {
                    JSONObject championData = dataRiot.getJSONObject("data");
                    Iterator<String> itr = championData.keys();

                    while(itr.hasNext()){
                        String elem = itr.next();
                        String name = championData.getJSONObject(elem).get("name").toString();
                        String id = championData.getJSONObject(elem).get("id").toString();
                        mydb.insertContact(elem, name, id);
                        //     System.out.println(elem + "  id  " + id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //for(String s :mydb.getAllCotacts())
               // tv.setText(tv.getText() + s + "\n");

            tv.setText(team1str);
            tv2.setText(team2str);


            //setText(tv.getText() + s + "\n");



        }
    }
}
