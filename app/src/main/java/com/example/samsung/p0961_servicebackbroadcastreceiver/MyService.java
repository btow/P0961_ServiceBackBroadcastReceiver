package com.example.samsung.p0961_servicebackbroadcastreceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private String LOG_TAG = "myLogs";
    private String message = "";

    ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        message = "MyService onCreate()";
        Log.d(LOG_TAG, message);
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        message = "MyService onStartCommand()";
        Log.d(LOG_TAG, message);

        int time = intent.getIntExtra(MainActivity.PARAM_TIME, 1);
        int task = intent.getIntExtra(MainActivity.PARAM_TASK, 0);

        MyRun myRun = new MyRun(time, startId, task);
        executorService.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        message = "MyService onDestroy()";
        Log.d(LOG_TAG, message);
    }

    private class MyRun implements Runnable{

        private int time, startId, task;

        public MyRun(final int time, final int startId, final int task) {
            this.time = time;
            this.startId = startId;
            this.task = task;
            message = getString(R.string.my_run_) + startId + getString(R.string._create);
            Log.d(LOG_TAG, message);
        }

        @Override
        public void run() {

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            message = getString(R.string.my_run_) + startId + getString(R.string._start_time_) + time;
            Log.d(LOG_TAG, message);
            try {
                //Сообщение о старте задачи
                intent.putExtra(MainActivity.PARAM_TASK, task);
                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START);
                sendBroadcast(intent);
                //Эмуляция выполнения задачи
                TimeUnit.SECONDS.sleep(time);
                //Сообщение о финише задачи
                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH);
                intent.putExtra(MainActivity.PARAM_RESULT, time * 100);
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }

        private void stop() {
            message = getString(R.string.my_run_) + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId);
            Log.d(LOG_TAG, message);
        }
    }
}
