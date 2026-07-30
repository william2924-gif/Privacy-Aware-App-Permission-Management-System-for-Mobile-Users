package com.example.privacyawarepermissionsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtEmptyHistory;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView =
                findViewById(R.id.recyclerHistory);
        txtEmptyHistory =
                findViewById(R.id.txtEmptyHistory);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        databaseHelper =
                new DatabaseHelper(
                        getApplicationContext());

        loadApplications();
    }

    private void loadApplications() {
        List<AppInfo> appList =
                databaseHelper.getAllApps();

        boolean isEmpty =
                appList.isEmpty();

        txtEmptyHistory.setVisibility(
                isEmpty
                        ? View.VISIBLE
                        : View.GONE);

        recyclerView.setVisibility(
                isEmpty
                        ? View.GONE
                        : View.VISIBLE);

        HistoryAdapter adapter =
                new HistoryAdapter(
                        appList,
                        app -> {
                            Intent intent =
                                    new Intent(
                                            HistoryActivity.this,
                                            DetailActivity.class);

                            intent.putExtra(
                                    "selectedApp",
                                    app);

                            startActivity(intent);
                        });

        recyclerView.setAdapter(adapter);
    }
}
