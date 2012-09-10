import java.io.*;
import java.net.*;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

public class CancelAKeywordJSON {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh";

        URL url = new URL("https://app.eztexting.com/keywords/honey?format=json&_method=DELETE");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

	int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);

        boolean isSuccesResponse = responseCode < 400;

        if (!isSuccesResponse) {
            InputStream responseStream = conn.getErrorStream();
            if (responseStream != null) {
                String responseString = IOUtils.toString(responseStream);
                responseStream.close();

                //Use json-lib (http://json-lib.sourceforge.net/) for response processing
                JSONObject response1 = (JSONObject) JSONSerializer.toJSON(responseString);
                JSONObject response = response1.getJSONObject("Response");
                System.out.println("Status: " + response.getString("Status"));
                System.out.println("Code: " + response.getString("Code"));
                Object ErrorMessage[] = (Object[]) JSONArray.toArray(response.getJSONArray("Errors"));
                for (int i = 0; i < ErrorMessage.length; i++) {
                    System.out.println("Error: " + ErrorMessage[i]);
                }
            }
        }
 
        wr.close();
    }
}
