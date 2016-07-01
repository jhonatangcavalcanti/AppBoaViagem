package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplolivroandroid.jhonatan.boaviagem.dao.BoaViagemDAO;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Gasto;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Viagem;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jhonatan on 24/06/16. *
 */
public class ViagemListActivity extends ListActivity implements OnItemClickListener, OnClickListener, ViewBinder{
    private List< Map< String, Object > > viagens;
    private AlertDialog alertDialog;
    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private BoaViagemDAO dao;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //helper = new DatabaseHelper(this);
        dao = new BoaViagemDAO(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.dataListaViagem, R.id.valor, R.id.barraProgresso};

        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(), R.layout.lista_viagem, de, para);

        adapter.setViewBinder(this);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    private List< Map<String, Object > > listarViagens(){

        viagens = new ArrayList<>();

        List<Viagem> listaViagens = dao.listarViagens();

        for(Viagem viagem : listaViagens) {
            Map<String, Object> item = new HashMap<>();

            item.put("id", viagem.getId());

            if(viagem.getTipoViagem() == Constantes.VIAGEM_LAZER){
                item.put("imagem", R.drawable.lazer);
            }
            else{
                item.put("imagem", R.drawable.negocios);
            }

            item.put("destino", viagem.getDestino());

            String periodo = dateFormat.format(viagem.getDataChegada()) + " a " + dateFormat.format(viagem.getDataSaida());

            item.put("data", periodo);

            double totalGasto = dao.calcularTotalGasto(viagem);

            item.put("total", "Gasto total R$ " + totalGasto);

            double alerta = viagem.getOrcamento() * valorLimite / 100;
            Double[] valores = new Double[] {viagem.getOrcamento(), alerta, totalGasto};
            item.put("barraProgresso", valores);

            viagens.add(item);
        }
        return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id){
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM gasto WHERE viagem_id = ?", new String[]{ id });
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if(view.getId() == R.id.barraProgresso){
            Double valores[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: " + destino;

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, GastoListActivity.class));
        this.viagemSelecionada = position;
        alertDialog.show();
    }

    private AlertDialog criaDialogConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();
    }

    private AlertDialog criaAlertDialog(){
        final CharSequence[] items = { getString(R.string.editar),
                                       getString(R.string.novo_gasto),
                                       getString(R.string.gastos_realizados),
                                       getString(R.string.remover) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        Intent intent;
        Integer id = (Integer) viagens.get(viagemSelecionada).get("id");

        switch (item){
            case 0: // Editar Viagem
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id.toString());
                startActivity(intent);
                getListView().invalidateViews();
                break;
            case 1: // Novo Gasto
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id.toString());
                intent.putExtra(Constantes.VIAGEM_DESTINO, viagens.get(viagemSelecionada).get("destino").toString());
                startActivity(intent);
                break;
            case 2: // Gastos Realizados
                intent = new Intent(this, GastoListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id.toString());
                startActivity(intent);
                break;
            case 3: // Remover viagem
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE: // Confirmação positiva da remoção da viagem
                viagens.remove(this.viagemSelecionada);
                dao.removerViagem(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE: // Confirmação negativa da remoção da viagem
                dialogConfirmacao.dismiss();
                break;
        }
    }
}
