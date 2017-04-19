package com.example.samsung.p0961_servicebackbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "myLogs";
    private String message = "";

    final int TASK1_CODE = 1,
            TASK2_CODE = 2,
            TASK3_CODE = 3;

    public final static int STATUS_START = 100,
                            STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time",
                                PARAM_TASK = "task",
                                PARAM_RESULT = "result",
                                PARAM_STATUS = "status",
                                BROADCAST_ACTION = "com.example.samsung.p0961_servicebackbroadcastreceiver";
    TextView tvTask1, tvTask2, tvTask3;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask1.setText(R.string.task_1);
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask2.setText(R.string.task_2);
        tvTask3 = (TextView) findViewById(R.id.tvTask3);
        tvTask3.setText(R.string.task_3);
        //Создание BroadcastReceiver'а
        broadcastReceiver = new BroadcastReceiver() {
            //Действи при получении сообщений
            @Override
            public void onReceive(Context context, Intent intent) {

                int task = intent.getIntExtra(PARAM_TASK, 0),
                        status = intent.getIntExtra(PARAM_STATUS, 0);
                message = "onReceive: task = " + task + ", status = " + status;
                Log.d(LOG_TAG, message);
                //Перехват сообщений о старте задач
                if (status == STATUS_START) {

                    switch (task) {

                        case TASK1_CODE:
                            tvTask1.setText(getString(R.string.task_1) + getString(R.string._start));
                            break;
                        case TASK2_CODE:
                            tvTask2.setText(getString(R.string.task_2) + getString(R.string._start));
                            break;
                        case TASK3_CODE:
                            tvTask3.setText(getString(R.string.task_3) + getString(R.string._start));
                            break;
                        default:
                            break;
                    }
                }
                //Перехват сообщений о финише задач
                if (status == STATUS_FINISH) {

                    int result = intent.getIntExtra(PARAM_RESULT, 0);

                    switch (task) {

                        case TASK1_CODE:
                            tvTask1.setText(getString(R.string.task_1) + getString(R.string._finish_result) + result);
                            break;
                        case TASK2_CODE:
                            tvTask2.setText(getString(R.string.task_2) + getString(R.string._finish_result) + result);
                            break;
                        case TASK3_CODE:
                            tvTask3.setText(getString(R.string.task_3) + getString(R.string._finish_result) + result);
                            break;
                        default:
                            break;
                    }

                }
            }
        };
        //Создание фильтра для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        //Регистрация (включение) BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Дерегистрация (выключение) BroadcastReceiver
        unregisterReceiver(broadcastReceiver);
    }

    public void onClickStart(View view) {

        Intent intent;
        //Создание Intent'a для вызова сервиса и передача в него параметра времени и кода задачи 1
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_TASK, TASK1_CODE);
        //Запуск сервиса
        startService(intent);
        //Создание Intent'a для вызова сервиса и передача в него параметра времени и кода задачи 2
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_TASK, TASK2_CODE);
        //Запуск сервиса
        startService(intent);
        //Создание Intent'a для вызова сервиса и передача в него параметра времени и кода задачи 3
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_TASK, TASK3_CODE);
        //Запуск сервиса
        startService(intent);
    }
}
