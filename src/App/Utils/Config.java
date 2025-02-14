package App.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javafx.scene.control.ComboBox;


public class Config {

    public static String apacheLogPath;
    public static String modsecLogPath;
    public static ArrayList<Profile> profiles;

    public static void createNewProfile(JsonObject profile) {
        apacheLogPath = profile.get("apacheLogPath").getAsString();
        modsecLogPath = profile.get("modsecLogPath").getAsString();
        
        String filePath = System.getProperty("user.dir") + "\\profile.txt";
        try (FileWriter writer = new FileWriter(filePath, true)){
            writer.write(profile.toString() + "\n");
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        
    }

    public static void loadProfile(ComboBox<String> profileCb) {
        String filePath = System.getProperty("user.dir") + "\\profile.txt";
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                Profile temp = gson.fromJson(line, Profile.class);
                profiles.add(temp);
                profileCb.getItems().add(temp.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkProfile(String pass, String name) {
        for(Profile p : profiles) {
            if (p.getName().equals(name)) {
                if (p.getPassword().equals(pass)) {
                    apacheLogPath = p.getApacheLogPath();
                    modsecLogPath = p.getModsecLogPath();
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    class Profile {
        public String profileName;
        public String password;
        public String apacheLogPath;
        public String modsecLogPath;

        public Profile() {

        }

        public String getName() {
            return profileName;
        }

        public String getPassword() {
            return password;
        }

        public String getApacheLogPath() {
            return apacheLogPath;
        }

        public String getModsecLogPath() {
            return modsecLogPath;
        }
    }
}
