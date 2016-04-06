package com.cidadaoandroid.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cidadaoandroid.lista.RecyclerViewFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GabrielSouza on 13/04/2015.
 */
public class TarefaPost extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private ProgressDialog dialog;

    public TarefaPost(Context context) {
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Lista esta sendo carregada");
        dialog.show();
        Log.e("Dialog","Foi chamado");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (RecyclerViewFragment.verificaConexao(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://www.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e("Erro", "Error checking internet connection", e);
            }
        } else {
            Log.d("Não existe conexao", "No network available!");
        }
        return false;
    }
    @Override
    protected void onPostExecute(Boolean aVoid) {
        dialog.setMessage("Lista Carregada");
        dialog.dismiss();
    }
}
