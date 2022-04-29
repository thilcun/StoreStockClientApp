package android.nexushub.confereestoqueapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thiago on 22/03/2018.
 */

public class Marca {
    public int Codigo;
    public String Descricao;

    public Marca(JSONObject object) throws JSONException{
        try {
            this.Codigo = object.getInt("Codigo");
            this.Descricao = object.getString("Descricao");

        } catch (JSONException e) {
            throw e;
        }
    }
    public JSONObject toJSON() throws JSONException{
        try {
            JSONObject jo = new JSONObject();
            jo.put("Codigo", Codigo);
            jo.put("Descricao", Descricao);

            return jo;
        }
        catch (JSONException e)
        {
            throw e;
        }

    }
}
