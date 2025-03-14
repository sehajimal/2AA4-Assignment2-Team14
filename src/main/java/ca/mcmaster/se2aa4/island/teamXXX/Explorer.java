package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.Directions;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private DroneController droneController;
    int i = 0;
    int j = 0;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));

        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");

        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        System.out.println(info);

        droneController = new DroneController(direction, batteryLevel);

        // initialize DroneController here (send in battery level and heading)
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject(); // ✅ Ensure it's initialized
        //int j = 0;

        if (i < 50) {
            if (j == 0) {
                droneController.scan();
                j++;
            } else if (j == 1) {
                droneController.goRight();
                logger.info("\n going left \n");
                j++;
            } else if (j == 2) {
                droneController.goLeft();
                logger.info("\n going right \n");
                j = 0;
            }
            decision = droneController.getDecision();
        } else {
            decision.put("action", "stop"); // ✅ Correctly modifies the existing variable
        }

        i++;
        //decision = droneController.getDecision();

        logger.info("** Decision: {}",decision.toString());

        //logger.info("\n take decision \n");

        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
        //logger.info("\n acknowledge results \n");
        droneController.setResult(response);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
