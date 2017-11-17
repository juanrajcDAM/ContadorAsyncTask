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
    private CounterTask counterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Contador numérico y barra de progreso.
        textView = (TextView) findViewById(R.id.textView);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

    }

    /**
     * onClick botón 'Comenzar contador'
     */
    public void startCounter(View view) {

        //Si el contador no está instanciado(no se ha iniciado nunca)...
        if(counterTask==null)
            //Se instancia y se ejecuta.
            counterTask = (CounterTask) new CounterTask().execute();
        //Si se ha instanciado(se ha iniciado alguna vez) y no está ejecutándose...
        else if(counterTask.getStatus()!= AsyncTask.Status.RUNNING)
            //Se inicia de nuevo.
            counterTask = (CounterTask) new CounterTask().execute();

        /*
        Así evitamos que si pulsamos el botón 'INICIAR CONTADOR' mientra se está
        ejecutando un contador, se pongan en cola otros contadores que se iniciarán
        al finalizar el que se está ejecutando en ese momento.
         */

    }

    /**
     * onClick botón 'Parar contador'
     */
    public void stopCounter(View view) {

        //Si el contador está instanciado(se ha iniciado alguna vez)...
        if(counterTask!=null)
            //Comprobamos que está ejecutándose...
            if(counterTask.getStatus()== AsyncTask.Status.RUNNING)
                //Si es así, se interrumpe.
                counterTask.cancel(true);

        /*
        Así evitamos que la aplicación se aborte si pulsamos el botón
        de 'PARAR CONTADOR' sin haber instanciado nunca el contador.
         */

    }

    /**
     * Contador AsyncTask.
     */
    public class CounterTask extends AsyncTask<Void, Integer, Void> {

        @Override
        //Antes de que se ejecute el hilo...
        protected void onPreExecute() {
            super.onPreExecute();

            //Asignamos el máximo del contador a la barra de progreso.
            progressBar.setMax(10);

        }

        @Override
        //Tarea asíncrona que se realiza en segundo plano.
        protected Void doInBackground(Void... params) {
            //Contador con límite de 10 o hasta que se cancele mediante el botón de cancelar.
            for (int i = 1; i <= 10 && !isCancelled(); i++) {
                //---llama al método para reportar el progreso, el valor del contador i---
                        publishProgress(i);
                try {
                    //Cada iteración se duerme el hilo durante un segundo.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("Threading", e.getLocalizedMessage());
                }
            }
            return null;
        }
        @Override
        //Durante la ejecución del hilo...
        protected void onProgressUpdate(Integer... progress) {
            //Se va actualizando el valor tanto en el contador numérico como en la barra de progreso.
            textView.setText(progress[0].toString());
            Log.d("Threading", "actualizando...");
            progressBar.setProgress(progress[0]);
        }

    }
}