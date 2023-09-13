package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = findViewById(R.id.tela);

        textName = findViewById(R.id.textName);
        textStatus = findViewById(R.id.textStatus);
        textOrigin = findViewById(R.id.textOrigin);
        textSpecie = findViewById(R.id.textSpecie);

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
    }}

//    public  boolean isInternetConnection() {
//
//        Context context = null;
//
//        if (context == null) {
//            return false;
//        }
//        else {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (!connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
//                return false;
//            } else {
//                return true;
//            }
//
//        }
//    }
//}



