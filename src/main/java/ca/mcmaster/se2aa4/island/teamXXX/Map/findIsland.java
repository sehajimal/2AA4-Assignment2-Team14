package ca.mcmaster.se2aa4.island.teamXXX.Map;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Map.Report;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

public class findIsland extends State {
    private Drone drone;
    private boolean turnRightNext = true; 

    public findIsland(Drone drone, Radar radar, Report report) {
        super(drone, radar, report); // Pass the required arguments to the State constructor
        this.drone = drone;
    }

    @Override
    public State getNextState(JSONObject response) {
        if (response.has("extras")) {
            JSONObject extras = response.getJSONObject("extras");
            if (extras.has("found") && extras.getBoolean("found")) {
                System.out.println("Land found! Stopping search.");
                return this; 
            }
        }
        
        if (turnRightNext) {
            drone.turnRight();
        } else {
            drone.turnLeft();
        }
        
        turnRightNext = !turnRightNext; 
        drone.moveForward(); 
        
        return this;
    }
}
