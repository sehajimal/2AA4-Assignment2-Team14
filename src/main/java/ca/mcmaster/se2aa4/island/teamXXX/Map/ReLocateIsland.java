package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

public class ReLocateIsland extends State {

    private static final Logger logger = LogManager.getLogger(ReturnToIsland.class);

    private Detector detector;
    private boolean echo;
    private boolean echoRight;
    private boolean echoLeft;
    private boolean checkLeft;
    private boolean checkRight;
    private boolean turnComplete;
    private boolean finalCheck;
    
    public ReLocateIsland(Movable drone, ScanningSystem radar) {

        super(drone, radar);

        this.detector = new Detector();

        echo = true;
        echoRight = true;
        echoLeft = false;
        checkRight = false;
        checkLeft = false;
        turnComplete = false;
        finalCheck = false;

        logger.info("** In Relocate Island State");

    }

    /*
     * no land ahead, echos left and right to try to detect land
     * can be optimized if unsuccessful to echo forward and as long as not OUT_OF_RANGE in 0 then go forward and repeat proces
     */
    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) { // if ground found then go to it
            if (detector.foundGround(response)) {
                return new GoToIsland(this.drone, this.radar, detector.getDistance(response));
            } // other wise repeat process in new orientation
            return new ReLocateIsland(this.drone, this.radar);
        }

        if (turnComplete) {
            radar.echoForward();
            finalCheck = true;
            turnComplete = false;
            return this;
        }

        if (checkRight) {
            if (detector.foundGround(response)) {
                drone.turnRight();
                turnComplete = true;
                return this;
            }
            checkRight = false;
        } else if (checkLeft) {
            if (detector.foundGround(response)) {
                drone.turnLeft();
                turnComplete = true;
                return this;
            }
            checkLeft = false;
        }

        if (echo) {
            if (echoRight) {
                radar.echoRight();
                checkRight = true;
                echoLeft = true;
                echoRight = false;
                return this;
            } else if (echoLeft) {
                radar.echoLeft();
                echo = false;
                checkLeft = true;
                echoLeft = false;
                return this;
            }
        } 
        drone.stop();
        return this;
    }
}
