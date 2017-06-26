package ua.com.vendetta8247.lollaner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.R.attr.name;

public class CharacterSelectorActivity extends AppCompatActivity {

    DBHelper database;
    TableLayout tableLayout;
    Cursor champions;
    int counterCap = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int scrnWidth = size.x;
        int scrnHeight = size.y;

        setContentView(R.layout.activity_character_selector);
        int counter = 0;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            counterCap = 4;
        else
        counterCap = 7;
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        database = new DBHelper(this);
        TableRow tableRow = new TableRow(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(params);

        champions=database.getAllChampions();
        champions.moveToFirst();
        while(!champions.isAfterLast())
        {
            if(counter==counterCap) {
                tableLayout.addView(tableRow);
                tableRow = new TableRow(this);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                tableRow.setLayoutParams(param);
                counter = 0;
            }
            final String name = champions.getString(champions.getColumnIndex("name"));
            final String key = champions.getString(champions.getColumnIndex("key"));
            final String title = champions.getString(champions.getColumnIndex("title"));
            final String tags = champions.getString(champions.getColumnIndex("tags"));
            final String url = champions.getString(champions.getColumnIndex("imageurl"));

            final String ad = String.format("%.0f",champions.getFloat(champions.getColumnIndex("attackdamage")));
            final String ap = new String("0");
            final String armor = String.format("%.0f",champions.getFloat(champions.getColumnIndex("armor")));
            final String mr = String.format("%.0f",champions.getFloat(champions.getColumnIndex("spellblock")));
            final String crit = String.format("%.0f",champions.getFloat(champions.getColumnIndex("crit")));
            final String as = String.format("%.3f",0.625f / (1 + champions.getFloat(champions.getColumnIndex("attackspeedoffset"))));
            final String cdr = "0";
            final String ms = String.format("%.0f",champions.getFloat(champions.getColumnIndex("movespeed")));

            //
            ImageView img = new ImageView(this);

            Resources resources = getResources();
            System.out.println(champions.getString(champions.getColumnIndex("key")).toLowerCase());

            Drawable image = Drawable.createFromPath(getFilesDir() + "/" + champions.getString(champions.getColumnIndex("imageurl")).toLowerCase());

            //image = new ScaleDrawable(image, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight()).getDrawable();

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            layoutParams.height = scrnWidth/counterCap-10;
            layoutParams.width = scrnWidth/counterCap-10;
            img.setImageDrawable(image);

            img.setLayoutParams(layoutParams);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("key", key);
                    intent.putExtra("name", name);
                    intent.putExtra("title", title);
                    intent.putExtra("tags", tags);
                    intent.putExtra("textViewAD", ad);
                    intent.putExtra("textViewAP", ap);
                    intent.putExtra("textViewArmor", armor);
                    intent.putExtra("textViewMR", mr);
                    intent.putExtra("textViewCrit", crit);
                    intent.putExtra("textViewAS", as);
                    intent.putExtra("textViewCDR", cdr);
                    intent.putExtra("textViewMS", ms);
                    intent.putExtra("imageUrl", url);

                    setResult(RESULT_OK,intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            });

            //


            //
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.whiteMain));
            tv.setText(champions.getString(champions.getColumnIndex("name")));
            //

            LinearLayout ll = new LinearLayout(this);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(params1);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPadding(5,5,5,5);
            ll.addView(img);
            ll.addView(tv);
            tableRow.addView(ll);
            counter++;

            champions.moveToNext();
        }

    }
}
