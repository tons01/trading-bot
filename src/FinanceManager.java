import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FinanceManager {

    private final double initialCapital;
    private double capitalInvested;
    private double capitalNotInvested;
    private double amountCoins;

    private Map<LocalDate, HistoricalFinanceDataLine> historicalFinanceData;

    @Builder
    public FinanceManager(double initialCapital) {
        this.initialCapital = initialCapital;
        this.capitalNotInvested = initialCapital;
        this.historicalFinanceData = new HashMap<>();
    }

    public double getCashTotal() {
        return capitalInvested + capitalNotInvested;
    }

    public void buy(double moneyAmount, double currentPrice) {
        if (moneyAmount > capitalNotInvested) {
            System.err.println("moneyAmount mustn't exceed capitalNotInvested");
            return;
        }

        capitalNotInvested -= moneyAmount;
        capitalInvested += moneyAmount;

        // refreshes amount of coins
        amountCoins += moneyAmount / currentPrice;
    }

    public void sell(double moneyAmount, double currentPrice) {
        if (moneyAmount > capitalInvested) {
            System.err.println("moneyAmount mustn't exceed capitalNotInvested");
            return;
        }

        capitalNotInvested += moneyAmount;
        capitalInvested -= moneyAmount;

        // refreshes amount of coins
        amountCoins -= moneyAmount / currentPrice;
    }

    public void refreshFinance(TradingAlgorithm algorithm, LocalDate date) {
        var currentPrice = algorithm.getData().get(date).getOpen();
        refreshCapitalInvested(currentPrice);


        HistoricalFinanceDataLine historicalFinanceDataLine = HistoricalFinanceDataLine.builder()
                .moneyInvested(capitalInvested)
                .moneyNoInvested(capitalNotInvested)
                .moneyTotal(getCashTotal())
                .amountCoins(amountCoins)
                .build();
        historicalFinanceData.put(date, historicalFinanceDataLine);

    }

    private void refreshCapitalInvested(double currentPrice) {
        capitalInvested = amountCoins * currentPrice;
    }

    @Getter
    @Setter
    @Builder
    public static class HistoricalFinanceDataLine {
        private double moneyInvested;
        private double moneyNoInvested;
        private double moneyTotal;
        private double amountCoins;
    }
}
