package ua.com.vendetta8247.lollaner;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Y500 on 19.11.2016.
 */

public class StaticDataLoader extends AsyncTask<Void, Void, Void> {

    DBHelper database;
    JSONObject dataStatic, tagsStatic;


    public StaticDataLoader(DBHelper database)
    {
        this.database = database;
    }


    @Override
    protected Void doInBackground(Void... params) {


        URL url = null;
        try {
            url = new URL("https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion?champData=stats&api_key=RGAPI-75D59888-2CBE-4ADD-82AA-8774239BAA60");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            dataStatic = new JSONObject(json.toString());
            JSONObject data = dataStatic.getJSONObject("data");



            url = new URL("https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion?champData=tags&api_key=RGAPI-75D59888-2CBE-4ADD-82AA-8774239BAA60");
            conn = (HttpsURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            json = new StringBuffer(1024);
            tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            tagsStatic = new JSONObject(json.toString());
            JSONObject tagsData = tagsStatic.getJSONObject("data");

            System.out.println(data.length());
            JSONArray names = data.names();
            for(int i = 0; i< names.length(); i++)
            {
                System.out.println(names.get(i).toString());
                JSONObject stats = data.getJSONObject(names.get(i).toString()).getJSONObject("stats");
                JSONArray tags = tagsData.getJSONObject(names.get(i).toString()).getJSONArray("tags");

                int id = data.getJSONObject(names.get(i).toString()).getInt("id");
                String key =  data.getJSONObject(names.get(i).toString()).getString("key");
                String name =  data.getJSONObject(names.get(i).toString()).getString("name");
                String title =  data.getJSONObject(names.get(i).toString()).getString("title");

                float armor =  (float) stats.getDouble("armor");
                float armorperlevel = (float) stats.getDouble("armorperlevel");
                float attackdamage =  (float) stats.getDouble("attackdamage");
                float attackdamageperlevel =  (float) stats.getDouble("attackdamageperlevel");
                float attackrange = (float) stats.getDouble("attackrange");
                float attackspeedoffset = (float) stats.getDouble("attackspeedoffset");
                float attackspeedperlevel = (float) stats.getDouble("attackspeedperlevel");
                float crit = (float) stats.getDouble("crit");
                float critperlevel = (float) stats.getDouble("critperlevel");
                float hp = (float) stats.getDouble("hp");
                float hpperlevel = (float) stats.getDouble("hpperlevel");
                float hpregen = (float) stats.getDouble("hpregen");
                float hpregenperlevel = (float) stats.getDouble("hpregenperlevel");
                float movespeed = (float) stats.getDouble("movespeed");
                float mp = (float) stats.getDouble("mp");
                float mpperlevel = (float) stats.getDouble("mpperlevel");
                float mpregen = (float) stats.getDouble("mpregen");
                float mpregenperlevel = (float) stats.getDouble("mpregenperlevel");
                float spellblock = (float) stats.getDouble("spellblock");
                float spellblockperlevel = (float) stats.getDouble("spellblockperlevel");

                String tagsString = "";

                for(int j = 0; j<tags.length(); j++)
                {
                    if(j!=0)
                    tagsString = new String(tagsString + ", " + tags.getString(j));
                    else tagsString = new String(tags.getString(j));
                }

                database.insertChampion(id,name, key, title, armor, armorperlevel, attackdamage, attackdamageperlevel, attackrange, attackspeedoffset, attackspeedperlevel,
                        crit, critperlevel, hp, hpperlevel, hpregen,hpregenperlevel, movespeed, mp, mpperlevel, mpregen, mpregenperlevel, spellblock, spellblockperlevel, tagsString);
            }

            System.out.println("DATA STATIC " + dataStatic.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
