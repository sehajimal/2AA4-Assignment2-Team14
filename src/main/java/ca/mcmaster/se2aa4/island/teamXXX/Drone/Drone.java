package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import java.util.EnumMap;
import java.util.Map;
import org.json.JSONObject;

public class Drone extends ExplorerSubject {

    private Directions heading;
    private Battery battery;
    private int x;
    private int y;
    private Actions actions = new Actions();
    private final Map<Directions, Directions> goingRight;
    private final Map<Directions, Directions> goingLeft;
    private final Map<Directions, int[]> moveForward;

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
    }

    private void updateCoordinates() {
        int[] move = moveForward.get(this.heading);
        this.x += move[0];
        this.y += move[1];
    }

    public void moveForward() {
        int[] move = moveForward.get(this.heading);
        this.x += move[0];
        this.y += move[1];

        update(actions.fly());
    }

    // moves the drone once forward in current heading, then once in new heading
    public void updateHeading(Directions direction) {
        updateCoordinates();
        this.heading = direction;
        updateCoordinates();

        update(actions.heading(direction));
    }

    public void stop() {
        update(actions.stop());
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
