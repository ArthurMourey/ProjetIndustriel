package miage.org.projetindustriel;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Nom de l'application");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                    String response = DAO.post("http://www.madpumpkin.fr/index.php",keys, values);
                    //ArrayList<HashMap<String,String>> listResponse = DAO.parseResponse(response);
                    System.out.println(response);
                    if(response.contains("SUCCESS")){ //A modifier par la suite
                        isValid = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(isValid){
                    Intent intent = new Intent(Login.this, Musique.class);
                    startActivity(intent);
                }
                else {
                    TextView t = (TextView) findViewById(R.id.errorMessage);
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
}


