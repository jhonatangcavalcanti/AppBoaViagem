package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jhonatan on 13/06/16. *
 */
public class DashboardActivity extends Activity {

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.dashboard);
    }

    public void selecionarOpcao(View view){
        TextView textview = (TextView) view;
        String opcao = "Opção " + textview.getText().toString();
        Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
    }

}
