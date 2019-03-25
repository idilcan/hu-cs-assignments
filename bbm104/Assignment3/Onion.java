public class Onion extends PizzaDecorator {
    public Onion(PizzaBase pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " Onion" ;
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 2;
    }
}
