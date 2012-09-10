import java.io.*;
import java.net.*;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

public class BuyCreditsJSON_saved {

    public static void main(String[] args) throws Exception {

        String data = "User=demo&Password=password&NumberOfCredits=1000&StoredCreditCard=1111";

        URL url = new URL("https://app.eztexting.com/billing/credits?format=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

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
                System.out.println("Credits purchased: " + entry.getString("BoughtCredits"));
                System.out.println("Amount charged, $: " + entry.getString("Amount"));
                System.out.println("Discount, $: " + entry.getString("Discount"));
	        System.out.println("Plan credits: " + entry.getString("PlanCredits"));
	        System.out.println("Anytime credits: " + entry.getString("AnytimeCredits"));
        	System.out.println("Total: " + entry.getString("TotalCredits"));
            } else {
                Object ErrorMessage[] = (Object[]) JSONArray.toArray(response.getJSONArray("Errors"));
                for (int i = 0; i < ErrorMessage.length; i++) {
                    System.out.println("Error: " + ErrorMessage[i]);
                }
            }
        }

        wr.close();
    }
}

