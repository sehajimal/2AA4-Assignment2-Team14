package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

public class TurnDrone extends State {

    boolean rightTurn;
    boolean leftTurn;

    public TurnDrone(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        rightTurn = false;
        leftTurn = false;
    }

    @Override
    public State getNextState(JSONObject response) {
        if (rightTurn) {
            drone.turnRight();
            return new Searcher(this.drone, this.radar, this.report);
        } else if (leftTurn) {
            drone.turnLeft();
            return new Searcher(this.drone, this.radar, this.report);
        } else if (drone.getHeading() == Directions.N || drone.getHeading() == Directions.E) {
            rightTurn = true;
            drone.turnRight();
            return this;
        } else if (drone.getHeading() == Directions.S || drone.getHeading() == Directions.W) {
            leftTurn = true;
            drone.turnLeft();
            return this;
        } else {
            throw new IllegalStateException("Invalid state reached");
        }
    }
    
}
