package com.alibenalidoctor.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.alibenalidoctor.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }



}

