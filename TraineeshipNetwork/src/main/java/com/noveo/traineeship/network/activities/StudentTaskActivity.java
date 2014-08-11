package com.noveo.traineeship.network.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.noveo.traineeship.network.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StudentTaskActivity extends ActionBarActivity {
    private ListView list;
    private LoadNewsDialog loadNewsDialog;
    private ArrayList<String> items;
    private static final String KEY_ITEMS = "com.noveo.traineeship.network.activities.items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_task);
        list = (ListView) findViewById(R.id.list);
        if(savedInstanceState == null) {
            loadNewsDialog = new LoadNewsDialog();
            loadNewsDialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> arrayList = savedInstanceState.getStringArrayList(KEY_ITEMS);
        if(arrayList != null) {
            taskResult(arrayList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_ITEMS, items);
    }

    public void taskResult(ArrayList<String> result) {
        items = result;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static class LoadNewsDialog extends DialogFragment {
        private volatile ArrayList<String> list;
        private ProgressBar progressBar;
        private TextView loadState;

        public LoadNewsDialog() {
            setRetainInstance(true);
            task.execute("http://androidtraining.noveogroup.com/news/getAll");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_load_news, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            loadState = (TextView) view.findViewById(R.id.loadText);
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if(!((StudentTaskActivity)getActivity()).isOnline()) {
                task.cancel(true);
                progressBar.setVisibility(View.GONE);
                loadState.setText(getText(R.string.no_connetion));
                ((StudentTaskActivity)getActivity()).taskResult(new ArrayList<String>());
            }
            if(list != null) {
                showResult();
            }
        }

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setDismissMessage(null);
            super.onDestroyView();
            progressBar = null;
            loadState = null;
        }

        private void showResult() {
            if(getActivity() != null) {
                ((StudentTaskActivity) getActivity()).taskResult(list);
            }
            dismiss();
        }

        private AsyncTask<String, Void, ArrayList<String>> task = new AsyncTask<String, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(String... strings) {
                HttpURLConnection connection;
                try {
                    connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                    String json = read(connection.getInputStream());
                    connection.disconnect();
                    JSONArray jsonArray = new JSONArray(json);
                    ArrayList<String> list = new ArrayList<String>();
                    for(int i = 0;i < jsonArray.length(); i++) {
                        list.add(jsonArray.getJSONObject(i).getString("title"));
                    }
                    return list;
                } catch (IOException e) {
                    return new ArrayList<String>();
                } catch (JSONException e) {
                    return new ArrayList<String>();
                }
            }

            private String read(InputStream is) throws IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int val;
                while ((val = is.read()) != -1) {
                    baos.write(val);
                }
                is.close();
                return new String(baos.toByteArray());
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                list = strings;
                showResult();
            }
        };
    }
}
