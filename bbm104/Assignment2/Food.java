public class Food {

    public String id, name;
    public int cals;

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getCals(){
        return cals;
    }

    public Food(String idofFood, String nameofFood, String calsodFood) {
        id = idofFood;
        name = nameofFood;
        cals = Integer.parseInt(calsodFood);
    }

}
