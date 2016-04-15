/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.cidadaoandroid.lista;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
import com.cidadaoandroid.entidades.Municipio;
import com.cidadaoandroid.principal.VizualizarConvenio;
import com.cidadaoandroid.tasks.BuscaInter;
import com.cidadaoandroid.tasks.IdPropInter;
import com.cidadaoandroid.tasks.TarefaPost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RecyclerViewFragment extends Fragment implements RecyclerViewOnClickListenerHack, IdPropInter, BuscaInter {

    private RecyclerView mRecyclerView;
    private ArrayList<Convenios> convenios;
    private CustomAdapter adapter;
    private Municipio municipio;

    public static boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }

    public static boolean hasInternetAccess(Context context) {
        Boolean resultado = null;
        try {
            resultado = new TarefaPost(context).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        convenios = new ArrayList<>();
        convenios = getArguments().getParcelableArrayList("convenios");
        //Log.e("ENTROUCre", String.valueOf(convenios.size()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!hasInternetAccess(getActivity())) {
            Log.e("CONEXAO", "noConnection");
            return inflater.inflate(R.layout.progress, container, false);
        }
        final View view = inflater.inflate(R.layout.fragment_list,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_rec);
        mRecyclerView.setHasFixedSize(true);
/* List<Municipio> municipios = getActivity().getIntent().getParcelableArrayListExtra("municipios");
        String cidade = getActivity().getIntent().getStringExtra("cidade");

        municipio = MainActivity.achaMunicipio(municipios,cidade);*/

       /* mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                CustomAdapter adapter = (CustomAdapter) mRecyclerView.getAdapter();
                List<Convenios> auxId;
                    if (convenios.size() == llm.findLastVisibleItemPosition() + 1){
                        for (int i = 0; i < auxId.size(); i++) {
                            Log.e("Nome",auxId.get(i).getModalidade());
                            adapter.addListItem(auxId.get(i),convenios.size());
                        }
                    }
            }
        });*/

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(llm);
        //new TarefaIdProp(RecyclerViewFragment.this,getActivity()).execute(String.valueOf(municipio.getId()));

        adapter = new CustomAdapter(getContext(), convenios);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    public String[] arrayId(Municipio municipio, int ini) {
        String[] ids = new String[20];
        for (int i = ini; i < ini + 20; i++) {
            for (int j = 0; j < 20 && i < municipio.getId_proponentes().size(); j++) {
                ids[j] = municipio.getId_proponentes().get(i);
            }
        }
        return ids;
    }

    @Override
    public void onClickListener(View view, int position) {
        Log.e("BUNDLE",convenios.get(position).getProponente());
        Intent intent = new Intent(getContext(), VizualizarConvenio.class);
        intent.putExtra("convenio",convenios.get(position));
        startActivity(intent);
    }

    @Override
    public void depoisIdProps(List<String> id_prop) {
        municipio.setId_proponentes(id_prop);
        //TarefaBusca tarefaBusca = new TarefaBusca(RecyclerViewFragment.this.getActivity(),convenios,RecyclerViewFragment.this,0);
        //tarefaBusca.execute(id_prop.toArray(new String[id_prop.size()]));
    }

    @Override
    public void depoisEvent(ArrayList<Convenios> events) {
        convenios = events;
    }
    /*public static List<Convenios> buscarEstCid(Context context){
        List<Convenios> list = null;
        try {
            list = new TarefaBusca(context).execute().get();
            Log.e("TAMANHO",String.valueOf(list.size()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }*/

}
