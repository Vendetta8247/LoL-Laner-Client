package ua.com.vendetta8247.lollaner;
//
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Iterator;
import java.util.List;

public class MainPage extends AppCompatActivity {
    TextView tv;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        tv = (TextView) findViewById(R.id.hello);
        mydb = new DBHelper(this);
        new InitLoader().execute();
    }

    class InitLoader extends AsyncTask<Void, Void, Void>
    {
        JSONArray dataChampionGG;
        JSONObject dataRiot;

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
            mydb.clearTable();
            try {
                JSONObject championData = dataRiot.getJSONObject("data");
                Iterator<String> itr = championData.keys();

                while(itr.hasNext()){
                    String elem = itr.next();
                    String name = championData.getJSONObject(elem).get("name").toString();
                    String id = championData.getJSONObject(elem).get("id").toString();
                    mydb.insertContact(elem, name, id);
                    System.out.println(elem + "  id  " + id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(String s :mydb.getAllCotacts())
           tv.setText(tv.getText() + s + "\n");


        }
    }

}
