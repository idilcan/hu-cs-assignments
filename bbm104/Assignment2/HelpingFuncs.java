public class HelpingFuncs {

    public static int count = 0;
    public static String[] outputArray = new String[100];

    public static void controlNull(String first, int i) {
        if (first == null) {
            return;
        } else if (i != 0) {
            outputList("***************");
        }
    }

    public static String controlFirstElementProblem(String first) {
        if (first.indexOf('\uFEFF') == 0) {
            StringBuilder sb = new StringBuilder(first);
            sb.deleteCharAt(0);
            first = sb.toString();
        }
        return first;
    }

    public static boolean printPersonProperties(String first, Person[] personList, Person[] activePeople) {
        try {
            if (first.contains("print")) {
                if (first.contains("(")) {
                    String[] parts = first.split("\\(");
                    String[] par = parts[1].split("\\)");
                    Person nowPerson = PersonManager.findPerson(personList, par[0]);
                    nowPerson.printUser();
                } else if (first.contains("printList")) {
                    for (Person person : activePeople) {
                        person.printUser();
                    }
                }
                return true;
            }
            return false;
        }catch(NullPointerException e) {
            return true;
        }
    }

    public static void outputList(String outputLine){
        outputArray[count] = outputLine;
        count++;
    }

    public static String[] getOutputArray(){
        return outputArray;
    }

}
