package ua.com.vendetta8247.lollaner;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Y500 on 24.12.2016.
 */

public class SummonerInfoLoader {

    public static void loadRankedStats(int summonerId, String season)
    {
        new RankedStatsLoader(summonerId, season).execute();
    }

    static private class RankedStatsLoader extends AsyncTask<Void, Void, Void>
    {
        int summonerId;
        String season;
        JSONObject seasonData;
        public RankedStatsLoader(int summonerId, String season)
        {
            this.summonerId = summonerId;
            this.season = season;
        }

        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;
            try {
                url = new URL("https://secure-beyond-82184.herokuapp.com/summoner/" + summonerId + "/rankedStats?season=" + season);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                seasonData = new JSONObject(json.toString());
                JSONArray champions = seasonData.getJSONArray("champions");
                for(int i = 0; i< champions.length(); i++)
                {
                    if(champions.getJSONObject(i).getInt("id") == 202)
                    {
                        System.out.println(champions.get(i));
                    }
                }
                System.out.println(seasonData);

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
    }
            return null;
        }
    }

}
