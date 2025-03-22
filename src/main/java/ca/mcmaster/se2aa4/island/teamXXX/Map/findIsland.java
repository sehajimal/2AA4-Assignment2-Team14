package ca.mcmaster.se2aa4.island.teamXXX.Map;

//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
//import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
//import ca.mcmaster.se2aa4.island.teamXXX.Map.Report;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

//import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import org.json.JSONObject;
//import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.json.JSONObject;


public class FindIsland extends State {
    private static final Logger logger = LogManager.getLogger(FindIsland.class);
    // retrieves response from an echo
    private final Detector detector = new Detector();
    private boolean turnRightNext;
    private boolean turnLeftNext; 
    private boolean echo;

    public FindIsland(Movable drone, ScanningSystem radar, Report report) {
        super(drone, radar, report);
        this.turnRightNext = true;
        this.turnLeftNext = false;
        this.echo = false;

        logger.info("** In Find Island State");
    }

    /*
     * used to initially locate island
     * as the island is near the middle of the map, moves drone diagonally while echoing until island is detected
     */
    @Override
    public State getNextState(JSONObject response) {
        if (detector.foundGround(response)) {
            logger.info("** Ground has been Found: Going to Island");
            return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
        }
        if (echo) {
            radar.echoForward();
            echo = false;
        } else if (turnRightNext) {
            drone.turnRight();
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
}
