package ua.com.vendetta8247.lollaner;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.RunnableFuture;

import javax.net.ssl.HttpsURLConnection;

import static ua.com.vendetta8247.lollaner.Utils.SITENAME;

public class StaticDataLoader {
    String ver;

    public void loadVersion(SharedPreferences prefs, DBHelper database, Context context)
    {
        new StaticVersionLoader(prefs, database, context).execute();
    }

    public void loadChampions(DBHelper database, Context context)
    {
        new StaticChampionsLoader(database, context).execute();
    }

    public void loadChampionGGData (DBHelper database, Context context)
    {
        new StaticChampionGGDataLoader(database, context).execute();
    }

    public void loadSummonerIconsData(Context context)
    {
        new StaticSummonerIconDataLoader(context).execute();
    }

    private void loadSummonerIcon(String imageUrl, Context context)
    {
        new SummonerIconDownloader(context, imageUrl).execute();
    }

    private class StaticChampionsLoader extends AsyncTask<Void, Void, Void>{
        DBHelper database;
        JSONObject dataStatic, tagsStatic;
        Context context;



        public StaticChampionsLoader(DBHelper database, Context context) {

            this.database = database;
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... params) {


            URL url = null;
            try {
                url = new URL(SITENAME + "/static/champions");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                dataStatic = new JSONObject(json.toString());
                JSONArray data = dataStatic.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    int id = data.getJSONObject(i).getInt("id");
                    String key = data.getJSONObject(i).getString("key");
                    String name = data.getJSONObject(i).getString("name");
                    String title = data.getJSONObject(i).getString("title");

                    float armor = (float) data.getJSONObject(i).getDouble("armor");
                    float armorperlevel = (float) data.getJSONObject(i).getDouble("armorperlevel");
                    float attackdamage = (float) data.getJSONObject(i).getDouble("attackdamage");
                    float attackdamageperlevel = (float) data.getJSONObject(i).getDouble("attackdamageperlevel");
                    float attackrange = (float) data.getJSONObject(i).getDouble("attackrange");
                    float attackspeedoffset = (float) data.getJSONObject(i).getDouble("attackspeedoffset");
                    float attackspeedperlevel = (float) data.getJSONObject(i).getDouble("attackspeedperlevel");
                    float crit = (float) data.getJSONObject(i).getDouble("crit");
                    float critperlevel = (float) data.getJSONObject(i).getDouble("critperlevel");
                    float hp = (float) data.getJSONObject(i).getDouble("hp");
                    float hpperlevel = (float) data.getJSONObject(i).getDouble("hpperlevel");
                    float hpregen = (float) data.getJSONObject(i).getDouble("hpregen");
                    float hpregenperlevel = (float) data.getJSONObject(i).getDouble("hpregenperlevel");
                    float movespeed = (float) data.getJSONObject(i).getDouble("movespeed");
                    float mp = (float) data.getJSONObject(i).getDouble("mp");
                    float mpperlevel = (float) data.getJSONObject(i).getDouble("mpperlevel");
                    float mpregen = (float) data.getJSONObject(i).getDouble("mpregen");
                    float mpregenperlevel = (float) data.getJSONObject(i).getDouble("mpregenperlevel");
                    float spellblock = (float) data.getJSONObject(i).getDouble("spellblock");
                    float spellblockperlevel = (float) data.getJSONObject(i).getDouble("spellblockperlevel");

                    String tags = data.getJSONObject(i).getString("tags");
                    String imageGroup = data.getJSONObject(i).getString("imagegroup");
                    String imageUrl = data.getJSONObject(i).getString("imageurl");

                    database.insertChampion(id, name, key, title, armor, armorperlevel, attackdamage, attackdamageperlevel, attackrange, attackspeedoffset, attackspeedperlevel,
                            crit, critperlevel, hp, hpperlevel, hpregen, hpregenperlevel, movespeed, mp, mpperlevel, mpregen, mpregenperlevel, spellblock, spellblockperlevel, tags,
                            imageGroup, imageUrl);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            new ChampionImageDownloader(context, database).execute();
        }
    }
    private class StaticChampionGGDataLoader extends AsyncTask<Void, Void, Void>
    {
        JSONArray dataStatic;
        DBHelper database;
        Context context;

        public StaticChampionGGDataLoader(DBHelper database, Context context)
        {
            this.database = database;
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void... params) {
            database.clearChampionGGData();
            URL url = null;
            try {
                System.out.println("STARTED CHAMPION.GG");
                url = new URL(SITENAME + "/static/championgg");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                System.out.println(json.toString());
                dataStatic = new JSONArray(json.toString());
                for(int i = 0; i<dataStatic.length(); i++)
                {
                    JSONObject champion = dataStatic.getJSONObject(i);
                    String key = champion.getString("key");
                    String role = champion.getString("role");
                    String name = champion.getString("name");

                    JSONObject general = champion.getJSONObject("general");
                    int overallPositionChange = general.getInt("overallPositionChange");
                    int overallPosition = general.getInt("overallPosition");
                    int goldEarned = general.getInt("goldEarned");


                    float neutralMinionsKilledEnemyJungle = (float)general.getDouble("neutralMinionsKilledEnemyJungle");
                    float neutralMinionsKilledTeamJungle = (float)general.     getDouble("neutralMinionsKilledTeamJungle");
                    float minionsKilled = (float) general.                      getDouble("minionsKilled");
                    float largestKillingSpree = (float) general.                getDouble("largestKillingSpree");


                    int totalHeal = general.                    getInt("totalHeal");
                    int totalDamageTaken = general.             getInt("totalDamageTaken");
                    int totalDamageDealtToChampions = general.  getInt("totalDamageDealtToChampions");

                    float assists =         (float)general.getDouble("assists");
                    float deaths =          (float)general.getDouble("deaths");
                    float kills =           (float)general.getDouble("kills");
                    float experience =      (float)general.getDouble("experience");
                    float banRate =         (float)general.getDouble("banRate");
                    float playPercent =     (float)general.getDouble("playPercent");
                    float winPercent =      (float)general.getDouble("winPercent");

                    database.instertChampionGGData(key,role,name,overallPositionChange,overallPosition,goldEarned,neutralMinionsKilledEnemyJungle,neutralMinionsKilledTeamJungle,minionsKilled,largestKillingSpree,
                            totalHeal,totalDamageTaken,totalDamageDealtToChampions,assists,deaths,kills,experience, banRate,playPercent,winPercent);
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
    }

    private class StaticSummonerIconDataLoader extends AsyncTask<Void,Void,Void>
    {
        Context context;
        JSONObject dataStatic;
        List<String> urls;

        public StaticSummonerIconDataLoader(Context context)
        {
            this.context = context;
            urls = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                System.out.println("STARTED SUMMONER ICONS");
                url = new URL(SITENAME + "/static/summonericons");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                System.out.println(json.toString());
                dataStatic = new JSONObject(json.toString());
                JSONObject data = dataStatic.getJSONObject("data");

                for(int i = 0; i<data.names().length(); i++)
                {
                    JSONObject obj = data.getJSONObject(data.names().getString(i));
                    JSONObject image = obj.getJSONObject("image");
                    String imageUrl = image.getString("full");

                    urls.add(imageUrl);

                    //loadSummonerIcon(imageUrl, context);

                }

                new ConcurrentRequestSource(ver, new File(context.getFilesDir() + "/"), urls).download(new ConcurrentRequestSource.Callback()
                {
                    @Override
                    public void onEach(File image) {
                        //    System.out.println(image.getName() + " downloaded");
                    }

                    @Override
                    public void onDone(File imagesDir) {
                        System.out.println("done all");
                    }
                });

            }catch (MalformedURLException ex)
            {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("ENDED SUMMONER IMAGES");
        }
    }

    private class StaticVersionLoader extends AsyncTask<Void, Void, Void>{
        JSONObject dataStatic;
        String version = "";
        SharedPreferences prefs;
        DBHelper database;
        Context context;

        public StaticVersionLoader(SharedPreferences prefs, DBHelper database, Context context) {
            this.prefs = prefs;
            this.database = database;
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... params) {


            URL url = null;
            try {
                url = new URL(SITENAME + "/static/version");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                dataStatic = new JSONObject(json.toString());
                version = dataStatic.getString("version");

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
            if(!prefs.getString("version", "0").equals(version)) {
                ver = version;
                loadChampions(database, context);

                //loadItms(database);
                //loadMasteries(database);
                prefs.edit().putString("version", version).commit();
            }
            ver = version;
            loadSummonerIconsData(context);
            loadChampionGGData(database,context);
        }
    }

    private class ChampionImageDownloader extends AsyncTask<Void, Void, Void>
    {
        Context context;
        DBHelper database;

        public ChampionImageDownloader(Context context, DBHelper database)
        {
            this.context = context;
            this.database = database;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                System.out.println("Started champions");
                Cursor champions = database.getAllChampions();
                champions.moveToFirst();

                while(true) {


                    String path = "http://ddragon.leagueoflegends.com/cdn/" + ver + "/img/" + champions.getString(champions.getColumnIndex("imagegroup")) + "/" + champions.getString(champions.getColumnIndex("imageurl")) ;

                    URL url = new URL(path);

                    URLConnection ucon = url.openConnection();
                    ucon.setReadTimeout(5000);
                    ucon.setConnectTimeout(10000);

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

                    File file = new File(context.getFilesDir() + "/" + champions.getString(champions.getColumnIndex("imageurl")).toLowerCase());

                    if (file.exists()) {
                        file.delete();
                    }
                    System.out.println(file.createNewFile());
                    System.out.println("File created");



                    FileOutputStream outStream = new FileOutputStream(file);
                    byte[] buff = new byte[5 * 1024];

                    int len;
                    while ((len = inStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }

                    outStream.flush();
                    outStream.close();
                    inStream.close();

                    if(champions.isLast())
                        break;
                    else
                        champions.moveToNext();
                }
                System.out.println("Ended champions");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }


            return null;
        }

    }
    private class SummonerIconDownloader extends AsyncTask<Void, Void, Void>
    {
        Context context;
        String imageName;

        public SummonerIconDownloader(Context context, String imageName)
        {
            this.context = context;
            this.imageName = imageName;
        }

        @Override
        protected Void doInBackground(Void... params) {
            File file = new File(context.getFilesDir() + "/" + imageName);

            if (file.exists()) {
                return null;
            }
            try
            {
                System.out.println("Started summonerIcons");
                    String path = "http://ddragon.leagueoflegends.com/cdn/" + ver + "/img/profileicon/" + imageName;

                    URL url = new URL(path);

                    URLConnection ucon = url.openConnection();
                    ucon.setReadTimeout(5000);
                    ucon.setConnectTimeout(10000);

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);


                    System.out.println(file.createNewFile());
                    System.out.println("File created");



                    FileOutputStream outStream = new FileOutputStream(file);
                    byte[] buff = new byte[5 * 1024];

                    int len;
                    while ((len = inStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }

                    outStream.flush();
                    outStream.close();
                    inStream.close();

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }


            return null;
        }

    }

}
