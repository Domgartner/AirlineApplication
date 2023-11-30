package Code.Control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateUsersFile {

    public GenerateUsersFile(ArrayList<String> usersList) {
        generateUserTextFile(usersList);
    }

    // Function to generate a text file with user names and emails
    private static void generateUserTextFile(ArrayList<String> usersList) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users_info.txt"));
            // Iterate through the users list and write each user's information to the file
            for (String userInfo : usersList) {
                writer.write(userInfo);
                writer.newLine(); // Add a new line for each user
            }
            writer.close(); // Close the writer after writing is done
            System.out.println("User information written to users_info.txt");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions related to file writing (e.g., log or throw a runtime exception)
            throw new RuntimeException("Failed to generate user information file.");
        }
    }
}
