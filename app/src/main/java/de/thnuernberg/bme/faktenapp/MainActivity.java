package de.thnuernberg.bme.faktenapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements FactFragment.OnFactInteractionListener {

    private static final int CONTENT_VIEW_ID = 101010;

    FactsTable factsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factsTable = new FactsTable(this);
        factsTable.addFact("Wusstest du schon?", "Bienen können Farben sehen!", "C:\\Users\\ju\\AndroidStudioProjects\\FaktenApp\\app\\src\\main\\res\\drawable\\hai.jpg");


        TextView textView = findViewById(R.id.textView);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        textView.setText(formattedDate);

        // Hinzufügen des Fact-Fragments
        FactFragment factFragment = FactFragment.newInstance(
                "Wusstest du schon?", "Bienen können Farben sehen!", String.valueOf(R.drawable.hai)); // Ersetze durch dein Bild

        getSupportFragmentManager().beginTransaction().replace(R.id.fact_container, factFragment).commit();

        // Hinzufügen der Navbar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
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
                startActivity(new Intent(this, ProfileActivity.class));
                //finish();
                return true;
            }
            return false;
        });
    }

    private void createDB() {

        factsTable = new FactsTable(this);
    }

    @Override
    public void onFactLiked() {
        Toast.makeText(this, "das wars für heute, komm Morgen wieder", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFactDisliked() {
        Toast.makeText(this, "das wars für heute, komm Morgen wieder", Toast.LENGTH_SHORT).show();
    }
}