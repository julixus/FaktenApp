package de.thnuernberg.bme.faktenapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreActivity extends AppCompatActivity implements FactFragment.OnFactInteractionListener, SensorEventListener {

    int fact_counter = 0;

    private FactsTable factsTable;
    private Cursor factCursor;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorListener;

    private static final float TILT_THRESHOLD = 7.0f; // Empfindlichkeit
    private long lastTriggerTime = 0;
    private static final long TRIGGER_COOLDOWN_MS = 2000; // 1 Sekunde Pause zwischen Aktionen

    TextView acc_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);

        acc_text = findViewById(R.id.text_acc);
        acc_text.setText("text");

        factsTable = new FactsTable(this);
        factCursor = factsTable.getFacts();

        nextFact();

        makeBottomNav();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

    }


    private void makeBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_explore) {
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
        bottomNav.setSelectedItemId(R.id.navigation_explore);
    }



    @Override
    public void onFactLiked() {
        // Hier reagierst du auf "Gefällt mir" → lade nächsten Fakt
        Toast.makeText(this, "interessant", Toast.LENGTH_SHORT).show();
        nextFact();
        Log.v("explore", "liked");
    }

    @Override
    public void onFactDisliked() {
        // Hier reagierst du auf "Gefällt mir nicht"
        Toast.makeText(this, "egal", Toast.LENGTH_SHORT).show();
        nextFact();
        Log.v("explore", "disliked");
    }

    public void nextFact() {

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

            fact_counter++;

            // Wenn du durch alle Fakten durch willst, kannst du wieder von vorne anfangen:
            if (fact_counter >= factCursor.getCount()) {
                fact_counter = 0;
            }
            } else {
                // keine Daten vorhanden
                Toast.makeText(this, "Keine Fakten gefunden", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v("ExploreActivity", "Sensor event received");

        float x = event.values[0]; // Links-/Rechtsbewegung
        float y = event.values[1]; // Vor-/Rückwärtsbewegung
        float z = event.values[2]; // Drehung
        String text = "x: " + x + "; y: " + y + "; z: " + z;
        acc_text.setText(text);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTriggerTime < TRIGGER_COOLDOWN_MS) {
            return; // Cooldown aktiv
        }

        if (x > TILT_THRESHOLD) {
            // Nach links geschwenkt → Dislike
            onFactLiked();
            lastTriggerTime = currentTime;
        } else if (x < -TILT_THRESHOLD) {
            // Nach rechts geschwenkt → Like
            onFactDisliked();
            lastTriggerTime = currentTime;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ExploreActivity", "Sensor accuracy changed: " + accuracy);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Log.v("ExploreActivity", "Accelerometer not available");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }    }

}
