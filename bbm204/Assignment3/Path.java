import java.util.ArrayList;

public class Path{
    private String from;
    private ArrayList<Link> pathArray;
    private int hCount;
    private int aCount;
    private int rCount;

    public Path(String from) {
        this.from = from;
        pathArray = new ArrayList<>();
        hCount = 0;
        aCount = 0;
        rCount = 0;
    }

    public Path (Path that) {
        this.from = that.from;
        this.pathArray = (ArrayList<Link>) that.pathArray.clone();
        this.hCount = that.hCount;
        this.aCount = that.aCount;
        this.rCount = that.rCount;

    }

    public boolean countCheck(int hCount, int aCount, int rCount){
        if(hCount < 0){
            hCount = this.hCount;
        }
        if(aCount < 0){
            aCount = this.aCount;
        }
        if(rCount < 0){
            rCount = this.rCount;
        }
        return (this.hCount == hCount) && (this.aCount == aCount) && (this.rCount == rCount);
    }

    public void increase(char type){
        switch (type){
            case 'a':
                aCount++;
                break;
            case 'r':
                rCount++;
                break;
            case 'h':
                hCount++;
                break;
        }
    }
    public void decrease(char type){
        switch (type){
            case 'a':
                aCount--;
                break;
            case 'r':
                rCount--;
                break;
            case 'h':
                hCount--;
                break;
        }
    }

    public boolean includes(String elementName){
        for(Link i: pathArray){
            if(Map.getNames().get(i.getIndex()).equalsIgnoreCase(elementName)) return true;
        }
        return elementName.equalsIgnoreCase(from);
    }

    public void addLink(Link link){
        pathArray.add(link);
    }

    public Link removeLastLink(){
        if(pathArray.size() < 1) return null;
        return pathArray.remove(pathArray.size()-1);
    }

    public boolean has(String cityName, ArrayList<String> names){
        for(Link i: pathArray){
            if(names.get(i.getIndex()).equalsIgnoreCase(cityName))
                return true;
        }
        return false;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(from);
        for(Link i: pathArray){
            str.append(", ");
            str.append(Character.toUpperCase(i.getTransportWay()));
            str.append(", ");
            str.append(Map.getNames().get(i.getIndex()));
        }

        return str.toString();
    }


}
