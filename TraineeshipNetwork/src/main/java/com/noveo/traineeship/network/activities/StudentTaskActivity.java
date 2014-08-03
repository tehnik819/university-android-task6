package com.noveo.traineeship.network.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.noveo.traineeship.network.R;

public class StudentTaskActivity extends ActionBarActivity {
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_task);
        list = (ListView) findViewById(R.id.list);

    }
}
