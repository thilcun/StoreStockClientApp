package android.nexushub.confereestoqueapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Thiago on 23/03/2018.
 */

public class ItemProduto {
    public int AjusteId;
    public Produto Produto;
    public int Quantidade;

    public ItemProduto(JSONObject object) throws JSONException{
        try {
            this.AjusteId = object.getInt("AjusteId");
            this.Produto = new Produto(new JSONObject(object.getString("Produto")));
            this.Quantidade = object.getInt("Quantidade");

        } catch (JSONException e) {
            throw e;
        }
    }
    public static ArrayList<ItemProduto> fromJson(JSONArray jsonObjects) throws JSONException{
        ArrayList<ItemProduto> items = new ArrayList<ItemProduto>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                items.add(new ItemProduto(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                throw e;
            }
        }
        return items;
    }
}
