package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

public class Detector {

    public boolean foundGround(JSONObject response) {
        if (response == null) {
            return false;
        }
        if (response.has("extras")) {
            JSONObject extras = response.getJSONObject("extras");
            if (extras.has("found")) {
                String found = extras.getString("found");
                if ("GROUND".equals(found)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getDistance(JSONObject response) {
        int range = 0;
        if (response.has("extras")) {
            JSONObject extras = response.getJSONObject("extras");
            if (extras.has("range")) {
                range = extras.getInt("range");
            }
        }
        return range;
    }
}
