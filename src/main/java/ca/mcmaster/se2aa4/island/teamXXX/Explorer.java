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
    private int i = 0;

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

        // initialize DroneController here (send in battery level and heading)
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();
        //decision.put("action", "stop"); // we stop the exploration immediately
        if (i % 2 == 0) {
            decision.put("action", "fly");
        }
        else {
            JSONObject parameter = new JSONObject();
            decision.put("action", "scan");
            parameter.put("direction", Directions.N);
            decision.put("parameters", parameter);
        }
        i++;
        // JSONObject parameter = new JSONObject();
        // JSONObject parameter = new JSONObject();

        // decision.put("action", "scan");

        // parameter.put("direction", Directions.N);
        // decision.put("parameters", parameter);
        
        logger.info("** Decision: {}",decision.toString());
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
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
