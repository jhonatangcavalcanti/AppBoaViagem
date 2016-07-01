package com.exemplolivroandroid.jhonatan.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.exemplolivroandroid.jhonatan.boaviagem.DatabaseHelper;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Gasto;
import com.exemplolivroandroid.jhonatan.boaviagem.domain.Viagem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jhonatan on 30/06/16. *
 */
public class BoaViagemDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context){
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb(){
        if(db == null){
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close(){
        helper.close();
    }

    private Viagem criarViagem(Cursor cursor){
        return new Viagem(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Viagem._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Viagem.DESTINO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Viagem.TIPO_VIAGEM)),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Viagem.DATA_CHEGADA))),
                new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Viagem.DATA_SAIDA))),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Viagem.ORCAMENTO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Viagem.QUANTIDADE_PESSOAS))
        );
    }

    public boolean removerViagem(Integer id){
        int removidos;
        String whereClause = DatabaseHelper.Gasto.VIAGEM_ID + " = ?";
        String[] whereArgs = new String[]{ id.toString() };

        // remover todos os gastos associados a viagem
        getDb().delete(DatabaseHelper.Gasto.TABELA, whereClause, whereArgs);

        // remover a viagem
        whereClause = DatabaseHelper.Viagem._ID + " = ?";
        removidos = getDb().delete(DatabaseHelper.Viagem.TABELA, whereClause, whereArgs);

        return removidos > 0;
    }

    public long inserir(Viagem viagem){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Viagem.DESTINO, viagem.getDestino());
        values.put(DatabaseHelper.Viagem.TIPO_VIAGEM, viagem.getTipoViagem());
        values.put(DatabaseHelper.Viagem.DATA_CHEGADA, viagem.getDataChegada().getTime());
        values.put(DatabaseHelper.Viagem.DATA_SAIDA, viagem.getDataSaida().getTime());
        values.put(DatabaseHelper.Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(DatabaseHelper.Viagem.QUANTIDADE_PESSOAS, viagem.getQuantidadePessoas());

        return getDb().insert(DatabaseHelper.Viagem.TABELA, null, values);
    }

    public int atualizar(Viagem viagem){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Viagem.DESTINO, viagem.getDestino());
        values.put(DatabaseHelper.Viagem.TIPO_VIAGEM, viagem.getTipoViagem());
        values.put(DatabaseHelper.Viagem.DATA_CHEGADA, viagem.getDataChegada().getTime());
        values.put(DatabaseHelper.Viagem.DATA_SAIDA, viagem.getDataSaida().getTime());
        values.put(DatabaseHelper.Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(DatabaseHelper.Viagem.QUANTIDADE_PESSOAS, viagem.getQuantidadePessoas());

        String whereClause = DatabaseHelper.Viagem._ID + " = ?";
        String[] whereArgs = new String[]{ viagem.getId().toString() };

        return getDb().update(DatabaseHelper.Viagem.TABELA, values, whereClause, whereArgs);
    }

    public Viagem buscaViagemPorId(Integer id){
        String whereClause = DatabaseHelper.Viagem._ID + " = ? ";
        String[] whereArgs = new String[]{ id.toString() };

        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA, DatabaseHelper.Viagem.COLUNAS,
                                      whereClause, whereArgs,
                                      null, null, null);
        if(cursor.moveToNext()){
            Viagem viagem = criarViagem(cursor);
            cursor.close();
            return viagem;
        }
        cursor.close();
        return null;
    }

    public List<Viagem> listarViagens(){
        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,
                                      DatabaseHelper.Viagem.COLUNAS,
                                      null, null, null, null, null);
        List<Viagem> viagens = new ArrayList<>();
        while(cursor.moveToNext()) {
            Viagem viagem = criarViagem(cursor);
            viagens.add(viagem);
        }
        cursor.close();
        return viagens;
    }

    public Double calcularTotalGasto(Viagem viagem){
        Cursor cursor = getDb().rawQuery("SELECT SUM(" + DatabaseHelper.Gasto.VALOR + ") " +
                                         " FROM " + DatabaseHelper.Gasto.TABELA +
                                         " WHERE " + DatabaseHelper.Gasto.VIAGEM_ID + " = ?" ,
                                        new String[] { viagem.getId().toString() });
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;

    }

    private Gasto criaGasto(Cursor cursor){
        return new Gasto(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Gasto._ID)),
                        new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Gasto.DATA))),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Gasto.CATEGORIA)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Gasto.DESCRICAO)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Gasto.VALOR)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Gasto.LOCAL)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Gasto.VIAGEM_ID))
        );
    }

    public List<Gasto> listarGastos(Integer id){
        String whereClause = DatabaseHelper.Gasto.VIAGEM_ID + " = ? ";
        String[] whereArgs = new String[]{ id.toString() };

        List<Gasto> gastos = new ArrayList<>();

        Cursor cursor = getDb().query(DatabaseHelper.Gasto.TABELA,
                                      DatabaseHelper.Gasto.COLUNAS,
                                      whereClause, whereArgs, null, null, null);

        while (cursor.moveToNext()){
            gastos.add(criaGasto(cursor));
        }
        cursor.close();
        return gastos;
    }

    public long inserir(Gasto gasto){
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.Gasto.CATEGORIA, gasto.getCategoria());
        values.put(DatabaseHelper.Gasto.DATA, gasto.getData().getTime());
        values.put(DatabaseHelper.Gasto.DESCRICAO, gasto.getDescricao());
        values.put(DatabaseHelper.Gasto.LOCAL, gasto.getLocal());
        values.put(DatabaseHelper.Gasto.VALOR, gasto.getValor());
        values.put(DatabaseHelper.Gasto.VIAGEM_ID, gasto.getViagemID());

        return getDb().insert(DatabaseHelper.Gasto.TABELA, null, values);
    }
}
