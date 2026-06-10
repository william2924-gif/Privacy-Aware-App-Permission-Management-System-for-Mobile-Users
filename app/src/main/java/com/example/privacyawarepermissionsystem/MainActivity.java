package com.example.privacyawarepermissionsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtResults;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        Button btnScan = findViewById(R.id.btnScan);
        txtResults = findViewById(R.id.txtResults);

        btnScan.setOnClickListener(v -> scanApps());
    }

    private void scanApps() {
        AppScanner scanner = new AppScanner(this);
        List<String> apps = scanner.scanInstalledApps();

        StringBuilder builder = new StringBuilder();
        builder.append("Total apps scanned: ").append(apps.size()).append("\n\n");

        int limit = Math.min(apps.size(), 20);

        for (int i = 0; i < limit; i++) {
            builder.append(apps.get(i)).append("\n");
        }

        txtResults.setText(builder.toString());
    }
}