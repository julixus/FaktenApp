package de.thnuernberg.bme.faktenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateActivity extends AppCompatActivity {

    private EditText titleInput, textInput;
    private ImageView imagePreview;
    private Button selectImageButton, saveButton;

    private String image_path = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        bottomNav = findViewById(R.id.bottom_navigation);
        createNavbar();

        titleInput = findViewById(R.id.title_input);
        textInput = findViewById(R.id.text_input);
        imagePreview = findViewById(R.id.image_preview);
        selectImageButton = findViewById(R.id.select_image_button);
        saveButton = findViewById(R.id.save_fact_button);

        // === Bildauswahl registrieren ===
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();

                        if (uri != null) {
                            // Zugriff merken
                            getContentResolver().takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );

                            image_path = uri.toString(); // Speichern für Datenbank
                            imagePreview.setImageURI(uri); // Vorschau anzeigen
                            Log.d("Bild", "URI: " + image_path);
                        }
                    }
                }
        );

        // === Button: Bild auswählen ===
        selectImageButton.setOnClickListener(v -> openImagePicker());

        // === Button: Fakt speichern ===
        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String text = textInput.getText().toString().trim();
            FactsTable dbHelper = new FactsTable(this);

            if (title.isEmpty() || text.isEmpty()) {
                // einfache Validierung
                Log.e("Create", "Titel oder Text fehlt");
                return;
            }

            // ⬇ Hier würdest du deine SQLite-Insert-Methode aufrufen:
            // Beispiel:
            dbHelper.addFact(title, text, image_path);

            Log.d("Create", "Fakt gespeichert: " + title + ", Bild: " + image_path);

            finish(); // zur vorherigen Activity zurück
        });
    }

    private void createNavbar() {
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
                //finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                //finish();
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.navigation_create);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        imagePickerLauncher.launch(intent);
    }
}
