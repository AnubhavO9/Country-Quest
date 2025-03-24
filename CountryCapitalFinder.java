import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CountryCapitalFinder {
    public static void main(String[] args) {
        // Creating main window
        JFrame frame = new JFrame("Country Capital Finder");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        // Label
        JLabel label = new JLabel("Enter Country:");
        label.setBounds(20, 20, 100, 30);
        frame.add(label);
        
        // Text field 
        JTextField textField = new JTextField();
        textField.setBounds(130, 20, 200, 30);
        frame.add(textField);
        
        // Button 
        JButton button = new JButton("Find Capital");
        button.setBounds(130, 60, 150, 30);
        frame.add(button);
        
        //Result display
        JLabel resultLabel = new JLabel("Capital: ");
        resultLabel.setBounds(20, 100, 350, 30);
        frame.add(resultLabel);
        
        // Action listener 
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String country = textField.getText().trim();
                if (!country.isEmpty()) {
                    String capital = getCapitalOfCountry(country);
                    resultLabel.setText("Capital: " + (capital != null ? capital : "Not Found"));
                }
            }
        });
        
        frame.setVisible(true);
    }

    // API
    private static String getCapitalOfCountry(String country) {
        try {
            String urlString = "https://restcountries.com/v3.1/name/" + country + "?fullText=true";
            URL url = new URL(urlString);  //string to url conversion 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
    
            // If not successful
            if (conn.getResponseCode() != 200) {
                return "Invalid country or API issue";
            }
    
            // Read the API response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            conn.disconnect();
    
            // Parse JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject countryData = jsonArray.getJSONObject(0);
    
            // Check if "capital" field exists before accessing it
            if (countryData.has("capital")) {
                return countryData.getJSONArray("capital").getString(0);
            } else {
                return "Capital not found";
            }
    
        } catch (Exception e) {
            return "Error fetching data";
        }
    }
}