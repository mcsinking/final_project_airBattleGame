package com.android.mygame.finalproject_chengma_airbattlegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcsin on 2015/12/2.
 */
public class GameView extends SurfaceView {



    //backgroundroll
    private Bitmap background;
    private Bitmap background2;
    private int dy, dy2 = -1280;
    private Rect srcRect;

    private Rect destRect;
    private Rect destRect2;

    private SurfaceHolder holder;
    private Bitmap red_target;
    private Bitmap bullet;
    private int lvUpBaseSpeedUp=0;

    private GameThread gthread = null;


    private float bulletX = -50.0f;
    private float bulletY = -101.0f;
    private float playerX;
    private float playerY;
    private float playerWidth;
    private float playerHeight;
//    private float moveSpeed=3.0f;
    private int lvNo=0;
    private int lvHarder=5000;
    private boolean lvUpActive=false;
    private boolean lvNextUpActive=false;
    private boolean lvHarderUpActive=false;
    private boolean playerlife=true;
    long refreshTime=0;
    private long fireTimer=0;
    private long destroyTime=0;
    private double generateEnemy=0;
    private double generateEmemySpeedTime=0;
    private List<bullet> bullets=new ArrayList<bullet>();
    private List<enemyAircraft> enemies=new ArrayList<>();

    private float forwardSpeed=3.0f;

    private int score = 0;
    private Paint scorePaint;

    public int getLvUpBaseSpeedUp() {
        return lvUpBaseSpeedUp;
    }

    public void setLvUpBaseSpeedUp(int lvUpBaseSpeedUp) {
        this.lvUpBaseSpeedUp = lvUpBaseSpeedUp;
    }

    public float getForwardSpeed() {
        return forwardSpeed;
    }

    public void setForwardSpeed(float forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public GameView(Context context) {
        super(context);


        //backgroundroll
        this.setFocusable(true);
        srcRect = new Rect(0, 0, 720, 1280);
        destRect = new Rect(0, dy, 720, 1280);
        destRect2 = new Rect(0, dy2,720, 1280);
        background = BitmapFactory.decodeResource(getResources(),
                R.drawable.background);
        background2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.background2);

        holder = getHolder();
        holder.addCallback( new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                red_target = BitmapFactory.decodeResource(getResources(), R.drawable.target_bullseye);
                bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);

                scorePaint = new Paint();
                scorePaint.setColor(Color.BLUE);
                scorePaint.setTextSize(65);

                makeThread();

                gthread.setRunning(true);
                gthread.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        } );
    }

    public void makeThread()
    {
        gthread = new GameThread(this);

    }

    public void killThread()
    {
        boolean retry = true;
        gthread.setRunning(false);
        while(retry)
        {
            try
            {
                gthread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLvNo() {
        return lvNo;
    }

    public void setLvNo(int lvNo) {
        this.lvNo = lvNo;
    }

    public long getFireTimer() {
        return fireTimer;
    }

    public void setFireTimer(long fireTimer) {
        this.fireTimer = fireTimer;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);

        //backgroundroll
        dy =dy +(int)(forwardSpeed*0.75);
        dy2 = dy2+(int)(forwardSpeed*0.75);
        destRect.set(0, dy, 720, 1280 + dy);
        destRect2.set(0, dy2, 720, 1280 + dy2);
        canvas.drawBitmap(background, srcRect, destRect, null);
        canvas.drawBitmap(background2, srcRect, destRect2, null);

        if (dy >= getHeight())
            dy = -1280;
        if (dy2 >= getHeight())
            dy2 = -1280;


        canvas.drawText("Lv:" + lvNo + " Score: " + String.valueOf(score), 10.0f, 65.0f, scorePaint);

        if (score%5==0&&score!=0&&lvUpActive==false){
            lvUpActive= true;
            lvNo++;
            forwardSpeed=forwardSpeed+2.0f;
            refreshTime=System.currentTimeMillis();

            for (int i=0;i<enemies.size();i++){


                enemies.get(i).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boom));
            }

            bullets=new ArrayList<>();



        }
        if ((refreshTime!=0)&&(System.currentTimeMillis()>refreshTime+300)){
            enemies=new ArrayList<>();
            refreshTime=0;
        }

        if (score%5!=0){
            lvUpActive=false;
        }

        if (lvNo%5==0&&lvNo>=5&&lvNextUpActive==false){
            lvNextUpActive=true;
            lvUpBaseSpeedUp++;
            forwardSpeed=3.0f+lvUpBaseSpeedUp;
        }
        if (lvNo%5!=0){
            lvNextUpActive=false;
            lvHarderUpActive=false;
        }


        // game over
        if ((System.currentTimeMillis()>getDestroyTime()+3000)&&playerlife==false) {

            Intent intent = new Intent();

            intent.setClass(getContext(), GameOverActivity.class);

            Bundle b = new Bundle();
            b.putString("Endscore", score + "");

            intent.putExtras(b);

            getContext().startActivity(intent);

            System.exit(0);
        }

        //game win
        if (lvNo==30){

            Intent intent1 = new Intent();

            intent1.setClass(getContext(), GameWinActivity.class);

            Bundle b = new Bundle();
            b.putString("winscore", score + "");

            intent1.putExtras(b);


            getContext().startActivity(intent1);

            System.exit(0);


        }

        //Game Levels
         if (System.currentTimeMillis()>generateEnemy+generateEmemySpeedTime){
                moveEnemies();
         }




    //drawenemies
    for (int i = 0; i < enemies.size(); i++) {


            if (enemies.get(i).isEnemyDestroy()!=true){


            enemies.get(i).setEnemyY(enemies.get(i).getEnemyY() + forwardSpeed);

            }else {

                if (System.currentTimeMillis()>enemies.get(i).getDestroyTime()+100){
                    enemies.get(i).setEnemyDestroy(false);
                    enemies.get(i).setEnemyX(getWidth() + 100.0f);
                    enemies.get(i).setEnemyY(getHeight() + 100.0f);
                    enemies.get(i).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target_bullseye));
                }
            }

                canvas.drawBitmap(enemies.get(i).getBitmap(), enemies.get(i).getEnemyX(), enemies.get(i).getEnemyY(), null);
    }

        //drawbullet
        for (int i=bullets.size()-1;i>=0;i--){
            if(bullets.get(i).isBulletYActivy())
            {
               bullets.get(i).setBulletY(bullets.get(i).getBulletY() - 15);

                if(bullets.get(i).getBulletY()<80){
                    bullets.get(i).setBulletX(-50.0f);
                    bullets.get(i).setBulletY(-101.0f);
                   bullets.get(i).setBulletYActivy(false);
                //   bullets.remove(0);
                } else
                {
                    canvas.drawBitmap(bullets.get(i).getBitmap(), bullets.get(i).getBulletX(), bullets.get(i).getBulletY(), null);
                }
            }


          //hit issues
            for (int j=enemies.size()-1;j>=0;j--){

         //hit the target
            if ( bullets.get(i).getBulletX() >= enemies.get(j).getEnemyX() && bullets.get(i).getBulletX() <= enemies.get(j).getEnemyX() + enemies.get(j).getBitmap().getWidth()
                    && bullets.get(i).getBulletY() <= enemies.get(j).getEnemyY() + enemies.get(j).getBitmap().getHeight() &&  bullets.get(i).getBulletY() >= enemies.get(j).getEnemyY()
                    )
            {
                enemies.get(j).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boom));
                if (enemies.get(j).isEnemyDestroy()==false){
                    score++;
                    enemies.get(j).setEnemyDestroy(true);
                    enemies.get(j).setDestroyTime(System.currentTimeMillis());
                }
                bullets.get(i).setBulletYActivy(false);
                bullets.get(i).setBulletX(-50.0f);
                bullets.get(i).setBulletY(-101.0f);
              //  bullets.remove(0);
            }


            //enemy aircraft hit the player
            if (playerlife==true&&enemies.get(j).getEnemyX()+enemies.get(j).getBitmap().getWidth()>playerX&&enemies.get(j).getEnemyX()+enemies.get(j).getBitmap().getWidth()<playerX+playerWidth&&enemies.get(j).getEnemyY()+enemies.get(j).getBitmap().getHeight()>playerY
                    &&enemies.get(j).getEnemyY()+enemies.get(j).getBitmap().getHeight()<playerY+playerHeight){
                enemies.get(j).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boom));
                enemies.get(j).setEnemyDestroy(true);
                enemies.get(j).setDestroyTime(System.currentTimeMillis());
                setDestroyTime(System.currentTimeMillis());
                playerlife=false;
            }

            }


        }




    }

    private void moveEnemies() {
        enemies.add(new enemyAircraft(red_target.copy(red_target.getConfig(), true),(float) Math.random() * (getWidth()-red_target.getWidth()),65.0f,false));
        if (lvNo%5==0&&lvNo>=5&&lvHarder>=0&&lvHarderUpActive==false){
            lvHarderUpActive=true;
            lvHarder=lvHarder-800;
        }



        generateEnemy=System.currentTimeMillis();
        generateEmemySpeedTime=700+Math.random()*lvHarder;

    }

    public int getLvHarder() {
        return lvHarder;
    }

    public void setLvHarder(int lvHarder) {
        this.lvHarder = lvHarder;
    }

    public double getGenerateEmemySpeedTime() {
        return generateEmemySpeedTime;
    }

    public void setGenerateEmemySpeedTime(double generateEmemySpeedTime) {
        this.generateEmemySpeedTime = generateEmemySpeedTime;
    }


    public long getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(long destroyTime) {
        this.destroyTime = destroyTime;
    }


    public void fire(float gunX,float gunY,int gunWidth,int gunHeight)
    {


        bulletX = gunX +gunWidth/2;
        bulletY = gunY+gunHeight/3;

        bullets.add(new bullet(bullet.copy(bullet.getConfig(), true), bulletX,bulletY,true));

        fireTimer=System.currentTimeMillis();


    }

    public void currentPlayerPosition(float playerX,float playerY,float playerWidth,float playerHeight){
              this.playerX=playerX;
              this.playerY=playerY;
              this.playerWidth=playerWidth;
              this.playerHeight=playerHeight;

    }




    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public void onDestroy()
    {
        red_target.recycle();
        red_target = null;
        System.gc();
    }

    public boolean isPlayerlife() {
        return playerlife;
    }

    public void setPlayerlife(boolean playerlife) {
        this.playerlife = playerlife;
    }


}
