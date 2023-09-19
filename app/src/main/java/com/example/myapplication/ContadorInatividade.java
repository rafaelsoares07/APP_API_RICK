package com.example.myapplication;

import android.os.Handler;
import android.util.Log;

public class ContadorInatividade{
    private Runnable atualizarContador;
    private Handler handler = new Handler();
    private boolean isRunner = false;
    private int segundos = 0;

    public void iniciarContador() {
        if (!isRunner) {
            isRunner = true;

            atualizarContador = new Runnable() {
                @Override
                public void run() {
                    segundos++;
                    Log.i("state-app", String.valueOf(segundos));
                }
            };

            handler.postDelayed(atualizarContador, 1000);
        }
    }

    public void resetContador(){
        this.segundos = 0;

        this.isRunner = false;

        handler.removeCallbacks(atualizarContador);
    }

}