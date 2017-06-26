package ua.com.vendetta8247.lollaner;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CurrentGameActivity extends AppCompatActivity {

    public static final String SITENAME = "https://pure-bayou-47311.herokuapp.com";

    SharedPreferences prefs = null;

    String summonerId = "-1";

    DBHelper database;
    boolean statsLoaded = false;
    List<CurrentGameSummoner> summonerList;
    List<ImageView> imageList1, imageList2;
    List<ImageView> summonerSpell11, summonerSpell12, summonerSpell21, summonerSpell22, keystone1, keystone2;
    List<TextView> divisionList1, divisionList2;
    List<TextView> statsList1, statsList2;

    int activeSummoner1=-1, activeSummoner2=-1;

    ImageView drawerButton;
    DrawerLayout drawer;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout basicStats1, basicStats2;
    LinearLayout advancedStats1Container, advancedStats2Container;
    TextView advancedStats1Name, advancedStats1Games, advancedStats1GoldEarned, advancedStats1Kills, advancedStats1Deaths, advancedStats1Assists, advancedStats1Minions;
    TextView advancedStats1totalTurretsKilled, advancedStats1maxChampionsKilled, advancedStats1maxNumDeaths;

    TextView advancedStats2Name, advancedStats2Games, advancedStats2GoldEarned, advancedStats2Kills, advancedStats2Deaths, advancedStats2Assists, advancedStats2Minions;
    TextView advancedStats2totalTurretsKilled, advancedStats2maxChampionsKilled, advancedStats2maxNumDeaths;

    SwitchCompat queueSwitch;

    TextView queueTypeTv;
    TextView soloqText, flexqText;

    RelativeLayout loadingLayout, summonersLayout;
    TextView loadingText;
    GradientDrawable compareGradient;

    TextView compareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        summonerId = getIntent().getStringExtra("summonerId");

        //System.out.println(summonerId);

        queueSwitch = (SwitchCompat) findViewById(R.id.switchQueue);
        soloqText = (TextView) findViewById(R.id.soloqText);
        flexqText = (TextView) findViewById(R.id.flexqText);
        queueTypeTv = (TextView) findViewById(R.id.queueTypeTv);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerButton = (ImageView) findViewById(R.id.drawer_button);
        compareButton = (TextView) findViewById(R.id.compareButton);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        summonersLayout = (RelativeLayout) findViewById(R.id.summonersLayout);
        loadingText = (TextView) findViewById(R.id.loadingText);

        basicStats1 = (LinearLayout) findViewById(R.id.basicStats1);
        basicStats2 = (LinearLayout) findViewById(R.id.basicStats2);

        advancedStats1Container = (LinearLayout) findViewById(R.id.advancedStats1Container);
        advancedStats2Container = (LinearLayout) findViewById(R.id.advancedStats2Container);

        advancedStats1Name = (TextView) findViewById(R.id.advancedStats1Name);
        advancedStats1Games = (TextView) findViewById(R.id.advancedStats1Games);
        advancedStats1GoldEarned = (TextView) findViewById(R.id.advancedStats1GoldEarned);
        advancedStats1Kills = (TextView) findViewById(R.id.advancedStats1Kills);
        advancedStats1Deaths = (TextView) findViewById(R.id.advancedStats1Deaths);
        advancedStats1Assists = (TextView) findViewById(R.id.advancedStats1Assists);
        advancedStats1Minions = (TextView) findViewById(R.id.advancedStats1Minions);
        advancedStats1totalTurretsKilled = (TextView) findViewById(R.id.advancedStats1totalTurretsKilled);
        advancedStats1maxChampionsKilled = (TextView) findViewById(R.id.advancedStats1maxChampionsKilled);
        advancedStats1maxNumDeaths = (TextView) findViewById(R.id.advancedStats1maxNumDeaths);


        advancedStats2Name = (TextView) findViewById(R.id.advancedStats2Name);
        advancedStats2Games = (TextView) findViewById(R.id.advancedStats2Games);
        advancedStats2GoldEarned = (TextView) findViewById(R.id.advancedStats2GoldEarned);
        advancedStats2Kills = (TextView) findViewById(R.id.advancedStats2Kills);
        advancedStats2Deaths = (TextView) findViewById(R.id.advancedStats2Deaths);
        advancedStats2Assists = (TextView) findViewById(R.id.advancedStats2Assists);
        advancedStats2Minions = (TextView) findViewById(R.id.advancedStats2Minions);
        advancedStats2totalTurretsKilled = (TextView) findViewById(R.id.advancedStats2totalTurretsKilled);
        advancedStats2maxChampionsKilled = (TextView) findViewById(R.id.advancedStats2maxChampionsKilled);
        advancedStats2maxNumDeaths = (TextView) findViewById(R.id.advancedStats2maxNumDeaths);



        summonerList = new ArrayList<>();
        imageList1 = new ArrayList<>();
        imageList2 = new ArrayList<>();
        divisionList1 = new ArrayList<>();
        divisionList2 = new ArrayList<>();
        statsList1 = new ArrayList<>();
        statsList2 = new ArrayList<>();

        summonerSpell11 = new ArrayList<>();
        summonerSpell12 = new ArrayList<>();
        summonerSpell21 = new ArrayList<>();
        summonerSpell22 = new ArrayList<>();
        keystone1 = new ArrayList<>();
        keystone2 = new ArrayList<>();


        imageList1.add((ImageView)findViewById(R.id.champion11));
        imageList1.add((ImageView)findViewById(R.id.champion12));
        imageList1.add((ImageView)findViewById(R.id.champion13));
        imageList1.add((ImageView)findViewById(R.id.champion14));
        imageList1.add((ImageView)findViewById(R.id.champion15));

        imageList2.add((ImageView)findViewById(R.id.champion21));
        imageList2.add((ImageView)findViewById(R.id.champion22));
        imageList2.add((ImageView)findViewById(R.id.champion23));
        imageList2.add((ImageView)findViewById(R.id.champion24));
        imageList2.add((ImageView)findViewById(R.id.champion25));

        summonerSpell11.add((ImageView)findViewById(R.id.summonerSpell111));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell11.add((ImageView)findViewById(R.id.summonerSpell121));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell11.add((ImageView)findViewById(R.id.summonerSpell131));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell11.add((ImageView)findViewById(R.id.summonerSpell141));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell11.add((ImageView)findViewById(R.id.summonerSpell151));                        //первая цифра - команда, вторая - человек, третья - номер саммонера

        summonerSpell12.add((ImageView)findViewById(R.id.summonerSpell112));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell12.add((ImageView)findViewById(R.id.summonerSpell122));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell12.add((ImageView)findViewById(R.id.summonerSpell132));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell12.add((ImageView)findViewById(R.id.summonerSpell142));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell12.add((ImageView)findViewById(R.id.summonerSpell152));                        //первая цифра - команда, вторая - человек, третья - номер саммонера

        summonerSpell21.add((ImageView)findViewById(R.id.summonerSpell211));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell21.add((ImageView)findViewById(R.id.summonerSpell221));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell21.add((ImageView)findViewById(R.id.summonerSpell231));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell21.add((ImageView)findViewById(R.id.summonerSpell241));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell21.add((ImageView)findViewById(R.id.summonerSpell251));                        //первая цифра - команда, вторая - человек, третья - номер саммонера

        summonerSpell22.add((ImageView)findViewById(R.id.summonerSpell212));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell22.add((ImageView)findViewById(R.id.summonerSpell222));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell22.add((ImageView)findViewById(R.id.summonerSpell232));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell22.add((ImageView)findViewById(R.id.summonerSpell242));                        //первая цифра - команда, вторая - человек, третья - номер саммонера
        summonerSpell22.add((ImageView)findViewById(R.id.summonerSpell252));                        //первая цифра - команда, вторая - человек, третья - номер саммонера

        keystone1.add((ImageView)findViewById(R.id.summonerKeystone11));
        keystone1.add((ImageView)findViewById(R.id.summonerKeystone12));
        keystone1.add((ImageView)findViewById(R.id.summonerKeystone13));
        keystone1.add((ImageView)findViewById(R.id.summonerKeystone14));
        keystone1.add((ImageView)findViewById(R.id.summonerKeystone15));

        keystone2.add((ImageView)findViewById(R.id.summonerKeystone21));
        keystone2.add((ImageView)findViewById(R.id.summonerKeystone22));
        keystone2.add((ImageView)findViewById(R.id.summonerKeystone23));
        keystone2.add((ImageView)findViewById(R.id.summonerKeystone24));
        keystone2.add((ImageView)findViewById(R.id.summonerKeystone25));


        divisionList1.add((TextView) findViewById(R.id.division11));
        divisionList1.add((TextView) findViewById(R.id.division12));
        divisionList1.add((TextView) findViewById(R.id.division13));
        divisionList1.add((TextView) findViewById(R.id.division14));
        divisionList1.add((TextView) findViewById(R.id.division15));


        divisionList2.add((TextView) findViewById(R.id.division21));
        divisionList2.add((TextView) findViewById(R.id.division22));
        divisionList2.add((TextView) findViewById(R.id.division23));
        divisionList2.add((TextView) findViewById(R.id.division24));
        divisionList2.add((TextView) findViewById(R.id.division25));


        statsList1.add((TextView) findViewById(R.id.stats11));
        statsList1.add((TextView) findViewById(R.id.stats12));
        statsList1.add((TextView) findViewById(R.id.stats13));
        statsList1.add((TextView) findViewById(R.id.stats14));
        statsList1.add((TextView) findViewById(R.id.stats15));

        statsList2.add((TextView) findViewById(R.id.stats21));
        statsList2.add((TextView) findViewById(R.id.stats22));
        statsList2.add((TextView) findViewById(R.id.stats23));
        statsList2.add((TextView) findViewById(R.id.stats24));
        statsList2.add((TextView) findViewById(R.id.stats25));

        database = new DBHelper(this);


        Typeface beaufort = Typeface.createFromAsset(getAssets(), "beaufortforlol-bold.otf");
        queueTypeTv.setTypeface(beaufort);
        soloqText.setTypeface(beaufort);
        flexqText.setTypeface(beaufort);
        loadingText.setTypeface(beaufort);
        compareButton.setTypeface(beaufort);

        Typeface spiegelSemiBold = Typeface.createFromAsset(getAssets(), "spiegelcdot-semibold.otf");

        advancedStats1Name.setTypeface(spiegelSemiBold);
        advancedStats1Games.setTypeface(spiegelSemiBold);
        advancedStats1GoldEarned.setTypeface(spiegelSemiBold);
        advancedStats1Kills.setTypeface(spiegelSemiBold);
        advancedStats1Deaths.setTypeface(spiegelSemiBold);
        advancedStats1Assists.setTypeface(spiegelSemiBold);
        advancedStats1Minions.setTypeface(spiegelSemiBold);

        advancedStats1totalTurretsKilled.setTypeface(spiegelSemiBold);
        advancedStats1maxChampionsKilled.setTypeface(spiegelSemiBold);
        advancedStats1maxNumDeaths.setTypeface(spiegelSemiBold);

        advancedStats2Name.setTypeface(spiegelSemiBold);
        advancedStats2Games.setTypeface(spiegelSemiBold);
        advancedStats2GoldEarned.setTypeface(spiegelSemiBold);
        advancedStats2Kills.setTypeface(spiegelSemiBold);
        advancedStats2Deaths.setTypeface(spiegelSemiBold);
        advancedStats2Assists.setTypeface(spiegelSemiBold);
        advancedStats2Minions.setTypeface(spiegelSemiBold);

        advancedStats2totalTurretsKilled.setTypeface(spiegelSemiBold);
        advancedStats2maxChampionsKilled.setTypeface(spiegelSemiBold);
        advancedStats2maxNumDeaths.setTypeface(spiegelSemiBold);

        for(TextView v : divisionList1)
        v.setTypeface(spiegelSemiBold);

        for(TextView v : divisionList2)
            v.setTypeface(spiegelSemiBold);

        for(TextView v : statsList1)
            v.setTypeface(spiegelSemiBold);
        for(TextView v : statsList2)
            v.setTypeface(spiegelSemiBold);


        compareGradient = (GradientDrawable) getResources().getDrawable(R.drawable.compare_button);

        compareGradient.setStroke(Utils.dpToPx(2), 0xff999999);

        compareButton.setBackground(compareGradient);

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeSummoner1!=-1 && activeSummoner2!=-1) {
                    animateCompare();
                    Intent intent = new Intent(v.getContext(), ConstructorActivity.class);
                    startActivity(intent);
                }
            }
        });

        new currentGameLoader().execute();
        swipeRefreshLayout.setRefreshing(true);
       // System.out.println("SummonerList size = " + summonerList.size());

        queueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    switchToFlexQ();
                else switchToSoloQ();
            }
        });

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                summonerList.clear();
                statsLoaded = false;
                hideSummonerAdvancedData(1);
                hideSummonerAdvancedData(2);
                for(TextView v : statsList1)
                    v.setText("");
                for(TextView v : statsList2)
                    v.setText("");


                for(ImageView view : imageList1)
                {
                    view.setForeground(null);
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                }
                for(ImageView view : imageList2)
                {
                    view.setForeground(null);
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                }



                loadingText.setText("Loading...");
                new currentGameLoader().execute();

                loadingLayout.animate().alpha(1.0f).setDuration(200);
                summonersLayout.animate().alpha(0.0f).setDuration(200);
            }
        });

    }


    class currentGameLoader extends AsyncTask<Void, Void, Void>
    {

        JSONObject data;
        String idsArray = "";
        int gameQueueConfigId = -228;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            for(TextView v : statsList1)
                v.animate().alpha(0.0f).setDuration(400);
            //    v.setVisibility(View.GONE);
            for(TextView v : statsList2)
                v.animate().alpha(0.0f).setDuration(400);
            //    v.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(SITENAME + "/current-game/" + summonerId);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                data =  new JSONObject(json.toString());

                if(data.has("gameQueueConfigId"))
                    gameQueueConfigId = data.getInt("gameQueueConfigId");


                JSONArray participants = data.getJSONArray("participants");
                for(int i =0; i< participants.length(); i++)
                {
                    JSONObject obj = participants.getJSONObject(i);
                    int teamId = obj.getInt("teamId");
                    int spell1Id = obj.getInt("spell1Id");
                    int spell2Id = obj.getInt("spell2Id");
                    int championId = obj.getInt("championId");
                    String summonerName = obj.getString("summonerName");
                    int summonerId = obj.getInt("summonerId");
                    int keystone = 228;
                    JSONArray masteries = obj.getJSONArray("masteries");
                    for(int j = 0; j<masteries.length();j++)
                    {
                        JSONObject mastery = masteries.getJSONObject(j);
                        int masteryId = mastery.getInt("masteryId");
                        if(masteryId==6161||masteryId==6162||masteryId==6164||masteryId==6261||masteryId==6262||masteryId==6263||masteryId==6361||masteryId==6362||masteryId==6363)
                        {
                            keystone = masteryId;
                            break;
                        }

                    }

                    if(i!=participants.length()-1)
                        idsArray+=Integer.toString(summonerId) + ",";
                    else
                        idsArray+=Integer.toString(summonerId);

                    summonerList.add(new CurrentGameSummoner(summonerName,summonerId, championId, spell1Id, spell2Id, keystone, teamId, "", "UNRANKED", "", "UNRANKED"));
                }

                url = new URL(SITENAME + "/summoner/league/entry/" + idsArray);
                conn = (HttpsURLConnection) url.openConnection();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                json = new StringBuffer(1024);
                tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                data =  new JSONObject(json.toString());

                for(int i = 0; i<data.length(); i++)
                {
                    JSONArray summoner = data.getJSONArray(data.names().getString(i));
                    for(int j = 0; j < summoner.length(); j++)
                    {
                        if(summoner.getJSONObject(j).getString("queue").equals("RANKED_SOLO_5x5"))
                        {
                            JSONObject info = summoner.getJSONObject(j).getJSONArray("entries").getJSONObject(0);
                            String division = info.getString("division");
                            String tier = summoner.getJSONObject(j).getString("tier");
                            switch (tier)
                            {
                                case "BRONZE":
                                    tier = "BRON";
                                    break;
                                case "SILVER":
                                    tier = "SILV";
                                    break;
                                case "PLATINUM":
                                    tier = "PLAT";
                                    break;
                                case "DIAMOND":
                                    tier = "DIA";
                                    break;
                                case "MASTER":
                                    division = "";
                                    break;
                                case "CHALLENGER":
                                    division = "";
                                    tier = "CHAL";
                                    break;
                            }
                            for(CurrentGameSummoner summ : summonerList)
                            {
                                if(summ.summonerId == summoner.getJSONObject(j).getJSONArray("entries").getJSONObject(0).getInt("playerOrTeamId")) {
                                    summ.soloDivision = division;
                                    summ.soloTier = tier;
                                    break;
                                }
                            }
                        }
                        else if(summoner.getJSONObject(j).getString("queue").equals("RANKED_FLEX_SR"))
                        {
                            JSONObject info = summoner.getJSONObject(j).getJSONArray("entries").getJSONObject(0);
                            String division = info.getString("division");
                            String tier = summoner.getJSONObject(j).getString("tier");
                            switch (tier)
                            {
                                case "BRONZE":
                                    tier = "BRON";
                                    break;
                                case "SILVER":
                                    tier = "SILV";
                                    break;
                                case "PLATINUM":
                                    tier = "PLAT";
                                    break;
                                case "DIAMOND":
                                    tier = "DIA";
                                    break;
                                case "MASTER":
                                    division = "";
                                    break;
                                case "CHALLENGER":
                                    division = "";
                                    tier = "CHAL";
                                    break;
                            }
                            for(CurrentGameSummoner summ : summonerList)
                            {
                                if(summ.summonerId == summoner.getJSONObject(j).getJSONArray("entries").getJSONObject(0).getInt("playerOrTeamId")) {
                                    summ.flexDivision = division;
                                    summ.flexTier = tier;
                                    break;
                                }
                            }
                        }
                        else continue;
                    }

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


            for(ImageView view : imageList1)
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable d = v.getForeground();
                        for(ImageView v1 : imageList1) {
                            v1.setForeground(null);
                            v1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                        }

                        activeSummoner1 = imageList1.indexOf(v);

                        if(d != null) {
                            v.setForeground(null);
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                            activeSummoner1 = -1;
                            hideSummonerAdvancedData(1);

                        }
                        else {
                            v.setForeground(getResources().getDrawable(R.drawable.selected_item));
                            v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(10);
                            if(statsLoaded)
                            showSummonerAdvancedData(activeSummoner1, 1);
                        }

                        if(activeSummoner1!=-1 && activeSummoner2!=-1)
                            activateCompareButton();

                    }
                });
            }

            for(ImageView view : imageList2)
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable d = v.getForeground();
                        for(ImageView v1 : imageList2) {
                            v1.setForeground(null);
                            v1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                        }

                        activeSummoner2 = imageList2.indexOf(v);
                        //showSummonerAdvancedData(activeSummoner2);

                        //System.out.println(v.getForeground().toString() + " \t " + getResources().getDrawable(R.drawable.selected_item));
                        if(d != null) {
                            v.setForeground(null);
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(10);
                            activeSummoner2 = -1;
                            hideSummonerAdvancedData(2);
                        }
                        else {
                            v.setForeground(getResources().getDrawable(R.drawable.selected_item));
                            v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(10);
                            if(statsLoaded)
                            showSummonerAdvancedData(activeSummoner2, 2);
                        }

                        if(activeSummoner1!=-1 && activeSummoner2!=-1)
                            activateCompareButton();
                    }
                });
            }


            String idsArray = new String("");

            if(gameQueueConfigId == 440)
                queueTypeTv.setText("Flex Queue");
            else if(gameQueueConfigId == 65)
                queueTypeTv.setText("ARAM");
            else if(gameQueueConfigId == 2)
                queueTypeTv.setText("Normal Blind");
            else if(gameQueueConfigId == 400)
                queueTypeTv.setText("Normal Draft");
            else if(gameQueueConfigId == 420)
                queueTypeTv.setText("Solo/Duo Queue");
            else if(gameQueueConfigId == -228)
            {
                loadingText.setText("Game not found");
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            else
                queueTypeTv.setText("Custom");
            int currentItem1 = -1;
            int currentItem2 = -1;
            Resources resources = getResources();
            for(CurrentGameSummoner summoner : summonerList)
            {
                if(gameQueueConfigId == 440) {
                    switchToFlexQ();
                    queueSwitch.setChecked(true);
                }
                else {
                    switchToSoloQ();
                    queueSwitch.setChecked(false);
                }

                Cursor champion = database.getChampionCursorById(summoner.championId);

                Drawable image = Drawable.createFromPath(getFilesDir() + "/" + champion.getString(champion.getColumnIndex("imageurl")).toLowerCase());
                Drawable summonerSpell1 = ResourcesCompat.getDrawable(getResources(), Utils.getDrawableIdByName("s" + String.valueOf(summoner.summonerSpellId1), getApplicationContext()), null);
                Drawable summonerSpell2 = ResourcesCompat.getDrawable(getResources(), Utils.getDrawableIdByName("s" + String.valueOf(summoner.summonerSpellId2), getApplicationContext()), null);
                Drawable keystoneDrawable = ResourcesCompat.getDrawable(getResources(), Utils.getDrawableIdByName("m"+ String.valueOf(summoner.keystone), getApplicationContext()), null);

                if (summoner.teamId == 100)
                {
                    imageList1.get(++currentItem1).setImageDrawable(image);
                    summonerSpell11.get(currentItem1).setImageDrawable(summonerSpell1);
                    summonerSpell12.get(currentItem1).setImageDrawable(summonerSpell2);
                    keystone1.get(currentItem1).setImageDrawable(keystoneDrawable);
                }
                else
                {
                    imageList2.get(++currentItem2).setImageDrawable(image);
                    summonerSpell21.get(currentItem2).setImageDrawable(summonerSpell1);
                    summonerSpell22.get(currentItem2).setImageDrawable(summonerSpell2);
                    keystone2.get(currentItem2).setImageDrawable(keystoneDrawable);
                }
                idsArray = new String(idsArray + summoner.summonerId + ",");
            }

            if(idsArray.length()>0)

            new RankedStatsLoader(idsArray).execute();

            swipeRefreshLayout.setRefreshing(false);
            loadingLayout.animate().alpha(0.0f).setDuration(200);
            summonersLayout.animate().alpha(1.0f).setDuration(200);
            //loadingLayout.setVisibility(View.GONE);
            //summonersLayout.setVisibility(View.VISIBLE);
        }
    }


    class RankedStatsLoader extends AsyncTask<Void, Void, Void>
    {
        String idsArray;
        JSONObject data;

        public RankedStatsLoader(String idsArray)
        {
            this.idsArray = idsArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
            URL url = new URL(SITENAME + "/stats/ranked/" + idsArray);
               // System.out.println(url);
            HttpsURLConnection conn = null;

                conn = (HttpsURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            data =  new JSONObject(json.toString());
               // System.out.println(data);
                JSONArray answer = data.getJSONArray("answer");
                for(int j = 0; j < answer.length(); j++)
                {
                    if(answer.getJSONObject(j).has("champions")) {
                        JSONArray champions = answer.getJSONObject(j).getJSONArray("champions");
                        for (CurrentGameSummoner summoner : summonerList) {
                            if(Integer.parseInt(answer.getJSONObject(j).getString("id")) == summoner.summonerId) {
                                championCycle:
                                for (int i = 0; i < champions.length(); i++) {
                       //             System.out.println("SUMMONER " + summoner.summonerName + " CHAMPIONS ID " + champions.getJSONObject(i).getInt("id") + " \t " + summoner.championId);
                                    if (champions.getJSONObject(i).getInt("id") == summoner.championId) {
                                        JSONObject stats = champions.getJSONObject(i).getJSONObject("stats");
                                        int totalGames = stats.getInt("totalSessionsPlayed");
                                        int wonGames = stats.getInt("totalSessionsWon");
                                        int lostGames = stats.getInt("totalSessionsLost");

                                        int totalChampionKills = stats.getInt("totalChampionKills");
                                        int totalDeaths = stats.getInt("totalDeathsPerSession");
                                        int totalAssists = stats.getInt("totalAssists");

                                        int totalDamageDealt = stats.getInt("totalDamageDealt");
                                        int totalGoldEarned = stats.getInt("totalGoldEarned");

                                        int totalMinions = stats.getInt("totalMinionKills");

                                        int totalTurretsKilled = stats.getInt("totalTurretsKilled");
                                        int totalDamageTaken = stats.getInt("totalDamageTaken");

                                        int maxChampionsKilled = stats.getInt("maxChampionsKilled");
                                        int maxNumDeaths = stats.getInt("maxNumDeaths");

                                        summoner.addLeagueStats(totalGames, wonGames, lostGames, totalChampionKills, totalDeaths, totalAssists,
                                                totalDamageDealt, totalGoldEarned, totalMinions, totalTurretsKilled, totalDamageTaken,
                                                maxChampionsKilled, maxNumDeaths);
                      //                  System.out.println(summoner.summonerName + " " + totalGames + "(" + wonGames + "/" + lostGames + ")");
                                        break championCycle;
                                    }
                                }
                            }
                        }
                    }
                }


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

            int currentItem1 = -1;
            int currentItem2 = -1;
            for(CurrentGameSummoner summoner : summonerList)
            {

                if (summoner.teamId == 100)
                {
                    if(summoner.totalGames == 0)
                        statsList1.get(++currentItem1).setText("No \n games");
                    else
                    statsList1.get(++currentItem1).setText(" W/L: \n" +  summoner.wonGames + "/" + summoner.lostGames + "\n KDA: \n" +
                            String.format("%.2f",((float)summoner.totalChampionKills/(float)(summoner.totalGames==0? 1 : summoner.totalGames) +
                            (float)summoner.totalAssists/(float)(summoner.totalGames==0? 1 : summoner.totalGames)) /
                                    ((float)summoner.totalDeaths/(float)(summoner.totalGames==0? 1 : summoner.totalGames))) + "\n CS: \n" +
                            summoner.totalMinions/(summoner.totalGames==0? 1 : summoner.totalGames)
                            );
                }
                else
                {
                    if(summoner.totalGames == 0)
                        statsList2.get(++currentItem2).setText("No \n games");
                    else
                    statsList2.get(++currentItem2).setText(" W/L: \n" +  summoner.wonGames + "/" + summoner.lostGames + "\n KDA: \n" +
                            String.format("%.2f",((float)summoner.totalChampionKills/(float)(summoner.totalGames==0? 1 : summoner.totalGames) +
                                    (float)summoner.totalAssists/(float)(summoner.totalGames==0? 1 : summoner.totalGames)) /
                                    ((float)summoner.totalDeaths/(float)(summoner.totalGames==0? 1 : summoner.totalGames))) + "\n CS: \n" +
                            summoner.totalMinions/(summoner.totalGames==0? 1 : summoner.totalGames)
                    );
                }

                swipeRefreshLayout.setRefreshing(false);

                for(TextView v : statsList1)
                        v.animate().alpha(1.0f).setDuration(200);
                //    v.setVisibility(View.VISIBLE);
                for(TextView v : statsList2)
                    v.animate().alpha(1.0f).setDuration(200);
                //    v.setVisibility(View.VISIBLE);
//
               // System.out.println(summoner.summonerName + "\nFLEX: " + summoner.flexDivision + " " + summoner.flexTier + "\nSOLO: " + summoner.soloDivision + " " + summoner.soloTier);

              //  idsArray = new String(idsArray + summoner.summonerId + ",");
            }

            if(activeSummoner1!=-1 && activeSummoner2!=-1)
            {
                showSummonerAdvancedData(activeSummoner1,1);
                showSummonerAdvancedData(activeSummoner2,2);
            }

            statsLoaded = true;
        }
    }


    public void switchToSoloQ()
    {
        int currentItem1 = -1;
        int currentItem2 = -1;
        Resources resources = getResources();
        for(CurrentGameSummoner summoner : summonerList) {

            if (summoner.teamId == 100) {
                divisionList1.get(++currentItem1).setText(summoner.soloTier + " " + summoner.soloDivision);

                if (summoner.soloTier.equals("UNRANKED")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unranked_elo_outline));
                }

                if (summoner.soloTier.equals("BRON")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bronze_elo_outline));
                }
                if (summoner.soloTier.equals("SILV")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.silver_elo_outline));
                }
                if (summoner.soloTier.equals("GOLD")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gold_elo_outline));
                }
                if (summoner.soloTier.equals("PLAT")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.plat_elo_outline));
                }
                if (summoner.soloTier.equals("DIA")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dia_elo_outline));
                }
                if (summoner.soloTier.equals("MASTER")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.master_elo_outline));
                }
                if (summoner.soloTier.equals("CHAL")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chal_elo_outline));
                }
            }
            else
            {

                divisionList2.get(++currentItem2).setText(summoner.soloTier + " " + summoner.soloDivision);

                if(summoner.soloTier.equals("UNRANKED"))
                {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unranked_elo_outline));
                }

                if (summoner.soloTier.equals("BRON")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bronze_elo_outline));
                }
                if (summoner.soloTier.equals("SILV")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.silver_elo_outline));
                }
                if (summoner.soloTier.equals("GOLD")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gold_elo_outline));
                }
                if (summoner.soloTier.equals("PLAT")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.plat_elo_outline));
                }
                if (summoner.soloTier.equals("DIA")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dia_elo_outline));
                }
                if (summoner.soloTier.equals("MASTER")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.master_elo_outline));
                }
                if (summoner.soloTier.equals("CHAL")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chal_elo_outline));
                }
            }
        }
    }

    public void switchToFlexQ()
    {
        int currentItem1 = -1;
        int currentItem2 = -1;
        Resources resources = getResources();
        for(CurrentGameSummoner summoner : summonerList) {

            int resourceId = resources.getIdentifier(database.getKeyById(summoner.championId).toLowerCase(), "drawable",
                    getPackageName());

            if (summoner.teamId == 100) {
                //imageList1.get(++currentItem1).setImageDrawable(getResources().getDrawable(resourceId));
                divisionList1.get(++currentItem1).setText(summoner.flexTier + " " + summoner.flexDivision);

                if (summoner.flexTier.equals("UNRANKED")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unranked_elo_outline));
                }

                if (summoner.flexTier.equals("BRON")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bronze_elo_outline));
                }
                if (summoner.flexTier.equals("SILV")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.silver_elo_outline));
                }
                if (summoner.flexTier.equals("GOLD")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gold_elo_outline));
                }
                if (summoner.flexTier.equals("PLAT")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.plat_elo_outline));
                }
                if (summoner.flexTier.equals("DIA")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dia_elo_outline));
                }
                if (summoner.flexTier.equals("MASTER")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.master_elo_outline));
                }
                if (summoner.flexTier.equals("CHAL")) {
                    divisionList1.get(currentItem1).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chal_elo_outline));
                }
            }
            else
            {
               // imageList2.get(++currentItem2).setImageDrawable(getResources().getDrawable(resourceId));

                divisionList2.get(++currentItem2).setText(summoner.flexTier + " " + summoner.flexDivision);

                if(summoner.flexTier.equals("UNRANKED"))
                {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unranked_elo_outline));
                }

                if (summoner.flexTier.equals("BRON")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bronze_elo_outline));
                }
                if (summoner.flexTier.equals("SILV")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.silver_elo_outline));
                }
                if (summoner.flexTier.equals("GOLD")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gold_elo_outline));
                }
                if (summoner.flexTier.equals("PLAT")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.plat_elo_outline));
                }
                if (summoner.flexTier.equals("DIA")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dia_elo_outline));
                }
                if (summoner.flexTier.equals("MASTER")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.master_elo_outline));
                }
                if (summoner.flexTier.equals("CHAL")) {
                    divisionList2.get(currentItem2).setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chal_elo_outline));
                }
            }
        }
    }

    public void showSummonerAdvancedData(int activeSummoner, int team)
    {
        List<CurrentGameSummoner> team1 = new ArrayList<>();
        List<CurrentGameSummoner> team2 = new ArrayList<>();
        for(CurrentGameSummoner s : summonerList)
        {
            if(s.teamId == 100)
            {
                team1.add(s);
            }
            else team2.add(s);
        }
        CurrentGameSummoner summoner;

        if(team == 1)
        {
            summoner = team1.get(activeSummoner);


            advancedStats1Name.setText(summoner.summonerName);

            String championName = database.getChampionById(summoner.championId);

            Cursor champion = database.getChampionGGDataByName(championName);
            System.out.println(champion.getCount());
            champion.moveToFirst();

            float kills = 0.0f;
            float deaths = 0;
            float assists = 0;
            float winPercent = 0;
            int goldEarned = 0;
            float minions = 0;
            int gamesToCalc = 1;



            if(champion.getCount()>0) {
                kills = champion.getFloat(champion.getColumnIndex("kills"));
                deaths = champion.getFloat(champion.getColumnIndex("deaths"));
                assists = champion.getFloat(champion.getColumnIndex("assists"));
                winPercent = champion.getFloat(champion.getColumnIndex("winPercent"));
                goldEarned = champion.getInt(champion.getColumnIndex("goldEarned"));
                minions = champion.getFloat(champion.getColumnIndex("minionsKilled"));





                if ((float) summoner.totalGames != 0) gamesToCalc = summoner.totalGames;

                //KILLS
                {
                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalChampionKills / (float) gamesToCalc - kills > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;

                    SpannableString spannableKills = new SpannableString("Kills: " + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc - kills)) + ")");


                    float percent = Math.abs(100 * ((float) summoner.totalChampionKills / (float) gamesToCalc - kills) / ((float) summoner.totalChampionKills / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 8.5f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 8.5f;
                        }
                    }


                    System.out.println("Kills Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);


                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableKills.setSpan(foregroundSpan, 0, spannableKills.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats1Kills.setText(spannableKills);
                }
                //DEATHS
                {

                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalDeaths / (float) gamesToCalc - deaths > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;


                    SpannableString spannableDeaths = new SpannableString("Deaths: " + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc - deaths)) + ")");

                    float percent = Math.abs(100 * ((float) summoner.totalDeaths / (float) gamesToCalc - deaths) / ((float) summoner.totalDeaths / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 0;
                            redColor = 255;
                        } else {
                            greenColor = 255;
                            redColor = 0;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255 - percent * 8.5f;
                            redColor = 255;
                        } else {
                            redColor = 255 - percent * 8.5f;
                            greenColor = 255;
                        }
                    }


                    System.out.println("Deaths Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableDeaths.setSpan(foregroundSpan, 0, spannableDeaths.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats1Deaths.setText(spannableDeaths);
                }
                //ASSISTS
                {
                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalAssists / (float) gamesToCalc - assists > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;


                    SpannableString spannableAssists = new SpannableString("Assists: " + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc - assists)) + ")");

                    float percent = Math.abs(100 * ((float) summoner.totalAssists / (float) gamesToCalc - assists) / ((float) summoner.totalAssists / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 8.5f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 8.5f;
                        }
                    }


                    System.out.println("Assists Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableAssists.setSpan(foregroundSpan, 0, spannableAssists.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats1Assists.setText(spannableAssists);
                }

                //MINIONS
                {
                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalMinions / (float) gamesToCalc - minions > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;

                    SpannableString spannableMinions = new SpannableString("Minions: " + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc - minions)) + ")");


                    float percent = Math.abs(100 * ((float) summoner.totalMinions / (float) gamesToCalc - minions) / ((float) summoner.totalMinions / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 5.1f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 5.1f;
                        }
                    }


                    System.out.println("Minions Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableMinions.setSpan(foregroundSpan, 0, spannableMinions.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats1Minions.setText(spannableMinions);
                }


                advancedStats1GoldEarned.setText("Gold: " + (summoner.totalGoldEarned / gamesToCalc) + " (" + ((summoner.totalGoldEarned / gamesToCalc) - goldEarned) + ")");
            }
            else
            {
                advancedStats1Kills.setText("Kills: " + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc)));
                advancedStats1Deaths.setText("Deaths: " + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc)));
                advancedStats1Assists.setText("Assists: " + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc)));
                advancedStats1Minions.setText("Minions: " + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc)));
            }

            SpannableString spannableWinrate = new SpannableString("Games: " + summoner.totalGames + ": " + summoner.wonGames + "/" + summoner.lostGames + " (" + summoner.wonGames * 100 / gamesToCalc + "%)");
            boolean positive;
            float redColor = 255, greenColor = 255;

            if (summoner.wonGames * 100 / gamesToCalc > 50)
                positive = true;
            else
                positive = false;

            float percent = summoner.wonGames * 100 / gamesToCalc;
            if (percent > 60) {
                if (positive) {
                    greenColor = 255;
                    redColor = 0;
                } else if (percent < 40) {
                    greenColor = 0;
                    redColor = 255;
                }

            } else {
                if (positive) {
                    greenColor = 255;
                    redColor = 255 - Math.abs(percent - 50) * 25.5f;
                } else {
                    redColor = 255;
                    greenColor = 255 - Math.abs(percent - 50) * 25.5f;
                }
            }


            System.out.println("Winrate Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

            ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
            spannableWinrate.setSpan(foregroundSpan, 0, spannableWinrate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            advancedStats1Games.setText(spannableWinrate);

            advancedStats1totalTurretsKilled.setText("Turrets: " + summoner.totalTurretsKilled);
            advancedStats1maxChampionsKilled.setText("Max kills: " + summoner.maxChampionsKilled);
            advancedStats1maxNumDeaths.setText("Max deaths: " + summoner.maxNumDeaths);

            if(summoner.totalGames > 0) {
                basicStats1.animate().alpha(0.0f).setDuration(400);
                basicStats1.setVisibility(View.GONE);
                advancedStats1Container.animate().alpha(1.0f).setDuration(400);
                advancedStats1Container.setVisibility(View.VISIBLE);
            }
            else
            {
                basicStats1.animate().alpha(1.0f).setDuration(400);
                basicStats1.setVisibility(View.VISIBLE);
                advancedStats1Container.animate().alpha(0.0f).setDuration(400);
                advancedStats1Container.setVisibility(View.GONE);
            }
        }
        else
        {
            summoner = team2.get(activeSummoner);


            advancedStats2Name.setText(summoner.summonerName);

            String championName = database.getChampionById(summoner.championId);

            Cursor champion = database.getChampionGGDataByName(championName);
            champion.moveToFirst();

            float kills = 0.0f;
            float deaths = 0;
            float assists = 0;
            float winPercent = 0;
            int goldEarned = 0;
            float minions = 0;

            int gamesToCalc = 1;
            if ((float) summoner.totalGames != 0) gamesToCalc = summoner.totalGames;


            if(champion.getCount()>0) {
                kills = champion.getFloat(champion.getColumnIndex("kills"));
                deaths = champion.getFloat(champion.getColumnIndex("deaths"));
                assists = champion.getFloat(champion.getColumnIndex("assists"));
                winPercent = champion.getFloat(champion.getColumnIndex("winPercent"));
                goldEarned = champion.getInt(champion.getColumnIndex("goldEarned"));
                minions = champion.getFloat(champion.getColumnIndex("minionsKilled"));




                //KILLS
                {

                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalChampionKills / (float) gamesToCalc - kills > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;

                    SpannableString spannableKills = new SpannableString("Kills: " + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc - kills)) + ")");


                    float percent = Math.abs(100 * ((float) summoner.totalChampionKills / (float) gamesToCalc - kills) / ((float) summoner.totalChampionKills / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 8.5f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 8.5f;
                        }
                    }


                    System.out.println("Kills Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableKills.setSpan(foregroundSpan, 0, spannableKills.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats2Kills.setText(spannableKills);
                }
                //DEATHS
                {
                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalDeaths / (float) gamesToCalc - deaths > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;

                    SpannableString spannableDeaths = new SpannableString("Deaths: " + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc - deaths)) + ")");


                    float percent = Math.abs(100 * ((float) summoner.totalDeaths / (float) gamesToCalc - deaths) / ((float) summoner.totalDeaths / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 0;
                            redColor = 255;
                        } else {
                            greenColor = 255;
                            redColor = 0;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255 - percent * 8.5f;
                            redColor = 255;
                        } else {
                            redColor = 255 - percent * 8.5f;
                            greenColor = 255;
                        }
                    }


                    System.out.println("Deaths Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableDeaths.setSpan(foregroundSpan, 0, spannableDeaths.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats2Deaths.setText(spannableDeaths);
                }
                //ASSISTS
                {

                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalAssists / (float) gamesToCalc - assists > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;


                    SpannableString spannableAssists = new SpannableString("Assists: " + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc - assists)) + ")");

                    float percent = Math.abs(100 * ((float) summoner.totalAssists / (float) gamesToCalc - assists) / ((float) summoner.totalAssists / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 8.5f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 8.5f;
                        }
                    }


                    System.out.println("Assists Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableAssists.setSpan(foregroundSpan, 0, spannableAssists.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats2Assists.setText(spannableAssists);
                }


                //MINIONS
                {
                    boolean positive;
                    String plus = "";
                    float redColor = 255, greenColor = 255;

                    if ((float) summoner.totalMinions / (float) gamesToCalc - minions > 0) {
                        positive = true;
                        plus = "+";
                    } else
                        positive = false;

                    SpannableString spannableMinions = new SpannableString("Minions: " + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc)) + " (" + plus + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc - minions)) + ")");


                    float percent = Math.abs(100 * ((float) summoner.totalMinions / (float) gamesToCalc - minions) / ((float) summoner.totalMinions / (float) gamesToCalc));
                    if (percent > 30) {
                        if (positive) {
                            greenColor = 255;
                            redColor = 0;
                        } else {
                            greenColor = 0;
                            redColor = 255;
                        }

                    } else {
                        if (positive) {
                            greenColor = 255;
                            redColor = 255 - percent * 5.1f;
                        } else {
                            redColor = 255;
                            greenColor = 255 - percent * 5.1f;
                        }
                    }


                    System.out.println("Minions Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                    ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                    spannableMinions.setSpan(foregroundSpan, 0, spannableMinions.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    advancedStats2Minions.setText(spannableMinions);
                }
            }
            else
            {
                advancedStats2Kills.setText("Kills: " + String.format("%.2f", ((float) summoner.totalChampionKills / (float) gamesToCalc)));
                advancedStats2Deaths.setText("Deaths: " + String.format("%.2f", ((float) summoner.totalDeaths / (float) gamesToCalc)));
                advancedStats2Assists.setText("Assists: " + String.format("%.2f", ((float) summoner.totalAssists / (float) gamesToCalc)));
                advancedStats2Minions.setText("Minions: " + String.format("%.2f", ((float) summoner.totalMinions / (float) gamesToCalc)));
            }

            //WINRATE
            {
                SpannableString spannableWinrate = new SpannableString("Games: " + summoner.totalGames + ": " + summoner.wonGames + "/" + summoner.lostGames + " (" + summoner.wonGames * 100 / gamesToCalc + "%)");
                boolean positive;
                float redColor = 255, greenColor = 255;

                if (summoner.wonGames * 100 / gamesToCalc > 50)
                    positive = true;
                else
                    positive = false;

                float percent = summoner.wonGames * 100 / gamesToCalc;
                if (percent > 60) {
                    if (positive) {
                        greenColor = 255;
                        redColor = 0;
                    } else if (percent < 40) {
                        greenColor = 0;
                        redColor = 255;
                    }

                } else {
                    if (positive) {
                        greenColor = 255;
                        redColor = 255 - Math.abs(percent - 50) * 25.5f;
                    } else {
                        redColor = 255;
                        greenColor = 255 - Math.abs(percent - 50) * 25.5f;
                    }
                }


                System.out.println("Winrate Positive: " + positive + "\t percent: " + percent + "\t red: " + redColor + " green: " + greenColor);

                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.argb(200, (int) redColor, (int) greenColor, 0));
                spannableWinrate.setSpan(foregroundSpan, 0, spannableWinrate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                advancedStats2Games.setText(spannableWinrate);
            }

            advancedStats2GoldEarned.setText("Gold: " + (summoner.totalGoldEarned/gamesToCalc) + " (" + ((summoner.totalGoldEarned/gamesToCalc) - goldEarned) + ")");


            advancedStats2totalTurretsKilled.setText("Turrets: " + summoner.totalTurretsKilled);
            advancedStats2maxChampionsKilled.setText("Max kills: " + summoner.maxChampionsKilled);
            advancedStats2maxNumDeaths.setText("Max deaths: " + summoner.maxNumDeaths);


            if(summoner.totalGames > 0) {
                basicStats2.animate().alpha(0.0f).setDuration(400);
                basicStats2.setVisibility(View.GONE);
                advancedStats2Container.animate().alpha(1.0f).setDuration(400);
                advancedStats2Container.setVisibility(View.VISIBLE);
            }
            else
            {
                basicStats2.animate().alpha(1.0f).setDuration(400);
                basicStats2.setVisibility(View.VISIBLE);
                advancedStats2Container.animate().alpha(0.0f).setDuration(400);
                advancedStats2Container.setVisibility(View.GONE);
            }
        }


        // TODO: 01.02.2017 функционал по показу подробной статистики. А то спать хочу капец 

    }

    public void hideSummonerAdvancedData (int team)
    {
        if(team == 1) {
            advancedStats1Container.animate().alpha(0.0f).setDuration(400);
            advancedStats1Container.setVisibility(View.GONE);

            basicStats1.animate().alpha(1.0f).setDuration(400);
            basicStats1.setVisibility(View.VISIBLE);

        }
        else
        {
            advancedStats2Container.animate().alpha(0.0f).setDuration(400);
            advancedStats2Container.setVisibility(View.GONE);

            basicStats2.animate().alpha(1.0f).setDuration(400);
            basicStats2.setVisibility(View.VISIBLE);
        }

        deactivateCompareButton();

    }

    void activateCompareButton()
    {
        compareButton.setTextColor(0xffffffff);
        compareGradient.setStroke(Utils.dpToPx(2), 0xff09bcd5);
    }

    void deactivateCompareButton()
    {
        compareButton.setTextColor(0xff999999);
        compareGradient.setStroke(Utils.dpToPx(2), 0xff999999);
    }

    void animateCompare()
    {
        ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(),0xff09bcd5,0xffAAF0FA);

        animation.setDuration(150);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                compareGradient.setStroke(Utils.dpToPx(2), (Integer)animation.getAnimatedValue());
            }
        });
        animation.start();

        animation = ValueAnimator.ofFloat(24,18);

        animation.setDuration(150);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                compareButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, (Float)animation.getAnimatedValue());
            }
        });
        animation.start();



        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(),0xffAAF0FA, 0xff09bcd5);

                animation.setDuration(200);
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        compareGradient.setStroke(Utils.dpToPx(2), (Integer)animation.getAnimatedValue());
                    }
                });
                animation.start();

                animation = ValueAnimator.ofFloat(18,24);

                animation.setDuration(200);
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        compareButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, (Float)animation.getAnimatedValue());
                    }
                });
                animation.start();

            }
        };

        handler.postDelayed(r, 200);
    }
}

