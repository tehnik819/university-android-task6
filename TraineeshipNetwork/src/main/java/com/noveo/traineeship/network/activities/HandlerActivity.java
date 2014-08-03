package com.noveo.traineeship.network.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noveo.traineeship.network.R;

public class HandlerActivity extends ActionBarActivity{
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        textView = (TextView) findViewById(R.id.text);

        final Button crashMeButton = (Button) (findViewById(R.id.crash_me));
        final Button viewPostButton = (Button) (findViewById(R.id.view_post));
        final Button runUiButton = (Button) (findViewById(R.id.run_ui));
        final Button handlerButton = (Button) (findViewById(R.id.handler));

        crashMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crashMeListener();
            }
        });

        viewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPostListener();
            }
        });

        runUiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runUiListener();
            }
        });

        handlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerListener();
            }
        });
    }

    private void crashMeListener() {
        new Thread() {
            @Override
            public void run() {
                textView.setText(R.string.crash_me_text);
            }
        }.start();
    }

    private void viewPostListener() {
        new Thread() {
            @Override
            public void run() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.view_post_text);
                    }
                });
            }
        }.start();
    }

    private void runUiListener() {
        new Thread() {
            @Override
            public void run() {
                HandlerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.run_ui_text);
                    }
                });
            }
        }.start();
    }

    private void handlerListener() {
        new Thread() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.handler_text);
                    }
                });
            }
        }.start();
    }
}
