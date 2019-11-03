package com.example.afm.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.afm.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static TextView ad_text, headline, deadline;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String adString;
    private static String adHeadline;
    private static String adDeadline;


    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public SecondFragment newInstance(int position) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_second, container, false);
        ad_text = root.findViewById(R.id.ad_text);
        headline = root.findViewById(R.id.headline);
        deadline = root.findViewById(R.id.deadline);
        return root;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public static void fillFragment(JSONObject obj) {
        try {
            headline.setText(obj.getString("headline"));

            adDeadline = "Publicerad:&nbsp;&nbsp;&nbsp;&nbsp;" + obj.getString("publication_date") +
                    "<br>Deadline:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + obj.getString("application_deadline");
            deadline.setText(Html.fromHtml(adDeadline));
            adString = obj.getJSONObject("description").getString("text");
            ad_text.setText(adString);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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

            "salary_description":null, "duration":{
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

}
