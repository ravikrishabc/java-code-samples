import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import org.jdom.*; 
import org.jdom.input.*; 

public class RentKeywordXML {

    public static void main(String[] args) throws Exception {

        String data = "User=winnie&Password=the-pooh&Keyword=honey&FirstName=Winnie&LastName=The Pooh&Street=Hollow tree, under the name of Mr. Sanders&City=Hundred Acre Woods&State=New York&Zip=12345&Country=US&CreditCardTypeID=Visa&Number=4111111111111111&expm=11&SecurityCode=123&ExpirationMonth=10&ExpirationYear=2017";

        URL url = new URL("https://app.eztexting.com/keywords?format=xml");
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
                System.out.println("Keyword ID: " + entry.getChildText("ID"));
                System.out.println("Keyword: " + entry.getChildText("Keyword"));
                System.out.println("Is double opt-in enabled: " + entry.getChildText("EnableDoubleOptIn"));
                System.out.println("Confirm message: " + entry.getChildText("ConfirmMessage"));
                System.out.println("Join message: " + entry.getChildText("JoinMessage"));
                System.out.println("Forward email: " + entry.getChildText("ForwardEmail"));
                System.out.println("Forward url: " + entry.getChildText("ForwardUrl"));
                System.out.println("Groups: " + implodeXML(entry.getChild("ContactGroupIDs"), ", "));
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
                    