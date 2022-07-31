import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceManager {

    public final double initialCapital;
    public double moneyInvested;
    public double moneyNoInvested;
    public double amountCoins;

    public FinanceManager(double initialCapital) {
        this.initialCapital = initialCapital;
        this.moneyNoInvested = initialCapital;
    }

    public double getCashTotal() {
        return moneyInvested + moneyNoInvested;
    }

    public void buy(double moneyAmount, double currentPrice) {
        moneyNoInvested -= moneyAmount;
        moneyInvested += moneyAmount;

        // refreshes amount of coins
        amountCoins += moneyAmount / currentPrice;
    }

    public void sell(double moneyAmount, double currentPrice) {
        moneyNoInvested += moneyAmount;
        moneyInvested -= moneyAmount;

        // refreshes amount of coins
        amountCoins -= moneyAmount / currentPrice;
    }

    public void refreshCapitalInvested(double currentPrice) {
        moneyInvested = amountCoins * currentPrice;
    }

}
