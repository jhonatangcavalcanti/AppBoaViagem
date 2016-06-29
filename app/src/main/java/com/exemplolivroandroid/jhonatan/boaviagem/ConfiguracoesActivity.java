package com.exemplolivroandroid.jhonatan.boaviagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by jhonatan on 29/06/16. *
 */
public class ConfiguracoesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
