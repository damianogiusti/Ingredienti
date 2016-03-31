package it.damianogiusti.ingredienti.connLibreria;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
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

    public void caricaIngrediente() {

        String tempHost = txtHost.getText().toString().trim();
        final String ingrediente = txtIngrediente.getText().toString().trim();

        if (tempHost.length() > 0) {
            host = tempHost;
        }

        if (ingrediente.length() > 0) {

            RequestParams params = new RequestParams();
            params.add("item", ingrediente);


            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post("http://" + host + "/" + nomeFile, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    boolean success;
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        // Log.d(TAG, "onSuccess: " + new String(responseBody));
                        success = jsonObject.getBoolean("success");

                        if (success) {
                            Toast.makeText(MainActivity.this, "Ingrediente inserito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Errore!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Errore eccezionale", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Connessione fallita: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
