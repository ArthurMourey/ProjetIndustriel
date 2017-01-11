package miage.projetindustriel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Musique extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musique);

        setTitle("Lecture de musique");

        final ImageButton bouton_map = (ImageButton) findViewById(R.id.button_map_music);
        bouton_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Musique.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_disconnect = (ImageButton) findViewById(R.id.button_disconnect_music);
        bouton_disconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Musique.this, Login.class);
                startActivity(intent);
            }
        });
    }


}
