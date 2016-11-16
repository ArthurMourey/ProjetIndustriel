package miage.org.projetindustriel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setTitle("Map");

        final ImageButton bouton_music = (ImageButton) findViewById(R.id.button_music_map);
        bouton_music.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Musique.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_disconnect = (ImageButton) findViewById(R.id.button_disconnect_map);
        bouton_disconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
