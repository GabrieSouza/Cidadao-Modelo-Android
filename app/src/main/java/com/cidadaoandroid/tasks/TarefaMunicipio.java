package com.cidadaoandroid.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cidadaoandroid.entidades.Municipio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabri on 11/04/2016.
 */
public class TarefaMunicipio extends AsyncTask<String, Integer, ArrayList<Municipio>> {
    private List<String> id_proponentes;
    private ArrayList<Municipio> municipios;
    private MunicipioInter municipioInter;
    private Activity context;
    private ProgressDialog dialog;

    public TarefaMunicipio(ArrayList<Municipio> municipios, MunicipioInter municipioInter, Activity context) {
        this.municipios = municipios;
        this.municipioInter = municipioInter;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("DIALOG", "entrou");
        dialog = new ProgressDialog(context);
        dialog.setTitle("Aguardando sua localização");
        dialog.setMessage("espere um pouco");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected ArrayList<Municipio> doInBackground(String... params) {
        Log.e("PARAMS", params[0]);
        publishProgress();
        municipios = new ArrayList<>();
        JSONObject jsonObject1 = TarefaBusca.requestWebService("http://api.convenios.gov.br/siconv/v1/consulta/municipios.json?uf=" + params[0]);
        try {
            JSONArray array = jsonObject1.getJSONArray("municipios");
            int total = jsonObject1.getJSONObject("metadados").getInt("total_registros");
            if (total < 500) {
                for (int i = 0; i < array.length(); i++) {
                    Municipio municipio = new Municipio();
                    JSONObject object = array.getJSONObject(i);
                    municipio.setId(object.getInt("id"));
                    municipio.setNome(object.getString("nome"));

                    municipios.add(municipio);
                }
            } else {
                int cont = 0;
                while (cont < total) {
                    for (int i = 0; i < 500 && cont < total; i++) {
                        Municipio municipio = new Municipio();
                        JSONObject object = array.getJSONObject(i);
                        municipio.setId(object.getInt("id"));
                        municipio.setNome(object.getString("nome"));
                        municipios.add(municipio);
                        cont++;
                    }
                    jsonObject1 = TarefaBusca.requestWebService("http://api.convenios.gov.br/siconv/v1/consulta/municipios.json?uf=" + params[0] + "&offset=" + cont);
                    array = jsonObject1.getJSONArray("municipios");
                }

            }


            for (int i = 0; i < municipios.size(); i++) {
                Log.e("IDPROP", municipios.get(i).getNome());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return municipios;
    }

    @Override
    protected void onPostExecute(ArrayList<Municipio> municipios) {
        super.onPostExecute(municipios);
        municipioInter.depoisMunicipio(municipios);
        dialog.dismiss();
    }
}
