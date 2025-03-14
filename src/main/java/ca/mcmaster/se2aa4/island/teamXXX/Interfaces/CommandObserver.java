package ca.mcmaster.se2aa4.island.teamXXX.Interfaces;

import org.json.JSONObject;

public interface CommandObserver {

    public void addCommand(JSONObject decision);

    public JSONObject getLatestCommand();
    
}
