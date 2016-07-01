package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.exemplolivroandroid.jhonatan.boaviagem.dao.BoaViagemDAO;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Viagem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jhonatan on 20/06/16.*
 */
public class ViagemActivity extends Activity {
    private Date dataChegada, dataSaida;
    private int dia, mes, ano;
    private Button dataChegadaButton;
    private Button dataSaidaButton;
    private BoaViagemDAO dao;
    private EditText destino, orcamento, quantidadePessoas;
    private RadioGroup radioGroupTipoViagem;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);

        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        destino = (EditText) findViewById(R.id.destino);
        orcamento = (EditText) findViewById(R.id.orcamento);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        radioGroupTipoViagem = (RadioGroup)	findViewById(R.id.tipoViagem);

        dao = new BoaViagemDAO(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if(id != null){
            prepararEdicao();
        }
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    private void prepararEdicao(){
        Viagem viagem = dao.buscaViagemPorId(Integer.valueOf(id));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(viagem.getTipoViagem() == Constantes.VIAGEM_LAZER){
            radioGroupTipoViagem.check(R.id.lazer);
        }
        else{
            radioGroupTipoViagem.check(R.id.negocios);
        }

        destino.setText(viagem.getDestino());
        dataChegada = viagem.getDataChegada();
        dataSaida = viagem.getDataSaida();
        dataChegadaButton.setText(dateFormat.format(dataChegada));
        dataSaidaButton.setText(dateFormat.format(dataSaida));
        quantidadePessoas.setText(viagem.getQuantidadePessoas().toString());
        orcamento.setText(viagem.getOrcamento().toString());
    }

    public void salvarViagem(View view){
        int tipo = radioGroupTipoViagem.getCheckedRadioButtonId();
        if(tipo == R.id.lazer){
            tipo = Constantes.VIAGEM_LAZER;
        }
        else{
            tipo = Constantes.VIAGEM_NEGOCIOS;
        }

        Viagem viagem = new Viagem(destino.getText().toString(),
                                   tipo,
                                   new Date(dataChegada.getTime()),
                                   new Date(dataSaida.getTime()),
                                   Double.valueOf(orcamento.getText().toString()),
                                   Integer.valueOf(quantidadePessoas.getText().toString()));
        long resultado;
        if(id == null){
            resultado = dao.inserir(viagem);
        }
        else{
            viagem.setId(Integer.valueOf(id));
            resultado = dao.atualizar(viagem);
        }

        if(resultado != -1){
            Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }
    }

    public void selecionarDataViagem(View view){
        showDialog(view.getId());
    }

    private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado){
        Calendar calendar = Calendar.getInstance();
        calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
        return calendar.getTime();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.dataChegada:
                return new DatePickerDialog(this, listener_entrada, ano, mes, dia);
            case R.id.dataSaida:
                return new DatePickerDialog(this, listener_saida, ano, mes, dia);
        }
        return null;
    }

    private OnDateSetListener listener_entrada = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dataChegada = criarData(year, monthOfYear, dayOfMonth);
            dataChegadaButton.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
        }
    };

    private OnDateSetListener listener_saida = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dataSaida = criarData(year, monthOfYear, dayOfMonth);
            dataSaidaButton.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);

        return true;

        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            case R.id.remover:
                /// TODO remover viagem do banco de dados
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
        //return super.onMenuItemSelected(featureId, item);
    }
}
