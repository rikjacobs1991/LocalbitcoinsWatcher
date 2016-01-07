package com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Rik on 6-1-2016.
 */

/*
Sending signature

    Signature is sent via HTTP headers. A total of three fields are needed:
    1.Apiauth-Key: HMAC authentication key.
    2.Apiauth-Nonce: The nonce in this particular request.
    3.Apiauth-Signature: HMAC signature.
 */
public class LocalbitcoinsApiRequestTask extends AsyncTask<String, Integer, JSONObject> {
    private static int timeOutTime = 5000;
    private final static String baseUrl = "https://localbitcoins.com";
    private LocalbitcoinsApiListener localbitcoinsApiListener;
    private LocalbitcoinsApiRequest localbitcoinsApiRequest;


    public enum RequestType{
        POST,
        GET
    }

    public LocalbitcoinsApiRequestTask(LocalbitcoinsApiRequest localbitcoinsApiRequest){
        this.localbitcoinsApiRequest = localbitcoinsApiRequest;
    }

    protected JSONObject doInBackground(String... request) {

        String response = "";

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Apiauth-Key", localbitcoinsApiRequest.apiConfig.key);
        map.put("Apiauth-Nonce", localbitcoinsApiRequest.nonce + "");
        map.put("Apiauth-Signature", localbitcoinsApiRequest.hmac);

        String url = baseUrl + localbitcoinsApiRequest.relativePath + localbitcoinsApiRequest.requestParams;

        switch (localbitcoinsApiRequest.requestType){
            case POST:
                response = post(url, map);
                break;

            case GET:
                response = get(url, map);
                break;
        }

        JSONObject a;

        try {
            a = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return a;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(JSONObject result) {
        localbitcoinsApiListener.JSONFetched(result);
    }

    public String get(String requestURL,  HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);


            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            /*conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            for(Map.Entry<String, String> entry : postDataParams.entrySet()){
                conn.setRequestProperty(URLEncoder.encode(entry.getKey(), "UTF-8"),
                        URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(localbitcoinsApiRequest.requestParams);

            writer.flush();
            writer.close();
            os.close();*/
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String post(String requestURL,  HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(getRequestDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getRequestDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void setLocalbitcoinsApiListener(LocalbitcoinsApiListener l) {
        localbitcoinsApiListener = l;
    }
}
