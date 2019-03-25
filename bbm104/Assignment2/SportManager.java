import java.io.File;
import java.util.Scanner;

public class SportManager {
    public static Sport[] readFile(File file) {
        Scanner m;
        try {
            m = new Scanner(file);
            int i = 0;
            Sport[] sports = new Sport[100];
            while (m.hasNext()) {
                String id = m.next();
                if (id.indexOf('\uFEFF') == 0) {
                    StringBuilder sb = new StringBuilder(id);
                    sb.deleteCharAt(0);
                    id = sb.toString();
                }
                String name = m.next();
                String cals = m.next();
                Sport now = new Sport(id, name, cals);
                sports[i] = now;

                i++;
            }
            return sports;
        } catch (Exception e) {
            System.out.printf("COULD NOT OPEN THE FILE %s BECAUSE OF %s\n", file.getName(), e);
            return null;
        }
    }
    public static Sport findSport(Sport[] arr, String id) {
        for (Sport sport : arr) {
            if (sport == null)
                return null;
            if (sport.getId().equals(id)) {
                return sport;
            }
        }
        return null;
    }
}
