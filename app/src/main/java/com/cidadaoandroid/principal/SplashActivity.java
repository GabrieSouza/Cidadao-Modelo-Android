package com.cidadaoandroid.principal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Estados;
import com.cidadaoandroid.entidades.Municipio;
import com.cidadaoandroid.tasks.MunicipioInter;
import com.cidadaoandroid.tasks.TarefaMunicipio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by GabrielSouza on 06/12/2014.
 */
public class SplashActivity extends Activity implements Runnable, MunicipioInter {
    private LocationManager locationManager;
    private Location location;
    private Address endereco;
    private ArrayList<Municipio> municipios;
    private Estados estado;
    private String sigla = "";
    private String cidade = "";

    public static String tiraAcento(String s) {

        String item = s;
        item = item.replaceAll("[çÇ]", "c");
        item = item.replaceAll("[áàäãâ]", "a");
        item = item.replaceAll("[ÁÀÄÃÂ]", "A");
        item = item.replaceAll("[éèëê]", "e");
        item = item.replaceAll("[ÉÈËÊ]", "E");
        item = item.replaceAll("[íìïî]", "i");
        item = item.replaceAll("[ÍÌÏÎ]", "I");
        item = item.replaceAll("[óòöõô]", "o");
        item = item.replaceAll("[ÓÒÖÕÔ]", "O");
        item = item.replaceAll("[úùüû]", "u");
        item = item.replaceAll("[ÚÙÜÛ]", "U");
        item = item.toUpperCase();
        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.splash);

        Handler splash = new Handler();
        splash.postDelayed(SplashActivity.this,2000);
        municipios = new ArrayList<>();

    }

    @Override
    public void run() {
        Log.e("Primeiro", "RUN");
        if (getSharedPreferences("municipio", Context.MODE_PRIVATE).contains("municipios")) {

            Intent intent;
            intent = new Intent(SplashActivity.this,MainActivity.class);

            Gson gson = new Gson();
            String json = getSharedPreferences("municipio", Context.MODE_PRIVATE).getString("municipios", "");
            Type listType = new TypeToken<ArrayList<Municipio>>() {
            }.getType();
            ArrayList<Municipio> obj = gson.fromJson(json, listType);
            Log.e("PREF", json);
            Log.e("PREF2", getSharedPreferences("municipio", Context.MODE_PRIVATE).getString("cidade", ""));
            intent.putExtra("cidade", getSharedPreferences("municipio", Context.MODE_PRIVATE).getString("cidade", ""));
            intent.putExtra("municipios", obj);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        } else {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("Sem", "PERMISSAO");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                //callDialog("É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION},this);
            } else {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    endereco = buscaEndereco(location.getLatitude(), location.getLongitude());
                    Log.e("CIDADE1", endereco.getLocality());
                    Log.e("Estado1", endereco.getAdminArea());

                    estado = new Estados();
                    for (int i = 0; i < estado.getSiglas().length; i++) {
                        Log.e("Aqui", String.valueOf(i));
                        if (estado.getNomes()[i].equals(endereco.getAdminArea())) {
                            Log.e("ESTADOS", estado.getSiglas()[i]);
                            sigla = estado.getSiglas()[i];
                        }
                    }
                    Log.e("SIGLA", sigla);
                    cidade = tiraAcento(endereco.getLocality());
                    new TarefaMunicipio(municipios, this, this).execute(sigla);

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Primeiro", "Request");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    Log.e("ERRO", "LOCATIOn");
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    //return;
                }
                endereco = buscaEndereco(location.getLatitude(), location.getLongitude());
                Log.e("CIDADE1", endereco.getLocality());
                Log.e("Estado1", endereco.getAdminArea());
                System.err.println(endereco.getSubAdminArea());
                estado = new Estados();
                for (int i = 0; i < estado.getSiglas().length; i++) {
                    Log.e("Aqui", String.valueOf(i));
                    if (estado.getNomes()[i].equals(endereco.getAdminArea())) {
                        Log.e("ESTADOS", estado.getSiglas()[i]);
                        sigla = estado.getSiglas()[i];
                    }
                }
                cidade = tiraAcento(endereco.getLocality());
                new TarefaMunicipio(municipios, this, this).execute(sigla);

            }

        }
    }

    private Address buscaEndereco(double latitude, double longitude) {

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void depoisMunicipio(ArrayList<Municipio> municipios) {
        Intent intent;
        intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("cidade", cidade);
        intent.putExtra("municipios", municipios);
        salvarSharedPref(municipios, cidade);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void salvarSharedPref(List<Municipio> municipios, String cidade) {

        SharedPreferences preferences = getSharedPreferences("municipio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(municipios);
        editor.putString("cidade", cidade);
        editor.putString("municipios", json);

        editor.commit();
    }
}
