package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

public class GoToIsland extends State {

    int distance;

    public GoToIsland(Movable drone, ScanningSystem radar, int distance) {
        super(drone, radar);
        this.distance = distance;
    }
    
    /*
     * after locating land, flies to land
     * given a distance forward to land and flys that many times
     */
    @Override
    public State getNextState(JSONObject response) {
        if (this.distance > 0) {
            distance--;
            drone.moveForward();
            return this;
        }
        return new Searcher(this.drone, this.radar);
    }

}
