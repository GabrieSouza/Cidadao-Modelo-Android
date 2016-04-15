package com.cidadaoandroid.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabri on 14/04/2016.
 */
public class TarefaIdProp extends AsyncTask<String, Void, List<String>> {

    private JSONObject json;
    private IdPropInter propInter;
    private Context context;
    private ProgressDialog dialog;


    public TarefaIdProp(IdPropInter propInter, Context context) {
        this.propInter = propInter;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Aguardando informações da cidade");
        dialog.setMessage("espere um pouco");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected List<String> doInBackground(String... params) {

        json = TarefaBusca.requestWebService("http://api.convenios.gov.br/siconv/v1/consulta/proponentes.json?id_municipio=" + params[0]);
        List<String> id_proponentes = new ArrayList<>();
        JSONArray items = null;
        try {
            items = json.getJSONArray("proponentes");
            for (int j = 0; j < items.length(); j++) {
                JSONObject jsonObject = items.getJSONObject(j);
                id_proponentes.add(jsonObject.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return id_proponentes;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        propInter.depoisIdProps(strings);
        dialog.dismiss();
    }
}
