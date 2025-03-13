package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import java.util.EnumMap;
import java.util.Map;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

public class Navigator {

    private final Map<Directions, Directions> goingRight;
    private final Map<Directions, Directions> goingLeft;
    private final Map<Directions, int[]> moveForward;

    public Navigator() {
        this.goingRight = new EnumMap<>(Directions.class);
        this.goingRight.put(Directions.N, Directions.E);
        this.goingRight.put(Directions.E, Directions.S);
        this.goingRight.put(Directions.S, Directions.W);
        this.goingRight.put(Directions.W, Directions.N);

        this.goingLeft = new EnumMap<>(Directions.class);
        this.goingLeft.put(Directions.N, Directions.W);
        this.goingLeft.put(Directions.W, Directions.S);
        this.goingLeft.put(Directions.S, Directions.E);
        this.goingLeft.put(Directions.E, Directions.N);

        this.moveForward = new EnumMap<>(Directions.class);
        this.moveForward.put(Directions.N, new int[]{0, 1});
        this.moveForward.put(Directions.S, new int[]{0, -1});
        this.moveForward.put(Directions.E, new int[]{1, 0});
        this.moveForward.put(Directions.W, new int[]{-1, 0});
    }

    public Directions getRight(Directions currHeading) {
        return goingRight.get(currHeading);
    }

    public Directions getLeft(Directions currHeading) {
        return goingLeft.get(currHeading);
    }

    public int[] getForwardMovement(Directions currHeading) {
        return moveForward.get(currHeading);
    }
    
}
