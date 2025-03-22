package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.DroneLimits;
import ca.mcmaster.se2aa4.island.teamXXX.Map.State;
import ca.mcmaster.se2aa4.island.teamXXX.Map.FindIsland;
import ca.mcmaster.se2aa4.island.teamXXX.Map.Report;
import org.json.JSONObject;


public class DroneController {

    private final Logger logger = LogManager.getLogger();

    private CommandTracker commandTracker;
    //private Movable drone;
    private Drone drone;
    private Radar radar;
    private Report report;
    //! can refactor to not hold the drone
    private DroneLimits limitations;
    private State currentState;
    private JSONObject latestResult;

    //! update constructor to initialize state to be findIsland
    public DroneController(String heading, int batteryLevel) {
        Directions eHeading = Directions.E;
        try {
            eHeading = Directions.valueOf(heading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //this.drone = new Drone(batteryLevel, heading);
        this.radar = new Radar(eHeading);
        this.drone = new Drone(batteryLevel, eHeading, this.radar);
        this.limitations = new DroneLimits(this.drone);
        this.report = Report.getInstance();
        //start state
        this.currentState = new FindIsland(this.drone, this.radar, this.report);

        List<ExplorerSubject> subjects = Arrays.asList(this.drone, this.radar);
        this.commandTracker = new CommandTracker(subjects);
        //logger.info("\n COMPLETED INITIALIZATION OF CONTROLLED \n");
    }

    public JSONObject makeDecision() {
        //logger.info("\n ENTERING makeDecision() \n");
        // logger.info("\n DRONE AND RADAR HEADINGS");
        // logger.info("\n");
        // logger.info(drone.getHeading());
        // logger.info("\n");
        // logger.info(radar.getHeading());
        // logger.info("\n");

        // updating battery
        updatebattery();
        logger.info("Battery Level: ", this.drone.getBatteryLevel());
        
        // checking if exploration should stop
        //logger.info("START BATTERY CHECK");
        if (limitations.insufficientBattery()) {
            //logger.info("FAILED BATTERY CHECK");
            return stopExploration();
        }

        //logger.info("PASSED BATTERY CHECK");
        State nextState;
        State currState;

        do {
            //logger.info("\n IN DO WHILE LOOP: prevState={}, newState={} \n", prevState, newState);
            
            currState = this.currentState; // Store previous state
            //logger.info("\n TEST1 \n");
            nextState = this.currentState.getNextState(this.latestResult);
            //logger.info("\n TEST2 \n");
        
            //logger.info("\n GOT NEXT STATE: prevState={}, newState={} \n", currState, nextState);
        
            this.currentState = nextState; // Update state
        
            //logger.info("\n UPDATED STATE: prevState={}, newState={} \n", currState, nextState);
        
        } while (!(currState == nextState));
        
        //logger.info("\n TEST \n");
        return commandTracker.getLatestCommand();
    }

    public void setResult(JSONObject result) {
        this.latestResult = result;
    }

    //! make private
    public JSONObject stopExploration() {
        this.drone.stop();
        return commandTracker.getLatestCommand(); 
    }

    public String getDiscoveries() {
        //return this.report.getDiscoveries().toString();
        return this.report.presentDiscoveries();
    }

    private void updatebattery() {
        if (latestResult != null) {
            int amount = latestResult.getInt("cost");
            this.drone.useBattery(amount);
        }
    }
}