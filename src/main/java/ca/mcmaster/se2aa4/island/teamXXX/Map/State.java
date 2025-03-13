package ca.mcmaster.se2aa4.island.teamXXX.Map;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import org.json.JSONObject;

public abstract class State {

    protected Drone drone;

    public State(Drone drone) {
        this.drone = drone;
    }

    public abstract State getNextState(JSONObject response);
    
}
