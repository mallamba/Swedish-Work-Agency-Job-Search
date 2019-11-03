package com.example.afm.ui.main;

import android.content.Context;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.afm.AfApiRequest;
import com.example.afm.CustomToast;
import com.example.afm.ListArrayAdapter;

import com.example.afm.MainActivity;
import com.example.afm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private ViewPager viewPager;

    private EditText search_field;
    private TextView textView;
    private Button search_btn;
    private ListView listview;
    private Vibrator vibe;




    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);



    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);
        textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Arbetsförmedlingen");
            }
        });

        viewPager = MainActivity.getViewPager();


        vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        search_field = root.findViewById(R.id.search_text);

        listview = root.findViewById(R.id.listview);

        search_btn = root.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibe.vibrate(450);
                final List[] lists = AfApiRequest.lists_3( search_field.getText().toString() );

                final ListArrayAdapter adapter = new ListArrayAdapter(getContext(),
                        android.R.layout.simple_list_item_1, lists[2], lists[0], lists[1]);
                listview.setAdapter(adapter);
                //listview.setVisibility(0);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            final int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        view.animate().setDuration(250).alpha(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        vibe.vibrate(250);
                                        viewPager.setCurrentItem(1);
                                        //lists[0].remove(item);
                                        adapter.notifyDataSetChanged();
                                        view.setAlpha(1);


                                        JSONArray arr = (JSONArray) lists[3].get(0);
                                        List<JSONObject> list = new ArrayList();
                                        JSONObject obj = null;
                                        try {
                                            obj = (JSONObject) arr.get(position);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        SecondFragment.fillFragment(obj );
                                    }
                                });
                    }

                });
            }
        });
        return root;
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









    /************************************************************************/
    /********************Implement the listener****************************/
    /************************************************************************/
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 1:
                CustomToast.makeText(getActivity().getBaseContext(), "item one 1 case", Toast.LENGTH_LONG);


                break;
            case 0:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    /************************************************************************/
    /************************************************************************/
    /************************************************************************/







}