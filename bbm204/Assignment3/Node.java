import java.util.ArrayList;

public class Node {

    private String cityName;

    public ArrayList<Link> getLinks() {
        return links;
    }

    private ArrayList<Link> links = new ArrayList<>();

    public Node(String cityName){
        this.cityName = cityName;
    }

    public Node(String cityName, Link initalLink){
        this.cityName = cityName;
        this.links.add(initalLink);
    }

    public void addLink(Link link){
        links.add(link);
    }


    public String getCityName() {
        return cityName;
    }

}
