package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplolivroandroid.jhonatan.boaviagem.dao.BoaViagemDAO;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Gasto;

import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by jhonatan on 23/06/16. *
 */
public class GastoActivity extends Activity {
    private int dia, mes, ano;
    private Date data;
    private Button dataGasto;
    private Spinner spinner_categoria;
    private BoaViagemDAO dao;
    private EditText descricao, valor, local;
    private TextView destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

        dao = new BoaViagemDAO(this);

        descricao = (EditText) findViewById(R.id.descricao);
        valor = (EditText) findViewById(R.id.valor);
        local = (EditText) findViewById(R.id.local);

        destino = (TextView) findViewById(R.id.destino);
        if(getIntent().hasExtra(Constantes.VIAGEM_DESTINO)){
            destino.setText(getIntent().getStringExtra(Constantes.VIAGEM_DESTINO));
        }

        /* carrega e adiciona a data ao botão para manter usuario informado*/
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGasto = (Button) findViewById(R.id.dataGasto);
        //dataGasto.setText(dia + "/" + (mes+1) + "/" + ano);

        /* Array Adapter atribuido ao spinner de categorias */
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.categoria_gasto,
                        android.R.layout.simple_spinner_item);
        spinner_categoria = (Spinner) findViewById(R.id.categoria);
        spinner_categoria.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    public void registrarGasto(View view){
        String viagem_id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        Gasto gasto = new Gasto(data,
                                spinner_categoria.getSelectedItem().toString(),
                                descricao.getText().toString(),
                                Double.valueOf(valor.getText().toString()),
                                local.getText().toString(),
                                Integer.valueOf(viagem_id) );

        if(dao.inserir(gasto) > 0){
            Toast.makeText(this, R.string.registro_salvo, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, R.string.erro_salvar, Toast.LENGTH_SHORT).show();
        }
    }

    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(R.id.dataGasto == id){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        return null;
    }

    private Date criarData(int dayOfMonth, int monthOfYear, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    private OnDateSetListener listener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data = criarData(dayOfMonth, monthOfYear, year);
            dataGasto.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
        return true;

        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish(); /// TODO remover do bando de dados
        return true;
        //return super.onMenuItemSelected(featureId, item);
    }
}
