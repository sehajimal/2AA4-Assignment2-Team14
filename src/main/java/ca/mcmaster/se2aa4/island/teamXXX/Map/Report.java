package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Report {
    private static Report instance = null;
    private JSONObject discoveries; // Store creeks, sites, etc.
    private boolean isValid; // Initially false

    private Report() {
        discoveries = new JSONObject();
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
        isValid = false;
    }

    // Get the singleton instance of Report
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    // Add a creek with ID, x, and y position
    public void addCreek(String creekId, int x, int y) {
        JSONObject creek = new JSONObject();
        creek.put("id", creekId);
        creek.put("x", x);
        creek.put("y", y);
        
        discoveries.getJSONArray("creeks").put(creek);
        updateValidity(); // Check if report is valid
    }

    // Add a site with ID, x, and y position
    public void addSite(String siteId, int x, int y) {
        JSONObject site = new JSONObject();
        site.put("id", siteId);
        site.put("x", x);
        site.put("y", y);
        
        discoveries.getJSONArray("sites").put(site);
        updateValidity(); // Check if report is valid
    }

    // Retrieve all creeks as a JSONArray
    public JSONArray getCreeks() {
        return discoveries.getJSONArray("creeks");
    }

    // Retrieve all sites as a JSONArray
    public JSONArray getSites() {
        return discoveries.getJSONArray("sites");
    }

    // Retrieve the entire discoveries object
    public JSONObject getDiscoveries() {
        return discoveries;
    }

    // Check if there is at least one creek and one site
    private void updateValidity() {
        isValid = (getCreeks().length() > 0 && getSites().length() > 0);
    }

    // Check if the report is valid
    public boolean isValid() {
        return isValid;
    }
}
