import java.io.File;
import java.util.*;

public class PersonManager {

    public static Person findPerson(Person[] arr, String id) {
        for (Person person : arr) {
            if (person == null)
                return null;
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public static Person[] readFile(File file) {
        Scanner x;
        try {
            x = new Scanner(file);
            int i = 0;
            Person[] people = new Person[50];
            while (x.hasNext()) {
                String id = x.next();
                if (id.indexOf('\uFEFF') == 0) {
                    StringBuilder sb = new StringBuilder(id);
                    sb.deleteCharAt(0);
                    id = sb.toString();
                }
                String name = x.next();
                String gender = x.next();
                String weight = x.next();
                String height = x.next();
                String year = x.next();
                Person now = new Person(id,name,gender,weight,height,year);

                people[i] = now;
                i++;
            }
            return people;
        } catch (Exception e) {
            return null;
        }
    }
}
