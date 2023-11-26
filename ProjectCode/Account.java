package ProjectCode;
public class Account {
    private String email;
    private Name name;
    private Address address;
    // private boolean recieveNews;
    private String password; 
    
    public Account(String email, Address address, boolean receiveNews, Name name, String password) {
        this.email = email;
        this.address = address;
        // this.recieveNews = receiveNews;
        this.name = name;
        this.password = password;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for address
    public void setAddress(Address address) {
        this.address = address;
    }

    // Getter for address
    public Address getAddress() {
        return address;
    }

    // Getter for recieve news
    // public void setRecieveNews(boolean b) {
    //     recieveNews = b;
    // }

    // // Setter for address
    // public boolean isReceiveNews() {
    //     return recieveNews;
    // }

    // Setter for name
    public void setName(Name name) {
        this.name = name;
    }

    // Getter for name
    public Name getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}