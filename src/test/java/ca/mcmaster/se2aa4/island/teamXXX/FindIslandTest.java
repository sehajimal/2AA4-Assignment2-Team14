package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FindIslandTest {

    private FindIsland findIsland;
    private MockDrone drone;
    private MockRadar radar;
    private Report report;

    @BeforeEach
    public void setUp() {
        drone = new MockDrone();
        radar = new MockRadar();
        report = Report.getInstance();
        findIsland = new FindIsland(drone, radar, report);
    }

    @Test
    public void testEchoTriggersNextState() {
        JSONObject oceanEcho = new JSONObject();
        oceanEcho.put("found", "OUT_OF_RANGE");

        State newState = findIsland.getNextState(oceanEcho);
        assertNotNull(newState);
    }

    @Test
    public void testIslandFoundChangesState() {
        JSONObject echo = new JSONObject();
        echo.put("found", "GROUND");
        echo.put("range", 3);

        State next = findIsland.getNextState(echo);
        assertNotEquals(findIsland, next);
        assertTrue(next instanceof GoToIsland);
    }
}