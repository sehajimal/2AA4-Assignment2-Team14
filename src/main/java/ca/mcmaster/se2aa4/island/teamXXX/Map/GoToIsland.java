package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

public class GoToIsland extends State {

    int distance;

    public GoToIsland(Movable drone, Radar radar, Report report, int distance) {
        super(drone, radar, report); // Pass the required arguments to the State constructor
        this.distance = distance;
    }
    
    @Override
    public State getNextState(JSONObject response) {
        if (this.distance > 0) {
            distance--;
            drone.moveForward();
            return this;
        }
        return new Searcher(this.drone, this.radar, this.report);
    }

}
