package de.thnuernberg.bme.faktenapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FactAdapter adapter;
    private FactsTable dbHelper;
    private List<Fact> factList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new FactsTable(this);
        loadFactsFromDb();

        adapter = new FactAdapter(this, factList, dbHelper);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                //finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, CreateActivity.class));
                //finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                //finish();
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.navigation_profile);
    }

    private void loadFactsFromDb() {
        Cursor cursor = dbHelper.getFacts();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("fact_title"));
            String text = cursor.getString(cursor.getColumnIndexOrThrow("fact_text"));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
            factList.add(new Fact(id, title, text, imagePath));
        }
        cursor.close();
    }
}
