package com.android.mygame.finalproject_chengma_airbattlegame;

import android.graphics.Bitmap;

/**
 * Created by mcsin on 2015/12/16.
 */
public class enemyAircraft {

    private Bitmap bitmap;
    private float enemyX;
    private float enemyY;
    private boolean enemyDestroy;
    private long destroyTime=0;


    public enemyAircraft(Bitmap bitmap, float enemyX, float enemyY,boolean enemyDestroy) {
        this.bitmap = bitmap;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.enemyDestroy=enemyDestroy;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getEnemyX() {
        return enemyX;
    }

    public void setEnemyX(float enemyX) {
        this.enemyX = enemyX;
    }

    public float getEnemyY() {
        return enemyY;
    }

    public void setEnemyY(float enemyY) {
        this.enemyY = enemyY;
    }

    public boolean isEnemyDestroy() {
        return enemyDestroy;
    }

    public void setEnemyDestroy(boolean enemyDestroy) {
        this.enemyDestroy = enemyDestroy;
    }

    public long getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(long destroyTime) {
        this.destroyTime = destroyTime;
    }
}
