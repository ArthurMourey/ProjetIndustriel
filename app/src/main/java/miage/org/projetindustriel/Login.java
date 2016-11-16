package miage.org.projetindustriel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Nom de l'application");
        final Button loginButton = (Button) findViewById(R.id.connection_button);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Musique.class);
                startActivity(intent);
            }
        });

        final Button inscriptionButton = (Button) findViewById(R.id.inscription_button);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Inscription.class);
                startActivity(intent);
            }
        });

    }
}
