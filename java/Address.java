package java;

public class Address {
    private String street;
    private String postalCode;
    private int houseNum;
    private String city;
    private String country;

    public Address(String s, String pc, int hn, String c, String ct) {
        this.street = s;
        this.postalCode = pc;
        this.houseNum = hn;
        this.city = c;
        this.country = ct;
    }

    public String getAddress() {
        // Format the address string
        String formattedAddress = String.format("%d %s, %s, %s, %s %s",
                houseNum, street, city, country, postalCode);

        return formattedAddress;
    }
    
}
