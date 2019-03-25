import java.io.File;
import java.util.Scanner;

public class FoodManager {
    public static Food[] readFile(File file) {
        Scanner m;
        try {
            m = new Scanner(file);
            int i = 0;
            Food[] foods = new Food[100];
            while (m.hasNext()) {
                String ff = m.next();
                if (ff.indexOf('\uFEFF') == 0) {
                    StringBuilder sb = new StringBuilder(ff);
                    sb.deleteCharAt(0);
                    ff = sb.toString();
                }
                Food now = new Food(ff, m.next(), m.next());
                foods[i] = now;

                i++;
            }
            return foods;
        } catch (Exception e) {
            return null;
        }
    }
    public static Food findFood(Food[] arr, String id) {
        for (Food food : arr) {
            if (food == null)
                return null;
            if (food.getId().equals(id)) {
                return food;
            }
        }
        return null;
    }
}
