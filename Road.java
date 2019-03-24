import java.util.ArrayList;
import java.util.List;

public class Road {
    private int ID;
    private int roadType;
    private String label;
    private String city;
    private boolean oneWay;
    private int speedLimit;
    private int roadClass;
    private boolean notForCar;
    private boolean notForPed;
    private boolean notForBicy;
    private List<Segment> roadSegments = new ArrayList<Segment>();


    public Road(int ID, int roadType, String label, String city, boolean oneWay, int speedLimit, int roadClass, boolean notForCar, boolean notForPed, boolean notForBicy){
      this.ID = ID;
      this.roadType = roadType;
      this.label = label;
      this.city = city;
      this.oneWay = oneWay;
      this.speedLimit = speedLimit;
      this.roadClass = roadClass;
      this.notForCar = notForCar;
      this.notForPed = notForPed;
      this.notForBicy = notForBicy;
      //this.roadSegments = roadSegments;
    }

    public int getID() {
        return ID;
    }
    public int getRoadType() {
        return roadType;
    }
    public String getLabel() {
        return label;
    }
    public String getCity() {
        return city;
    }
    public boolean isOneWay() {
        return oneWay;
    }
    public int getSpeedLimit() {
        return speedLimit;
    }
    public int getRoadClass() {
        return roadClass;
    }
    public boolean isNotForCar() {
        return notForCar;
    }
    public boolean isNotForPed() {
        return notForPed;
    }
    public boolean isNotForBicy() {
        return notForBicy;
    }
    public void addSegments(Segment s){
        roadSegments.add(s);
    }
    public List<Segment> getSegments() {
        return roadSegments;
    }
}
