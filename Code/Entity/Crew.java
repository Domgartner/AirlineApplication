package Code.Entity;

public class Crew {
    private String firstName;
    private String lastName;
    private String flightNumber;

    public Crew(String firstName, String lastName, String flightNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.flightNumber = flightNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }       
}