package ua.com.vendetta8247.lollaner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LoLLaner.db";


    public static final String CHAMPIONS_TABLE_NAME =                                   "champions";
    public static final String CHAMPIONS_COLUMN_ID =                                    "id";
    public static final String CHAMPIONS_COLUMN_KEY =                                   "key";
    public static final String CHAMPIONS_COLUMN_NAME =                                  "name";
    public static final String CHAMPIONS_COLUMN_TITLE =                                 "title";
    public static final String CHAMPIONS_COLUMN_BASE_ARMOR =                            "armor";
    public static final String CHAMPIONS_COLUMN_PER_LVL_ARMOR =                         "armorperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_AD =                               "attackdamage";
    public static final String CHAMPIONS_COLUMN_PER_LVL_AD =                            "attackdamageperlevel";
    public static final String CHAMPIONS_COLUMN_ATTACK_RANGE =                          "attackrange";
    public static final String CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET =                   "attackspeedoffset";
    public static final String CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED =                  "attackspeedperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_CRIT =                             "crit";
    public static final String CHAMPIONS_COLUMN_PER_LVL_CRIT =                          "critperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_HP =                               "hp";
    public static final String CHAMPIONS_COLUMN_PER_LVL_HP =                            "hpperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_HP_REGEN =                         "hpregen";
    public static final String CHAMPIONS_COLUMN_PER_LVL_HP_REGEN =                      "hpregenperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED =                   "movespeed";
    public static final String CHAMPIONS_COLUMN_BASE_MANA_POINTS =                      "mp";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS =                   "mpperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_MANA_REGEN =                       "mpregen";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN =                    "mpregenperlevel";
    public static final String CHAMPIONS_COLUMN_BASE_MAGIC_RESIST =                     "spellblock";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST =                  "spellblockperlevel";

    public static final String CHAMPION_COLUMN_TAGS =                                   "tags";
    public static final String CHAMPION_COLUMN_IMAGE_GROUP =                            "imagegroup";
    public static final String CHAMPION_COLUMN_IMAGE_URL =                              "imageurl";



    //TODO ADD NEW TABLE

    public static final String CREATE_CHAMPIONS_TABLE_REQUEST = "create table " + CHAMPIONS_TABLE_NAME +
            " (" + CHAMPIONS_COLUMN_ID + " integer primary key, " + CHAMPIONS_COLUMN_NAME + " text, " + CHAMPIONS_COLUMN_KEY + " text, " + CHAMPIONS_COLUMN_TITLE + " text, " +
            CHAMPIONS_COLUMN_BASE_ARMOR + " float, " + CHAMPIONS_COLUMN_PER_LVL_ARMOR + " float, " + CHAMPIONS_COLUMN_BASE_AD + " float, " + CHAMPIONS_COLUMN_PER_LVL_AD + " float, " +
            CHAMPIONS_COLUMN_ATTACK_RANGE + " float, " + CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET + " float, " + CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED + " float, " + CHAMPIONS_COLUMN_BASE_CRIT + " float, " +
            CHAMPIONS_COLUMN_PER_LVL_CRIT + " float, " + CHAMPIONS_COLUMN_BASE_HP + " float, " + CHAMPIONS_COLUMN_PER_LVL_HP + " float, " + CHAMPIONS_COLUMN_BASE_HP_REGEN + " float, " +
            CHAMPIONS_COLUMN_PER_LVL_HP_REGEN + " float, " + CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED + " float, " + CHAMPIONS_COLUMN_BASE_MANA_POINTS + " float, " + CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS + " float, " +
            CHAMPIONS_COLUMN_BASE_MANA_REGEN + " float, " + CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN + " float, " + CHAMPIONS_COLUMN_BASE_MAGIC_RESIST + " float, " + CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST + " float, " +
            CHAMPION_COLUMN_TAGS + " text, " + CHAMPION_COLUMN_IMAGE_GROUP + " text, " + CHAMPION_COLUMN_IMAGE_URL +");";


    public static final String SUMMONER_TABLE_NAME = "summoners";
    public static final String SUMMONER_COLUMN_ID = "id";
    public static final String SUMMONER_COLUMN_NAME = "name";
    public static final String SUMMONER_COLUMN_STATUS = "status";
    public static final String SUMMONER_COLUMN_ACCOUNT_ID = "accId";
    public static final String SUMMONER_COLUMN_ICON = "icon";
    public static final String SUMMONER_COLUMN_LAST_EDITED = "lastedited";

    public static final String CREATE_SUMMONERS_TABLE_REQUEST = "create table " + SUMMONER_TABLE_NAME + " (" + SUMMONER_COLUMN_ID  + " integer primary key, "+ SUMMONER_COLUMN_ACCOUNT_ID + " integer, " + SUMMONER_COLUMN_ICON + " integer, " + SUMMONER_COLUMN_LAST_EDITED + " integer, " + SUMMONER_COLUMN_STATUS + " integer, " + SUMMONER_COLUMN_NAME +" text, unique(" + SUMMONER_COLUMN_NAME +"))";


    public static final String CHAMPION_GG_TABLE_NAME = "championgg";
    public static final String CHAMPION_GG_ID = "id";
    public static final String CHAMPION_GG_KEY = "key";
    public static final String CHAMPION_GG_ROLE = "role";
    public static final String CHAMPION_GG_NAME = "name";

    public static final String CHAMPION_GG_OVERALL_POS_CHANGE = "overallPositionChange";
    public static final String CHAMPION_GG_OVERALL_POS = "overallPosition";
    public static final String CHAMPION_GG_GOLD_EARNED = "goldEarned";
    public static final String CHAMPION_GG_NEUTRAL_ENEMY = "neutralMinionsKilledEnemyJungle";
    public static final String CHAMPION_GG_NEUTRAL_TEAM = "neutralMinionsKilledTeamJungle";
    public static final String CHAMPION_GG_MINIONS_KILLED = "minionsKilled";
    public static final String CHAMPION_GG_LARGEST_KILLING_SPREE = "largestKillingSpree";
    public static final String CHAMPION_GG_TOTAL_HEAL = "totalHeal";
    public static final String CHAMPION_GG_TOTAL_DAMAGE_TAKEN = "totalDamageTaken";
    public static final String CHAMPION_GG_TOTAL_DAMAGE_DEALT = "totalDamageDealtToChampions";
    public static final String CHAMPION_GG_ASSISTS = "assists";
    public static final String CHAMPION_GG_DEATHS = "deaths";
    public static final String CHAMPION_GG_KILLS = "kills";
    public static final String CHAMPION_GG_EXPERIENCE = "experience";
    public static final String CHAMPION_GG_BAN_RATE = "banRate";
    public static final String CHAMPION_GG_PLAY_PERCENT = "playPercent";
    public static final String CHAMPION_GG_WIN_PERCENT = "winPercent";


    public static final String CREATE_CHAMPION_GG_TABLE_REQUEST = "create table " + CHAMPION_GG_TABLE_NAME + "(" + CHAMPION_GG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CHAMPION_GG_KEY + " text," + CHAMPION_GG_ROLE + " text," +
            CHAMPION_GG_NAME + " text," + CHAMPION_GG_OVERALL_POS_CHANGE + " integer, " + CHAMPION_GG_OVERALL_POS + " integer," + CHAMPION_GG_GOLD_EARNED + " integer," + CHAMPION_GG_NEUTRAL_ENEMY + " float," +
            CHAMPION_GG_NEUTRAL_TEAM + " float,"  + CHAMPION_GG_MINIONS_KILLED + " float," + CHAMPION_GG_LARGEST_KILLING_SPREE + " float, " + CHAMPION_GG_TOTAL_HEAL + " integer," + CHAMPION_GG_TOTAL_DAMAGE_TAKEN + " integer," +
            CHAMPION_GG_TOTAL_DAMAGE_DEALT + " integer," +CHAMPION_GG_ASSISTS + " float," + CHAMPION_GG_DEATHS + " float," +CHAMPION_GG_KILLS + " float," + CHAMPION_GG_EXPERIENCE + " float," + CHAMPION_GG_BAN_RATE + " float," +
            CHAMPION_GG_PLAY_PERCENT + " float," + CHAMPION_GG_WIN_PERCENT + " float);";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAMPIONS_TABLE_REQUEST);
        db.execSQL(CREATE_SUMMONERS_TABLE_REQUEST);
        db.execSQL(CREATE_CHAMPION_GG_TABLE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact  (String name, String champion, String lolid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert or replace into champions (name, champion, lolid) values (\"" + name + "\",\"" + champion + "\",\"" + lolid +"\");");
        return true;
    }

    public Cursor getChampionGGDataByName(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CHAMPION_GG_TABLE_NAME + " where key = \'" + name + "\';", null);
        return res;
    }

    public String getKeyById(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String key = "";
        Cursor res = db.rawQuery("select * from champions where id=" + id, null);
        res.moveToFirst();
        key = res.getString(res.getColumnIndex("key"));
        return key;
    }

    public String getChampionById(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from champions where id="+id+"", null );
        res.moveToFirst();

        return res.getString(res.getColumnIndex(CHAMPIONS_COLUMN_KEY));
    }

    public Cursor getChampionCursorById(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from champions where id="+id+"", null );
        res.moveToFirst();

        return res;
    }

    public Cursor getChampionByName(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from champions where name=" + name +"", null);
        return res;
    }

    public Cursor getAllChampions()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from champions order by name asc;", null);
        return res;
    }

    public Cursor getAllSummoners()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from summoners order by name asc;", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CHAMPIONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String champion, String lolid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("champion", champion);
        contentValues.put("lolid", lolid);
        db.update("champions", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean insertSummoner  (String name, String id, int status, int accId, int icon, long lastedited)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(status == 1)
        {
            db.execSQL("update summoners set status = 2 where status = 1");
        }
        db.execSQL("insert or replace into summoners (" + SUMMONER_COLUMN_ID + "," + SUMMONER_COLUMN_NAME + "," + SUMMONER_COLUMN_ACCOUNT_ID + "," + SUMMONER_COLUMN_STATUS + "," + SUMMONER_COLUMN_ICON + "," + SUMMONER_COLUMN_LAST_EDITED +") values (\"" + id + "\",\"" + name + "\", " + accId + ", " + status +", " + icon +"," + lastedited + ");");
        return true;
    }

    public void removeMySummoner()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update summoners set status = 2 where status = 1");
    }

    public Cursor getMySummoner()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from summoners where status = 1",null);
        return res;
    }

    public Cursor getSummonerByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from summoners where name = \'" + name + "\'",null);
        return res;
    }




    public boolean insertChampion(int id, String name, String key, String title, float baseArmor, float perLvlArmor, float baseAd, float perLvlAd, float attackRange, float asOffset,
                                  float perLvlAs, float baseCrit, float perLvlCrit, float baseHp, float perLvlHp, float baseHpRegen, float perLvlHpRegen, float baseMs, float baseMp,
                                  float perLvlMp, float baseManaRegen, float perLvlManaRegen, float baseMr, float perLvlMr, String tags, String imageGroup, String imageUrl)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert or replace into " +CHAMPIONS_TABLE_NAME +  "(" + CHAMPIONS_COLUMN_ID + ", " + CHAMPIONS_COLUMN_NAME+ ", " +CHAMPIONS_COLUMN_KEY+ ", " +CHAMPIONS_COLUMN_TITLE + ", " +
                CHAMPIONS_COLUMN_BASE_ARMOR+ ", " + CHAMPIONS_COLUMN_PER_LVL_ARMOR+ ", " +CHAMPIONS_COLUMN_BASE_AD+ ", " +CHAMPIONS_COLUMN_PER_LVL_AD+ ", " +CHAMPIONS_COLUMN_ATTACK_RANGE+ ", " +
                CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET+ ", " + CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED+ ", " +CHAMPIONS_COLUMN_BASE_CRIT+ ", " +CHAMPIONS_COLUMN_PER_LVL_CRIT+ ", " +CHAMPIONS_COLUMN_BASE_HP+ ", " +
                CHAMPIONS_COLUMN_PER_LVL_HP+ ", " +CHAMPIONS_COLUMN_BASE_HP_REGEN+ ", " +CHAMPIONS_COLUMN_PER_LVL_HP_REGEN+ ", " +CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED+ ", " +CHAMPIONS_COLUMN_BASE_MANA_POINTS+ ", " +
                CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS+ ", " +CHAMPIONS_COLUMN_BASE_MANA_REGEN+ ", " +CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN+ ", " +CHAMPIONS_COLUMN_BASE_MAGIC_RESIST + ", " + CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST+ "," + CHAMPION_COLUMN_TAGS + "," + CHAMPION_COLUMN_IMAGE_GROUP +
                "," + CHAMPION_COLUMN_IMAGE_URL + ") values (" +
        id + ", \"" + name + "\", \"" + key + "\",\"" + title + "\", " + baseArmor + ", " + perLvlArmor+ ", " +baseAd+ ", " +perLvlAd+ ", " +attackRange+ ", " +asOffset+ ", " +perLvlAs+ ", " +baseCrit+ ", " +perLvlCrit+ ", " +baseHp+ ", " +perLvlHp+ ", " +
        baseHpRegen+ ", " +perLvlHpRegen+ ", " +baseMs+ ", " +baseMp+ ", " +perLvlMp+ ", " +baseManaRegen+ ", " +perLvlManaRegen+ ", " +baseMr+ ", " +perLvlMr+ ", \"" +tags+"\",\"" + imageGroup + "\", \"" + imageUrl +"\");");
        return true;
    }

    public void clearChampionGGData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CHAMPION_GG_TABLE_NAME + ";");
        db.execSQL("insert into " + CHAMPION_GG_TABLE_NAME + "(id) values (-1);");
    }

    public boolean instertChampionGGData(String key, String role, String name, int positionChange, int position, int goldEarned, float neutralEnemy, float neutralTeam, float minionsKilled,
                                         float largestKillingSpree, int totalHeal, int totalDamageTaken, int totalDamageDealt, float assists, float deaths, float kills, float experience, float banRate,
                                         float playPercent, float winPercent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert or replace into " +CHAMPION_GG_TABLE_NAME +  "(" + CHAMPION_GG_KEY + "," + CHAMPION_GG_ROLE + "," +
                CHAMPION_GG_NAME + "," + CHAMPION_GG_OVERALL_POS_CHANGE + " , " + CHAMPION_GG_OVERALL_POS + " ," + CHAMPION_GG_GOLD_EARNED + " ," + CHAMPION_GG_NEUTRAL_ENEMY + " ," +
                CHAMPION_GG_NEUTRAL_TEAM + " ,"  + CHAMPION_GG_MINIONS_KILLED + " ," + CHAMPION_GG_LARGEST_KILLING_SPREE + " , " + CHAMPION_GG_TOTAL_HEAL + " ," + CHAMPION_GG_TOTAL_DAMAGE_TAKEN + " ," +
                CHAMPION_GG_TOTAL_DAMAGE_DEALT + " ," +CHAMPION_GG_ASSISTS + " ," + CHAMPION_GG_DEATHS + " ," +CHAMPION_GG_KILLS + " ," + CHAMPION_GG_EXPERIENCE + " ," + CHAMPION_GG_BAN_RATE + " ," +
                CHAMPION_GG_PLAY_PERCENT + " ," + CHAMPION_GG_WIN_PERCENT  + ") values (\"" + key + "\", \"" + role + "\",\"" + name + "\", " + positionChange + ", " + position+ ", " +goldEarned+ ", " +neutralEnemy+ ", " +neutralTeam+ ", " +minionsKilled+ ", " +largestKillingSpree
                + ", " +totalHeal+ ", " +totalDamageTaken+ ", " +totalDamageDealt+ ", " +assists+ ", " +
                deaths+ ", " +kills+ ", " +experience+ ", " +banRate+ ", " +playPercent+ ", " +winPercent+ ");");
        return true;
    }

}