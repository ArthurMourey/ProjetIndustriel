package miage.org.projetindustriel;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                keys.add("login");
                keys.add("password");
                EditText loginEditText = (EditText) findViewById(R.id.login);
                String login = loginEditText.getText().toString();
                values.add(login);
                EditText mdpEditText = (EditText) findViewById(R.id.password);
                String mdp = mdpEditText.getText().toString();
                values.add(mdp);
                try {
                    String a = post("http://www.madpumpkin.fr/index.php",keys, values);
                    System.out.println("Réponse : "+a);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public static String post(String adress, List<String> keys, List<String> values) throws IOException {
        String result = "";
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            //encodage des paramètres de la requête
            String data="";
            for(int i=0;i<keys.size();i++){
                if (i!=0) data += "&#34;,&#34;"; //&quot;
                data += URLEncoder.encode(keys.get(i), "UTF-8")+":"+ URLEncoder.encode(values.get(i), "UTF-8");
            }
            //création de la connection
            URL url = new URL(adress);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            //envoi de la requête
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            //lecture de la réponse
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                result+=ligne;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{writer.close();}catch(Exception e){}
            try{reader.close();}catch(Exception e){}
        }
        return result;
    }
}


