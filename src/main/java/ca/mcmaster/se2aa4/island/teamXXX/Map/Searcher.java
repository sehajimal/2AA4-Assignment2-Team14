package ca.mcmaster.se2aa4.island.teamXXX.Map;

import java.security.PKCS12Attribute;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.Drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.Drone.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Interfaces.Movable;

public class Searcher extends State {

    private static final Logger logger = LogManager.getLogger(Searcher.class);
    private final Detector detector;

    // boooleans indicating whether to fly or scan on this iteration
    private boolean scan;
    private boolean fly;
    private boolean leavingSearch;

    //  values used to check if the row or column checked has been visited, avoids looping on same path
    private Integer searchLength;
    private Integer alreadyVisited;

    /*
     * bandaid fix for handling case where if at shore, drone gets stuck in turns
     * (switches between TurnDrone and Search), long term an additional state or 
     * more secure logic can be added
     */
    private boolean foundLand;
    
    public Searcher(Movable drone, Radar radar, Report report) {
        super(drone, radar, report);

        this.detector = new Detector();

        scan = true;
        fly = false;
        foundLand = false;
        leavingSearch = false;

        searchLength = 0;
        alreadyVisited = 0;
    }

    @Override
    public State getNextState(JSONObject response) {

        //logger.info("\n IN SEARCHER \n");

        if (leavingSearch) {
            if (detector.foundGround(response)) {
                // currently above ocean but land ahead, fly to this land
                return new GoToIsland(this.drone, this.radar, this.report, detector.getDistance(response));
            } else {
                // checking threshold for if this row / column has already been searched
                if (alreadyVisited > (int) (0.6 * searchLength)) {
                    logger.info("ALREADY SEARCHED SECTION");
                    // starting new search patter (looking for opposite axis)
                    return new ReLocateIsland(this.drone, this.radar, this.report);
                }
                // turn the drone and continus current grid search
                return new TurnDrone(this.drone, this.radar, this.report);
            }
        }

        if (fly) {
            // ADD LOGIC TO CHECK FOR CREEKS OR SITES
            logger.info("\n IN FLY \n");

            if (foundCreek(response)) {
                String[] creeks = getCreeks(response);
                for (String creek : creeks) {
                    addCreekToReport(creek);
                }
                //return new State(this.drone, this.radar, this.report);
            }
            if (foundSite(response)) {
                String[] sites = getSites(response);
                for (String site : sites) {
                    addSiteToReport(site);
                }
                //return new State(this.drone, this.radar, this.report);
            }
            if (inOcean(response) && foundLand) {
                leavingSearch = true;
                radar.echoForward();
                return this;
                // logger.info("\n TRANSITION \n");
                // drone.addTurnPoint();
                // logger.info("ALREADY VISITED CHECK");
                // logger.info(alreadyVisited);
                // logger.info(searchLength);
                // if (alreadyVisited > (int) (0.6 * searchLength)) {
                //     logger.info("ALREADY SEARCHED SECTION");
                //     return new ReLocateIsland(this.drone, this.radar, this.report);
                // }
                // return new TurnDrone(this.drone, this.radar, this.report);
            } else if (!inOcean(response) && !foundLand) {
                logger.info("\n DONT TRANSITION YET \n");
                foundLand = true;
            }
            drone.moveForward();
            fly = false;
            scan = true;

        } else if (scan) {
            searchLength++;

            // check if current location is known to be a turning point
            if (drone.isTurnPoint()) {
                //return new TurnDrone(this.drone, this.radar, this.report);
                return new ReLocateIsland(this.drone, this.radar, this.report);
            }
            logger.info("\n IN SCAN \n");
            if (drone.hasVisitedLocation()) {
                alreadyVisited++;
                drone.moveForward();
                foundLand = true;
                logger.info("\n HAS VISITED \n");
                //alreadyVisited++;
            } else {
                radar.scan();
                fly = true;
                scan = false;
                logger.info("\n HAS NOT VISITED \n");
            }
            // radar.scan();
            // fly = true;
            // scan = false;
        }

        return this;
    }

    // private boolean containsOcean(JSONObject response) {
    //     if (!response.has("extras")) return false;

    //     JSONObject extras = response.getJSONObject("extras");
    //     if (!extras.has("biomes")) return false;

    //     JSONArray biomes = extras.getJSONArray("biomes");
    //     for (int i = 0; i < biomes.length(); i++) {
    //         if (biomes.getString(i).equals("OCEAN")) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    private boolean inOcean(JSONObject response) {

        logger.info("\n IN CHECK OCEAN \n");
        if (!response.has("extras")) return false;

        JSONObject extras = response.optJSONObject("extras"); // Use optJSONObject to avoid exceptions
        if (extras == null || !extras.has("biomes")) return false;

        //logger.info("\n BIOMES EXIST \n");
        JSONArray biomes = extras.optJSONArray("biomes"); // Use optJSONArray to avoid exceptions
        //boolean result = biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
        //logger.info(result);
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

    //! need improvement for check (handling arrays of length > 1)
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
        return new String[0]; // Return an empty array if no creeks are found
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
        return new String[0]; // Return an empty array if no sites are found
    }
    

    public void addCreekToReport(String creekId) {
        int x = this.drone.getX();
        int y = this.drone.getY();
        this.report.addCreek(creekId, x, y);
    }

    // Add a site to the report from the drone's coordinates
    public void addSiteToReport(String siteId) {
        int x = this.drone.getX();
        int y = this.drone.getY();
        this.report.addSite(siteId, x, y);
    }  

}
