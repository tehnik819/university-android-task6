package com.noveo.traineeship.network.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class NumberService extends IntentService{
    public static final String ACTION = "com.noveo.traineeship.network";
    public static final String RESULT_KEY = "result";
    public static final String INPUT_KEY = "input_number";
    public static final String OUTPUT_KEY = "output_number";

    public NumberService() {
        super("Number");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Integer value = (Integer) intent.getSerializableExtra(INPUT_KEY);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publish(value + 100);
    }

    private void publish(Integer value) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(OUTPUT_KEY, value);
        intent.putExtra(RESULT_KEY, Activity.RESULT_OK);
        sendBroadcast(intent);
    }
}
