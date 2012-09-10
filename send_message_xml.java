import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import org.jdom.*; 
import org.jdom.input.*; 

public class SendingSMSMessagesXML {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh&PhoneNumbers[]=2123456785&PhoneNumbers[]=2123456786&Subject=From Winnie&Message=I am a Bear of Very Little Brain, and long words bother me&StampToSend=1305582245&MessageTypeID=1";

        URL url = new URL("https://app.eztexting.com/sending/messages?format=xml");
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

        //Use JDOM (http://www.jdom.org) for xml response handling
	Element response = new SAXBuilder().build(responseStream).getRootElement(); 
	System.out.println("Status: " + response.getChildText("Status"));
        System.out.println("Code: " + response.getChildText("Code"));
        if (isSuccesResponse) {
            Element entry = response.getChild("Entry");
            System.out.println("Message ID: " + entry.getChildText("ID"));
            System.out.println("Subject: " + entry.getChildText("Subject"));
            System.out.println("Message: " + entry.getChildText("Message"));
            System.out.println("Message Type ID: " + entry.getChildText("MessageTypeID"));
            System.out.println("Total Recipients: " + entry.getChildText("RecipientsCount"));
            System.out.println("Credits Charged: " + entry.getChildText("Credits"));
            System.out.println("Time To Send: " + entry.getChildText("StampToSend"));
            System.out.println("Phone Numbers: " + implodeXML(entry.getChild("PhoneNumbers"), ", "));
            System.out.println("Locally Opted Out Numbers: " + implodeXML(entry.getChild("LocalOptOuts"), ", "));
            System.out.println("Globally Opted Out Numbers: " + implodeXML(entry.getChild("GlobalOptOuts"), ", "));
        } else {
            System.out.println("Errors: " + implodeXML(response.getChild("Errors"), "\n"));
        }

        responseStream.close();
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

              