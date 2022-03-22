package settlers;

public class Settler {
    private String nameOfSettler;
    private int amountOfTobacco;
     private final int PRICE_PER_TON =500;

    public Settler(String nameOfSettler, int amountOfTobacco) {
        this.nameOfSettler = nameOfSettler;
        this.amountOfTobacco = amountOfTobacco;
    }

    public String getNameOfSettler() {
        return nameOfSettler;
    }

    public int getAmountOfTobacco() {
        return amountOfTobacco;
    }

    public int getExpectedIncome() {
        return amountOfTobacco*PRICE_PER_TON;
    }
}
