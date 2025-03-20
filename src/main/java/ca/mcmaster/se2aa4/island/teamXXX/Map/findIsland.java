package ca.mcmaster.se2aa4.island.teamXXX.Map;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Map.Report;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


public class FindIsland extends State {
    private static final Logger logger = LogManager.getLogger(FindIsland.class);
    private final Detector detector = new Detector();
    private boolean turnRightNext;
    private boolean turnLeftNext; 
    private boolean moveForward;
    private boolean echo;

    public FindIsland(Movable drone, Radar radar, Report report) {
        super(drone, radar, report); // Pass the required arguments to the State constructor
        this.turnRightNext = true;
        this.turnLeftNext = false;
        this.echo = false;
    }

    @Override
    public State getNextState(JSONObject response) {
        //logger.info("\n IN STATE \n");
        if (detector.foundGround(response)) {
            // move to go to island state
            return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
        }
        if (echo) {
            radar.echoForward();
            echo = false;
        } else if (turnRightNext) {
            //logger.info("\n Taking Right \n");
            drone.turnRight();
            //logger.info("\n check 1 \n");
            turnRightNext = false;
            turnLeftNext = true;
            echo = true;
            logger.info(drone.getHeading());
        } else if (turnLeftNext) {
            drone.turnLeft();
            turnLeftNext = false;
            turnRightNext = true;
            echo = true;
            logger.info(drone.getHeading());
        }
        return this;
    }

    // private boolean foundGround(JSONObject response) {
    //     if (response == null) {
    //         return false;
    //     }
    //     if (response.has("extras")) {
    //         JSONObject extras = response.getJSONObject("extras");
    //         if (extras.has("found")) {
    //             String found = extras.getString("found");
    //             if ("GROUND".equals(found)) {
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

    // private int getDistance(JSONObject response) {
    //     int range = 0;
    //     if (response.has("extras")) {
    //         JSONObject extras = response.getJSONObject("extras");
    //         if (extras.has("range")) {
    //             range = extras.getInt("range");
    //         }
    //     }
    //     return range;
    // }
}
