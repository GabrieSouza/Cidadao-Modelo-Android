package com.cidadaoandroid.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.cidadaoandroid.entidades.Convenios;
import com.cidadaoandroid.principal.Comparar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
public class TarefaBusca extends AsyncTask<String, Void, ArrayList<Convenios>> {

    private static final String TAG = "LOGG";
    private Activity context;
    private ProgressDialog dialog;
    private ArrayList<Convenios> convenios;
    private BuscaInter buscaInter;

    public TarefaBusca(Activity context, ArrayList<Convenios> convenios, BuscaInter buscaInter) {
        this.context = context;
        this.convenios = convenios;
        this.buscaInter = buscaInter;
    }

    public static JSONObject requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
                Log.e("ERROR", String.valueOf(statusCode));
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
    protected ArrayList<Convenios> doInBackground(String... params) {

        //convenios = findAllItems();
        Log.e("LINK1", String.valueOf(params.length));
        for (int i = 0; i < params.length; i++) {
            String auxId = "id_proponente=" + params[i];
            convenios.addAll(lerXML(auxId));
        }
        Log.e("NUMERO", String.valueOf(convenios.size()));

        for (int i = 0; i < convenios.size(); i++) {
            Log.e("ID", String.valueOf(convenios.get(i).getId()));
            Log.e("Modalidade", convenios.get(i).getModalidade());
            Log.e("Proponente", convenios.get(i).getProponente());
            Log.e("LinkProponente", convenios.get(i).getLink_proponente());
            Log.e("valor_repasse", convenios.get(i).getValor_repasse());
            Log.e("valor_global", convenios.get(i).getValor_global());
            if (convenios.get(i).getValor_global() != null) {
                Log.e("valor_contra_partida", String.valueOf(convenios.get(i).getValor_contra_partida()));
            }
            Log.e("situacao", convenios.get(i).getSituacao());
            Log.e("objeto_resumido", convenios.get(i).getObjeto_resumido());
            if (convenios.get(i).getData_publicacao() != null) {
                Log.e("data_publicacao", convenios.get(i).getData_publicacao());

            }
            if (convenios.get(i).getData_assinatura() != null) {
                Log.e("data_assinatura", convenios.get(i).getData_assinatura());
            }
            Log.e("data_inicio_vigencia", convenios.get(i).getData_inicio_vigencia());
            Log.e("data_fim_vigencia", convenios.get(i).getData_fim_vigencia());
            Log.e("orgao_concedente", convenios.get(i).getOrgao_concedente());
            Log.e("justificativa_resumida", convenios.get(i).getJustificativa_resumida());
        }
        //Log.e("dates",convenios.get(0).getModalidade());
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
        ArrayList<Convenios> foundItems = new ArrayList<Convenios>();

        JSONObject serviceResult = requestWebService(
                "http://api.convenios.gov.br/siconv/v1/consulta/convenios.json?");



        try {
            Log.e("JSON", String.valueOf(serviceResult));

            JSONArray items = serviceResult.getJSONArray("convenios");

            Log.e("JSONTAM", String.valueOf(items.length()));
            Log.e("JSONTAM", String.valueOf(items));


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
                Log.e("Prop", "Aqui");
                //String json = getJSON("http://api.convenios.gov.br/siconv/v1/consulta/convenios.json?",5000);
                Log.e("TAGJSON", String.valueOf(obj.getJSONObject("proponente").getJSONObject("Proponente").getString("title")));
                //JSONObject serviceResult1 = requestWebService("http://api.convenios.gov.br/siconv/dados/proponente/"+obj.getJSONObject("proponente").getJSONObject("Proponente").getString("id")+".json");
                //JSONObject serviceResult1 = requestWebService("http://api.convenios.gov.br/siconv/v1/consulta/convenios.json?");
                //Log.e("TAGJSON",serviceResult1.toString());
                //JSONObject object = serviceResult1.getJSONObject("proponentes");
                //Log.e("Prop",object.getString("nome"));
                //convenios.setProponente(object.getString("nome"));
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

    private ArrayList<Convenios> lerXML(String link) {
        XmlPullParserFactory pullParserFactory;
        ArrayList<Convenios> convenios = new ArrayList<>();
        try {


            URL url = new URL("http://api.convenios.gov.br/siconv/v1/consulta/convenios.xml?" + link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream in_s = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(in_s);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder xmlAsString = new StringBuilder();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    xmlAsString.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(false);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(xmlAsString.toString()));

            convenios = parseXML(parser);
            in_s.close();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convenios;
    }

    private ArrayList<Convenios> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Convenios> products = null;
        int eventType = parser.getEventType();
        Convenios currentProduct = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    products = new ArrayList();
                    Log.e("MODALIDADE", "ARRAy");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    Log.e("MODALIDADE", parser.getName());
                    if (name.equals("convenio")) {
                        currentProduct = new Convenios();
                        Log.e("MODALIDADE", "AQUI2");
                    } else if (currentProduct != null) {
                        if (name.equals("modalidade")) {
                            //Log.e("MODALIDADE1",parser.nextText());
                            currentProduct.setModalidade(parser.nextText());
                        } else if (name.equals("id")) {
                            //Log.e("ID", String.valueOf(parser.next()));
                            currentProduct.setId(Integer.parseInt(parser.nextText()));
                        } else if (name.equals("Proponente")) {
                            Log.e("HREF", parser.getAttributeValue(null, "href"));
                            currentProduct.setLink_proponente(parser.getAttributeValue(null, "href"));
                            currentProduct.setProponente(parser.nextText());
                            //Log.e("LinkProp",String.valueOf(parser.nextTag()));
                        } else if (name.equals("justificativa_resumida")) {
                            //Log.e("LinkProp",String.valueOf(parser.nextTag()));
                            currentProduct.setJustificativa_resumida(parser.nextText());
                        } else if (name.equals("Orgao")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setOrgao_concedente(parser.nextText());
                        } else if (name.equals("data_assinatura")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setData_assinatura(parser.nextText());
                        } else if (name.equals("data_fim_vigencia")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setData_fim_vigencia(parser.nextText());
                        } else if (name.equals("data_inicio_vigencia")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setData_inicio_vigencia(parser.nextText());
                        } else if (name.equals("data_assinatura")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setData_assinatura(parser.nextText());
                        } else if (name.equals("data_publicacao")) {
                            //Log.e("DATAPUB",String.valueOf(parser.nextText()));
                            currentProduct.setData_publicacao(parser.nextText());
                        } else if (name.equals("objeto_resumido")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setObjeto_resumido(parser.nextText());
                        } else if (name.equals("SituacaoConvenio")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setSituacao(parser.nextText());
                        } else if (name.equals("valor_contra_partida")) {
                            //Log.e("Contra",String.valueOf(parser.nextText()));
                            currentProduct.setValor_contra_partida(parser.nextText());
                        } else if (name.equals("valor_global")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setValor_global(parser.nextText());
                        } else if (name.equals("valor_repasse")) {
                            //Log.e("PROPONENTE1",String.valueOf(parser.nextText()));
                            currentProduct.setValor_repasse(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase("convenio") && currentProduct != null) {
                        products.add(currentProduct);
                        Log.e("MODALIDADE", "PULA");
                    }
            }
            eventType = parser.next();
        }
        return products;
    }
}

