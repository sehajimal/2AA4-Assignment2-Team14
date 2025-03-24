
package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DroneControllerTest {

    private DroneController controller;

    @BeforeEach
    public void setUp() {
        controller = new DroneController("E", 1000);
    }

    @Test
    public void testInitialDecisionNotNull() {
        JSONObject decision = controller.makeDecision();
        assertNotNull(decision);
        assertTrue(decision.has("action"));
    }

    @Test
    public void testStopWhenLowBattery() {
        controller = new DroneController("E", 0); // force low battery
        JSONObject decision = controller.makeDecision();
        assertEquals("stop", decision.getString("action"));
        assertTrue(controller.isExplorationComplete());
    }

    @Test
    public void testSetResultUpdatesDecision() {
        JSONObject result = new JSONObject();
        result.put("cost", 10);
        result.put("status", "OK");
        result.put("extras", new JSONObject());

        controller.setResult(result);
        JSONObject decision = controller.makeDecision();

        assertNotNull(decision);
    }

    /* @Test
    public void testReportReturnsJson() {
        String report = controller.getDiscoveries();
        assertNotNull(report);
        assertTrue(report.contains("{") || report.isEmpty()); // since we don't have full Report logic
    } */
}
