package Code.Entity;

public class Flight {
    private String flightNum;
    private int aircraftID;
    private Aircraft aircraft;
    private Date departureDate;
    private String destination;
    private String startPoint;
    private String departureTime;

    public Flight(String flightNum, Date departureDate, int aircraftID, String dest, String start, String depTime) {
        this.flightNum = flightNum;
        this.aircraftID = aircraftID;
        this.departureDate = departureDate;
        this.destination = dest;
        this.startPoint = start;
        this.departureTime = depTime;
    }

    public Flight(String flightNum, Date departureDate, Aircraft aircraft, String dest, String start, String depTime) {
        this.flightNum = flightNum;
        this.aircraft = aircraft;
        this.departureDate = departureDate;
        this.destination = dest;
        this.startPoint = start;
        this.departureTime = depTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public String getdepTime() {
        return departureTime;
    }

    public int getAircraftID() {
        return aircraftID;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartPoint() {
        return startPoint;
    }
}
