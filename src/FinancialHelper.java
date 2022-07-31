import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialHelper {

    public double capitalInvested;
    public double capitalNotInvested;

    public double amountCoins;

    public FinancialHelper(double initialCapital) {
        this.capitalNotInvested = initialCapital;
    }

    public void buy(double moneyAmount, double currentPrice) {
        capitalNotInvested -= moneyAmount;
        capitalInvested += moneyAmount;


        // refreshes amount of coins
        amountCoins += moneyAmount / currentPrice;
    }

    public void sell(double moneyAmount, double currentPrice) {
        capitalNotInvested += moneyAmount;
        capitalInvested -= moneyAmount;

        // refreshes amount of coins
        amountCoins -= moneyAmount / currentPrice;
    }

    public void refreshCapitalInvested(double currentPrice) {
        capitalInvested = amountCoins * currentPrice;
    }


    public double getCashTotal() {
        return capitalInvested + capitalNotInvested;
    }
}
