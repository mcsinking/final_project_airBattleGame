package com.android.mygame.finalproject_chengma_airbattlegame;

import java.util.Timer;
import java.util.TimerTask;



import android.os.Handler;
import android.os.Message;
public class testA {

    Timer mTimer=new Timer();
    TimerTask task=new TimerTask(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();

            handler.sendMessage(message);

        }

    };
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if(mOnTestListening!=null){
                mOnTestListening.TestListening(0);
            }

            super.handleMessage(msg);
        }

    };
    public void run(){
        mTimer.schedule(task, 100,100);//every 100ms execute handler
    }
    public interface OnTestListening{
        void TestListening(int i);
    }

    OnTestListening mOnTestListening=null;
    public void setOnTestListening(OnTestListening e){
        mOnTestListening=e;
    }
}
