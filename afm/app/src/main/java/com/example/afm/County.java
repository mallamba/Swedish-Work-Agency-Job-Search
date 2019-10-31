package com.example.afm;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a Swedish county
 */
class County {
	/** ID **/
	String id;
	
	/** Name **/
	String name;
	
	/** Number of job adverts in the county **/
	int numJobAdverts;

	/** Number of jobs in the county **/
	int numJobs;
	
	/** Offices in the county (platser) **/
	List<Office> offices;
	
	/** Number of job adverts per city/municipality **/ 
	Map<String, Integer> jobAdvertsPerCity;

	public String toString() {
		String ret = "County: " + name + "\n";
		ret += "Number of id: " + id + "\n";
		ret += "Number of jobs: " + numJobs + "\n";
		ret += "Number of job adverts: " + numJobAdverts + "\n";

		ret += "Offices:\n";
		if (offices == null || offices.isEmpty())
			ret += "No offices found";
		else {
			for (Office k : offices) {
				ret += "  " + k + "\n";
			}
		}

		ret += "Job adverts per city:\n";
		if (jobAdvertsPerCity == null || jobAdvertsPerCity.isEmpty())
			ret += "No job adverts found";
		else {
			for (Entry<String, Integer> entry : jobAdvertsPerCity.entrySet()) {
				ret += "  " + entry.getKey() + ": " + entry.getValue() + "\n";
			}
		}
		return ret;
	}
}

