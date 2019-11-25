package com.example.cta_t4;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrlConnection {
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    private static final String USER_AGENT = "Mozilla/5.0";


    public String getHttpUrlConnection(String url) {
        String data = "";
        try {
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) myurl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
            Log.w("JsonData", data);
            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return data;

    }

    public String getHttpUrlPostConnection(String url, String datajson) {
        String result = "";
        try {
            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("User-Agent", USER_AGENT);
            Log.e("data", "json:" + datajson);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(datajson.getBytes("UTF-8"));
            os.flush();
            os.close();
            int responseCode = con.getResponseCode();
            switch (responseCode) {
                case 200:
                    result = "ok";//all ok
                    break;
                case 401:
                case 403:
                    result = "failed";
                    break;
                default:
                    result = "failed";
                    String httpResponse = con.getResponseMessage();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            Log.d("error", "    " + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

            con.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

}

