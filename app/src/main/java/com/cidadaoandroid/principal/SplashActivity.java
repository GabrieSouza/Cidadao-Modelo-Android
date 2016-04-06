package com.cidadaoandroid.principal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cidadaoandroid.R;


/**
 * Created by GabrielSouza on 06/12/2014.
 */
public class SplashActivity extends Activity implements Runnable {
    private Context context;
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

        this.context = this;
    }



    @Override
    public void run() {
        Intent intent;
            intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

}
