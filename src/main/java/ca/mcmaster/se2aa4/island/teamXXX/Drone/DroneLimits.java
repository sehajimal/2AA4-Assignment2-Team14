package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import java.util.EnumMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

/*
 * validates drone moves prior to making them
 */
public class DroneLimits {
    
    private final Drone drone;
    
    // can be added in the future to store dimensions of map, currently avoids going MIA without using these coords
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

    // currently unused as due to drone navigator system, a drone is never able to make a 180 degree turn directly
    public boolean is180turn(Directions currHeading, Directions newHeading) {
        return !(invalidMoves.get(currHeading) == newHeading);
    }

    // indicates if drone must return to base now
    public boolean insufficientBattery() {
        // comfortable threshold for going home
        if (this.drone.getBatteryLevel() < 50) {
            logger.info("** Battery Check: Insuffiient **\n");
            logger.info(this.drone.getBatteryLevel());
            return true;
        }
        return false;
    }
}
