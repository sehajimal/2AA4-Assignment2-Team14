package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TurnDrone extends State {

    private static final Logger logger = LogManager.getLogger(FindIsland.class);
    private final Detector detector = new Detector();

    boolean rightTurn;
    boolean leftTurn;
    boolean turnComplete;
    boolean checkGround;

    public TurnDrone(Movable drone, ScanningSystem radar) {
        super(drone, radar);

        rightTurn = false;
        leftTurn = false;
        turnComplete = false;
        checkGround = false;

        logger.info("** In U-Turn State");
    }

    @Override
    public State getNextState(JSONObject response) {
        if (checkGround) {
            if (detector.foundGround(response)) { // if ground is there go to it
                return new GoToIsland(this.drone, this.radar, detector.getDistance(response));
            } else { // otherwise search for ground again
                return new ReturnToIsland(this.drone, this.radar);
            }
        } else if (turnComplete) {
            // once turn is complete check if ground is in current heading
            logger.info("** Turn Complete");
            checkGround = true;
            radar.echoForward();
            return this;
        } else if (rightTurn) { // second right turn
            drone.turnRight();
            turnComplete = true;
            return this;
        } else if (leftTurn) { // second left turn
            drone.turnLeft();
            turnComplete = true;
            return this;
        /*
         * sequence below determines direction for U-Turn
         * based on current grid search direction (left --> right, right --> left, up --> down, or down --> up)
         * will decide correct direction for U-Turn and complete first turn (avoids going MIA)
         */
        } else if (drone.getSearchDirection() == Directions.S || drone.getSearchDirection() == Directions.E) {
            if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) {
                rightTurn = true;
                drone.turnRight();
                return this;
            } else if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) {
                leftTurn = true;
                drone.turnLeft();
                return this;
            }
        } else if (drone.getSearchDirection() == Directions.N || drone.getSearchDirection() == Directions.W) {
            if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) {
                rightTurn = true;
                drone.turnRight();
                return this;
            } else if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) {
                leftTurn = true;
                drone.turnLeft();
                return this;
            }
        }
        else {
            throw new IllegalStateException("Invalid state reached");
        }
        return null;
    }
    
}
