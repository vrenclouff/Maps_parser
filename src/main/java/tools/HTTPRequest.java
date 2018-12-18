package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class HTTPRequest {

    public static Optional<String> GET(String target) {
        try {
            URL url = new URL(target);
            URLConnection connection = url.openConnection();
            InputStreamReader is = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(is);
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            return Optional.of(result.toString());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
