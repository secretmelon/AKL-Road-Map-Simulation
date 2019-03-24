import com.sun.org.apache.xpath.internal.operations.Bool;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.lang.reflect.Array;


public class AucklandRoadMap extends GUI {



    //Collections after Loading
    public Map<Integer, Road> allRoads = new HashMap<Integer,Road>();
    public Map<Integer, Node> allNodes = new HashMap<Integer,Node>();
    public List<Segment> allSegments = new ArrayList<Segment>();
    public List<Node> nodeList = new ArrayList<Node>();

    //Collection from Search
    private List<Road> matchingRoads = new ArrayList<>();
    private List<Node> matchingNodes = new ArrayList<>();

    private Location origin = new Location(0,0);
    private double scale = 0;

    @Override
    protected void onSearch() {
        JTextField searchBox = getSearchBox();
        String searchInput = searchBox.getText();

        for(Road r : allRoads.values()){
            String roadName = r.getLabel();
            if(roadName.contentEquals(searchInput)){
                matchingRoads.add(r);
            }
        }
        for(Road r : matchingRoads){
            int id = r.getID();
            for(Node n : allNodes.values()){
                if(n.getID() == id){
                    matchingNodes.add(n);
                }
            }
        }
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) {
        loadNodeFile(nodes);
        loadRoadFile(roads);
        loadSegmentFile(segments);
        scalePoints();
    }

    @Override
    protected void redraw(Graphics g) {
        if(!allRoads.isEmpty()) {
            g.setColor(Color.RED);
            for (Node n : allNodes.values()) {
                drawNode(g, n);
            }

            g.setColor(Color.BLACK);
            for (Road r : allRoads.values()) {
                drawRoad(g, r);
            }
        }
//        if (!matchingRoads.isEmpty()) { //!matchingRoads.isEmpty()
//            g.setColor(Color.ORANGE);
//            for (Road r : matchingRoads) {
//                drawSegments1(g, s);
//            }
//        }

        if (!matchingNodes.isEmpty()) {
            g.setColor(Color.ORANGE);
            for (Node n : matchingNodes) {
                drawNode(g, n);
            }
        }


    }
    @Override
    protected void onMove(Move m) {
        if (m.equals(Move.ZOOM_IN)) {
            scale += (scale * 0.25);
        } else if (m.equals(Move.ZOOM_OUT)) {
            scale -= (scale * 0.25);
        } else if (m.equals(Move.NORTH)) {
            origin = origin.moveBy(500,500);//= new Location(origin.x, origin.y + 1);
        } else if (m.equals(Move.EAST)) {
            origin = new Location(origin.x + 1, origin.y);
        } else if (m.equals(Move.SOUTH)) {
            origin = new Location(origin.x, origin.y - 1);
        } else if (m.equals(Move.WEST)) {
            origin = new Location(origin.x - 1, origin.y);
        }
        redraw();

    }

    @Override
    protected void onClick(MouseEvent e) {
        Location mouse = new Location(e.getX(),e.getY());
        double closestDistance = Double.POSITIVE_INFINITY;
        Node closestNode;

        for(Node n : nodeList){ //can also write this as allNodes.values()
//            int nodeX = n.getLocation().asPoint(origin, scale).x; //redundant?
//            int nodeY = n.getLocation().asPoint(origin,scale).y;
            double mouseToNodeDistance = mouse.distance(n.getLocation());

            if(mouseToNodeDistance < closestDistance){
                closestNode = n;
                closestDistance = mouseToNodeDistance;
            }

        }
    }
    private void findIntersectingRoads(){}
    public void scalePoints(){
        double top = Double.NEGATIVE_INFINITY;
        double right = Double.NEGATIVE_INFINITY;
        double bottom = Double.POSITIVE_INFINITY;
        double left = Double.POSITIVE_INFINITY;

        for (Node n : nodeList) {
            Location nodeLoc = n.getLocation();
            if (nodeLoc.y > top) {
                top = nodeLoc.y;
            }
            if (nodeLoc.y < bottom) {
                bottom = nodeLoc.y;
            }
            if (nodeLoc.x > right) {
                right = nodeLoc.x;
            }
            if (nodeLoc.x < left) {
                left = nodeLoc.x;
            }
        }

        origin = new Location(left, top);
        scale = Math.min(getDrawingAreaDimension().getWidth() / (right - left), getDrawingAreaDimension().getHeight() / (top - bottom));
    }
    public void loadRoadFile(File roads){
        /* loadRoads method does the following:
         * - Takes each line in the .tab file and places the line into a String array 'currentLine'
         * - Each component of Road information is separated via tabs, so each piece of info is now a separate index.
         * - The value of each index is placed into a temporary variable by converting the String into the appropriate variable type (e.g. ID: String -> Integer)
         * - Now that each variable has been given a value, these variables are used to create a new Road object, stored in the allRoads hash-map.
         *
         * Sources:
         * BUFFERED READER TAB FILE: https://stackoverflow.com/questions/19575308/read-a-file-separated-by-tab-and-put-the-words-in-an-arraylist
         */

        try {
            BufferedReader buf = new BufferedReader(new FileReader(roads));
            String[] currentLine;
            String lineJustFetched;

            // initialise each component of road object
            int ID;
            int roadType;
            String label;
            String city;
            boolean oneWay;
            int speedLimit;
            int roadClass;
            boolean notForCar;
            boolean notForPed;
            boolean notForBicy;

            buf.readLine(); //skip first line

            for(lineJustFetched = buf.readLine(); lineJustFetched != null; lineJustFetched = buf.readLine()){
                currentLine = lineJustFetched.split("\t"); //put the read line into array

                //Taking Strings and converting to appropriate variable type
                ID = Integer.parseInt(currentLine[0]);                  //take 0th index of CurrentLine array and correspond to ID
                roadType = Integer.parseInt(currentLine[1]);
                label = currentLine[2];
                city = currentLine[3];
                oneWay = Boolean.parseBoolean(currentLine[4]);
                speedLimit = Integer.parseInt(currentLine[5]);
                roadClass = Integer.parseInt(currentLine[6]);
                notForCar = Boolean.parseBoolean(currentLine[7]);
                notForPed = Boolean.parseBoolean(currentLine[8]);
                notForBicy = Boolean.parseBoolean(currentLine[9]);

                allRoads.put(ID, new Road(ID, roadType, label, city, oneWay, speedLimit, roadClass, notForCar, notForPed, notForBicy));
            }
            buf.close();
        }


        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void loadNodeFile(File nodes){
        /* loadNodes method does the following:
         * - This method works the same way as loadRoads
         * - When a new Node object is placed in allNodes (hash-map), the location is also passed into the Node object for convenience in the drawNode() method.
         */
        try {
            BufferedReader buf = new BufferedReader(new FileReader(nodes));
            String[] currentLine;
            String lineJustFetched = null;

            // initialise each component of node object
            int ID;
            double latitude;
            double longitude;
            Location nodeLoc;

            for(lineJustFetched = buf.readLine(); lineJustFetched != null; lineJustFetched = buf.readLine()){
                currentLine = lineJustFetched.split("\t"); //put the read line into array

                //Taking Strings and converting to appropriate variable type
                ID          = Integer.parseInt(currentLine[0]);
                latitude    = Double.parseDouble(currentLine[1]);
                longitude   = Double.parseDouble(currentLine[2]);
                Node n = new Node(ID, latitude, longitude, Location.newFromLatLon(latitude, longitude));
                allNodes.put(ID, n);

                // added below
                nodeList.add(n); //added into a list as well for searching purposes
            }
            buf.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadSegmentFile(File segments){
        /* loadSegments method does the following:
         * - Takes each line in the .tab file and places the line into a String array 'currentLine'
         * - This then takes the first 4 string objects in the array and places them into a separate array 'segmentInfo'
         * - The rest of the segment components (co-ordinates) are placed into another array 'segmentCoords'
         * - segmentInfo is corresponded with the appropriate variables
         * - A list 'segmentLocations' is created to store pairs of co-ordinates,
         * - A for loop is then used to iterate through every pair of co-ordinates, and placing each one in the 'segmentLocations' list cont...
         *
         */
        try {
            BufferedReader buf = new BufferedReader(new FileReader(segments));
            String[] currentLine;
            List<String> segmentInfo = new ArrayList<>();
            List<String> segmentCoords = new ArrayList<>();
            String lineJustFetched;

            // initialise each component of segment object
            int roadID;
            double length;
            int nodeID1;
            int nodeID2;


            buf.readLine(); //skip the first line
            for(lineJustFetched = buf.readLine(); lineJustFetched != null; lineJustFetched = buf.readLine()){
                currentLine = lineJustFetched.split("\t"); //put the read line into array

                //Takes first 4 values of currentLine and places into segmentInfo
                for(int i = 0; i < 4; i++){
                    segmentInfo.add(currentLine[i]);
                }
                /*Takes rest of the currentLine and places into segmentCo-ords*/
                for(int i = 4; i != currentLine.length; i++){
                    segmentCoords.add(currentLine[i]);
                }

                //Taking Strings and converting to appropriate variable type
                roadID          = Integer.parseInt(segmentInfo.get(0));
                length          = Double.parseDouble(segmentInfo.get(1));
                nodeID1         = Integer.parseInt(segmentInfo.get(2));
                nodeID2         = Integer.parseInt(segmentInfo.get(3));


                Road roadOfSegments = allRoads.get(roadID);
                List<Location> segmentLocations = new ArrayList<Location>();


                double lat;
                double lon;

                Node n1 = allNodes.get(nodeID1);
                Node n2 = allNodes.get(nodeID2);

                for(int i = 0; i != segmentCoords.size(); i+=2){
                    lat = Double.parseDouble(segmentCoords.get(i));
                    lon = Double.parseDouble(segmentCoords.get(i+1));
                    segmentLocations.add(Location.newFromLatLon(lat, lon));
                }
                Segment s = new Segment(roadID, length, nodeID1, nodeID2, segmentLocations);
                n1.addNewSegment(s);
                n2.addNewSegment(s);
                allSegments.add(s);
                roadOfSegments.addSegments(s);
            }
            buf.close();

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void loadPolygonFile(File polygons){} //still need to implement
    private void drawRoad(Graphics g, Road road) {
        /* drawRoad method does the following:
         * - Iterates through every segment of a road object
         * - Iterates through the list of locations of that segment
         * - Extracts the x & y co-ordinates from the location object in the location list
         * - Draws a line using the two pairs of co-ordinates extracted from the two locations
         */


        for (Segment s : road.getSegments()){
            List<Location> loc = s.getLocations();
            for(int i = 0; i <s.getLocations().size()-1; i++){
                int x1 = loc.get(i).asPoint(origin, scale).x;
                int y1 = loc.get(i).asPoint(origin, scale).y;
                int x2 = loc.get(i+1).asPoint(origin, scale).x;
                int y2 = loc.get(i+1).asPoint(origin, scale).y;
                g.drawLine(x1,y1,x2,y2);
            }
        }

//        if (road == null) {
//            return;
//        }

//
//        for(Segment s: road.getSegments()){ //for each segment in the map
//            for(int i = 0; i < s.getLocations().size()-1; i++){ //get the size of the CoOrdinate array and iterate for that long
//                Point point1 = s.getLocations().get(i).asPoint(origin, scale); //make a point using the first location of the segment
//                Point point2 = s.getLocations().get(i+1).asPoint(origin, scale); //make a point using the second point of the segment
//
//                g.drawLine(point1.x, point1.y, point2.x, point2.y); //draw a line from first to second point
//            }
//        }

    }


    private void drawNode(Graphics g, Node node){
        /* drawNode method does the following:
         * - Extracts the co-ordinates from the node location object
         * - Draws an oval with the co-ordinates
         */
        int x = node.getLocation().asPoint(origin, scale).x;
        int y = node.getLocation().asPoint(origin, scale).y;
        g.fillOval(x - 1, y - 1, 4, 4);

    }

    public static void main(String args[]){
        new AucklandRoadMap();
    }
}
