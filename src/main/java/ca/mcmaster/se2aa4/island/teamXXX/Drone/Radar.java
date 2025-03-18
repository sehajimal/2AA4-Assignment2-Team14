package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;

public class Radar extends ExplorerSubject {

    private Navigator navigator = new Navigator();
    private Actions action = new Actions();
    private Directions heading;

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
        this.heading = newHeading;
    }

}
