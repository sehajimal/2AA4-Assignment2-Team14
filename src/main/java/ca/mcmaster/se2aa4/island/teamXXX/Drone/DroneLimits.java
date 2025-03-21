package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import java.util.EnumMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import eu.ace_design.island.game.actions.Actions;

/*
 * will need to implement validation of drone moves here, and call actions after validation:
 * staying within radio bounds
 * avoiding invalid moves
 * returning home when needed to avoid going MIA
 */
public class DroneLimits {
    
    private final Drone drone;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private final Logger logger = LogManager.getLogger();
    private final Map<Directions, Directions> invalidMoves;

    public DroneLimits(Drone drone) {
        this.drone = drone;

        this.invalidMoves = new EnumMap<>(Directions.class);
        this.invalidMoves.put(Directions.N, Directions.S);
        this.invalidMoves.put(Directions.S, Directions.N);
        this.invalidMoves.put(Directions.E, Directions.W);
        this.invalidMoves.put(Directions.W, Directions.E);
    }

    //  public void commandValidator(Actions action, Directions newDirection, JSONObject parameter) {
    //     if (newDirection == this.drone.getHeading()) {
    //         logger.info("Currently going in right direction, continuing exploration");
    //     } else {
    //         if (invalidMoves.get(this.drone.getHeading()) == newDirection) {
    //             logger.error("Invalid move: Cannot make a 180-degree turn.");
    //             return;
    //         } else {
    //             //action.heading(parameter, newDirection, drone);
    //         }
    //     }
    // }

    public boolean is180turn(Directions currHeading, Directions newHeading) {
        return !(invalidMoves.get(currHeading) == newHeading);
    }

    // indicates if we must return to base now
    public boolean insufficientBattery() {
        //logger.info("IN BATTERY CHECK");
        if (this.drone.getBatteryLevel() < ((double) this.drone.getInitialBatteryLevel() * 0.007)) {
            logger.info("BATTERY CHECK: INSUFFICIENT \n");
            logger.info(this.drone.getBatteryLevel());
            return true;
        }
        //logger.info("BATTERY CHECK: SUFFICIENT");
        return false;
    }

    //? for going home, can check if battery is below 50% of original level

}
