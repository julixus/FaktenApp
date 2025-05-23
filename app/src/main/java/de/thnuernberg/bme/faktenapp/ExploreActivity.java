package de.thnuernberg.bme.faktenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreActivity extends AppCompatActivity implements FactFragment.OnFactInteractionListener {

    int fact_counter = 0;
    String[] fact_titles = {"Käse ist sehr gesund", "Fahrradfahren ist overrated", "Das ist kein Brot"};
    String[] fact_texts = {"Käse ist ein festes Milcherzeugnis,[1] das – bis auf wenige Ausnahmen – durch Gerinnen aus einem Eiweißanteil der Milch, dem Kasein, gewonnen wird. Es ist das älteste Verfahren zur Haltbarmachung von Milch und deren Erzeugnissen. Das neuhochdeutsche Wort „Käse“ geht über mittelhochdeutsch kæse (auch kese und khese[2]), „Käse, Quark“, althochdeutsch kāsi auf lateinisch caseus (eigentlich: „Gegorenes, sauer Gewordenes“) zurück, das unter anderem auch dem englischen Wort cheese und dem spanischen queso zu Grunde liegt.",
            "Im Allgemeinen gilt: Sie dürfen als Fahrradfahrer zu zweit nebeneinander fahren, solange der Verkehr nicht behindert wird. Eine Behinderung liegt vor, wenn Sie nebeneinander fahren und Sie von anderen Verkehrsteilnehmern nicht mehr überholt werden können, obwohl dies möglich wäre, wenn Sie hintereinanderfahren würden.",
            "Weißbrot (auch Weizenbrot) ist Brot, das aus Weizenmehl gebacken wird. Als Backtriebmittel wird vorwiegend Hefe, seltener Weizensauer verwendet.\n" +
                    "\n" +
                    "In Deutschland muss das verwendete Mehl zu mindestens 90 % aus Weizenmehl bestehen, damit es im Handel als Weißbrot verkauft werden darf.[1] Außerdem können bis zu 10 % Prozent andere Getreideerzeugnisse zugegeben werden. Das Brot enthält weniger als 10 Gewichtsanteile Fett und/oder Zucker auf 90 Gewichtsanteile Getreide und/oder Getreideerzeugnisse."};
    int[] fact_images = {R.drawable.cheese, R.drawable.bike, R.drawable.brot};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);

        nextFact();



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
    }


    @Override
    public void onFactLiked() {
        // Hier reagierst du auf "Gefällt mir" → lade nächsten Fakt
        nextFact();
    }

    @Override
    public void onFactDisliked() {
        // Hier reagierst du auf "Gefällt mir nicht"
        nextFact();
    }

    public void nextFact() {
        FactFragment factFragment = FactFragment.newInstance(
                fact_titles[fact_counter], fact_texts[fact_counter], fact_images[fact_counter]);
        getSupportFragmentManager().beginTransaction().replace(R.id.fact_container, factFragment).commit();

        if (fact_counter + 1 < fact_titles.length) {
            fact_counter++;
            Log.v("fact_counter", "fact counter ++");
        }
        else if (fact_counter +1 == fact_titles.length) {
            Log.v("fact_counter", String.valueOf(fact_counter) + "rest in rip");
            Toast.makeText(this, "Das waren alle Fakten!", Toast.LENGTH_SHORT).show();
        }
        Log.v("fact_counter", String.valueOf(fact_counter) + " / " + fact_titles.length);
    }
}
