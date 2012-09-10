import java.io.*;
import java.net.*;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

public class SendingSMSMessagesJSON {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh&PhoneNumbers[]=2123456785&PhoneNumbers[]=2123456786&Subject=From Winnie&Message=I am a Bear of Very Little Brain, and long words bother me&StampToSend=1305582245&MessageTypeID=1";

        URL url = new URL("https://app.eztexting.com/sending/messages?format=json");
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
                System.out.println("Message ID: " + entry.getString("ID"));
                System.out.println("Subject: " + entry.getString("Subject"));
                System.out.println("Message: " + entry.getString("Message"));
                System.out.println("Message Type ID: " + entry.getString("MessageTypeID"));
                System.out.println("Total Recipients: " + entry.getString("RecipientsCount"));
                System.out.println("Credits Charged: " + entry.getString("Credits"));
                System.out.println("Time To Send: " + entry.getString("StampToSend"));
                System.out.println("Phone Numbers: " + entry.optString("PhoneNumbers", ""));
                System.out.println("Locally Opted Out Numbers: " + entry.optString("LocalOptOuts", ""));
                System.out.println("Globally Opted Out Numbers: " + entry.optString("GlobalOptOuts", ""));
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
              