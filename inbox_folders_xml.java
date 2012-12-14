import com.EzTexting.Encoding;
import com.EzTexting.Folder;
import com.EzTexting.EzTextingConnection;
import com.EzTexting.EzTextingException;

/**
 * Demonstration of Folder usage.
 */
public class InboxFolderExamples {
    public static void main(String[] args) throws Exception {

        try {
            System.out.println("Folder XML example");
            EzTextingConnection sms = new EzTextingConnection("centerft", "texting121212", Encoding.XML);

            System.out.println("Get folders : " + sms.getFolders());

            Folder folder = new Folder(null, "Customers");
            folder = (Folder) sms.create(folder);
            System.out.println("Folder create:" + folder);

            String folderId = folder.id;
            folder = sms.getFolder(folderId);
            System.out.println("Folder get:" + folder);

            folder.id = folderId;
            folder.name = "test";
            sms.update(folder);
            System.out.println("Folder update");

            System.out.println("Folder delete");
            sms.delete(folder);

            //try to get exception - delete already deleted item
            System.out.println("Folder delete2");
            sms.delete(folder);
        } catch (EzTextingException e) {
            System.out.println("EzTexting error code:" + e.getResponseCode() + "; messages:" + e.getMessage());
        }


    }

}
