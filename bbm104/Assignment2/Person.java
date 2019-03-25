public class Person {

    public String id, name, gender;
    public String birthYearofPerson;
    public int weight, height;
    public int age;
    public int calNeed;
    public int calTaken, calBurned;
    public int netCals;

    public Person(String idofPerson, String nameofPerson, String genderofPerson, String weightofPerson, String heightofPErson, String birthYearofPerson){
        id = idofPerson;
        name = nameofPerson;
        gender = genderofPerson;
        weight = Integer.parseInt(weightofPerson);
        height = Integer.parseInt(heightofPErson);
        age = 2018 - Integer.parseInt(birthYearofPerson);
        calNeed = calNeeded();
        calTaken = 0;
        calBurned = 0;
        netCals = (calTaken -(calNeed + calBurned));
    }

    public String getId() {
        return id;
    }

    public void ateFood(Food food, String portion){
        if (food != null) {
            int cals = food.getCals();
            cals *= Integer.parseInt(portion);
            calTaken = calTaken + cals;
            netCals = netCals + cals;
            HelpingFuncs.outputList(id + "\thas\ttaken\t" + Integer.toString(cals) + "kcal\tfrom\t" + food.getName());
        }
    }
    public void doneSport(Sport sport, String minutes){
        if (sport != null) {
            int cals = (sport.getCals()*Integer.parseInt(minutes)) / 60;
            calBurned = calBurned + cals;
            netCals = netCals - cals;
            HelpingFuncs.outputList(id + "\thas\tburned\t" + Integer.toString(cals) + "kcal\tthanks\tto\t" + sport.getName());
        }
    }

    public int calNeeded() {
        float t;
        if (gender.equals("male")) {
            t = (float) (66 + (13.75 * weight) + (5 * height) - (6.8 * age));
        } else {
            t = (float) (665 + (9.6 * weight) + (1.7 * height) - (4.7 * age));
        }
        return Math.round(t);
    }

    public void printUser(){
        if (netCals > 0){
            HelpingFuncs.outputList(name+"\t"+Integer.toString(age)+"\t"+Integer.toString(calNeed)+"kcal\t"+Integer.toString(calTaken)+"kcal\t"+Integer.toString(calBurned)+"kcal\t+"+Integer.toString(netCals)+"kcal");
            return;
        }
        HelpingFuncs.outputList(name+"\t"+Integer.toString(age)+"\t"+Integer.toString(calNeed)+"kcal\t"+Integer.toString(calTaken)+"kcal\t"+Integer.toString(calBurned)+"kcal\t"+Integer.toString(netCals)+"kcal");
    }

}
