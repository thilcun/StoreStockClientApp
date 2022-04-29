package android.nexushub.confereestoqueapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nexushub.webjsonapi.WebJsonClient;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WebJsonClient webClient;
    Dialogo dialogo;
    ProgressDialog pd;
    SharedPreferences sharedPrefs;
    Context _context;

    ArrayList<ItemAjuste> itens;
    ItemAjusteAdapter itemAdapter;
    Button btnOK;
    ImageButton btnScan;
    ListView listView;
    EditText etSearch;
    TextView txtLastCodigo, txtLastDescricao, txtLastNcm, txtLastCodigoBarra, txtLastEndereco, txtLastMarca, txtLastQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webClient = WebJsonClient.getInstance();
        dialogo = new Dialogo(this);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        _context = this;
        btnOK = (Button) findViewById(R.id.btnSearch);
        listView = (ListView) findViewById(R.id.listItens);
        etSearch = (EditText) findViewById(R.id.etQuery);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(etSearch.getText().toString() != null || etSearch.getText().toString() != "")
                    {
                        btnOK.callOnClick();

                    }
                    else
                    {
                        dialogo.showDialog("Código vazio!", "Informe um código para procurar!");
                    }
                    return true;
                }
                return false;
            }
        });
        btnScan = (ImageButton) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        setupLastItem();

        itens = new ArrayList<ItemAjuste>();
        itemAdapter = new ItemAjusteAdapter(this, itens);
        itemAdapter.setItemAjusteAdapter(new ItemAjusteAdapter.ItemAjusteAjusteListener() {
            @Override
            public void onItemAjusteReceived(ItemAjuste item) {
                setLastItemAjusteInterface(item);
            }
        });
        listView.setAdapter(itemAdapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pd = ProgressDialog.show(_context, "Confere Estoque", "Procurando por produto. Por favor, aguarde!");
                    String ip = sharedPrefs.getString("servidor_ip", getResources().getString(R.string.pref_servidor_ip_value));
                    int porta = Integer.parseInt(sharedPrefs.getString("servidor_porta", "8080"));
                    final String url = "http://" + ip + ":" + porta + "/api/Produto/Consulta?q=" + etSearch.getText();

                    webClient.get(url, new WebJsonClient.JsonCallback() {
                        @Override
                        public void onResponse(int statusCode, String json) {
                            if (statusCode == 200) {
                                parseReponse(json);
                            } else {
                                dialogo.showDialog("Error " + statusCode, json);
                            }
                        }
                    });
                    pd.dismiss();
                }
                catch (Exception e){
                    dialogo.showDialog("Error - 404", "Aconteceu um erro ao procurar item!");
                }
            }
        });
        //webClient.get("", new WebJsonClient.JsonCallback() {
        //    @Override
        //    public void onResponse(int statusCode, JSONObject json) {

        //    }
        //});
        //webClient.post("", null, new WebJsonClient.JsonCallback() {
        //    @Override
        //    public void onResponse(int statusCode, JSONObject json) {

        //    }
        //});
    }
    public void onClick(View v){
        if(v.getId()==R.id.btnScan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan(scanIntegrator.ONE_D_CODE_TYPES);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            if(scanningResult.getContents() != null) {
                etSearch.setText(scanningResult.getContents());
                btnOK.callOnClick();
            }
        }
    }
    private void setupLastItem() {
        txtLastCodigo = (TextView) findViewById(R.id.txtLastCodigo);
        txtLastDescricao = (TextView) findViewById(R.id.txtLastDescricao);
        txtLastCodigoBarra = (TextView) findViewById(R.id.txtLastCodigoBarra);
        txtLastNcm = (TextView) findViewById(R.id.txtLastNcm);
        txtLastMarca = (TextView) findViewById(R.id.txtLastMarca);
        txtLastQuantidade = (TextView) findViewById(R.id.txtLastQuantidade);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
        }

        return true;
    }
    private void setLastItemAjusteInterface(ItemAjuste item){
        txtLastCodigo.setText(String.valueOf(item.Produto.CodigoProduto));
        txtLastDescricao.setText(item.Produto.Descricao);
        txtLastCodigoBarra.setText(item.Produto.CodigoBarra);
        txtLastMarca.setText(item.Produto.Marca.Descricao + "(" + String.valueOf(item.Produto.Marca.Codigo) + ")");
        txtLastNcm.setText(item.Produto.Ncm);
        txtLastQuantidade.setText(String.valueOf(item.Quantidade));
        //txtLastEndereco.setText(String.valueOf(item.Endereco));

        itens.clear();
        itemAdapter.notifyDataSetChanged();

        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
        btnScan.callOnClick();
    }

    private void
    parseReponse(String json){
        try {
            ArrayList<ItemProduto> produtos = ItemProduto.fromJson(new JSONArray(json));
            itemAdapter.clear();
            itens.clear();
            for (ItemProduto p: produtos) {
                ItemAjuste item = new ItemAjuste();
                item.Produto = p.Produto;
                item.Quantidade = p.Quantidade;
                item.AjusteId = p.AjusteId;

                itens.add(item);
            }
            itemAdapter = new ItemAjusteAdapter(this, itens);
            itemAdapter.setItemAjusteAdapter(new ItemAjusteAdapter.ItemAjusteAjusteListener() {
                @Override
                public void onItemAjusteReceived(ItemAjuste item) {
                    setLastItemAjusteInterface(item);
                }
            });
            listView.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();
            if(itens.size() < 1){
               dialogo.showDialog("Produto não encontrado!", "Nenhum produto encontrado com este codigo!");
            }
        }
        catch (Exception e)
        {
            dialogo.showDialog("Erro ao Converter", e.getMessage());
        }
    }

}
