package de.thnuernberg.bme.faktenapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Random;
import java.util.random.RandomGenerator;


public class MainActivity extends AppCompatActivity implements FactFragment.OnFactInteractionListener {

    private static final int CONTENT_VIEW_ID = 101010;

    FactsTable factsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factsTable = new FactsTable(this);
        //factsTable.addFact("Wusstest du schon?", "Bienen können Farben sehen!", "C:\\Users\\ju\\AndroidStudioProjects\\FaktenApp\\app\\src\\main\\res\\drawable\\hai.jpg");


        TextView textView = findViewById(R.id.textView);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        textView.setText(formattedDate);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if ( 5 < hour && hour < 12) {
            greeting = "Guten Morgen!";
        } else if (hour < 18) {
            greeting = "Guten Tag!";
        } else if (hour < 21){
            greeting = "Guten Abend!";
        }
        else {
            greeting = "Gute Nacht!";
        }
        TextView greetingView = findViewById(R.id.textView2);
        greetingView.setText(greeting);

        nextFact();

        // Hinzufügen des Fact-Fragments
        //FactFragment factFragment = FactFragment.newInstance(
        //        "Wusstest du schon?", "Bienen können Farben sehen!", String.valueOf(R.drawable.hai)); // Ersetze durch dein Bild

        //getSupportFragmentManager().beginTransaction().replace(R.id.fact_container, factFragment).commit();

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

    public void nextFact() {

        Cursor factCursor;
        factCursor = factsTable.getFacts();
        int fact_counter = new Random().nextInt(factCursor.getCount());

        if (factCursor != null && factCursor.moveToPosition(fact_counter)) {

            String title = factCursor.getString(factCursor.getColumnIndexOrThrow("fact_title"));
            Log.v("title", "" + title);
            String text = factCursor.getString(factCursor.getColumnIndexOrThrow("fact_text"));
            Log.v("text", "" + text);
            String imagePath = factCursor.getString(factCursor.getColumnIndexOrThrow("image_path"));
            if (imagePath == null || imagePath.isEmpty()) {
                // Verwende einen speziellen String, um später das Drawable zu laden
                imagePath = "drawable://" + R.drawable.hai;
            }

            Log.v("image", "" + imagePath);

            FactFragment fragment = FactFragment.newInstance(title, text, imagePath); //TODO: Image path als String in Fact Fragment

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fact_container, fragment)
                    .commit();
        }
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