import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FinanceManager {

    public final double initialCapital;
    public double moneyInvested;
    public double moneyNoInvested;
    public double amountCoins;

    @Builder
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

    public void refreshFinance(TradingAlgorithm algorithm, LocalDate date) {
        var currentPrice = algorithm.getData().get(date).getOpen();

        refreshCapitalInvested(currentPrice);
    }

    private void refreshCapitalInvested(double currentPrice) {
        moneyInvested = amountCoins * currentPrice;
    }

}
