import java.io.*;
import java.net.*;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

public class CheckKeywordAvailabilityJSON {

    public static void main(String[] args) throws Exception {


        URL url = new URL("https://app.eztexting.com/keywords/new?format=json&Keyword=honey&User=winnie&Password=the-pooh");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

        boolean isSuccesResponse = responseCode < 400;

        InputStream responseStream = isSuccesResponse ? conn.getInputStream() : conn.getErrorStream();
        if (responseStream != null) {
            String responseString = IOUtils.toString(responseStream);
            responseStream.close();

            //Use json-lib (http://json-lib.sourceforge.net/) for response processing
            JSONObject response1 = (JSONObject) JSONSerializer.toJSON(responseString);
            JSONObject response = response1.getJSONObject("Response");
            System.out.println("Status: " + response.getString("Status"));
            System.out.println("Code: " + response.getString("Code"));
            if (isSuccesResponse) {
                JSONObject entry = response.getJSONObject("Entry");
                System.out.println("Keyword: " + entry.getString("Keyword"));
                System.out.println("Availability: " + entry.getString("Available"));
            } else {
                Object ErrorMessage[] = (Object[]) JSONArray.toArray(response.getJSONArray("Errors"));
                for (int i = 0; i < ErrorMessage.length; i++) {
                    System.out.println("Error: " + ErrorMessage[i]);
                }
            }
        }

    }
}

              