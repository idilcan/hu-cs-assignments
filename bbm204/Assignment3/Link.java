public class Link{
    private int index;
    private char transportWay;
    public Link(int index, char transportWay){
        this.index = index;
        this.transportWay = transportWay;
    }

    public int getIndex() {
        return index;
    }

    public char getTransportWay() {
        return transportWay;
    }
}