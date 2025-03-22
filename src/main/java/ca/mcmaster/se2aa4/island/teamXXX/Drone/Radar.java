package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;


// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

public class Radar extends ExplorerSubject implements ScanningSystem {

    private Navigator navigator = new Navigator();
    private Actions action = new Actions();
    private Directions heading;

    //private static final Logger logger = LogManager.getLogger(Radar.class);

    public Radar(Directions heading) {
        this.heading = heading;
    }

    @Override
    public void scan() {
        update(action.scan());
    }
    
    @Override
    public void echoForward() {
        update(action.echo(this.heading));
    }

    @Override
    public void echoRight() {
        update(action.echo(navigator.getRight(this.heading)));
    }

    @Override
    public void echoLeft() {
        update(action.echo(navigator.getLeft(this.heading)));
    }

    @Override
    public void setHeading(Directions newHeading) {
        this.heading = newHeading;
    }

    @Override
    public Directions getHeading() {
        return this.heading;
    }

}
