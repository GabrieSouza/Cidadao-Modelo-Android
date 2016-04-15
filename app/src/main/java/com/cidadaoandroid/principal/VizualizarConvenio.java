package com.cidadaoandroid.principal;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class VizualizarConvenio extends AppCompatActivity {

    private com.mikhaellopez.circularprogressbar.CircularProgressBar circularProgressBar;
    private Convenios convenios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizar_convenio);


        convenios = getIntent().getParcelableExtra("convenio");

        circularProgressBar = (CircularProgressBar)findViewById(R.id.progress);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBack));

        DateTime dateTime = new DateTime(DateTime.parse(convenios.getData_inicio_vigencia()));
        DateTime dateTime1 = new DateTime(DateTime.parse(convenios.getData_fim_vigencia()));
        DateTime time = new DateTime();
        Log.e("DATAINI", convenios.getData_inicio_vigencia());
        Log.e("DATAFIM", convenios.getData_fim_vigencia());
        //GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        Duration duration = new Duration(dateTime,time);
        Duration duration1 = new Duration(dateTime,dateTime1);

        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.getMenuIconView().setImageDrawable(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_camera).colorRes(R.color.accent).sizeDp(30));
        menu.setMenuButtonColorNormalResId(R.color.primary);
        menu.setMenuButtonColorPressedResId(R.color.colorAccent);

        FloatingActionButton camera = (FloatingActionButton) findViewById(R.id.camera);
        camera.setImageDrawable(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_camera).colorRes(R.color.primary).sizeDp(15));
        camera.setColorNormalResId(R.color.colorAccent);
        camera.setColorPressedResId(R.color.primary);
        camera.setLabelColors(R.color.primary, R.color.colorAccent, R.color.md_white_1000);
        FloatingActionButton galeria = (FloatingActionButton) findViewById(R.id.galeria);
        galeria.setImageDrawable(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_file_image_o).colorRes(R.color.primary).sizeDp(15));
        galeria.setColorNormalResId(R.color.colorAccent);
        galeria.setColorPressedResId(R.color.primary);
        galeria.setLabelColors(R.color.primary, R.color.colorAccent, R.color.md_black_1000);

        IconicsImageView imageConvenio = (IconicsImageView) findViewById(R.id.image_convenio);
        imageConvenio.setIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_picture_o).color(ContextCompat.getColor(this, R.color.accent)).sizeDp(60));
        IconicsImageView imageCurti = (IconicsImageView) findViewById(R.id.curtir);
        imageCurti.setIcon(FontAwesome.Icon.faw_thumbs_o_up);
        IconicsImageView imageDenun = (IconicsImageView) findViewById(R.id.denuncia);
        imageDenun.setIcon(FontAwesome.Icon.faw_exclamation_triangle);
        IconicsImageView imageDenunTex = (IconicsImageView) findViewById(R.id.denuncias);
        imageDenunTex.setIcon(FontAwesome.Icon.faw_exclamation_triangle);
        TextView percent = (TextView) findViewById(R.id.percent);
        TextView obj = (TextView) findViewById(R.id.descricao);
        obj.setText(convenios.getObjeto_resumido());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_chevron_left).colorRes(R.color.accent).sizeDp(30));
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        mCollapsingToolbarLayout.setTitle(convenios.getProponente());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int perc = (int) ((duration.getStandardDays()*100)/duration1.getStandardDays());
        Log.e("DATEMILLIS", String.valueOf(duration1.getStandardDays()));
        Log.e("PORCENTAGEM", String.valueOf(perc));
        int animationDuration = 2500; // 2500ms = 2,5s
        percent.setText(perc + "%");
        circularProgressBar.setProgressWithAnimation(perc, animationDuration);
    }
}
