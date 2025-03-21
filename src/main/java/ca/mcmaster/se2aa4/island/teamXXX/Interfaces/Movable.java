package ca.mcmaster.se2aa4.island.teamXXX.Interfaces;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

public interface Movable {

    public void moveForward();

    public void turnRight();

    public void turnLeft();

    public void stop();
    
    public int getBatteryLevel();

    public void useBattery(int amount);

    public int getX();

    public int getY();

    public Directions getHeading();

    public int getInitialBatteryLevel();

    public boolean hasVisitedLocation();
    
}
