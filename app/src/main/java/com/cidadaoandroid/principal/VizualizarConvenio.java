package com.cidadaoandroid.principal;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
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
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));

        DateTime dateTime = new DateTime(DateTime.parse(convenios.getData_inicio_vigencia()));
        DateTime dateTime1 = new DateTime(DateTime.parse(convenios.getData_fim_vigencia()));
        DateTime time = new DateTime();
        Log.e("DATAINI", convenios.getData_inicio_vigencia());
        Log.e("DATAFIM", convenios.getData_fim_vigencia());
        //GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        Duration duration = new Duration(dateTime,time);
        Duration duration1 = new Duration(dateTime,dateTime1);

        System.out.println(duration.getStandardDays());
        System.out.println(convenios.getData_fim_vigencia());
        int perc = (int) ((duration.getStandardDays()*100)/duration1.getStandardDays());
        Log.e("DATEMILLIS", String.valueOf(duration1.getStandardDays()));
        Log.e("PORCENTAGEM", String.valueOf(perc));
        int animationDuration = 2500; // 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(perc, animationDuration);
    }
}
