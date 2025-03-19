package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class Report {
    private static Report instance = null;
    private JSONObject discoveries; // Store creeks, sites, biomes, etc.

    private Report() {
        discoveries = new JSONObject();
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
    }

    // Get the single instance of Report
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    // Test to see if works with creekId
    public void addCreek(String creekId) {
        discoveries.getJSONArray("creeks").put(creekId);
    }

    // Add a site to the report
    public void addSite(String siteId) {
        discoveries.getJSONArray("sites").put(siteId);
    }

    // Retrieve all creeks
    public JSONArray getCreeks() {
        return discoveries.getJSONArray("creeks");
    }

    // Retrieve all sites
    public JSONArray getSites() {
        return discoveries.getJSONArray("sites");
    }

    // Retrieve the entire discoveries object
    public JSONObject getDiscoveries() {
        return discoveries;
    }
}
