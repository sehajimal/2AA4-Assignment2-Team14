package ca.mcmaster.se2aa4.island.teamXXX.Map;

import java.security.PKCS12Attribute;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

public class Searcher extends State {

    // boooleans indicating whether to fly or scan on this iteration
    private boolean scan;
    private boolean fly;
    /*
     * bandaid fix for handling case where if at shore, drone gets stuck in turns
     * (switches between TurnDrone and Search), long term an additional state or 
     * more secure logic can be added
     */
    private boolean foundLand;
    
    public Searcher(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        scan = true;
        fly = false;
        foundLand = false;
    }

    public State getNextState(JSONObject response) {

        if (fly) {
            // ADD LOGIC TO CHECK FOR CREEKS OR SITES

            //! add logic to add to report if creek or site is found
            if (foundCreek(response)) {
                //return new State(this.drone, this.radar, this.report);
            }
            if (foundSite(response)) {
                //return new State(this.drone, this.radar, this.report);
            }

            // logic to break out of search state
            // if (containsOcean(response) && onLand) {
            //     return new TurnDrone(this.drone, this.radar, this.report);
            // } else if (!containsOcean(response)) {
            //     onLand = true;
            // }
            if (inOcean(response) && foundLand) {
                return new TurnDrone(this.drone, this.radar, this.report);
            } else if (!inOcean(response) && !foundLand) {
                foundLand = true;
            }

            drone.moveForward();
            fly = false;
            scan = true;
        } else if (scan) {
            radar.scan();
            fly = true;
            scan = false;
        }

        return this;
    }

    private boolean containsOcean(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("biomes")) return false;

        JSONArray biomes = extras.getJSONArray("biomes");
        for (int i = 0; i < biomes.length(); i++) {
            if (biomes.getString(i).equals("OCEAN")) {
                return true;
            }
        }
        return false;
    }

    private boolean inOcean(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.optJSONObject("extras"); // Use optJSONObject to avoid exceptions
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes"); // Use optJSONArray to avoid exceptions
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }

    private boolean foundCreek(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("creeks")) return false;

        JSONArray creeks = extras.getJSONArray("creeks");
        if (creeks.length() > 0) {
            return true;
        }
        return false;
    }

    //! need improvement for check (handling arrays of length > 1)
    private boolean foundSite(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("sites")) return false;

        JSONArray sites = extras.getJSONArray("creeks");
        if (sites.length() > 0) {
            return true;
        }
        return false;
    }

}
