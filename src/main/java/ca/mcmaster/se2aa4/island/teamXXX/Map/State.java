package ca.mcmaster.se2aa4.island.teamXXX.Map;

//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

import org.json.JSONObject;

public abstract class State {

    protected final Movable drone;
    protected final ScanningSystem radar;

    // could have put Logger in this class to make it easier
    public State(Movable drone, ScanningSystem radar) {
        this.drone = drone;
        this.radar = radar;
    }

    public abstract State getNextState(JSONObject response);
    
}
