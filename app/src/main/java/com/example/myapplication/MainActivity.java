package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.myapplication.Api.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Boolean reqStatus = false;
   // private String urlImage;
    private ConstraintLayout tela;
    private View loading;
    private TextView textName;
    private TextView textStatus;
    private TextView textOrigin;
    private TextView textSpecie;
    private Button btnFetchPerson;
    private Button btnSharedInfo;
    private ImageView imagePerson;
    private TextView textHasConnection;


    private int segundos = 0;
    private boolean emExecucao = true;
    private TextView contadorTextView;

    ContadorInatividade contador = new ContadorInatividade();

    PowerManager pm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);



        tela = findViewById(R.id.tela);

        textName = findViewById(R.id.textTitle);
        textStatus = findViewById(R.id.textStatus);
        textOrigin = findViewById(R.id.textOrigin);
        textSpecie = findViewById(R.id.textSpecie);

        textHasConnection = findViewById(R.id.textConnection);

        btnFetchPerson = findViewById(R.id.btnFetchPerson);
        btnSharedInfo= findViewById(R.id.btnSharedInfo);

        imagePerson = findViewById(R.id.imagePerson);

        loading = findViewById(R.id.loading);

        btnFetchPerson.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            tela.setVisibility(View.GONE);
            fetchPerson();
        });

        btnSharedInfo.setOnClickListener(view -> {
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, urlImage);
//            shareIntent.setType("image/jpeg");
//            startActivity(Intent.createChooser(shareIntent, "Enviar"));

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "O personagem que eu tirei foi: "+ textName.getText() + "\n" +
                            "Esse personagem esta :" + textStatus.getText()+"\n" +
                            "E a sua origem é esta: " +textOrigin.getText());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

        } else {
            tela.setVisibility(View.GONE);
            textHasConnection.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Sem conexão de rede", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        checkInative();
    }

    @Override
        public void onStop() {

            super.onStop();

            checkInative();


        }


    private void fetchPerson(){
        int randomNumber = new Random().nextInt(500);
        String url = "https://rickandmortyapi.com/api/character/" + randomNumber ;

        Log.i("urlç", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        Log.i("api", response.toString());
                        textName.setText(response.getString("name"));
                        textStatus.setText(response.getString("status"));
                        textSpecie.setText(response.getString("species"));

                        loading.setVisibility(View.GONE);
                        tela.setVisibility(View.VISIBLE);
                        btnSharedInfo.setEnabled(true);

                        JSONObject jsonObjectOrigin = response.getJSONObject("origin");
                        String origin = jsonObjectOrigin.getString("name");
                        textOrigin.setText(origin);

                        //urlImage = response.getString("image");

                        Glide.with(MainActivity.this).load(response.getString("image")).into(imagePerson);

                    } catch (JSONException e) {
                        loading.setVisibility(View.GONE);
                        btnSharedInfo.setEnabled(false);
                        throw new RuntimeException(e);
                    }
                }, error -> {
                    // TODO: Handle error
                    loading.setVisibility(View.GONE);
                    btnSharedInfo.setEnabled(false);

                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void checkInative(){
        if (pm.isInteractive()) {
            contador.resetContador();
            Log.i("state-app", "Esta em modo INTERATIVO");
        }
        else{
            contador.iniciarContador();
            Log.i("state-app", "Esta em modo NÃO INTERATIVO");
        }
    }

}



