package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jhonatan on 20/06/16.*
 */
public class ViagemActivity extends Activity {
    private int dia_chegada, dia_saida;
    private int mes_chegada, mes_saida;
    private int ano_chegada, ano_saida;
    private Button data_chegada;
    private Button data_saida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        data_chegada = (Button) findViewById(R.id.dataChegada);
        data_saida = (Button) findViewById(R.id.dataSaida);
    }

    public void salvarViagem(View view){

    }

    public void selecionarDataViagem(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.dataChegada == id){
            return new DatePickerDialog(this, listener_entrada, ano_chegada, mes_chegada, dia_chegada);
        }
        else if(R.id.dataSaida == id){
            return new DatePickerDialog(this, listener_saida, ano_saida, mes_saida, dia_saida);
        }

        return null;
    }

    private OnDateSetListener listener_entrada = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano_chegada = year;
            mes_chegada = monthOfYear;
            dia_chegada = dayOfMonth;

            data_chegada.setText(dia_chegada + "/" + (mes_chegada+1) + "/" + ano_chegada);
        }
    };

    private OnDateSetListener listener_saida = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano_saida = year;
            mes_saida = monthOfYear;
            dia_saida = dayOfMonth;

            data_saida.setText(dia_saida + "/" + (mes_saida+1) + "/" + ano_saida);
        }
    };

}
