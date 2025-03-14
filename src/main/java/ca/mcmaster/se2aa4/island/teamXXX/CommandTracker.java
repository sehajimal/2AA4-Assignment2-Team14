package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;
import java.util.Deque;
import java.util.LinkedList;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.CommandObserver;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ExplorerSubject;
import java.util.List;

public class CommandTracker implements CommandObserver {

    private Deque<JSONObject> commands = new LinkedList<>();

    public CommandTracker(List<ExplorerSubject> subjects) {
        for (ExplorerSubject subject : subjects) {
            subject.addObserver(this); // Register as an observer
        }
    }

    // observes the drone and the radar, upon calling whatever decision is made will be added to the queue
    @Override
    public void addCommand(JSONObject decision) {
        commands.push(decision);
    }

    // returns latest command
    @Override
    public JSONObject getLatestCommand() {
        return commands.peek();
    }
}
