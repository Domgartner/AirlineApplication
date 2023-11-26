package Code;

public class Flight {
    private String flightName;
    private String flightNum;
    private Aircraft aircraft;
    private Date departureDate;


    Flight(String flightName, String flightNum, Aircraft aircraft, Date departureDate) {
        this.flightName = flightName;
        this.flightNum = flightNum;
        this.aircraft = aircraft;
        this.departureDate = departureDate;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}
