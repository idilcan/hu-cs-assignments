public class Sport {
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
    public Sport(String idofSport, String nameofSport, String calsodSport){
        id = idofSport;
        name = nameofSport;
        cals = Integer.parseInt(calsodSport);
    }

}
