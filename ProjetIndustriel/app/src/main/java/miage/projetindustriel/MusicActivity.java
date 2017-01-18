package miage.projetindustriel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musique);

        setTitle("Lecture de musique");

        final ImageButton bouton_recherche = (ImageButton) findViewById(R.id.recherche);
        bouton_recherche.setImageResource(R.drawable.loupe);

        final ImageButton bouton_music = (ImageButton) findViewById(R.id.button_music_music);
        bouton_music.setImageResource(R.drawable.musique);

        final ImageButton bouton_map = (ImageButton) findViewById(R.id.button_map_music);
        bouton_map.setImageResource(R.drawable.geolocalisation);
        bouton_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_disconnect = (ImageButton) findViewById(R.id.button_disconnect_music);
        bouton_disconnect.setImageResource(R.drawable.deconnexion);
        bouton_disconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, Login.class);
                startActivity(intent);
            }
        });

        final ImageButton bouton_lecture = (ImageButton) findViewById(R.id.lecture);
        bouton_lecture.setImageResource(R.drawable.lecture);

        final ImageButton bouton_precedent = (ImageButton) findViewById(R.id.precedent);
        bouton_precedent.setImageResource(R.drawable.precedent);

        final ImageButton bouton_pause = (ImageButton) findViewById(R.id.pause);
        bouton_pause.setImageResource(R.drawable.pause);

        final ImageButton bouton_suivant = (ImageButton) findViewById(R.id.suivant);
        bouton_suivant.setImageResource(R.drawable.suivant);
    }


}
