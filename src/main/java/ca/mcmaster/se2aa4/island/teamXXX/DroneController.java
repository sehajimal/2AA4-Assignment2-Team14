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

        this.drone = new Drone(batteryLevel, heading);
        this.radar = new Radar(this.drone.getHeading());
        this.limitations = new DroneLimits(drone);
        this.report = Report.getInstance();
        //start state
        this.currentState = new FindIsland(this.drone, this.radar, this.report);

        List<ExplorerSubject> subjects = Arrays.asList(this.drone, this.radar);
        this.commandTracker = new CommandTracker(subjects);
        //logger.info("\n COMPLETED INITIALIZATION OF CONTROLLED \n");
    }

    public JSONObject makeDecision() {
        //logger.info("\n ENTERING makeDecision() \n");

        // updating battery
        updatebattery();
        //! check limitations first
        if (this.drone.shouldGoHome()) {
            //? print battery level?
            JSONObject decision = new JSONObject();
            decision.put("action", "stop");  
            return decision;  
        }

        State nextState;
        State currState;

        do {
            //logger.info("\n IN DO WHILE LOOP: prevState={}, newState={} \n", prevState, newState);
            
            currState = this.currentState; // Store previous state
            nextState = this.currentState.getNextState(this.latestResult);
        
            //logger.info("\n GOT NEXT STATE: prevState={}, newState={} \n", currState, nextState);
        
            this.currentState = nextState; // Update state
        
            //logger.info("\n UPDATED STATE: prevState={}, newState={} \n", currState, nextState);
        
        } while (!(currState == nextState));
        
        //logger.info("\n TEST2 \n");
        System.out.println("\n BATTERY LEVEL " + this.drone.getBatteryLevel());
        return commandTracker.getLatestCommand();
    }

    public void setResult(JSONObject result) {
        this.latestResult = result;
    }

    //! make private
    public JSONObject stopExploration() {
        //this.drone.stop();
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");  
        return decision; 
    }

    private void updatebattery() {
        if (latestResult != null) {
            int amount = latestResult.getInt("cost");
            this.drone.useBattery(amount);
        }
    }
}