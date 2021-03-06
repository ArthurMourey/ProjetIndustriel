package miage.projetindustriel.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.projetindustriel.R;
import miage.projetindustriel.connexion.DAO;

public class Inscription extends AppCompatActivity {

    //Firebase realtime database
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        setTitle("Inscription");
        final Button submit_inscription_button = (Button) findViewById(R.id.submit_inscription_button);
        submit_inscription_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<String> keys = new ArrayList<String>();
                List<String> values= new ArrayList<String>();
                String login = "";
                String mdp = "";
                String prenom = "";
                String nom = "";
                String mail = "";

                EditText loginEditText = (EditText) findViewById(R.id.login_inscription);
                login += loginEditText.getText().toString();
                EditText mdpEditText = (EditText) findViewById(R.id.mdp_inscription);
                mdp += mdpEditText.getText().toString();
                EditText prenomEditText = (EditText) findViewById(R.id.prenom_inscription);
                prenom += prenomEditText.getText().toString();
                EditText nomEditText = (EditText) findViewById(R.id.nom_inscription);
                nom += nomEditText.getText().toString();
                EditText mailEditText = (EditText) findViewById(R.id.mail_inscription);
                mail += mailEditText.getText().toString();
                keys.add("login_inscription");
                keys.add("mdp_inscription");
                keys.add("prenom_inscription");
                keys.add("nom_inscription");
                keys.add("mail_inscription");
                values.add(login);
                values.add(mdp);
                values.add(prenom);
                values.add(nom);
                values.add(mail);

                if((login=="")||(mdp=="")||(nom=="")||(prenom=="")||(mail=="")){
                    TextView t = (TextView) findViewById(R.id.errorMessageInscription);
                    t.setText("Veuillez remplir TOUS les champs ci-dessus.");
                }
                else{
                    try {
                        String response = DAO.post("http://www.madpumpkin.fr/login_inscription.php",keys, values);
                        System.out.println(response);
                        if(response.contains("SUCCESS")){
                            Intent intent = new Intent(Inscription.this, Login.class);
                            Toast.makeText(getApplicationContext(),"Inscription Réussie",Toast.LENGTH_LONG).show();
                            DatabaseReference myRef = database.getReference(login+"/Info/Musique/Titre");
                            myRef.setValue("rien pour le moment");
                            myRef = database.getReference(login+"/Info/Location/Latitude");
                            myRef.setValue("1");
                            myRef = database.getReference(login+"/Info/Location/Longitude");
                            myRef.setValue("1");
                            startActivity(intent);
                            finish();
                        }
                        else{
                            TextView t = (TextView) findViewById(R.id.errorMessageInscription);
                            t.setText("Le login que vous avez renseigné est déjà pris.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
