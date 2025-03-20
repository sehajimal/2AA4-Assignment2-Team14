package ca.mcmaster.se2aa4.island.teamXXX.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Map.FindIsland;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone extends ExplorerSubject implements Movable {

    private static final Logger logger = LogManager.getLogger(Drone.class);

    private Navigator navigator = new Navigator();
    private Directions heading;
    private Battery battery;
    private int x;
    private int y;
    private int costPerMove;
    private Actions actions = new Actions();

    public Drone(Integer amount, String starting) {
        this.battery = new Battery(amount);
        this.costPerMove = costPerMove;
        x = 0;
        y = 0;
        try {
            this.heading = Directions.valueOf(starting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCoordinates() {
        int[] move = navigator.getForwardMovement(this.heading);
        this.x += move[0];
        this.y += move[1];
    }

    @Override
    public void moveForward() {
        int[] move = navigator.getForwardMovement(this.heading);
        this.x += move[0];
        this.y += move[1];

        update(actions.fly());
    }

    // moves the drone once forward in current heading, then once in new heading
    // public void updateHeading(Directions direction) {
    //     updateCoordinates();
    //     this.heading = direction;
    //     updateCoordinates();

    //     update(actions.heading(direction));
    // }

    @Override
    public void turnRight() {
        updateCoordinates();
        this.heading = navigator.getRight(this.heading);
        updateCoordinates();

        update(actions.heading(this.heading));
    }

    @Override
    public void turnLeft() {
        updateCoordinates();
        logger.info(getHeading());
        this.heading = navigator.getLeft(this.heading);
        updateCoordinates();
        logger.info(getHeading());

        update(actions.heading(this.heading));
    }

    @Override
    public void stop() {
        update(actions.stop());
    }

    public void returnHome() {
        System.out.println("Return home in progress... Battery levels low!");
        while (x > 0) {
            x--;
            battery.useBattery(costPerMove);
        }
        while (y > 0) {
            y--;
            battery.useBattery(costPerMove);
        }
        stop();
    }

    public boolean shouldGoHome() {
        if (this.battery.getBatteryLevel() < 50) {
            return true;
        }
        return false;
    }

    
    @Override
    public int getBatteryLevel() {
        return this.battery.getBatteryLevel();
    }

    @Override
    public void useBattery(int amount) {
        battery.useBattery(amount);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public Directions getHeading() {
        return this.heading;
    }
}
