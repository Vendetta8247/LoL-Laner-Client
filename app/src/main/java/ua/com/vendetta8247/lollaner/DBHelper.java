package ua.com.vendetta8247.lollaner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
    public static final String CHAMPIONS_COLUMN_CHAMPION = "champion";
    public static final String CHAMPIONS_COLUMN_NAME = "name";
    public static final String CHAMPIONS_COLUMN_LOL_ID = "lolid";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CHAMPIONS_TABLE_NAME +
                        " (" + CHAMPIONS_COLUMN_ID  + " integer primary key, " + CHAMPIONS_COLUMN_NAME +" text, " + CHAMPIONS_COLUMN_CHAMPION + " text, " + CHAMPIONS_COLUMN_LOL_ID + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact  (String name, String champion, String lolid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("champion", champion);
        contentValues.put("lolid", lolid);
        db.insert("champions", null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from champions where id="+id+"", null );
        return res;
    }

    public Cursor getChampionByName(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from champions where name=" + name +"", null);
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

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from champions order by name asc", null );
       // Cursor c = db.query(CHAMPIONS_TABLE_NAME, null, null, null, null, null, CHAMPIONS_COLUMN_NAME + " ASC");
       // c.moveToFirst();
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CHAMPIONS_COLUMN_CHAMPION)));
            res.moveToNext();
        }
        return array_list;
    }
}