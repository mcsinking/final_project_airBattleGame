package com.android.mygame.finalproject_chengma_airbattlegame;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main2Activity extends Activity{

    static Main2Activity instance=null;

    private GameView gv;
    private Button shootButton;
    private MediaPlayer song2;
    private MediaPlayer coo;
    private boolean fireSound=true;
    private  Button musicButton;

    public Button getShootButton() {
        return shootButton;
    }

    public void setShootButton(Button shootButton) {
        this.shootButton = shootButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //gets rid of the title at the top of the app


        gv = new GameView(this);



        testA mtestA=new testA();
        mtestA.run();

        mtestA.setOnTestListening(new testA.OnTestListening() {

            @Override
            public void TestListening(int i) {
                if (gv.isPlayerlife() == false) {
                    shootButton.setX(gv.getPlayerX());

                    shootButton.setY(gv.getPlayerY());
                    shootButton.setBackgroundResource(R.drawable.boom);
                    shootButton.setText("Dead");
                }


            }
        });




        musicButton = new Button(this);

        musicButton.setPadding(0, 0, 0, 10);
        musicButton.setText("Vol On");
        musicButton.setOnClickListener(musicOnButtonListener);


        Button EndGameButton = new Button(this);

        EndGameButton.setPadding(0, 0, 0, 10);
        EndGameButton.setText("Exit");
        EndGameButton.setOnClickListener(EndGameButtonListener);


        LinearLayout musicVolumeButtonLayout = new LinearLayout(this);
        musicVolumeButtonLayout.setGravity(Gravity.TOP | Gravity.RIGHT);
        musicVolumeButtonLayout.addView(musicButton);
        musicVolumeButtonLayout.addView(EndGameButton);




        shootButton = new Button(this);
        shootButton.setWidth(75);
        shootButton.setHeight(75);
        shootButton.setBackgroundColor(Color.LTGRAY);
        shootButton.setTextColor(Color.RED);
        shootButton.setTextSize(20);
        shootButton.setBackgroundResource(R.drawable.aircraft);
        shootButton.setText("Player");

        shootButton.setGravity(Gravity.CENTER);

        FrameLayout GameLayout = new FrameLayout(this);
        LinearLayout ButtonLayout = new LinearLayout(this);
        ButtonLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        ButtonLayout.addView(shootButton);

        GameLayout.addView(gv);
        GameLayout.addView(ButtonLayout);
        GameLayout.addView(musicVolumeButtonLayout);

        setContentView(GameLayout);


    }

    public View.OnClickListener EndGameButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Main2Activity.this, MainActivity.class);

            startActivity(intent);
            gv.onDestroy();
        }
    };


    public View.OnClickListener musicOnButtonListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            if(!song2.isPlaying()) {
                song2.start();
                fireSound = true;
                musicButton.setText("Vol On");

            }else if(song2.isPlaying()) {
                song2.pause();
                fireSound=false;
                musicButton.setText("Vol Off");
            }
        }
    };


    //backgroundsounds
    public void allocateSounds()
    {
        if(song2 == null)
            song2 = MediaPlayer.create(this.getBaseContext(), R.raw.airbattle_world3);
        song2.setVolume(0.7f, 0.7f);
        song2.setOnPreparedListener(song2PListener);
        song2.setOnCompletionListener(song2CListener);

        if(coo == null) coo = MediaPlayer.create(this.getBaseContext(), R.raw.short_lazer);
        coo.setVolume(0.2f, 0.2f);
    }

    private MediaPlayer.OnPreparedListener song2PListener = new MediaPlayer.OnPreparedListener()
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            playSong2();
        }
    };

    private MediaPlayer.OnCompletionListener song2CListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            playSong2();
        }
    };

    public void playSong2()
    {
        if (song2.isPlaying())
        {
            song2.pause();
        }

        if(song2.getCurrentPosition() != 1)
        {
            song2.seekTo(1);
        }
        song2.start();
    }

    public void playCoo()
    {


        if (!coo.isPlaying())
        {
            if(coo.getCurrentPosition() != 1)
            {
                coo.seekTo(1);
            }
            coo.start();
        }
    }

    public void deallocateSounds()
    {

        if (song2.isPlaying())
        {
            song2.stop();
        }

        if (coo.isPlaying())
        {
            coo.stop();
        }


        if (!(song2 == null))
        {
            song2.release();
            song2 = null;
        }

        if (!(coo == null))
        {
            coo.release();
            coo = null;
        }


        song2CListener = null;
        song2PListener = null;
    }


    @Override
    protected void onPause()
    {
        SharedPreferences prefs = getSharedPreferences("AirBattleData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String tempscore=gv.getScore()+"";
        String tempGenerateEmemySpeedTime=gv.getGenerateEmemySpeedTime()+"";
        String tempLvHarder;
        String templvUpBaseSpeedUp;
        String tempforwardSpeed;
        String templv;
        if (gv.getLvNo()%5==0&&gv.getLvNo()>=5){
            templvUpBaseSpeedUp=gv.getLvUpBaseSpeedUp()-1+"";
            tempLvHarder=gv.getLvHarder()+800+"";
        }else {
            templvUpBaseSpeedUp=gv.getLvUpBaseSpeedUp()+"";
            tempLvHarder=gv.getLvHarder()+"";
        }

        if (gv.getScore()%5==0&&gv.getScore()!=0){
            templv=gv.getLvNo()-1+"";
            tempforwardSpeed=gv.getForwardSpeed()-3-gv.getLvUpBaseSpeedUp()+"";
        }else {
            templv=gv.getLvNo()+"";
            tempforwardSpeed=gv.getForwardSpeed()+"";
        }



        editor.putString("AirBattleScore", tempscore);
        editor.putString("GenerateEmemySpeedTime", tempGenerateEmemySpeedTime);
        editor.putString("LvHarder", tempLvHarder);
        editor.putString("AirBattlelv", templv);
        editor.putString("forwardSpeed", tempforwardSpeed);
        editor.putString("lvUpBaseSpeedUp", templvUpBaseSpeedUp);

        editor.commit();

        deallocateSounds();
        super.onPause();
        gv.killThread(); //Notice this reaches into the GameView object and runs the killThread mehtod.
    }

    @Override
    protected void onResume()
    {

        SharedPreferences prefs = getSharedPreferences("AirBattleData",MODE_PRIVATE);

        String retrievedHighScore = prefs.getString("AirBattleScore", "0");
        String retrievedlv = prefs.getString("AirBattlelv", "0");
        String retrievedGenerateEmemySpeedTime = prefs.getString("GenerateEmemySpeedTime", "0");
        String retrievedLvHarder = prefs.getString("LvHarder", "0");
        String retrievedForwardSpeed = prefs.getString("forwardSpeed", "0");
        String retrievedlvUpBaseSpeedUp=prefs.getString("lvUpBaseSpeedUp","0");

        gv.setScore(Integer.valueOf(retrievedHighScore));
        gv.setGenerateEmemySpeedTime(Float.parseFloat(retrievedGenerateEmemySpeedTime));
        gv.setLvHarder(Integer.valueOf(retrievedLvHarder));
        gv.setLvNo(Integer.valueOf(retrievedlv));
        gv.setForwardSpeed(Float.parseFloat(retrievedForwardSpeed));
        gv.setLvUpBaseSpeedUp(Integer.valueOf(retrievedlvUpBaseSpeedUp));

        super.onResume();

        allocateSounds();
    }

    @Override
    protected void onDestroy() {

        SharedPreferences prefs = getSharedPreferences("AirBattleData", MODE_PRIVATE);
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

        super.onDestroy();
        gv.onDestroy();

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

            if (fireSound==true){
                playCoo();
            }
            gv.currentPlayerPosition(shootButton.getX(), shootButton.getY(),shootButton.getWidth(),shootButton.getHeight());
            float touchedX = e.getX();
            float touchedY = e.getY();
            if (touchedX > shootButton.getX() + shootButton.getWidth()/2)
                shootButton.setX(shootButton.getX() + 10.0f);
            if (touchedX < shootButton.getX() + shootButton.getWidth()/2)
                shootButton.setX(shootButton.getX() - 10.0f);

            if (touchedY > shootButton.getY() + shootButton.getHeight()/2)
                shootButton.setY(shootButton.getY() + 10.0f);
            if (touchedY < shootButton.getY() + shootButton.getHeight()/2)
                shootButton.setY(shootButton.getY() - 10.0f);


            if ((System.currentTimeMillis() > (gv.getFireTimer() + 650)) || gv.getFireTimer() == 0)
                gv.fire(shootButton.getX(), shootButton.getY(), shootButton.getWidth(), shootButton.getHeight());

        return true;
    }

}
