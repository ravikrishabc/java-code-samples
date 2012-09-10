import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import org.jdom.*; 
import org.jdom.input.*; 

public class BuyCreditsXML_saved {

    public static void main(String[] args) throws Exception {

        String data = "User=demo&Password=password&NumberOfCredits=1000&StoredCreditCard=1111";

        URL url = new URL("https://app.eztexting.com/billing/credits?format=xml");
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
            //Use JDOM (http://www.jdom.org) for xml response handling
            Element response = new SAXBuilder().build(responseStream).getRootElement(); 
            System.out.println("Status: " + response.getChildText("Status"));
            System.out.println("Code: " + response.getChildText("Code"));
            if (isSuccesResponse) {
            	Element entry = response.getChild("Entry");
                    System.out.println("Credits purchased: " + entry.getChildText("BoughtCredits"));
                    System.out.println("Amount charged, $: " + entry.getChildText("Amount"));
                    System.out.println("Discount, $: " + entry.getChildText("Discount"));
    	        System.out.println("Plan credits: " + entry.getChildText("PlanCredits"));
    	        System.out.println("Anytime credits: " + entry.getChildText("AnytimeCredits"));
            	System.out.println("Total: " + entry.getChildText("TotalCredits"));
            } else {
                System.out.println("Errors: " + implodeXML(response.getChild("Errors"), "\n"));
            }
            responseStream.close();
        }

  
        wr.close();
    }

    public static String implodeXML(Element container, String delim) {
	if (container == null) return "";
	List objs = container.getChildren();
        StringBuffer buf = new StringBuffer();
        int size = objs.size();

        for (int i=0; i<size - 1; i++) {
            buf.append(((Element)(objs.get(i))).getText() + delim);
        }

        if (size != 0) {
            buf.append(((Element)(objs.get(size - 1))).getText());
        }

        return buf.toString();
    }
}

                    