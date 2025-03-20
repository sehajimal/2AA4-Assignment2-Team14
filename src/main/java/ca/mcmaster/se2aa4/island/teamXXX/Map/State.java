package ca.mcmaster.se2aa4.island.teamXXX.Map;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import org.json.JSONObject;

public abstract class State {

    protected final Movable drone;
    protected final Radar radar;
    protected Report report;

    //! tight coupling with Radar, can be improved (use interface)
    public State(Movable drone, Radar radar, Report report) {
        this.drone = drone;
        this.radar = radar;
        this.report = report;
    }

    public abstract State getNextState(JSONObject response);
    
}
