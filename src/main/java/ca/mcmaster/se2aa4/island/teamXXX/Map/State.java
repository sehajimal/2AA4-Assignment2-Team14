package ca.mcmaster.se2aa4.island.teamXXX.Map;

//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

import org.json.JSONObject;

public abstract class State {

    protected final Movable drone;
    protected final ScanningSystem radar;
    protected Report report;

    // could have put Logger in this class to make it easier
    //! tight coupling with Radar, can be improved (use interface)
    public State(Movable drone, ScanningSystem radar, Report report) {
        this.drone = drone;
        this.radar = radar;
        this.report = report;
    }

    public abstract State getNextState(JSONObject response);
    
}
