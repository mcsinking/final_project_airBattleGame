package com.android.mygame.finalproject_chengma_airbattlegame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GameOverActivity extends Activity {

    SQLiteDatabase mydatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView tv =(TextView)findViewById(R.id.sr);
        tv.setText("Your Scores are: " + getIntent().getExtras().getString("Endscore"));



        Button savedButton=(Button)findViewById(R.id.saved);

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("AirBattleData",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String tempscore=Integer.toString(0);
                String templv=Integer.toString(0);
                String tempGenerateEmemySpeedTime=Integer.toString(0);
                String tempLvHarder=Integer.toString(5000);
                String tempForwardSpeed=Integer.toString(3);
                String templvUpBaseSpeedUp=Integer.toString(0);

                editor.putString("AirBattleScore", tempscore);
                editor.putString("GenerateEmemySpeedTime", tempGenerateEmemySpeedTime);
                editor.putString("LvHarder", tempLvHarder);
                editor.putString("AirBattlelv", templv);
                editor.putString("forwardSpeed", tempForwardSpeed);
                editor.putString("lvUpBaseSpeedUp",templvUpBaseSpeedUp);

                editor.commit();



                String rString = getIntent().getExtras().getString("Endscore");

                EditText playerName=(EditText)findViewById(R.id.pn);


                mydatabase = openOrCreateDatabase("myDatabase",MODE_PRIVATE,null);
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Test(playerName VARCHAR,score int);");
                mydatabase.execSQL("INSERT INTO Test VALUES('" + playerName.getText().toString() + "','" + rString + "');");

                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });








    }

}
