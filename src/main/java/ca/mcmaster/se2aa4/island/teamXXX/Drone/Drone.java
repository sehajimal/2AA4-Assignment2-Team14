package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import java.util.EnumMap;
import java.util.Map;

public class Drone {

    private Directions heading;
    private Battery battery;
    private int x;
    private int y;
    private final Map<Directions, Directions> goingRight;
    private final Map<Directions, Directions> goingLeft;
    private final Map<Directions, int[]> moveForward;
    private final Map<Directions, Directions> invalidMoves;

    public Drone(Integer amount, String starting) {
        this.battery = new Battery(amount);
        try {
            this.heading = Directions.valueOf(starting);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        this.invalidMoves = new EnumMap<>(Directions.class);
        this.invalidMoves.put(Directions.N, Directions.S);
        this.invalidMoves.put(Directions.S, Directions.N);
        this.invalidMoves.put(Directions.E, Directions.W);
        this.invalidMoves.put(Directions.W, Directions.E);
    }

    public void updateCoordinates() {
        int[] move = moveForward.get(this.heading);
        this.x += move[0];
        this.y += move[1];
    }

    // moves the drone once forward in current heading, then once in new heading
    public void updateHeading(Directions direction) {
        if (invalidMoves.get(this.heading) == direction) {
            System.out.println("Invalid move: Cannot make a 180-degree turn.");
            return; // ignore invalid move
        }
        updateCoordinates();
        this.heading = direction;
        updateCoordinates();
    }
    
    public int getBatteryLevel() {
        return this.battery.getBatteryLevel();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Directions getHeading() {
        return this.heading;
    }
}
