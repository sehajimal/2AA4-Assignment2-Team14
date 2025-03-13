package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

public class Actions {



    public JSONObject echo(Directions direction) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "echo");
        parameters.put("direction", direction);
        decision.put("parameters", parameters);
        return decision;
    }

    public JSONObject heading(Directions direction) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "heading");
        parameters.put("direction", direction);
        decision.put("parameters", parameters);
        return decision;
    }

    public JSONObject stop() {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");
        return decision;
    }
    
}
