class Neapolitan implements PizzaBase {
    public Neapolitan() {
    }

    @Override
    public String getDescription() {
        return "Neapolitan";
    }

    @Override
    public int getCost() {
        return 10;
    }
}

