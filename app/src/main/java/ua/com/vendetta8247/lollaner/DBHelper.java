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


    public static final String CHAMPIONS_TABLE_NAME = "champions";
    public static final String CHAMPIONS_COLUMN_ID = "id";
    public static final String CHAMPIONS_COLUMN_KEY = "key";
    public static final String CHAMPIONS_COLUMN_NAME = "name";
    public static final String CHAMPIONS_COLUMN_TITLE = "title";

    public static final String CHAMPIONS_COLUMN_BASE_ARMOR = "armor";
    public static final String CHAMPIONS_COLUMN_PER_LVL_ARMOR = "perlvlarmor";

    public static final String CHAMPIONS_COLUMN_BASE_AD = "ad";
    public static final String CHAMPIONS_COLUMN_PER_LVL_AD = "perlvlad";

    public static final String CHAMPIONS_COLUMN_ATTACK_RANGE = "range";
    public static final String CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET = "asoffset";
    public static final String CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED = "perlvlas";

    public static final String CHAMPIONS_COLUMN_BASE_CRIT = "crit";
    public static final String CHAMPIONS_COLUMN_PER_LVL_CRIT = "perlvlcrit";

    public static final String CHAMPIONS_COLUMN_BASE_HP = "hp";
    public static final String CHAMPIONS_COLUMN_PER_LVL_HP = "perlvlhp";
    public static final String CHAMPIONS_COLUMN_BASE_HP_REGEN = "hpregen";
    public static final String CHAMPIONS_COLUMN_PER_LVL_HP_REGEN = "perlvlhpregen";

    public static final String CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED = "ms";

    public static final String CHAMPIONS_COLUMN_BASE_MANA_POINTS = "mp";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS = "perlvlmp";
    public static final String CHAMPIONS_COLUMN_BASE_MANA_REGEN = "mpregen";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN = "perlvlmpregen";

    public static final String CHAMPIONS_COLUMN_BASE_MAGIC_RESIST = "mr";
    public static final String CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST = "perlvlmr";

    public static final String CHAMPION_COLUMN_TAGS = "tags";



    //TODO ADD NEW TABLE

    public static final String CREATE_CHAMPIONS_TABLE_REQUEST = "create table " + CHAMPIONS_TABLE_NAME +
            " (" + CHAMPIONS_COLUMN_ID + " integer primary key, " + CHAMPIONS_COLUMN_NAME + " text, " + CHAMPIONS_COLUMN_KEY + " text, " + CHAMPIONS_COLUMN_TITLE + " text, " +
            CHAMPIONS_COLUMN_BASE_ARMOR + " float, " + CHAMPIONS_COLUMN_PER_LVL_ARMOR + " float, " + CHAMPIONS_COLUMN_BASE_AD + " float, " + CHAMPIONS_COLUMN_PER_LVL_AD + " float, " +
            CHAMPIONS_COLUMN_ATTACK_RANGE + " float, " + CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET + " float, " + CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED + " float, " + CHAMPIONS_COLUMN_BASE_CRIT + " float, " +
            CHAMPIONS_COLUMN_PER_LVL_CRIT + " float, " + CHAMPIONS_COLUMN_BASE_HP + " float, " + CHAMPIONS_COLUMN_PER_LVL_HP + " float, " + CHAMPIONS_COLUMN_BASE_HP_REGEN + " float, " +
            CHAMPIONS_COLUMN_PER_LVL_HP_REGEN + " float, " + CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED + " float, " + CHAMPIONS_COLUMN_BASE_MANA_POINTS + " float, " + CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS + " float, " +
            CHAMPIONS_COLUMN_BASE_MANA_REGEN + " float, " + CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN + " float, " + CHAMPIONS_COLUMN_BASE_MAGIC_RESIST + " float, " + CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST + " float, " + CHAMPION_COLUMN_TAGS + " text);";


    public static final String SUMMONER_TABLE_NAME = "summoners";
    public static final String SUMMONER_COLUMN_ID = "id";
    public static final String SUMMONER_COLUMN_NAME = "name";
    public static final String SUMMONER_COLUMN_STATUS = "status";

    public static final String CREATE_SUMMONERS_TABLE_REQUEST = "create table " + SUMMONER_TABLE_NAME + " (" + SUMMONER_COLUMN_ID  + " integer primary key, " + SUMMONER_COLUMN_STATUS + " integer, " + SUMMONER_COLUMN_NAME +" text, unique(" + SUMMONER_COLUMN_NAME +"))";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAMPIONS_TABLE_REQUEST);
        db.execSQL(CREATE_SUMMONERS_TABLE_REQUEST);
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

    public String getKeyById(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String key = "";
        Cursor res = db.rawQuery("select * from champions where id=" + id, null);
        res.moveToFirst();
        key = res.getString(res.getColumnIndex("key"));
        return key;
    }

    public ArrayList<String> getChampionById(int id){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from champions where lolid="+id+"", null );
        res.moveToFirst();
        System.out.println(res.getCount());

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CHAMPIONS_COLUMN_KEY)));
            res.moveToNext();
        }

        return array_list;
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

    public void clearTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from champions");
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("champions",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }


    public boolean insertSummoner  (String name, String id, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(status == 1)
        {
            db.execSQL("update summoners set status = 2 where status = 1");
        }
        db.execSQL("insert or replace into summoners (" + SUMMONER_COLUMN_ID + "," + SUMMONER_COLUMN_NAME + "," + SUMMONER_COLUMN_STATUS + ") values (\"" + id + "\",\"" + name + "\", " + status +");");
        return true;
    }

    public Cursor getMySummoner()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from summoners where status = 1",null);
        return res;
    }


    public boolean insertChampion(int id, String name, String key, String title, float baseArmor, float perLvlArmor, float baseAd, float perLvlAd, float attackRange, float asOffset,
                                  float perLvlAs, float baseCrit, float perLvlCrit, float baseHp, float perLvlHp, float baseHpRegen, float perLvlHpRegen, float baseMs, float baseMp,
                                  float perLvlMp, float baseManaRegen, float perLvlManaRegen, float baseMr, float perLvlMr, String tags)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert or replace into " +CHAMPIONS_TABLE_NAME +  "(" + CHAMPIONS_COLUMN_ID + ", " + CHAMPIONS_COLUMN_NAME+ ", " +CHAMPIONS_COLUMN_KEY+ ", " +CHAMPIONS_COLUMN_TITLE + ", " +
                CHAMPIONS_COLUMN_BASE_ARMOR+ ", " + CHAMPIONS_COLUMN_PER_LVL_ARMOR+ ", " +CHAMPIONS_COLUMN_BASE_AD+ ", " +CHAMPIONS_COLUMN_PER_LVL_AD+ ", " +CHAMPIONS_COLUMN_ATTACK_RANGE+ ", " +
                CHAMPIONS_COLUMN_ATTACK_SPEED_OFFSET+ ", " + CHAMPIONS_COLUMN_PER_LVL_ATTACK_SPEED+ ", " +CHAMPIONS_COLUMN_BASE_CRIT+ ", " +CHAMPIONS_COLUMN_PER_LVL_CRIT+ ", " +CHAMPIONS_COLUMN_BASE_HP+ ", " +
                CHAMPIONS_COLUMN_PER_LVL_HP+ ", " +CHAMPIONS_COLUMN_BASE_HP_REGEN+ ", " +CHAMPIONS_COLUMN_PER_LVL_HP_REGEN+ ", " +CHAMPIONS_COLUMN_BASE_MOVEMENT_SPEED+ ", " +CHAMPIONS_COLUMN_BASE_MANA_POINTS+ ", " +
                CHAMPIONS_COLUMN_PER_LVL_MANA_POINTS+ ", " +CHAMPIONS_COLUMN_BASE_MANA_REGEN+ ", " +CHAMPIONS_COLUMN_PER_LVL_MANA_REGEN+ ", " +CHAMPIONS_COLUMN_BASE_MAGIC_RESIST + ", " + CHAMPIONS_COLUMN_PER_LVL_MAGIC_RESIST+ "," + CHAMPION_COLUMN_TAGS + ") values (" +
        id + ", \"" + name + "\", \"" + key + "\",\"" + title + "\", " + baseArmor + ", " + perLvlArmor+ ", " +baseAd+ ", " +perLvlAd+ ", " +attackRange+ ", " +asOffset+ ", " +perLvlAs+ ", " +baseCrit+ ", " +perLvlCrit+ ", " +baseHp+ ", " +perLvlHp+ ", " +
        baseHpRegen+ ", " +perLvlHpRegen+ ", " +baseMs+ ", " +baseMp+ ", " +perLvlMp+ ", " +baseManaRegen+ ", " +perLvlManaRegen+ ", " +baseMr+ ", " +perLvlMr+ ", \"" +tags+"\");");
        return true;
    }

}