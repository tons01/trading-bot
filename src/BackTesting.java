import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BackTesting {
    public TradingAlgorithm algorithm;

    public BackTesting(TradingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void backtest(LocalDate startDate, LocalDate endDate) {

        startDate.datesUntil(endDate).forEach(date -> {

            var open = algorithm.getData().get(date).getOpen();

            algorithm.getFinanceManager().refreshCapitalInvested(open);
            algorithm.getIndicators().calculateRSI(algorithm.getData(), algorithm.getFinanceManager(), date);
            algorithm.performAlgorithm(date);

            printTradingInfo(date);
        });

        System.out.printf("out-performance: %s%%\n", getOutperformance(startDate, endDate));


    }

    private double getOutperformance(LocalDate startDate, LocalDate endDate) {
        double openInitial = algorithm.getData().get(startDate).getOpen();
        double openEnd = algorithm.getData().get(endDate).getOpen();
        double initialCapital = algorithm.getFinanceManager().getInitialCapital();
        double currentCashTotal = algorithm.getFinanceManager().getCashTotal();
        return currentCashTotal / (openEnd / openInitial * initialCapital) * 100 - 100;
    }

    private void printTradingInfo(LocalDate date) {
        System.out.printf("date: %s, invested: %s, not invested: %s, total: %s, coins amount: %s\n",
                date,
                algorithm.getFinanceManager().getMoneyInvested(),
                algorithm.getFinanceManager().getMoneyNoInvested(),
                algorithm.getFinanceManager().getCashTotal(),
                algorithm.getFinanceManager().getAmountCoins());
    }
}


