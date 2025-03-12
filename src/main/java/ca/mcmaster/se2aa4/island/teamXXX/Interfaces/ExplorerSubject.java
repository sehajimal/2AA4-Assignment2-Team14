package ca.mcmaster.se2aa4.island.teamXXX.Interfaces;

import org.json.JSONObject;

/*
 * drone and radar will implement this
 * upon any decision
 */
public abstract class ExplorerSubject {
    
    private CommandObserver observer;

    public void addObserver(CommandObserver observer) {
        this.observer = observer;
    }

    protected void update(JSONObject decision) {
        if (observer != null) {
            observer.addCommand(decision);
        }
    }

}
