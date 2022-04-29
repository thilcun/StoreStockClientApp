package android.nexushub.confereestoqueapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thiago on 22/03/2018.
 */

public class Produto {
    public int CodigoProduto;
    public String CodigoBarra;
    public String Descricao;
    public String Ncm;
    public double ValorUnitario;
    public Marca Marca;

    public Produto(int codigo, String codigoBarra, String descricao, String ncm, double valorUnitario, Marca marca)
    {
        CodigoProduto = codigo;
        CodigoBarra = codigoBarra;
        Descricao = descricao;
        Ncm = ncm;
        ValorUnitario = valorUnitario;
        Marca = marca;
    }
    public JSONObject toJSON() throws JSONException{
        try {
            JSONObject jo = new JSONObject();
            jo.put("CodigoProduto", CodigoProduto);
            jo.put("CodigoBarra", CodigoBarra);
            jo.put("Descricao", Descricao);
            jo.put("Ncm", Ncm);
            jo.put("ValorUnitario", ValorUnitario);
            jo.put("Marca", Marca.toJSON());

            return jo;
        }
        catch (JSONException e)
        {
           throw e;
        }
    }
    public Produto(JSONObject object) throws JSONException{
        try {
            this.CodigoProduto = object.getInt("CodigoProduto");
            this.Descricao = object.getString("Descricao");
            this.CodigoBarra = object.getString("CodigoBarra");
            this.Ncm = object.getString("Ncm");
            this.ValorUnitario = object.getDouble("ValorUnitario");
            this.Marca = new Marca(new JSONObject(object.getString("Marca")));

        } catch (JSONException e) {
            throw e;
        }
    }
    public static ArrayList<Produto> fromJson(JSONArray jsonObjects) throws JSONException{
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                produtos.add(new Produto(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                throw e;
            }
        }
        return produtos;
    }

}
