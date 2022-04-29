package android.nexushub.confereestoqueapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Thiago on 22/03/2018.
 */

public class ItemAjuste {
    public int Codigo;
    public int AjusteId;
    public int ItemNumero;
    public Produto Produto;
    public int Quantidade;
    public int Usuario;
    public int Endereco;
    public String Alterado;

    public ItemAjuste()
    {}

    public ItemAjuste(int codigo, int ajusteId, int itemNumero, Produto produto, int quantidade, int usuario, int endereco, String alterado)
    {
        Codigo = codigo;
        AjusteId = ajusteId;
        ItemNumero = itemNumero;
        Produto = produto;
        Quantidade = quantidade;
        Usuario = usuario;
        Endereco = endereco;
        Alterado = alterado;
    }
    public ItemAjuste(JSONObject object) throws JSONException{
        try {
            this.Codigo = object.getInt("Codigo");
            this.AjusteId = object.getInt("AjusteId");
            this.ItemNumero = object.getInt("ItemNumero");
            this.Produto = new Produto(new JSONObject(object.getString("Produto")));
            this.Quantidade = object.getInt("Quantidade");
            this.Usuario = object.getInt("Usuario");
            this.Endereco = object.getInt("Endereco");

        } catch (JSONException e) {
            throw e;
        }
    }
    public JSONObject toJSON() throws JSONException{
        try {
            JSONObject jo = new JSONObject();
            jo.put("Codigo", Codigo);
            jo.put("AjusteId", AjusteId);
            jo.put("ItemNumero", ItemNumero);
            jo.put("Produto", Produto.toJSON());
            jo.put("Quantidade", Quantidade);
            jo.put("Usuario", Usuario);
            jo.put("Endereco", Endereco);
            jo.put("Alterado", Alterado);

            return jo;
        }
        catch (JSONException e)
        {
            throw e;
        }

    }
    public static ArrayList<ItemAjuste> fromJson(JSONArray jsonObjects) throws JSONException{
        ArrayList<ItemAjuste> items = new ArrayList<ItemAjuste>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                items.add(new ItemAjuste(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                throw e;
            }
        }
        return items;
    }
}
