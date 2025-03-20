package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TurnDrone extends State {

    private static final Logger logger = LogManager.getLogger(FindIsland.class);

    boolean rightTurn;
    boolean leftTurn;
    boolean turnComplete;

    public TurnDrone(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        rightTurn = false;
        leftTurn = false;
        turnComplete = false;

        logger.info("\n TURN DRONE \n");
    }

    @Override
    public State getNextState(JSONObject response) {
        logger.info(drone.getHeading());
        if (turnComplete) {
            return new Searcher(this.drone, this.radar, this.report);
        } else if (rightTurn) {
            drone.turnRight();
            logger.info("\n SECOND RIGHT \n");
            logger.info(drone.getHeading());
            turnComplete = true;
            return this;
        } else if (leftTurn) {
            drone.turnLeft();
            logger.info("\n SECOND LEFT \n");
            logger.info(drone.getHeading());
            turnComplete = true;
            return this;
        } else if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) {
            rightTurn = true;
            drone.turnRight();
            logger.info("\n FIRST RIGHT \n");
            logger.info(drone.getHeading());
            return this;
        } else if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) {
            leftTurn = true;
            drone.turnLeft();
            logger.info("\n FIRST LEFT \n");
            logger.info(drone.getHeading());
            return this;
        } else {
            throw new IllegalStateException("Invalid state reached");
        }
    }
    
}
