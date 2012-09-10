import com.EzTexting.Group;
import com.EzTexting.Encoding;
import com.EzTexting.EzTextingConnection;
import com.EzTexting.EzTextingException;

/**
 * Demonstration of Group usage.
 */
public class GroupExamples {
    public static void main(String[] args) throws Exception {


        try {
            System.out.println("Group XML example");
            EzTextingConnection ez = new EzTextingConnection("ezdemo", "password", Encoding.XML);

            System.out.println("Get groups from group Honey Lovers: " + ez.getGroups("Name", "asc", "10", "1"));

            Group group = new Group("Tubby Bears", "A bear, however hard he tries, grows tubby without exercise");
            group = (Group) ez.create(group);
            System.out.println("Group create:" + group);

            group = ez.getGroup(group.id);
            System.out.println("Group get:" + group);

            group = (Group) ez.update(group);
            System.out.println("Group update:" + group);

            ez.delete(group);

            //try to get exception - delete already deleted item
            ez.delete(group);
        } catch (EzTextingException e) {
            System.out.println("EzTexting error code:" + e.getResponseCode() + "; messages:" + e.getMessage());
        }


    }

}


