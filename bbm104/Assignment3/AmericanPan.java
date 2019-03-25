public class AmericanPan implements PizzaBase {
    public AmericanPan() {
    }

    @Override
    public String getDescription() {
        return "AmericanPan";
    }

    @Override
    public int getCost() {
        return 5;
    }
}
