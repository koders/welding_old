package lv.welding.orders.models.charts;

/**
 * Created by Rihards on 27.08.2014.
 */
public class Customer {

    private String name;
    private int moneySpent;

    public Customer(String name, int moneySpent) {
        this.name = name;
        this.moneySpent = moneySpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(int moneySpent) {
        this.moneySpent = moneySpent;
    }
}
