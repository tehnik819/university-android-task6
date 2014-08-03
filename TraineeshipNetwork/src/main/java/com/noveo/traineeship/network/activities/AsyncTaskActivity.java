package com.noveo.traineeship.network.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.noveo.traineeship.network.R;

import java.util.concurrent.TimeUnit;

public class AsyncTaskActivity extends ActionBarActivity {
    private static final String TAG = "async_task_activity";
    private TextView textView;
    private DialogAsyncTask currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        textView = (TextView) findViewById(R.id.text);
        currentTask = new DialogAsyncTask();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 3);
        } else {
            currentTask.execute(3);
        }
    }

    @Override
    protected void onDestroy() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }
        super.onDestroy();
    }

    private void setupSuccessfulText() {
        if (textView != null) {
            textView.setTextColor(getResources().getColor(R.color.pink));
            textView.setText(R.string.successful_text);
        }
    }

    private void setupFailText() {
        if (textView != null) {
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setText(R.string.fail_text);
        }
    }

    private class DialogAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AsyncTaskActivity.this);
            dialog.setTitle(R.string.async_task_text);
            dialog.setMessage(getString(R.string.loading_text));
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (currentTask != null) {
                        currentTask.cancel(true);
                    }

                    currentTask = null;
                }
            });
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            if (params == null || params.length == 0) {
                return false;
            }

            final int number = params[0];
            for (int i = 0; i < number; ++i) {
                if (isCancelled()) {
                    return false;
                }

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(number - i);
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            final String message = String.format(
                    "%s %d",
                    getString(R.string.loading_text),
                    values[0]
            );

            if (dialog != null) {
                dialog.setMessage(message);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result) {
                setupSuccessfulText();
            } else {
                setupFailText();
            }

            currentTask = null;
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            Log.d(TAG, "cancel");

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            setupFailText();
            currentTask = null;
        }
    }
}
