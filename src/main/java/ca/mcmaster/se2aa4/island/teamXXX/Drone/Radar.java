package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//! create interface for radar (with scan, echo, and setHeading)
public class Radar extends ExplorerSubject {

    private Navigator navigator = new Navigator();
    private Actions action = new Actions();
    private Directions heading;

    private static final Logger logger = LogManager.getLogger(Radar.class);

    public Radar(Directions heading) {
        this.heading = heading;
    }

    public void scan() {
        update(action.scan());
    }
    
    public void echoForward() {
        update(action.echo(this.heading));
    }

    public void echoRight() {
        update(action.echo(navigator.getRight(this.heading)));
    }

    public void echoLeft() {
        update(action.echo(navigator.getLeft(this.heading)));
    }

    public void setHeading(Directions newHeading) {
        //logger.info("radar check 1");
        this.heading = newHeading;
        //logger.info("radar check 2");
    }

}
