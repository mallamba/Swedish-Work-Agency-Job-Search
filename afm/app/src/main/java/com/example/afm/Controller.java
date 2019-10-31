package com.example.afm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Louay
 * Controller class to be called in Main class
 */
public class Controller {
    /** Scanner for input from console **/
    Scanner reader;

    /** The input for a county from console **/
    String countyInput;

    /** An Integer to check a condition **/
    int countyId = -1;

    /** Instance of AfApiRequest to handle Arbetsförmedlingen's API **/
    AfApiRequest af_api_request;

    /** JSONObject, JSONArrays to handle data from Arbetsförmedlingen's API **/
    JSONObject countyJson;
    JSONArray officeJarray, jobJarray;


    /** Constructor where the AfApiRequest gets created **/
    public Controller(){
        af_api_request = new AfApiRequest();
    }


    /** Start finding a county by calling method: readInput() **/
    public void runIt(){
        try {
            countyJson = af_api_request.findCounty(this.readInput());
            System.out.println("countyJson: " + countyJson);
        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /** Using the scanner with Cp850-encoding for Swedish letters **/
    /** Returning the input as a String **/
    public String readInput(){
        reader = new Scanner(System.in, "Cp850");
        System.out.println("Enter the input: ");
        countyInput = reader.next();
        System.out.println("Entered Input: " + countyInput);
        return countyInput;
    }


    /** Returns true if a JSONObject is not null **/
    public boolean isCountyValid(){
        return (countyJson != null);
    }


    /** Returns true if a JSONArray is not null **/
    public boolean isOfficeValid(){
        return (officeJarray != null);
    }


    /** Returns a County after obtaining needed data  **/
    public County createdCounty() throws Exception{
        County county = null;
        if(isCountyValid()){
            countyId =  (int) countyJson.getInt("id");
            county = new County();
            county.id = countyJson.get("id").toString();
            county.name = countyJson.get("namn").toString();
            county.numJobs= (int) countyJson.get("antal_ledigajobb");
            county.numJobAdverts= (int) countyJson.get("antal_platsannonser");
            county.offices = offices();
            county.jobAdvertsPerCity = jobsMap();
        }
        return county;
    }


    /** Returns an Office after obtaining needed data **/
    public Office createdOffice(String code, String name, String e){
        Office office = new Office();
        if(isOfficeValid()){
            office.officeCode = code;
            office.officeName = name;
            office.email = e;
        }
        return office;
    }


    /** Returns an ArrayList with offices**/
    public List<Office> offices() throws JSONException {
        List<Office> offices = new ArrayList();
        JSONObject json = new JSONObject();
        Office office = new Office();
        String officeId;
        if( countyId >= 0 ){
            try{
                officeJarray = af_api_request.findOffices(countyId);
            } catch(Exception ex){
                System.out.println("Failure when obtaining JSONArray: " );
            }

            for(int i = 0; i < officeJarray.length(); i++){
                String code, name, e = "";
                json = officeJarray.getJSONObject(i);

                officeId = json.getString("afplatskod");
                try {
                    json = af_api_request.findOffice(officeId);
                    code = json.getString("afplatskod");
                    name = json.getString("afplatsnamn");
                    e = json.getString("epostadress");
                    office = createdOffice(code, name, e);
                    offices.add(office);
                }catch(Exception ex) {
                    System.out.println("Failure when obtaining code, name and email: " );
                }
            }
        }
        return offices;
    }


    /** Returns a HashMap with cities and job ads per city **/
    public Map<String, Integer> jobsMap() {
        Map<String, Integer> jobsMap = new HashMap();
        JSONObject json = new JSONObject();
        try{
            jobJarray = af_api_request.findJobAdverts(countyId);
            for(int i = 0; i < jobJarray.length(); i++){
                json = jobJarray.getJSONObject(i);
                try {
                    String cityName = cityName = json.getString("namn");
                    int jobsNumber = jobsNumber = (int) json.get("antal_platsannonser");
                    jobsMap.put(cityName,jobsNumber );
                }catch(Exception ex) {
                    System.out.println("Failure when obtaining code, name and email: " );
                }
            }
        } catch(Exception ex){
            System.out.println("Failure when obtaining JSONArray: " );
        }


        return jobsMap;
    }

}
