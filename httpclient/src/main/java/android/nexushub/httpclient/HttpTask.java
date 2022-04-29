package android.nexushub.httpclient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by amitaymolko on 2/16/16.
 */

public class HttpTask extends AsyncTask<HttpRequest, String, HttpResponse> {

    @Override
    protected HttpResponse doInBackground(HttpRequest... params){
        URL url;
        HttpURLConnection urlConnection = null;
        HttpResponse response = new HttpResponse();

        HttpRequest request = params[0];
        int responseCode = 500;
        try {
            //HttpRequest request = params[0];
            if (request == null || request.getURL() == null || request.getMethod() == null) {
                Log.e("HttpTask", "BAD HttpRequest");
                throw new Exception();
            }
            url = new URL(request.getURL());
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.v("HttpTask", request.getMethodString());

            urlConnection.setRequestMethod(request.getMethodString());

            if (request.getHeaders() != null) {
                for (HashMap.Entry<String, String> pair : request.getHeaders().entrySet()) {
                    urlConnection.setRequestProperty(pair.getKey(), pair.getValue());
                }
            }
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            if (request.getPostData() != null) {
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream());
                byte[] postData       = request.getPostData().getBytes();
                wr.write( postData );
            }
            urlConnection.connect();

            responseCode = urlConnection.getResponseCode();
            String responseString;
            if(responseCode == HttpsURLConnection.HTTP_OK){
                responseString = readStream(urlConnection.getInputStream());
            }else{
                responseString = readStream(urlConnection.getErrorStream());
            }
            Log.v("HttpTask", "Response code:"+ responseCode);
            Log.v("HttpTask", responseString);
            response = new HttpResponse(responseCode, responseString, request.getCallback());

        } catch (Exception e) {
            response = new HttpResponse(responseCode, e.getMessage(), request.getCallback());
            //e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        super.onPostExecute(response);
        response.getCallback().onResponse(response);
    }
}