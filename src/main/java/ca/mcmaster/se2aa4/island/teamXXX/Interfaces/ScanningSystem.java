package ca.mcmaster.se2aa4.island.teamXXX.Interfaces;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

public interface ScanningSystem {

    void scan();

    void echoForward();

    void echoRight();
    
    void echoLeft();
    
    void setHeading(Directions newHeading);
    
    Directions getHeading();
    
}
