package com.htcdiurno.contadorasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // marcador
        textView = (TextView) findViewById(R.id.textView);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        progressBar.setMax(10);

    }

    //onClick botón 'Comenzar contador'
    public void startCounter(View view) {
        CounterTask counterTask = (CounterTask) new CounterTask().execute();
    }

    public class CounterTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 1; i <= 10; i++) {
                //---llama al método para reportar el progreso, el valor del contador i---
                        publishProgress(i);
                try {
                    Thread.sleep(1000); //duerme el hilo
                } catch (InterruptedException e) {
                    Log.d("Threading", e.getLocalizedMessage());
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            textView.setText(progress[0].toString());
            Log.d("Threading", "actualizando...");
            progressBar.setProgress(progress[0]);
        }
    }
}