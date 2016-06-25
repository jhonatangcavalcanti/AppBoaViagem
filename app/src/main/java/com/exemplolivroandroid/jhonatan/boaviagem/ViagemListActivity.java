package com.exemplolivroandroid.jhonatan.boaviagem;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jhonatan on 24/06/16. *
 */
public class ViagemListActivity extends ListActivity implements OnItemClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listarViagens()));
        ListView listView = getListView();
        listView.setOnItemClickListener(this);
    }

    private List<String> listarViagens(){
        return Arrays.asList("São Paulo", "Bonito", "Maceió");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        String mensagem = "Viagem selecionada: " + textView.getText();

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListActivity.class));
    }
}
