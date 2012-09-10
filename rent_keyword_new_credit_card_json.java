import java.io.*;
import java.net.*;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

public class RentKeywordJSON {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh&Keyword=honey&FirstName=Winnie&LastName=The Pooh&Street=Hollow tree, under the name of Mr. Sanders&City=Hundred Acre Woods&State=New York&Zip=12345&Country=US&CreditCardTypeID=Visa&Number=4111111111111111&expm=11&SecurityCode=123&ExpirationMonth=10&ExpirationYear=2017";

        URL url = new URL("https://app.eztexting.com/keywords?format=json");
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
                System.out.println("Keyword ID: " + entry.getString("ID"));
                System.out.println("Keyword: " + entry.getString("Keyword"));
                System.out.println("Is double opt-in enabled: " + entry.getString("EnableDoubleOptIn"));
                System.out.println("Confirm message: " + entry.getString("ConfirmMessage"));
                System.out.println("Join message: " + entry.getString("JoinMessage"));
                System.out.println("Forward email: " + entry.getString("ForwardEmail"));
                System.out.println("Forward url: " + entry.getString("ForwardUrl"));
                System.out.println("Groups: " + entry.optString("ContactGroupIDs", ""));
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

