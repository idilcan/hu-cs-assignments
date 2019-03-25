import java.io.File;
import java.io.PrintWriter;
import java.util.*;
public class CommandsManager {


    public static void commands(String in1) {

        File peopleFile = new File("people.txt");
        if(!peopleFile.exists())
            peopleFile = new File("src/people.txt");
        File foodFile = new File("food.txt");
        if(!foodFile.exists())
            foodFile = new File("src/food.txt");
        File sportFile = new File("sport.txt");
        if(!sportFile.exists())
            sportFile = new File("src/sport.txt");
        File commandFile = new File(in1);




        Person[] personArray = PersonManager.readFile(peopleFile);
        Food[] foodArray = FoodManager.readFile(foodFile);
        Sport[] sportArray = SportManager.readFile(sportFile);

        try {
            Person[] activePeople = new Person[50];
            Scanner x;
            int i = 0;
            int activeCount = 0;
            x = new Scanner(commandFile);
            while (x.hasNext()) {
                String id = x.next();
                if (id.indexOf('\uFEFF') == 0) {
                    StringBuilder sb = new StringBuilder(id);
                    sb.deleteCharAt(0);
                    id = sb.toString();
                }
                HelpingFuncs.controlNull(id, i);
                i++;
                Person personInActive = PersonManager.findPerson(activePeople, id);
                if (personInActive == null && !id.contains("print")) {
                    Person tobeActive = PersonManager.findPerson(personArray,id);
                    activePeople[activeCount] = tobeActive;
                    activeCount++;
                }
                id = HelpingFuncs.controlFirstElementProblem(id);
                Person currentPerson = PersonManager.findPerson(personArray,id);
                boolean isIn = HelpingFuncs.printPersonProperties(id, personArray, activePeople);
                if (!isIn) {
                    String done = x.next();
                    String amount = x.next();
                    Sport sport = SportManager.findSport(sportArray, done);
                    Food food = FoodManager.findFood(foodArray, done);
                    if (food != null) {
                        currentPerson.ateFood(food, amount);

                    } else if (sport != null) {
                        currentPerson.doneSport(sport, amount);

                    }
                }
            }

            try {
                final Formatter output = new Formatter("monitoring.txt");
                PrintWriter writer = new PrintWriter("monitoring.txt", "UTF-8");
                String[] outputList = HelpingFuncs.getOutputArray();
                for(String out: outputList){
                    if(out != null) {
                        writer.println(out);
                    }else{
                        break;
                    }
                }
                writer.close();
            } catch (Exception e) {
                File output = new File("monitoring.txt");
                PrintWriter writer = new PrintWriter(output, "UTF-8");
                String[] outputList = HelpingFuncs.getOutputArray();
                for(String out: outputList){
                    if(out != null) {
                        writer.print(out);
                        writer.print("\n");
                    }else{
                        break;
                    }
                }
                writer.close();
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
