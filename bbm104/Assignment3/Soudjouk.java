public class Soudjouk extends PizzaDecorator {
    public Soudjouk(PizzaBase pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " Soudjouk" ;
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 3;
    }
}
