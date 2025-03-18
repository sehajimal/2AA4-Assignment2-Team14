package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;
import java.util.List;

public class Report {
    private static Report instance = null;
    private JSONObject discoveries; // Store creeks, sites, biomes, etc.

    private Report() {
        discoveries = new JSONObject();
    }

    // Get the single instance of Report
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    //! IMPLEMENT METHODS TO ADD CREEKS, SITE, AND RETRIEVE THEM
}
