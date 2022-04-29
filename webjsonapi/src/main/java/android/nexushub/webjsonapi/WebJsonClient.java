package android.nexushub.webjsonapi;

import android.nexushub.httpclient.HttpRequest;
import android.nexushub.httpclient.HttpResponse;
import android.nexushub.httpclient.HttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Thiago on 17/12/2017.
 */

public class WebJsonClient {

    private static WebJsonClient ourInstance = new WebJsonClient();
    private HashMap<String, String> headers = new HashMap<>();

    private WebJsonClient(){
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
    }

    public interface JsonCallback {
        void onResponse(int statusCode, String json);
    }

    public static WebJsonClient getInstance(){ return ourInstance; }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }
    public void removeHeader(String name) {
        headers.remove(name);
    }

    public void get(String url, final JsonCallback callback) throws Exception{
        try {
            HttpRequest r = new HttpRequest(HttpRequest.Method.GET, url);
            r.setHeaders(headers);
            r.setCallback(callbackForJsonCallback(callback));
            new HttpTask().execute(r);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void post(String url, JSONObject postData, JsonCallback callback) {
        HttpRequest r = new HttpRequest(HttpRequest.Method.POST, url);
        r.setHeaders(headers);
        r.setCallback(callbackForJsonCallback(callback));
        r.setPostData(postData.toString());
        new HttpTask().execute(r);
    }
    public void put(String url, JSONObject postData, JsonCallback callback){
        HttpRequest r = new HttpRequest(HttpRequest.Method.UPDATE, url);
        r.setHeaders(headers);
        r.setCallback(callbackForJsonCallback(callback));
        r.setPostData(postData.toString());
        new HttpTask().execute(r);
    }
    public void delete(String url, final JsonCallback callback){
        HttpRequest r = new HttpRequest(HttpRequest.Method.DELETE, url);
        r.setHeaders(headers);
        r.setCallback(callbackForJsonCallback(callback));
        new HttpTask().execute(r);
    }

    private HttpRequest.RequestCallback callbackForJsonCallback(final JsonCallback jsonCallback) {
        return new HttpRequest.RequestCallback() {
            @Override
            public void onResponse(HttpResponse r) {
                //JSONObject json = null;
                //try {
                //    json =  new JSONObject(r.getResponse());
                //} catch (JSONException e) {
                //    e.printStackTrace();
                //}

                jsonCallback.onResponse(r.getResponseCode(), r.getResponse());
            }
        };
    }
}
