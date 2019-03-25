public class Hotpepper extends PizzaDecorator {
    public Hotpepper(PizzaBase pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " Hot Pepper" ;
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 1;
    }
}
