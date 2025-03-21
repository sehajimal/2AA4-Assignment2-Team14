package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
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
    
    public ReturnToIsland(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        this.detector = new Detector();

        goForward = false;
        goLeft= false;
        goRight = false;
        turnComplete = false;
        finalCheck = false;
    }

    @Override
    public State getNextState(JSONObject response) {

        logger.info("\n IN RETURN TO ISLAND \n");

        if (finalCheck) {
            if (detector.foundGround(response)) {
                return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
            }
            //return new FindIsland(this.drone, this.radar, this.report);
            return new ReLocateIsland(this.drone, this.radar, this.report);
            //drone.stop();
            //return this;
        }

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

        // if (this.drone.getHeading() == Directions.S || this.drone.getHeading() == Directions.W) { //RFL
        //     drone.turnRight();
        //     goForward = true;
        //     goLeft = true;
        //     return this;
        // } else if (this.drone.getHeading() == Directions.N || this.drone.getHeading() == Directions.E) { // LFR
        //     drone.turnLeft();
        //     goForward = true;
        //     goRight = true;
        //     return this;
        // }

        //--------------------------

        if (drone.getSearchDirection() == Directions.S || drone.getSearchDirection() == Directions.E) {
            logger.info("\n CHECKING DOWN OR RIGHT \n");
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
