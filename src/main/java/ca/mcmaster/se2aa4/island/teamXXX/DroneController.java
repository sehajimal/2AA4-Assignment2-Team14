package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

import java.util.Arrays;
import java.util.List;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.DroneLimits;

public class DroneController {

    private CommandTracker commandTracker;
    //private Movable drone;
    private Drone drone;
    private Radar radar;
    //! can refactor to not hold the drone
    private DroneLimits limitations;

    public DroneController(String heading, int batteryLevel) {

        this.drone = new Drone(batteryLevel, heading);
        this.radar = new Radar(this.drone.getHeading());
        this.limitations = new DroneLimits(drone);

        List<ExplorerSubject> subjects = Arrays.asList(drone, radar);
        this.commandTracker = new CommandTracker(subjects);
    }

}