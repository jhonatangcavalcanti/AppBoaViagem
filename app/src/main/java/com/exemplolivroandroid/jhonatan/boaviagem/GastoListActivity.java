package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplolivroandroid.jhonatan.boaviagem.dao.BoaViagemDAO;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Gasto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jhonatan on 24/06/16. *
 */
public class GastoListActivity extends ListActivity implements OnItemClickListener{
    private List< Map<String, Object> > gastos;
    private String dataAnterior = "";
    private BoaViagemDAO dao;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new BoaViagemDAO(this);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String[] de = {"data", "descricao", "valor", "categoria"};
        int[] para = {R.id.dataListaGasto, R.id.descricao, R.id.valor, R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this, listarGastos(), R.layout.lista_gasto, de, para);

        adapter.setViewBinder(new GastoViewBinder());

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // registrando o menu de contexto
        registerForContextMenu(getListView());
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionado: " + descricao;

        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private List<Map<String, Object>> listarGastos(){
        Integer viagem_id = Integer.valueOf(getIntent().getStringExtra(Constantes.VIAGEM_ID));

        gastos = new ArrayList<>();
        List<Gasto> gastos_viagem = dao.listarGastos(viagem_id);
        for (Gasto gasto : gastos_viagem){
            Map<String, Object> item = new HashMap<>();

            switch (gasto.getCategoria()){ // TODO - organizar constantes
                case "Alimentação":
                    item.put(DatabaseHelper.Gasto.CATEGORIA, R.color.categoria_alimentacao);
                    break;
                case "Transporte":
                    item.put(DatabaseHelper.Gasto.CATEGORIA, R.color.categoria_transporte);
                    break;
                case "Hospedagem":
                    item.put(DatabaseHelper.Gasto.CATEGORIA, R.color.categoria_hospedagem);
                    break;
                default: // outros
                    item.put(DatabaseHelper.Gasto.CATEGORIA, R.color.categoria_outros);
                    break;
            }
            item.put(DatabaseHelper.Gasto.VALOR, "R$ " + gasto.getValor());
            item.put(DatabaseHelper.Gasto.DATA, dateFormat.format(gasto.getData()));
            item.put(DatabaseHelper.Gasto.DESCRICAO, gasto.getDescricao());
            item.put(DatabaseHelper.Gasto.LOCAL, gasto.getLocal());
            item.put(DatabaseHelper.Gasto._ID, gasto.getId());
            item.put(DatabaseHelper.Gasto.VIAGEM_ID, gasto.getViagemID());

            gastos.add(item);
        }

        return gastos;
    }


    private class GastoViewBinder implements ViewBinder{
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.dataListaGasto){
                if(!dataAnterior.equals(data)){
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                }
                else{
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if(view.getId() == R.id.categoria){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));

                return true;
            }

            return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.remover){
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            dao.removerGasto(Long.valueOf(gastos.get(info.position).get(DatabaseHelper.Gasto._ID).toString()));
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            return true;
        }

        return super.onContextItemSelected(item);
    }
}
