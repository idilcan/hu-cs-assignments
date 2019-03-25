public class PizzaDecorator implements PizzaBase {
    PizzaBase pizza;

    public PizzaDecorator(PizzaBase pizza) {
        this.pizza = pizza;
    }

    @Override
    public String getDescription() {
        return pizza.getDescription();
    }

    @Override
    public int getCost() {
        return pizza.getCost();
    }
}
