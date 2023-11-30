package Code.Entity;
import java.util.ArrayList;

public class Aircraft {
    private int aircraftID;
    private String aircraftType;
    private int numSeats;
    private static ArrayList<Crew> crewMembers;

    public Aircraft(int id, String type, int num) {
        aircraftID = id;
        aircraftType = type;
        numSeats = num;
    }

    public Aircraft(String type, int num) {
        aircraftType = type;
        numSeats = num;
    }

    public static void setCrew(ArrayList<Crew> c) {
        crewMembers = c;
    }
    public static ArrayList<Crew> getCrew() {
        return crewMembers;
    }

    public int getAircraftID() {
        return aircraftID;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public int getNumSeats() {
        return numSeats;
    }
}
