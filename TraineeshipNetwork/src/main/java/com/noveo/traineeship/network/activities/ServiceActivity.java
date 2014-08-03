package com.noveo.traineeship.network.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noveo.traineeship.network.R;
import com.noveo.traineeship.network.services.NumberService;

public class ServiceActivity extends ActionBarActivity {
    private static int count = 0;
    private TextView textView;
    private Button startServiceButton;

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && textView != null &&
                    bundle.getInt(NumberService.RESULT_KEY, 0) == Activity.RESULT_OK
            ) {
                textView.setText(getString(R.string.prefix_result) + bundle.getInt(NumberService.OUTPUT_KEY));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_layout);

        textView = (TextView) findViewById(R.id.text);
        startServiceButton = (Button) findViewById(R.id.start_service);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServiceListener();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(NumberService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void startServiceListener() {
        Intent intent = new Intent(this, NumberService.class);
        intent.putExtra(NumberService.INPUT_KEY, count);
        count++;
        startService(intent);
    }
}
