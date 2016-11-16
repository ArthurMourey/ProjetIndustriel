package miage.org.projetindustriel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        setTitle("Inscription");
        final Button submit_inscription_button = (Button) findViewById(R.id.submit_inscription_button);
        submit_inscription_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inscription.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
