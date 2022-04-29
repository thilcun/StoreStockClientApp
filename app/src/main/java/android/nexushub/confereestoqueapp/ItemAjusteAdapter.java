package android.nexushub.confereestoqueapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nexushub.webjsonapi.WebJsonClient;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thiago on 23/03/2018.
 */

public class ItemAjusteAdapter extends ArrayAdapter<ItemAjuste> {

    Dialogo dialogo;
    ProgressDialog pd;
    Context _context;
    private ItemAjusteAjusteListener _listener;

    private SharedPreferences sharedPrefs;
    ViewHolder viewholder;
    WebJsonClient webClient;

    private class ViewHolder {
        View v;
        TextView txtCodigo, txtDescricao, txtMarca, txtEan,txtEndereco;
        EditText etQuantidade;
        ImageButton btnEnviar;
    }

    public ItemAjusteAdapter(Context context, ArrayList<ItemAjuste> itens) {
        super(context, 0, itens);
        _context = context;
        dialogo = new Dialogo(context);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
        webClient = WebJsonClient.getInstance();
        //listViewHolder = new HashMap<String, ViewHolder>();
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemAjuste item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item, parent, false);
            viewholder = new ViewHolder();

            viewholder.v = convertView;
            viewholder.txtCodigo = (TextView) convertView.findViewById(R.id.txtCodigo);
            viewholder.txtDescricao = (TextView) convertView.findViewById(R.id.txtDescricao);
            viewholder.txtEan = (TextView) convertView.findViewById(R.id.txtCodigoBarra);
            viewholder.txtEndereco = (TextView) convertView.findViewById(R.id.txtEndereco);
            viewholder.txtMarca = (TextView) convertView.findViewById(R.id.txtMarca);
            viewholder.etQuantidade = (EditText) convertView.findViewById(R.id.etQuantidade);
            viewholder.btnEnviar = (ImageButton) convertView.findViewById(R.id.btnEnviar);

            //listViewHolder.put(item.produto.Codigo, viewholder);
            convertView.setTag(viewholder);



        } else {
            viewholder = (ViewHolder) convertView.getTag();
            //listViewHolder.put(item.produto.Codigo, viewholder);
        }

        //MyClass item = getItem(position);



        viewholder.txtEndereco.setText(String.valueOf(item.Endereco));
        viewholder.etQuantidade.setText(String.valueOf(item.Quantidade));
        viewholder.txtCodigo.setText(String.valueOf(item.Produto.CodigoProduto));
        viewholder.txtDescricao.setText(item.Produto.Descricao);
        viewholder.txtMarca.setText(item.Produto.Marca.Descricao + "(" + String.valueOf(item.Produto.Marca.Codigo) + ")");
        viewholder.txtEan.setText(item.Produto.CodigoBarra);
        final EditText editQuantidade = (EditText) convertView.findViewById(R.id.etQuantidade);
        //editQtdConf.setText(String.valueOf(item.quantidadeconferida));
        //final ImageView status = (ImageView) convertView.findViewById(R.id.imgStatus);
        //status.setImageResource(R.drawable.send);
        viewholder.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    pd = ProgressDialog.show(_context, "Confere Estoque", "Adicionando Item. Por favor, aguarde!");
                    String usuarioid = String.valueOf(_context.getResources().getInteger(R.integer.pref_usuario_id_value));
                    item.Usuario = Integer.parseInt(sharedPrefs.getString("usuario_id", String.valueOf(usuarioid)));
                    String endereco = String.valueOf(_context.getResources().getInteger(R.integer.pref_endereco_value));
                    item.Endereco = Integer.parseInt(sharedPrefs.getString("endereco", endereco));
                    String qtd = editQuantidade.getText().toString();
                    item.Quantidade = Integer.parseInt(qtd);
                    ItemAjuste i = item;
                    String ip = sharedPrefs.getString("servidor_ip", _context.getResources().getString(R.string.pref_servidor_ip_value));
                    int porta = Integer.parseInt(sharedPrefs.getString("servidor_porta", "8080"));
                    final String url = "http://" + ip + ":" + porta + "/api/ItemAjuste/Salvar";
                    webClient.post(url, i.toJSON(), new WebJsonClient.JsonCallback() {
                        @Override
                        public void onResponse(int statusCode, String json) {
                            if (statusCode == 200)
                                parseResponse(json);
                            else
                                dialogo.showDialog("Error" + statusCode, "Erro ao salvar item!");
                        }
                    });
                    pd.dismiss();

                }
                catch (Exception e){
                    dialogo.showDialog("Erro", e.getMessage());
                }
            }
        });


        //if (item!= null) {
        // My layout has only one TextView
        // do whatever you want with your string and long
        //viewHolder.itemView.setText(String.format("%s %d", item.reason, item.long_val));
        //}

        return convertView;
    }
    private void parseResponse(String json){
        try{
            ItemAjuste item = new ItemAjuste(new JSONObject(json));
            _listener.onItemAjusteReceived(item);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void setItemAjusteAdapter(ItemAjusteAjusteListener listener){
        _listener = listener;
    }
    public interface ItemAjusteAjusteListener {
        public void onItemAjusteReceived(ItemAjuste item);
    }

}
