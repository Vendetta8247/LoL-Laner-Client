package ua.com.vendetta8247.lollaner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
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

            final String ad = String.format("%.0f",champions.getFloat(champions.getColumnIndex("ad")));
            final String ap = new String("0");
            final String armor = String.format("%.0f",champions.getFloat(champions.getColumnIndex("armor")));
            final String mr = String.format("%.0f",champions.getFloat(champions.getColumnIndex("mr")));
            final String crit = String.format("%.0f",champions.getFloat(champions.getColumnIndex("crit")));
            final String as = String.format("%.3f",0.625f / (1 + champions.getFloat(champions.getColumnIndex("asoffset"))));
            final String cdr = "0";
            final String ms = String.format("%.0f",champions.getFloat(champions.getColumnIndex("ms")));

            //
            ImageView img = new ImageView(this);
           // LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            //img.setLayoutParams(params2);
            Resources resources = getResources();
            System.out.println(champions.getString(champions.getColumnIndex("key")).toLowerCase());
            int resourceId = resources.getIdentifier(champions.getString(champions.getColumnIndex("key")).toLowerCase(), "drawable",
                    getPackageName());
            img.setImageDrawable(getResources().getDrawable(resourceId));

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
            tv.setText(champions.getString(champions.getColumnIndex("key")));
            //

            LinearLayout ll = new LinearLayout(this);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
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

//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            counterCap = 6;
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            counterCap = 4;
//            ScrollView a = new ScrollView(this);
//        }
//        recreate();
//    }

}
