package it.damianogiusti.ingredienti.stdconnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.damianogiusti.ingredienti.R;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final String nomeFile = "add-ingredient-v0.php";

    private EditText txtHost;
    private EditText txtIngrediente;
    private Button btnSubmit;

    // dato che android lavora su emulatore, il sistema usa
    // questo indirizzo per riferirsi a localhost
    private String host = "10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtHost = (EditText) findViewById(R.id.txtHost);
        txtIngrediente = (EditText) findViewById(R.id.txtIngrediente);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        if (txtHost != null && txtIngrediente != null && btnSubmit != null) {

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    caricaIngrediente();
                }

            });
        }

    }

    /**
     * Connessione usando il metodo nativo android
     */
    public String stdConnection(String ingrediente) throws Exception {
        URL url = new URL("http://" + host + "/" + nomeFile);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        String params = "item=" + ingrediente; // item=mioIngrediente
        connection.getOutputStream().write(params.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String temp = "";
        String response = "";
        while ((temp = reader.readLine()) != null) {
            response += temp;
        }

        connection.disconnect();

        return response;
    }


    public void caricaIngrediente() {

        String tempHost = txtHost.getText().toString().trim();
        final String ingrediente = txtIngrediente.getText().toString().trim();

        if (tempHost.length() > 0) {
            host = tempHost;
        }

        if (ingrediente.length() > 0) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        stdConnection(ingrediente);
                        // TODO: da gestire response

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(MainActivity.this, "Fatto", Toast.LENGTH_SHORT).show();
                    super.onPostExecute(aVoid);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
            }.execute();
        }
    }
}
