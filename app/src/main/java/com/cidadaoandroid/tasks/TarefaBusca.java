package com.cidadaoandroid.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.cidadaoandroid.principal.Comparar;
import com.cidadaoandroid.entidades.Convenios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by GabrielSouza on 13/04/2015.
 */
public class TarefaBusca extends AsyncTask<Void,Void,ArrayList<Convenios>>{

    private Context context;
    private ProgressDialog dialog;
    private ArrayList<Convenios> convenios;
    private BuscaInter buscaInter;

    public TarefaBusca(Context context,ArrayList<Convenios> convenios,BuscaInter buscaInter) {
        this.context = context;
        this.convenios = convenios;
        this.buscaInter = buscaInter;
    }
    @Override
    protected void onPreExecute(){
        dialog = new ProgressDialog(context);
        dialog.setTitle("Carregando Convenios");
        dialog.setMessage("espere um pouco");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected ArrayList<Convenios> doInBackground(Void... params) {
        convenios = findAllItems();
        Comparar comparar = new Comparar();
        Collections.sort(convenios, comparar);
        for (int i =0;i<convenios.size();i++){
            Log.e("dates",convenios.get(i).getData_fim_vigencia());
        }
        return convenios;
    }
    @Override
    protected void onPostExecute(ArrayList<Convenios> aVoid) {
        dialog.setMessage("Lista Carregada");
        buscaInter.depoisEvent(convenios);
        dialog.dismiss();
    }

    public ArrayList<Convenios> findAllItems() {
        JSONObject serviceResult = requestWebService(
                "http://api.convenios.gov.br/siconv/v1/consulta/convenios.json?");


        ArrayList<Convenios> foundItems = new ArrayList<Convenios>();

        try {
            JSONArray items = serviceResult.getJSONArray("convenios");
            Log.e("JSONTAM", String.valueOf(items.length()));
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                Convenios convenios = new Convenios();
                convenios.setId(obj.getInt("id"));
                convenios.setJustificativa_resumida(obj.getString("justificativa_resumida"));
                convenios.setModalidade(obj.getString("modalidade"));
                convenios.setOrgao_concedente(obj.getJSONObject("orgao_concedente").getJSONObject("Orgao").getString("href"));
                convenios.setData_assinatura(obj.getString("data_assinatura"));
                convenios.setData_fim_vigencia(obj.getString("data_fim_vigencia"));
                convenios.setData_inicio_vigencia(obj.getString("data_inicio_vigencia"));
                convenios.setData_publicacao(obj.getString("data_publicacao"));
                convenios.setObjeto_resumido(obj.getString("objeto_resumido"));
                convenios.setProponente(obj.getJSONObject("proponente").getJSONObject("Proponente").getString("id"));
                convenios.setSituacao(obj.getJSONObject("situacao").getJSONObject("SituacaoConvenio").getString("href"));
                convenios.setValor_contra_partida(obj.getString("valor_contra_partida"));
                convenios.setValor_global(obj.getString("valor_global"));
                convenios.setValor_repasse(obj.getString("valor_repasse"));

                foundItems.add(convenios);
            }


        } catch (JSONException e) {
            // handle exception
        }
        Log.e("JSONTAM2", String.valueOf(foundItems.size()));
        return foundItems;
    }
    public static JSONObject requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(5500);
            urlConnection.setReadTimeout(5500);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
