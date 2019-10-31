/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.example.afm;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Louay
 * AfApiRequest class to handle all API requests
 */
public class AfApiRequest {
    
    static OkHttpClient client;
    static Response response;
    static Request request;
    static String api_key = "bG91YXkua2hhbGlsQGxpdmUuc2U";
    
    /** Constructor OkHttpClient gets created **/
    public AfApiRequest(){
        client = new OkHttpClient();
    }
    
    
    /** Returns a JSONObject from a given url **/
    private static JSONObject extractJSON(String url) {

        JSONObject jsonObject = null;
        request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("api-key", "bG91YXkua2hhbGlsQGxpdmUuc2U")
                .addHeader("User-Agent", "PostmanRuntime/7.18.0")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "jobsearch.api.jobtechdev.se")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Cookie", "24440eaa2d18dec2438eb27e11cad579=db0952c0c747a35d5d2764da3646d4ba")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            response = client.newCall(request).execute();
            jsonObject = new JSONObject(response.body().string());
        } catch (IOException ex) {
            Logger.getLogger(AfApiRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    
    
    /** Returns a JSONObject with county data from a given county name **/
    public JSONObject findCounty(String countyName) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/soklista/lan");
        
        JSONObject json = null;
        JSONArray jArray = null;
        
        jArray = extractJSON(url).getJSONObject("soklista").getJSONArray("sokdata");
        
        if(!countyName.contains("län")){
            countyName += " län";
        }
        for(int i = 0; i < jArray.length(); i++){
            json = jArray.getJSONObject(i);
            if(json.get("namn").toString().equalsIgnoreCase(countyName)){
                
                i = jArray.length();
            }
            else
                json = null;
        }
        return json;
    }



    /** Returns a JSONObject with county data from a given county name **/
    private static JSONArray findJobs(String search_word) throws Exception {
        client = new OkHttpClient();
        String url = "https://jobsearch.api.jobtechdev.se/search?q=" + search_word + "&offset=0&limit=10";
        return extractJSON(url).getJSONArray("hits");
    }


    public static List[] lists_3(String search_word){
        Log.d("....................",search_word);
        List[] lists = new List[3];
        JSONArray j = sortRelevance( jsonArray(search_word) );
        lists[0] = headList(j);
        lists[1] = secondList(j);
        lists[2] = logoList(j);
        return lists;
    }

    private static JSONArray jsonArray(String search_word) {
        Search searchObject = new Search(search_word);
        Thread thread = new Thread(searchObject);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("............... object",searchObject.toString());
        return searchObject.getValue();
    }


    private static JSONArray sortRelevance(JSONArray jA) {
        double rel1, rel2;
        JSONObject j1, j2, tempJ = new JSONObject();
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jA.length() - 1; i++) {
            for (int x = 1; x < jA.length(); x++  ) {
                try {
                    j1 = jA.getJSONObject(i);
                    j2 = jA.getJSONObject(x);
                    rel1 = j1.getDouble("relevance");
                    rel2 = j2.getDouble("relevance");
                    tempJ = j1;
                    if(rel2 > rel1)
                        tempJ = j2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            newArray.put(tempJ);
        }
        Log.d(".......... sort ",newArray.toString());
        return newArray;
    }


    private static List<String> headList(JSONArray j) {
        JSONObject jO;
        String headline;
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i);
                headline = jO.getString("headline");
                list1.add(headline);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    private static List<String> secondList(JSONArray j) {
        JSONObject jO;
        String secondLine, employer, municipality, region = "";
        final ArrayList<String> list2 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i);
                municipality = jO.getJSONObject("workplace_address").getString("municipality");
                region = "";
                if ( municipality.equals("null") )
                    municipality = jO.getJSONObject("workplace_address").getString("country");
                else
                    region = jO.getJSONObject("workplace_address").getString("region");
                employer = jO.getJSONObject("employer").getString("name");
                secondLine = municipality + ", " + region + ", " + employer;
                list2.add(secondLine);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list2;
    }

    private static List<Drawable> logoList(JSONArray j) {
        JSONObject jO;
        String logo_url;
        final ArrayList<Drawable> list0 = new ArrayList<Drawable>();
        Thread thread;
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i);
                logo_url = jO.getString("logo_url");
                Draw draw = new Draw( logo_url );
                thread = new Thread(draw);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                list0.add( draw.getValue() );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list0;
    }

    
    /** Returns a JSONArray with offices data from a given county id  **/
    public JSONArray findOffices(int countyId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/platser?lanid=" + countyId);
        
        JSONObject json = null;
        JSONArray jArray = null;
        try {
            json = extractJSON(url).getJSONObject("arbetsformedlingslista");
            jArray = json.getJSONArray("arbetsformedlingplatsdata");
        } catch(Exception e) {
            json = null;
        }
        return jArray;
    }
    
    
    /** Returns a JSONObject with office data from a given office id **/
    public JSONObject findOffice(String officeId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/" + officeId);
        JSONObject json = null;
        
        try {
            json = extractJSON(url).getJSONObject("arbetsformedling");
        } catch(Exception e) {
            json = null;
        }
        return json;
    }
    
    
    /** Returns a JSONArray with job ads data from a given county id **/
    public JSONArray findJobAdverts(int countyId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/platsannonser/soklista/kommuner?lanid=" + countyId);
        
        JSONArray jArray = null;
        try {
            jArray = extractJSON(url).getJSONObject("soklista").getJSONArray("sokdata");
        } catch(Exception e) {
            
        }
        return jArray;
    }




    public static class Search implements Runnable {
        private volatile JSONArray values;
        private String search_word = "";

        public Search(String search_word){
            this.search_word = search_word;
        }

        @Override
        public void run() {
            try {
                values = AfApiRequest.findJobs(search_word);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public JSONArray getValue() {
            return values;
        }
    }



    public static class Draw implements Runnable {
        private volatile Drawable drawable;
        private String url;

        public Draw(String url){
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Bitmap x;

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                drawable = new BitmapDrawable(Resources.getSystem(), x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Drawable getValue() {
            return drawable;
        }
    }



}
