package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
//import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

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
    private Drone drone;
    private Radar radar;
    private Report report;
    private DroneLimits limitations;
    private State currentState;
    // result from most recent decision
    private JSONObject latestResult;
    private boolean isExplorationComplete;

    public DroneController(String heading, int batteryLevel) {
        Directions eHeading = Directions.E;
        try {
            eHeading = Directions.valueOf(heading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.radar = new Radar(eHeading);
        this.drone = new Drone(batteryLevel, eHeading, this.radar);
        this.limitations = new DroneLimits(this.drone);
        this.report = Report.getInstance();
        // start state
        this.currentState = new FindIsland(this.drone, this.radar, this.report);

        // using observer to track all commands / decisions
        List<ExplorerSubject> subjects = Arrays.asList(this.drone, this.radar);
        this.commandTracker = new CommandTracker(subjects);

        isExplorationComplete = false;

        logger.info("** Controller has been Initialized");
    }

    public JSONObject makeDecision() {
        // updating battery
        updatebattery();
        logger.info("** Battery Level: ", this.drone.getBatteryLevel());
 
        // checking if there is sufficient battery to continue exploration
        if (limitations.insufficientBattery()) {
            setExplorationIsComplete(true);
            return stopExploration();
        }

        State nextState;
        State currState;

        do {
            currState = this.currentState; // store previous state
            nextState = this.currentState.getNextState(this.latestResult);
            this.currentState = nextState; // update state
        } while (!(currState == nextState)); // state will either make decision or return next state,
                                             // ensure decision is made before proceeding
        
        JSONObject decision = commandTracker.getLatestCommand();
        // checks if decision is to stop
        setExplorationIsComplete(decision);

        return decision;
    }

    public void setResult(JSONObject result) {
        this.latestResult = result;
    }

    private JSONObject stopExploration() {
        this.drone.stop();
        return commandTracker.getLatestCommand(); 
    }

    public String getDiscoveries() {
        return this.report.presentDiscoveries();
    }

    private void updatebattery() {
        if (latestResult != null) {
            int amount = latestResult.getInt("cost");
            this.drone.useBattery(amount);
        }
    }

    private void setExplorationIsComplete(JSONObject decision) {
        if (decision.has("action") && "stop".equals(decision.getString("action"))) {
            // The decision was to stop
            logger.info("** Stopping Exlporation");
            this.isExplorationComplete = true;
        }
    }

    private void setExplorationIsComplete(boolean decisionIsToStop) {
        this.isExplorationComplete = decisionIsToStop;
    }

    public boolean isExplorationComplete() {
        return this.isExplorationComplete;
    }
}