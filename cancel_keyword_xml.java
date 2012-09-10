import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import org.jdom.*; 
import org.jdom.input.*; 

public class CancelAKeywordXML {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh";

        URL url = new URL("https://app.eztexting.com/keywords/honey?format=xml&_method=DELETE");
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

            //Use JDOM (http://www.jdom.org) for xml response handling
            Element response = new SAXBuilder().build(responseStream).getRootElement(); 
            System.out.println("Status: " + response.getChildText("Status"));
            System.out.println("Code: " + response.getChildText("Code"));
            System.out.println("Errors: " + implodeXML(response.getChild("Errors"), "\n"));
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

