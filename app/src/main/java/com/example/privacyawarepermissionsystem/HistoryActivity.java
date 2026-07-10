package com.example.privacyawarepermissionsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        List<AppInfo> appList =
                databaseHelper.getAllApps();

        HistoryAdapter adapter = new HistoryAdapter(

                appList,

                app -> {

                    Intent intent = new Intent(
                            HistoryActivity.this,
                            DetailActivity.class
                    );

                    intent.putExtra("selectedApp", app);

                    startActivity(intent);

                }

        );

        recyclerView.setAdapter(adapter);

    }

}