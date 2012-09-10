package com.EzTexting;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * XML protocol support.
 */
public class XMLEncoding extends Encoding {
    @Override
    String getEncodingParam() {
        return "format=xml";
    }

    @Override
    String parseErrors(String encodedString) throws JDOMException, IOException {
        StringReader reader = new StringReader(encodedString);
        //Use JDOM (http://www.jdom.org) for xml response handling
        Element response = new SAXBuilder().build(reader).getRootElement();
        return implodeXML(response.getChild("Errors"), "; ");
    }

    private static String implodeXML(Element container, String delim) {
        if (container == null) return "";
        List objs = container.getChildren();
        StringBuilder buf = new StringBuilder();
        int size = objs.size();

        for (int i=0; i<size - 1; i++) {
            buf.append(((Element) (objs.get(i))).getText()).append(delim);
        }

        if (size != 0) {
            buf.append(((Element)(objs.get(size - 1))).getText());
        }

        return buf.toString();
    }
    @Override
    BaseObject parseObjectResponse(Class<BaseObject> aClass, String encodedString) throws JDOMException, IOException {
        StringReader reader = new StringReader(encodedString);
        //Use JDOM (http://www.jdom.org) for xml response handling
        Element response = new SAXBuilder().build(reader).getRootElement();
        Element entry = response.getChild("Entry");
        return parseEntry(aClass, entry);
    }

    private BaseObject parseEntry(Class<BaseObject> aClass, Element entry) {
        if (aClass.equals(Contact.class))
        {
            return new Contact(entry.getChildText("ID"),entry.getChildText("PhoneNumber"),entry.getChildText("FirstName"),
                    entry.getChildText("LastName"), entry.getChildText("Email"), entry.getChildText("Note"),entry.getChildText("Source"),
                    convertArrayToList(entry.getChild("Groups")),
                    entry.getChildText("CreatedAt"));
        }
        else if (aClass.equals(Group.class))
        {
            return new Group(entry.getChildText("ID"), entry.getChildText("Name"), entry.getChildText("Note"), Integer.parseInt(entry.getChildText("ContactCount")));
        }

        return null;
    }

    private List<String> convertArrayToList(Element container) {
        List<String> res = new ArrayList<String>();
        if (container != null)
        {
            for (Object obj : container.getChildren()) {
                res.add(((Element) obj).getText());
            }
        }
        return res;
    }

    @Override
    public List<BaseObject> parseObjectsResponse(Class<BaseObject> aClass, String encodedString) throws JDOMException, IOException {
        List<BaseObject> res = new ArrayList<BaseObject>();
        StringReader reader = new StringReader(encodedString);
        //Use JDOM (http://www.jdom.org) for xml response handling
        Element response = new SAXBuilder().build(reader).getRootElement();
        List entries = response.getChild("Entries").getChildren();
        for (Object entry : entries) {
            res.add(parseEntry(aClass, (Element)entry));
        }
        return res;
    }
}
