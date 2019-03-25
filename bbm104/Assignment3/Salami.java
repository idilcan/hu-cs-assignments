public class Salami extends PizzaDecorator{
    public Salami(PizzaBase pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " Salami" ;
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 3;
    }
}