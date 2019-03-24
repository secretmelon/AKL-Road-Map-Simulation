import java.util.ArrayList;
import java.util.List;

public class Segment {

    private int roadID;
    private double length;
    private int nodeID1;
    private int nodeID2;
    private List<Location> locations = new ArrayList<Location>();

    public Segment(int roadID, double length, int nodeID1, int nodeID2, List<Location> locations){
        this.roadID = roadID;
        this.length = length;
        this.nodeID1 = nodeID1;
        this.nodeID2 = nodeID2;
        this.locations = locations;
    }

    public int getRoadID() {
        return roadID;
    }

    public double getLength() {
        return length;
    }

    public int getNodeID1() {
        return nodeID1;
    }


    public int getNodeID2() {
        return nodeID2;
    }


    public List<Location> getLocations() {
        return locations;
    }

}
