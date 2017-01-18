package miage.projetindustriel;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission();

        setTitle("Musy");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Si une inscription s'est correctement effectuée
        Boolean inscription;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                inscription = null;
            } else {
                inscription = extras.getBoolean("inscription");
                TextView t = (TextView) findViewById(R.id.errorMessage);
                t.setTextColor(Color.GREEN);
                t.setText("Inscription réussie.");
            }
        } else {
            inscription= (Boolean) savedInstanceState.getSerializable("inscription");
            TextView t = (TextView) findViewById(R.id.errorMessage);
            t.setTextColor(Color.GREEN);
            t.setText("Inscription réussie.");
        }

        final Button loginButton = (Button) findViewById(R.id.connection_button);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<String> keys = new ArrayList<String>();
                List<String> values= new ArrayList<String>();
                EditText loginEditText = (EditText) findViewById(R.id.login);
                String login = loginEditText.getText().toString();
                EditText mdpEditText = (EditText) findViewById(R.id.password);
                String mdp = mdpEditText.getText().toString();
                keys.add("login");
                keys.add("password");
                values.add(login);
                values.add(mdp);
                boolean isValid = false;
                try {
                    String response = DAO.post("http://www.madpumpkin.fr/login_inscription.php",keys, values);
                    System.out.println(response);
                    if(response.contains("SUCCESS")){
                        isValid = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(isValid){
                    Intent intent = new Intent(Login.this, MusicActivity.class);
                    startActivity(intent);
                }
                else {
                    TextView t = (TextView) findViewById(R.id.errorMessage);
                    t.setTextColor(Color.RED);
                    t.setText("Login ou mot de passe invalide, veuillez réessayer.");
                }
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

    private void permission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
}


