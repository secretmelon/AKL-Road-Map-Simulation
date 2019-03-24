import java.util.ArrayList;
import java.util.List;

public class Node {

    private int ID;
    private double latitude;
    private double longitude;
    private Location nodeLoc;
    private List<Segment> segmentList = new ArrayList<>();


    public Node(int ID, double latitude, double longitude, Location nodeLoc){
        this.ID = ID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nodeLoc = nodeLoc;

    }

    public int getID() {
        return ID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Location getLocation(){
        return nodeLoc;
    }

    public List<Segment> getSegments(){ return segmentList;}

    public void addNewSegment(Segment s){
        this.segmentList.add(s);
    }



}