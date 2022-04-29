package android.nexushub.httpclient;


import java.util.HashMap;
/**
 * Created by amitaymolko on 2/16/16.
 */
public class HttpRequest {

    public interface RequestCallback {
        void onResponse(HttpResponse r);
    }

    public enum Method {
        GET,
        POST,
        UPDATE,
        DELETE
    }

    private Method method;
    private String URL;
    private HashMap<String, String> headers;
    private String postData;
    private RequestCallback callback;

    public HttpRequest(Method method, String URL) {
        this.method = method;
        this.URL = URL;
    }

    public HttpRequest(Method method, String URL, String postData) {
        this.method = method;
        this.URL = URL;
        this.postData = postData;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodString() {
        return method.toString();
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public RequestCallback getCallback() {
        return callback;
    }

    public void setCallback(RequestCallback callback) {
        this.callback = callback;
    }

}
