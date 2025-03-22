package ca.mcmaster.se2aa4.island.teamXXX.Interfaces;

import org.json.JSONObject;

/*
 * observes drone and radar to track any decisions
 */
public interface CommandObserver {

    public void addCommand(JSONObject decision);

    public JSONObject getLatestCommand();
    
}
