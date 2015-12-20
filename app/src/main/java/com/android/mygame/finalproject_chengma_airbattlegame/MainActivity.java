package com.android.mygame.finalproject_chengma_airbattlegame;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private MediaPlayer song1;

    private Button startButton;

    private Button continueGame;

    private Button TopScore;

    SQLiteDatabase mydatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton=(Button)findViewById(R.id.bn);
        continueGame=(Button)findViewById(R.id.cg);
        TopScore=(Button)findViewById(R.id.ts);
        startButton.setOnClickListener(startGameOnClickListener);
        continueGame.setOnClickListener(startGameOnClickListener);

        TopScore.setOnClickListener(btnOnClickListener);
    }

    Button.OnClickListener startGameOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            SharedPreferences prefs = getSharedPreferences("AirBattleData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            String retrievedHighScore = prefs.getString("AirBattleScore", "0");



            if (v==startButton){




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


                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }else if (v==continueGame&&Integer.valueOf(retrievedHighScore)!=0){
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }

        }
    };

    Button.OnClickListener btnOnClickListener
            = new Button.OnClickListener(){


        @Override
        public void onClick(View v) {



            if (v==TopScore){


                mydatabase = openOrCreateDatabase("myDatabase",MODE_PRIVATE,null);
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Test(playerName VARCHAR,score int);");

                Cursor resultSet = mydatabase.rawQuery("Select * from Test order by score desc", null);
                int rowCount = resultSet.getCount();

                resultSet.moveToFirst();

                String displayString = "";
                for(int i = 0; i< rowCount; i++)
                {

                    int order=i+1;
                    displayString = displayString +"No:"+order+" Name:"+ resultSet.getString(0);
                    displayString = displayString + " ";
                    displayString = displayString+"Score:" + resultSet.getString(1);
                    displayString = displayString + "\n";
                    resultSet.moveToNext();
                }

                TextView topRes=(TextView)findViewById(R.id.tp);

                topRes.setText(displayString);




            }



        }
    };

    Button.OnClickListener loadGameOnClickListener
            = new Button.OnClickListener(){


        @Override
        public void onClick(View v) {

        }
    };

    public void allocateSong1()
    {
        if(song1 == null)
            song1 = MediaPlayer.create(this.getBaseContext(), R.raw.menu);
        song1.setOnPreparedListener(song1PListener);
        song1.setOnCompletionListener(song1CListener);
    }

    private MediaPlayer.OnPreparedListener song1PListener = new MediaPlayer.OnPreparedListener()
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            playSong1();
        }
    };

    private MediaPlayer.OnCompletionListener song1CListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            playSong1();
        }
    };

    public void playSong1()
    {
        if (song1.isPlaying())
        {
            song1.pause();
        }
        if(song1.getCurrentPosition() != 1)
        {
            song1.seekTo(1);
        }
        song1.start();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        allocateSong1();
    }


    @Override
    protected void onPause()
    {
        deallocateSong1();
        super.onPause();
    }

    public void deallocateSong1()
    {

        if (song1.isPlaying())
        {
            song1.stop();
        }
        if (!(song1 == null))
        {
            song1.release();
            song1 = null;
        }
        song1CListener = null;
        song1PListener = null;
    }






}