import com.EzTexting.Contact;
import com.EzTexting.Encoding;
import com.EzTexting.EzTextingConnection;
import com.EzTexting.EzTextingException;

/**
 * Demonstration of Contacts usage.
 */
public class ContactExamples {

    public static void main(String[] args) throws Exception {

        try {
            System.out.println("Contact JSON example");
            EzTextingConnection ez = new EzTextingConnection("ezdemo", "password", Encoding.JSON);

            System.out.println("Get contacts from group Honey Lovers: " + ez.getContacts(null, null, null, "Honey Lovers", null, null, null, null));

            Contact contact = new Contact("2123456896", "Piglet", "P.", "piglet@small-animals-alliance.org", "It is hard to be brave, when you are only a Very Small Animal.", null);
            contact = (Contact) ez.create(contact);
            System.out.println("Contact create:" + contact);

            contact = ez.getContact(contact.id);
            System.out.println("Contact get:" + contact);

            contact.groups.add("Friends");
            contact.groups.add("Neighbors");
            contact = (Contact) ez.update(contact);

            System.out.println("Contact update:" + contact);

            ez.delete(contact);

            //try to get exception - delete already deleted item
            ez.delete(contact);
        } catch (EzTextingException e) {
            System.out.println("EzTexting error code:" + e.getResponseCode() + "; messages:" + e.getMessage());
        }


    }
}

