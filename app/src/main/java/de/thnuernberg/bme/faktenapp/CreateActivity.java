package de.thnuernberg.bme.faktenapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateActivity extends Activity {

    FactsTable factsTable;
    private static final int IMAGE_PICK_REQUEST = 100;
    private Uri selectedImageUri;
    private ImageView imagePreview;

    private String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        createNavbar();

        imagePreview = findViewById(R.id.image_preview);

        FloatingActionButton btnSelectImage = findViewById(R.id.btn_upload_image);
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        Button upload = findViewById(R.id.btn_upload);
        upload.setOnClickListener(v -> onUploadClick());


    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                imagePreview.setImageURI(selectedImageUri); // Vorschau anzeigen
                Log.d("Bild", "URI: " + selectedImageUri.toString());

                // Speichern in DB später z. B. so:
                image_path = selectedImageUri.toString();
                // db.insert(... imagePath ...)
            }
        }
    }

    private void createNavbar(){
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

    public void onUploadClick() {
        EditText title = findViewById(R.id.edit_Title);
        EditText text = findViewById(R.id.edit_Text);
        Button upload_img = findViewById(R.id.btn_upload_image);
        String fact_title = title.getText().toString();
        String fact_text = text.getText().toString();
        factsTable.addFact(fact_title, fact_text, image_path); //TODO: Bild URL fixen
        Toast.makeText(this, "hochgeladen!", Toast.LENGTH_SHORT).show();
    }

}
