package com.cidadaoandroid.principal;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
import com.cidadaoandroid.entidades.Municipio;
import com.cidadaoandroid.fragments.AltereLocal;
import com.cidadaoandroid.fragments.Cadastro;
import com.cidadaoandroid.fragments.Perfil;
import com.cidadaoandroid.fragments.Sobre;
import com.cidadaoandroid.lista.RecyclerViewFragment;
import com.cidadaoandroid.tasks.BuscaInter;
import com.cidadaoandroid.tasks.IdPropInter;
import com.cidadaoandroid.tasks.TarefaBusca;
import com.cidadaoandroid.tasks.TarefaIdProp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BuscaInter, IdPropInter {

    private Drawer navigationDrawer;
    private ArrayList<Convenios> convenios;
    private Toolbar toolbar;
    private Municipio municipio;

    public static Municipio achaMunicipio(List<Municipio> municipios, String s) {
        Municipio municipio = new Municipio();
        for (int i = 0; i < municipios.size(); i++) {
            if (s.equals(municipios.get(i).getNome()) || s.equals(String.valueOf(municipios.get(i).getId()))) {
                municipio = municipios.get(i);
                return municipio;
            }
        }
        return municipio;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.drawer);
        setSupportActionBar(toolbar);

        convenios = new ArrayList<>();
        Gson gson = new Gson();
        String json = getSharedPreferences("municipio", Context.MODE_PRIVATE).getString("municipios", "");
        Type listType = new TypeToken<ArrayList<Municipio>>() {
        }.getType();
        final ArrayList<Municipio> obj = gson.fromJson(json, listType);

        final List<Municipio> municipios = obj;
        String cidade = getSharedPreferences("municipio", Context.MODE_PRIVATE).getString("cidade", "");

        municipio = achaMunicipio(municipios, cidade);

        final String[] nomesCidades = new String[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            nomesCidades[i] = obj.get(i).getNome();
        }

        final LinearLayout llPesquisa = (LinearLayout) findViewById(R.id.ll_pesquisa);
        final AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.pesquisa);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nomesCidades);
        completeTextView.setAdapter(adapter);

        completeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < nomesCidades.length; i++) {
                    if (SplashActivity.tiraAcento(completeTextView.getText().toString()).equals(SplashActivity.tiraAcento(nomesCidades[i]))) {
                        convenios = new ArrayList<>();
                        new TarefaIdProp(MainActivity.this, MainActivity.this).execute(String.valueOf(obj.get(i).getId()));
                        //string_municipio = String.valueOf(municipios.get(i).getId());
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(completeTextView.getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Log.e("IDP", String.valueOf(municipio.getId_proponentes().size()));


        navigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .withHeaderPadding(false)
                .withSliderBackgroundColorRes(R.color.primary)
                .withActionBarDrawerToggle(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Inicio").withIcon(FontAwesome.Icon.faw_home).withIconColorRes(R.color.accent).withTextColorRes(R.color.md_white_1000),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Perfil").withIcon(FontAwesome.Icon.faw_user).withIconColorRes(R.color.accent).withTextColorRes(R.color.md_white_1000),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Altere seu local").withIcon(FontAwesome.Icon.faw_map_marker).withIconColorRes(R.color.accent).withTextColorRes(R.color.md_white_1000),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Cadastre-se").withIcon(FontAwesome.Icon.faw_pencil_square_o).withIconColorRes(R.color.accent).withTextColorRes(R.color.md_white_1000),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Sobre").withIcon(FontAwesome.Icon.faw_question_circle).withIconColorRes(R.color.accent).withTextColorRes(R.color.md_white_1000)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;
                        Log.e("Posi", String.valueOf(position));
                        if (position == -1) {
                            position = 2;
                        }
                        switch (position) {
                            case 2:
                                new TarefaIdProp(MainActivity.this, MainActivity.this).execute(String.valueOf(municipio.getId()));
                                llPesquisa.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                fragment = new Perfil();
                                llPesquisa.setVisibility(View.GONE);
                                break;
                            case 6:
                                fragment = new AltereLocal();
                                llPesquisa.setVisibility(View.GONE);
                                break;
                            case 8:
                                fragment = new Cadastro();
                                llPesquisa.setVisibility(View.GONE);
                                break;
                            case 10:
                                fragment = new Sobre();
                                llPesquisa.setVisibility(View.GONE);
                                break;
                        }

                        String title = ((PrimaryDrawerItem) drawerItem).getName().toString();
                        if (position != 2) {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.replace(R.id.container, fragment, title);
                            fragmentTransaction.commit();
                            toolbar.setTitle(title);
                        }

                        return false;
                    }
                })
                .withHeaderDivider(true)
                .withFooterDivider(false)
                .withHeaderDivider(true)
                .withStickyFooterDivider(false)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withCloseOnClick(true)
                .build();
        navigationDrawer.setSelectionAtPosition(2, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void depoisEvent(ArrayList<Convenios> events) {
        RecyclerViewFragment recy = new RecyclerViewFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Log.e("CREATE", String.valueOf(events.size()));
        bundle.putParcelableArrayList("convenios", events);
        recy.setArguments(bundle);
        ft.replace(R.id.container, recy);
        ft.commit();
    }

    @Override
    public void depoisIdProps(List<String> id_prop) {
        TarefaBusca tarefaBusca = new TarefaBusca(MainActivity.this, convenios, MainActivity.this);
        tarefaBusca.execute(id_prop.toArray(new String[id_prop.size()]));
    }
}