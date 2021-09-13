package com.naats.naatkidunya.activities;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.naats.naatkidunya.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;
public class SplashActivity extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.main_logo);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(1000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.fillColor); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1000);
        configSplash.setPathSplashFillColor(R.color.colorAccent); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("نعت کی دنیا");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(40f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/Pak Nastaleeq (Beta Release).ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {

        startActivity(new Intent(SplashActivity.this, HomeScreenActivity.class));
        finish();
    }
}
