package java;

public class Name {
    private String fName;
    private String lName;

    public Name(String f, String l) {
        fName = f;
        lName = l;
    }

    public String getName() {
        String formattedName = String.format("%s, %s,", fName, lName);
        return formattedName;
    }
}