import com.EzTexting.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstration of InboxMessage usage.
 */
public class InboxMessageExamples {
    public static void main(String[] args) throws Exception {

        try {
            System.out.println("InboxMessage JSON example");
            EzTextingConnection sms = new EzTextingConnection("centerft", "texting121212", Encoding.JSON);

            List<BaseObject> messages = sms.getInboxMessages(null, null, "Message", "asc", "10", "1");
            System.out.println("Get inboxMessages: " + messages );

            String messageId = messages.get(0).id;

            System.out.println("Move message to folder");
            sms.moveMessageToFolder(messageId, "141");

            InboxMessage inboxMessage = sms.getInboxMessage(messageId);
            System.out.println("InboxMessage get:" + inboxMessage);


            System.out.println("Delete message");
            sms.delete(inboxMessage);

            //try to get exception - delete already deleted item
            System.out.println("Delete message2");
            sms.delete(inboxMessage);
        } catch (EzTextingException e) {
            System.out.println("EzTexting error code:" + e.getResponseCode() + "; messages:" + e.getMessage());
        }

        try {
            System.out.println("InboxMessage XML example");
            EzTextingConnection sms = new EzTextingConnection("centerft", "texting121212", Encoding.XML);

            List<BaseObject> messages = sms.getInboxMessages(null, null, "Message", "asc", "10", "1");
            System.out.println("Get inboxMessages: " + messages );

            String messageId = messages.get(0).id;
            String messageId2 = messages.get(1).id;

            List<String> messageIds = new ArrayList<String>();
            messageIds.add(messageId);
            messageIds.add(messageId2);


            System.out.println("Move messages to folder");
            sms.moveMessagesToFolder(messageIds, "141");

            InboxMessage inboxMessage = sms.getInboxMessage(messageId2);
            System.out.println("InboxMessage get:" + inboxMessage);


            System.out.println("Delete message");
            sms.delete(inboxMessage);

            //try to get exception - delete already deleted item
            System.out.println("Delete message2");
            sms.delete(inboxMessage);
        } catch (EzTextingException e) {
            System.out.println("EzTexting error code:" + e.getResponseCode() + "; messages:" + e.getMessage());
        }


    }

}
