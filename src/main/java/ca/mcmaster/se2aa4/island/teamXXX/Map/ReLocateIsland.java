package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

public class ReLocateIsland extends State {
    private Detector detector;
    private boolean echo;
    private boolean echoRight;
    private boolean echoLeft;
    private boolean checkLeft;
    private boolean checkRight;
    //private boolean turnRight;
    //private boolean turnleft;
    private boolean turnComplete;
    private boolean finalCheck;
    Integer repositionCount;
    
    public ReLocateIsland(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        this.detector = new Detector();

        echo = true;
        echoRight = true;
        echoLeft = false;
        checkRight = false;
        checkLeft = false;
        //turnRight = false;
        //turnleft = false;
        turnComplete = false;
        finalCheck = false;
        repositionCount = 0;;
    }

    @Override
    public State getNextState(JSONObject response) {

        if (finalCheck) {
            if (detector.foundGround(response)) {
                return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
            }
            return new ReLocateIsland(this.drone, this.radar, this.report);
        }

        if (turnComplete = true) {
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
        } else {
            // hardcoded for now at 10 repositions
            if (repositionCount > 10) {
                drone.stop();
                return this;
            }
            drone.moveForward();
            echo = true;
            echoRight = true;
            echoLeft = false;
            checkRight = false;
            checkLeft = false;
            turnComplete = false;
            finalCheck = false;
            repositionCount++;
            return this;
        }
        drone.stop();
        return this;
    }
}
