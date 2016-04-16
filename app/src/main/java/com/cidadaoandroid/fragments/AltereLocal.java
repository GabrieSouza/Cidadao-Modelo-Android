package com.cidadaoandroid.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
import com.cidadaoandroid.entidades.Estados;
import com.cidadaoandroid.entidades.Municipio;
import com.cidadaoandroid.lista.RecyclerViewFragment;
import com.cidadaoandroid.principal.SplashActivity;
import com.cidadaoandroid.tasks.BuscaInter;
import com.cidadaoandroid.tasks.IdPropInter;
import com.cidadaoandroid.tasks.MunicipioInter;
import com.cidadaoandroid.tasks.TarefaBusca;
import com.cidadaoandroid.tasks.TarefaIdProp;
import com.cidadaoandroid.tasks.TarefaMunicipio;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AltereLocal extends Fragment implements MunicipioInter, BuscaInter, IdPropInter {


    private AutoCompleteTextView completeTextView;
    private Estados estados;
    private String estado;
    private String string_municipio;
    private ArrayList<Municipio> municipios;
    private AutoCompleteTextView completeTextView1;
    private String[] array_municipios;
    private Municipio municipio;
    private ArrayList<Convenios> convenios;

    public AltereLocal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_altere_local, container, false);
        estados = new Estados();
        municipios = new ArrayList<>();
        completeTextView = (AutoCompleteTextView) view.findViewById(R.id.auto_estado);
        completeTextView1 = (AutoCompleteTextView) view.findViewById(R.id.auto_municipio);
        completeTextView1.setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, estados.getNomes());
        completeTextView.setAdapter(adapter);
        completeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("LIST", String.valueOf(completeTextView.getListSelection()));
                for (int i = 0; i < estados.getNomes().length; i++) {
                    if (SplashActivity.tiraAcento(completeTextView.getText().toString()).equals(SplashActivity.tiraAcento(estados.getNomes()[i]))) {
                        estado = estados.getSiglas()[i];
                        new TarefaMunicipio(municipios, AltereLocal.this, getActivity()).execute(estado);
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(completeTextView.getWindowToken(), 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        completeTextView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("LIST", String.valueOf(completeTextView1.getListSelection()));
                for (int i = 0; i < array_municipios.length; i++) {
                    if (SplashActivity.tiraAcento(completeTextView1.getText().toString()).equals(SplashActivity.tiraAcento(array_municipios[i]))) {
                        string_municipio = String.valueOf(municipios.get(i).getId());
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(completeTextView1.getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button button = (Button) view.findViewById(R.id.bt_muda);
        convenios = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (municipios != null && !string_municipio.equals("")) {
                    new TarefaIdProp(AltereLocal.this, getContext()).execute(string_municipio);
                    salvarSharedPref(municipios, string_municipio);
                }
            }
        });

        Log.e("AUTO", "COMPLETE");
        return view;
    }

    @Override
    public void depoisMunicipio(ArrayList<Municipio> municipios) {
        this.municipios = municipios;
        array_municipios = new String[municipios.size()];
        for (int i = 0; i < municipios.size(); i++) {
            array_municipios[i] = municipios.get(i).getNome();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, array_municipios);
        completeTextView1.setAdapter(adapter1);
        completeTextView1.setEnabled(true);
    }

    @Override
    public void depoisEvent(ArrayList<Convenios> events) {
        RecyclerViewFragment recy = new RecyclerViewFragment();
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Log.e("CREATE", String.valueOf(events.size()));
        bundle.putParcelableArrayList("convenios", events);
        recy.setArguments(bundle);
        ft.replace(R.id.container, recy);
        ft.commit();
    }

    @Override
    public void depoisIdProps(List<String> id_prop) {
        new TarefaBusca(getActivity(), convenios, AltereLocal.this).execute(id_prop.toArray(new String[id_prop.size()]));
    }

    private void salvarSharedPref(List<Municipio> municipios, String cidade) {

        SharedPreferences preferences = getActivity().getSharedPreferences("municipio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(municipios);
        editor.putString("cidade", cidade);
        editor.putString("municipios", json);

        editor.commit();
    }

}
