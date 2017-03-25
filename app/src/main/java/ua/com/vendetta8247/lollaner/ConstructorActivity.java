package ua.com.vendetta8247.lollaner;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ConstructorActivity extends AppCompatActivity {

    ImageView addFirstChampionButton;
    ImageView secondChampionImage;
    TextView name, secondName, tags, textViewAD, textViewAP, textViewArmor, textViewMR, textViewCrit, textViewAS, textViewCDR, textViewMS;
    String championName;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor);

        database = new DBHelper(this);

        //new StaticDataLoader(database).execute();

        name = (TextView) findViewById(R.id.tvname);
        secondName = (TextView) findViewById(R.id.tvfullname);
        tags = (TextView) findViewById(R.id.tvrole);

        textViewAD = (TextView) findViewById(R.id.textViewAD);
        textViewAP = (TextView) findViewById(R.id.textViewAP);
        textViewArmor = (TextView) findViewById(R.id.textViewArmor);
        textViewMR = (TextView) findViewById(R.id.textViewMR);
        textViewCrit = (TextView) findViewById(R.id.textViewCrit);
        textViewAS = (TextView) findViewById(R.id.textViewAS);
        textViewCDR = (TextView) findViewById(R.id.textViewCDR);
        textViewMS = (TextView) findViewById(R.id.textViewMS);

        secondChampionImage = (ImageView) findViewById(R.id.secondChampionImage);


        if(savedInstanceState != null)
        {
            name.setText(savedInstanceState.getString("name"));
            championName = savedInstanceState.getString("key");
            if(championName!=null) {
                Resources resources = getResources();
                int resourceId = resources.getIdentifier(savedInstanceState.getString("key"), "drawable",
                        getPackageName());

                secondChampionImage.setImageDrawable(getResources().getDrawable(resourceId));
                secondName.setText(savedInstanceState.getString("title"));
                tags.setText(savedInstanceState.getString("tags"));

                textViewAD.setText(savedInstanceState.getString("textViewAD"));
                textViewAP.setText(savedInstanceState.getString("textViewAP"));
                textViewArmor.setText(savedInstanceState.getString("textViewArmor"));
                textViewMR.setText(savedInstanceState.getString("textViewMR"));
                textViewCrit.setText(savedInstanceState.getString("textViewCrit"));
                textViewAS.setText(savedInstanceState.getString("textViewAS"));
                textViewCDR.setText(savedInstanceState.getString("textViewCDR"));
                textViewMS.setText(savedInstanceState.getString("textViewMS"));
            }

        }
        addFirstChampionButton = (ImageView) findViewById(R.id.addFirstChampionButton);
        addFirstChampionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CharacterSelectorActivity.class);
                startActivityForResult(intent, 228, null);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==228 && resultCode == RESULT_OK)
        {
            System.out.println("YAY");
            name.setText(data.getStringExtra("key"));
            championName = data.getStringExtra("key").toLowerCase();
            secondName.setText(data.getStringExtra("title"));
            tags.setText(data.getStringExtra("tags"));
            textViewAD.setText("AD: " + data.getStringExtra("textViewAD"));
            textViewAP.setText("AP: " + data.getStringExtra("textViewAP"));
            textViewArmor.setText("Arm: " + data.getStringExtra("textViewArmor"));
            textViewMR.setText("MR: " + data.getStringExtra("textViewMR"));
            textViewCrit.setText("Crit: "  + data.getStringExtra("textViewCrit"));
            textViewAS.setText("AS: " + data.getStringExtra("textViewAS"));
            textViewCDR.setText("CDR: " + data.getStringExtra("textViewCDR"));
            textViewMS.setText("MS: " + data.getStringExtra("textViewMS"));

            Resources resources = getResources();
            System.out.println("image view = " + secondChampionImage + " key = " + data.getStringExtra("key").toLowerCase());
            int resourceId = resources.getIdentifier(data.getStringExtra("key").toLowerCase(), "drawable",
                    getPackageName());

            secondChampionImage.setImageDrawable(getResources().getDrawable(resourceId));
            System.out.println(data.getStringExtra("key"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",(String)name.getText());
        if(championName!= null) {
            outState.putString("key", championName);
            outState.putString("title", (String)secondName.getText());
            outState.putString("tags", (String)tags.getText());

            outState.putString("textViewAD", (String)textViewAD.getText());
            outState.putString("textViewAP", (String)textViewAP.getText());
            outState.putString("textViewArmor", (String)textViewArmor.getText());
            outState.putString("textViewMR", (String)textViewMR.getText());
            outState.putString("textViewCrit", (String)textViewCrit.getText());
            outState.putString("textViewAS", (String)textViewAS.getText());
            outState.putString("textViewCDR", (String)textViewCDR.getText());
            outState.putString("textViewMS", (String)textViewMS.getText());

        }
    }
}
