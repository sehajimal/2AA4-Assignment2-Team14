package ca.mcmaster.se2aa4.island.teamXXX.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.ScanningSystem;

public class Searcher extends State {

    private static final Logger logger = LogManager.getLogger(Searcher.class);
    private final Detector detector;

    // boooleans indicating whether to fly or scan on this iteration
    private boolean scan;
    private boolean fly;
    private boolean leavingSearch;

    // values used to check if the row or column checked has been visited, avoids looping on same path
    private Integer searchLength;
    private Integer alreadyVisited;
    
    public Searcher(Movable drone, ScanningSystem radar, Report report) {
        super(drone, radar, report);

        this.detector = new Detector();

        scan = true;
        fly = false;
        //foundLand = false;
        leavingSearch = false;

        searchLength = 0;
        alreadyVisited = 0;

        logger.info("** In Search State");
    }

    //! refactor to remove foundLand boolean
    /*
     * iterates between flying forward and scanning trying to locate POIs
     * takes a U turn or changes search pattern upon coming to end of island or
     * if scanning an already scanned row or column
     */
    @Override
    public State getNextState(JSONObject response) {
        if (leavingSearch) {
            if (detector.foundGround(response)) {
                // currently above ocean but land ahead, fly to this land
                return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
            } else {
                // checking threshold for if this row / column has already been searched
                if (alreadyVisited > (int) (0.6 * searchLength)) {
                    logger.info("** Current Section has Already Been Searched");
                    // starting new search pattern (looking for opposite axis)
                    return new ReLocateIsland(this.drone, this.radar, this.report);
                }
                // turn the drone and continue current grid search
                return new TurnDrone(this.drone, this.radar, this.report);
            }
        }

        if (fly) {
            // checks if creek is found from previous scan
            if (foundCreek(response)) {
                logger.info("** Creek Found!");
                String[] creeks = getCreeks(response);
                // adding creeks to report
                for (String creek : creeks) {
                    addCreekToReport(creek);
                }
            }
            // checks if emergency site is found from previous scan
            if (foundSite(response)) {
                logger.info("** Emergency Site Found!");
                String[] sites = getSites(response);
                // adding site to report
                for (String site : sites) {
                    addSiteToReport(site);
                }
            }

            // checking if in ocean (condition for leacing search state)
            if (inOcean(response)) {
                leavingSearch = true;
                // check if there is land further ahead
                radar.echoForward();
                return this;
            }

            // still on land, continue forward
            drone.moveForward();
            fly = false;
            scan = true;

        } else if (scan) {
            searchLength++;

            // check if current location is known to be a turning point (saves energy from scan)
            if (drone.isTurnPoint()) {
                // change search strategies as this means that this column or row has already been visited
                return new ReLocateIsland(this.drone, this.radar, this.report);
            }
            // avoids re-scanning already scanned location (saves energy from scan)
            if (drone.hasVisitedLocation()) {
                alreadyVisited++;
                drone.moveForward();
                //foundLand = true;
                logger.info("** Location has Already Been Scanned");
            } else { // unvisited location
                radar.scan();
                fly = true;
                scan = false;
            }
        }

        return this;
    }

    private boolean inOcean(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.optJSONObject("extras");
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes");
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }

    private boolean foundCreek(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("creeks")) return false;

        JSONArray creeks = extras.getJSONArray("creeks");
        if (creeks.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean foundSite(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("sites")) return false;

        JSONArray sites = extras.getJSONArray("sites");
        if (sites.length() > 0) {
            return true;
        }
        return false;
    }

    private String[] getCreeks(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("creeks")) {
            JSONArray creeksArray = extras.optJSONArray("creeks");
            String[] creeks = new String[creeksArray.length()];
            for (int i = 0; i < creeksArray.length(); i++) {
                creeks[i] = creeksArray.optString(i);
            }
            return creeks;
        }
        return new String[0]; // return an empty array if no creeks are found
    }
    
    private String[] getSites(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("sites")) {
            JSONArray sitesArray = extras.optJSONArray("sites");
            String[] sites = new String[sitesArray.length()];
            for (int i = 0; i < sitesArray.length(); i++) {
                sites[i] = sitesArray.optString(i);
            }
            return sites;
        }
        return new String[0]; // return an empty array if no sites are found
    }
    
    public void addCreekToReport(String creekId) {
        int x = this.drone.getX();
        int y = this.drone.getY();
        this.report.addCreek(creekId, x, y);
    }

    public void addSiteToReport(String siteId) {
        int x = this.drone.getX();
        int y = this.drone.getY();
        this.report.addSite(siteId, x, y);
    }  
}
