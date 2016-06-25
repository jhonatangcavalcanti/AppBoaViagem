package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jhonatan on 24/06/16. *
 */
public class GastoListActivity extends ListActivity implements OnItemClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listarGastos()));
        ListView listView = getListView();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        Toast.makeText(this, "Gasto selecionado: " + textView.getText(), Toast.LENGTH_SHORT).show();
    }

    private List<String> listarGastos(){
        return Arrays.asList("Sanduíche	R$	19,90",
                             "Táxi	Aeroporto	-	Hotel	R$	34,00",
                             "Revista	R$	12,00");
    }
}
