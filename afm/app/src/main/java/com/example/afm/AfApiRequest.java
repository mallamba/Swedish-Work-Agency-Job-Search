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
 * @author Louay
 * AfApiRequest class to handle all API requests
 */
public class AfApiRequest {
    static OkHttpClient client;
    static Response response;
    static Request request;
    static String api_key = "bG91YXkua2hhbGlsQGxpdmUuc2U";

    /**
     * Constructor OkHttpClient gets created
     **/
    public AfApiRequest() {
        client = new OkHttpClient();
    }







    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/

    public static List[] lists(String search_word) {
        List[] lists = new List[7];
        //JSONArray j = sortRelevance(jsonArray(search_word));
        JSONArray j = jsonArray(search_word);
        lists[0] = headList(j);
        lists[1] = secondList(j);
        lists[2] = new ArrayList<JSONArray>();
        lists[2].add(j);
        lists[3] = sideList1(j);
        lists[4] = sideList2(j);
        lists[5] = sideList3(j);
        lists[6] = sideList4(j);
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
            thread.interrupt();
        }
        thread.interrupt();
        return searchObject.getValue();
    }


    public static class Search implements Runnable {
        private volatile JSONArray values;
        private String search_word = "";

        public Search(String search_word) {
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


    /**
     * Returns a JSONObject with county data from a given county name
     **/
    private static JSONArray findJobs(String search_word) throws Exception {
        client = new OkHttpClient();
        String url = "https://jobsearch.api.jobtechdev.se/search?q=" + search_word + "&offset=0&limit=100";
        return extractJSON(url).getJSONArray("hits");
    }


    /**
     * Returns a JSONObject from a given url
     **/
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


    private static JSONArray sortRelevance(JSONArray jA) {
        double rel1, rel2;
        JSONObject j1, j2, tempJ = new JSONObject();
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jA.length() - 1; i++) {
            for (int x = 1; x < jA.length(); x++) {
                try {
                    j1 = jA.getJSONObject(i);
                    j2 = jA.getJSONObject(x);
                    rel1 = j1.getDouble("relevance");
                    rel2 = j2.getDouble("relevance");
                    tempJ = j1;
                    if (rel2 > rel1)
                        tempJ = j2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            newArray.put(tempJ);
        }
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
                if (municipality.equals("null"))
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


    private static List<String> sideList1(JSONArray j) {
        JSONObject jO;
        String number_of_vacancies;
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i);
                number_of_vacancies = jO.getString("number_of_vacancies");
                if (number_of_vacancies == "null")
                    number_of_vacancies = "";
                list1.add(number_of_vacancies);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    private static List<String> sideList2(JSONArray j) {
        JSONObject jO;
        String duration;
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i).getJSONObject("duration");
                duration = jO.getString("label");
                if (duration == "null")
                    duration = "";
                list1.add(duration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    private static List<String> sideList3(JSONArray j) {
        JSONObject jO;
        String working_hours_type;
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i).getJSONObject("working_hours_type");
                working_hours_type = jO.getString("label");
                if (working_hours_type == "null")
                    working_hours_type = "";
                list1.add(working_hours_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }


    private static List<String> sideList4(JSONArray j) {
        JSONObject jO;
        String occupation;
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < j.length(); i++) {
            try {
                jO = j.getJSONObject(i).getJSONObject("occupation");
                occupation = jO.getString("label");
                if (occupation == "null")
                    occupation = "";
                list1.add(occupation);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/
    /*******************************************/




    public void test() {
/*
    "hits":
    {
            "relevance":0,
            "id":"10521780",
            "external_id":"0018-618190",
            "webpage_url":
            "https:\/\/www.arbetsformedlingen.se\/For-arbetssokande\/Platsbanken\/annonser\/10521780",
                "logo_url":"https:\/\/jobsearch.api.jobtechdev.se\/ad\/10521780\/logo",
            "headline":"Vi söker kockar med erfarenhet av det asiatiska köket",
            "application_deadline":"2019-11-05T00:00:00",
            "number_of_vacancies":2,

            "description":{
                "text":
                "Verksamhetsbeskrivning:\nRestaurang Thai Hoa är en central belägen restaurang som serverar vällagad och smakrik mat från Asiens populäraste kök.
                Det finns även flera svenska klassiska rätter att välja mellan. Restaurangen är öppen veckans alla dagar och erbjuder både lunchbuffé och à
                la carte samt möjligheten att ta med maten hem. \n\nVi söker kockar med erfarenhet av det asiatiska
                köket.\n\nArbetsuppgifter: \nTillaga asiatiska maträtter och bidra med inspiration. \n\nKrav: \nKunskaper iform av utbildning
                och arbetserfarenheter inom det asiatiska köket \n\nVälkommen med din ansökan!",
                    "company_information":null, "needs":null, "requirements":null, "conditions":null
            },

            "employment_type":{
                "concept_id":"PFZr_Syz_cUq", "label":"Vanlig anställning", "legacy_ams_taxonomy_id":"1"
            },

            "salary_type":{
                "concept_id":"oG8G_9cW_nRf", "label":
                "Fast månads- vecko- eller timlön", "legacy_ams_taxonomy_id":"1"
            },

            "salary_description":null,

            "duration":{
                "concept_id":"a7uU_j21_mkL", "label":"Tillsvidare", "legacy_ams_taxonomy_id":"1"
            },

            "working_hours_type":{
                "concept_id":"6YE1_gAC_R2G", "label":"Heltid", "legacy_ams_taxonomy_id":"1"
            },

            "scope_of_work":{
                "min":100, "max":100
            },

            "access":null,

            "employer":{
                    "phone_number":"0490-19118", "email":null, "url":null, "organization_number":
                    "5569699480", "name":"Restaurang Thai Hoa i Västervik AB", "workplace":
                    "Restaurang Thai Hoa i Västervik AB"
            },

            "application_details":{
                    "information":null, "reference":null, "email":"krisphan@hotmail.com", "via_af":
                    false, "url":null, "other":null
            },

            "experience_required":true,
            "access_to_own_car":false,
            "driving_license_required":false,
            "driving_license":null,

            "occupation":{
                "concept_id":"YKhj_n5P_g4P", "label":"Specialkock", "legacy_ams_taxonomy_id":"5483"
            },

            "occupation_group":{
                "concept_id":"BStc_SJh_DKG", "label":"Kockar och kallskänkor", "legacy_ams_taxonomy_id":
                "5120"
            },

            "occupation_field":{
                "concept_id":"ScKy_FHB_7wT", "label":
                "Hotell, restaurang, storhushåll", "legacy_ams_taxonomy_id":"7"
            },

            "workplace_address":{
                "municipality_code":"0883", "municipality":"Västervik", "region_code":"08", "region":
                "Kalmar län", "country_code":"199", "country":"Sverige", "street_address":
                "Bredgatan 6", "postcode":"59330", "city":"VÄSTERVIK",
                "coordinates":[16.6409438602201, 57.7579635425733]
            },

            "must_have":{
                "skills":[],"languages":[],"work_experiences":[]
            },

            "nice_to_have":{
                "skills":[],"languages":[],"work_experiences":[]
            },

            "publication_date":"2019-10-05T05:00:02",
            "last_publication_date":"2019-11-05T23:59:59",
            "removed":false,
            "removed_date":null,
            "source_type":"VIA_AIS",
            "timestamp":1570244403354
    }

*/

    }












    /**
     * Returns a JSONObject with county data from a given county name
     **/
    public JSONObject findCounty(String countyName) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/soklista/lan");
        JSONObject json = null;
        JSONArray jArray = null;
        jArray = extractJSON(url).getJSONObject("soklista").getJSONArray("sokdata");
        if (!countyName.contains("län")) {
            countyName += " län";
        }
        for (int i = 0; i < jArray.length(); i++) {
            json = jArray.getJSONObject(i);
            if (json.get("namn").toString().equalsIgnoreCase(countyName)) {

                i = jArray.length();
            } else
                json = null;
        }
        return json;
    }


    private static List<Drawable> logoList(JSONArray j) {
        List<Drawable> list0;
        Thread thread;

/*
        Draw draw = new Draw(j);
        thread = new Thread(draw);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            thread.destroy();
        }
        list0 = draw.getValue();
        thread.interrupt();
*/

        list0 = new ArrayList<>();
        return list0;
    }


    /**
     * Returns a JSONArray with offices data from a given county id
     **/
    public JSONArray findOffices(int countyId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/platser?lanid=" + countyId);

        JSONObject json = null;
        JSONArray jArray = null;
        try {
            json = extractJSON(url).getJSONObject("arbetsformedlingslista");
            jArray = json.getJSONArray("arbetsformedlingplatsdata");
        } catch (Exception e) {
            json = null;
        }
        return jArray;
    }


    /**
     * Returns a JSONObject with office data from a given office id
     **/
    public JSONObject findOffice(String officeId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/arbetsformedling/" + officeId);
        JSONObject json = null;

        try {
            json = extractJSON(url).getJSONObject("arbetsformedling");
        } catch (Exception e) {
            json = null;
        }
        return json;
    }


    /**
     * Returns a JSONArray with job ads data from a given county id
     **/
    public JSONArray findJobAdverts(int countyId) throws Exception {
        String url = ("https://api.arbetsformedlingen.se/af/v0/platsannonser/soklista/kommuner?lanid=" + countyId);

        JSONArray jArray = null;
        try {
            jArray = extractJSON(url).getJSONObject("soklista").getJSONArray("sokdata");
        } catch (Exception e) {

        }
        return jArray;
    }


    public static class Draw implements Runnable {
        JSONObject jO;
        String logo_url;
        private volatile Drawable drawable;
        private JSONArray j;
        final ArrayList<Drawable> list0;
        Bitmap x;
        HttpURLConnection connection;
        InputStream input;

        public Draw(JSONArray j) {
            this.j = j;
            list0 = new ArrayList<>();
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < j.length(); i++) {
                    try {
                        jO = j.getJSONObject(i);
                        logo_url = jO.getString("logo_url");
                        connection = (HttpURLConnection) new URL(logo_url).openConnection();
                        connection.connect();
                        input = connection.getInputStream();
                        x = BitmapFactory.decodeStream(input);
                        drawable = new BitmapDrawable(Resources.getSystem(), x);
                        list0.add(drawable);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<Drawable> getValue() {
            return list0;
        }
    }


}

