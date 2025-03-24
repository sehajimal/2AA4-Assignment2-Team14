package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReturnToIsland extends State {

    private static final Logger logger = LogManager.getLogger(ReturnToIsland.class);

    private Detector detector;
    private boolean goForward;
    private boolean goLeft;
    private boolean goRight;
    private boolean turnComplete;
    private boolean finalCheck;
    
    public ReturnToIsland(Movable drone, ScanningSystem radar) {
        super(drone, radar);

        this.detector = new Detector();

        goForward = false;
        goLeft = false;
        goRight = false;
        turnComplete = false;
        finalCheck = false;

        logger.info("** In Return To Island State");
    }

    /*
     * after completing a grid search of island in one direction, will turn the drone to prepare it to
     *  complete grid search in opposite direction, visiting and scanning the missed rows / columns to complete thorough search
     */
    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) {
            if (detector.foundGround(response)) {
                return new GoToIsland(this.drone, this.radar, detector.getDistance(response));
            }
            // if not facing ground need to re-locate island
            return new ReLocateIsland(this.drone, this.radar);
        }

        /*
         * Turn Sequence:
         * turn back towards island
         * fly once to offset from previous grid search
         * turn again in opposite direction to slot into unvisited row / column
         */

        if (turnComplete) {
            radar.echoForward();
            finalCheck = true;
            return this;
        }

        if (goForward) {
            drone.moveForward();
            goForward = false;
            return this;
        }

        if (goRight) {
            drone.turnRight();
            turnComplete = true;
            return this;
        } else if (goLeft) {
            drone.turnLeft();
            turnComplete = true;
            return this;
        }

        // uses search direction and current heading to decide which directions the turn sequence should be in
        if (drone.getSearchDirection() == Directions.S || drone.getSearchDirection() == Directions.E) {
            if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) { //LFR
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) { //RFL
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        } else if (drone.getSearchDirection() == Directions.N || drone.getSearchDirection() == Directions.W) {
            if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) { //LFR
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) { //RFL
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        }
        return null;
    }
}
