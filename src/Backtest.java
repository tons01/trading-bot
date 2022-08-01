import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Backtest {
    private final TradingAlgorithm algorithm;
    public void backtest(LocalDate fromDate, LocalDate toDate) {

        algorithm.getIndicators().calculateIndicators(algorithm);

        fromDate.datesUntil(toDate).forEach(date -> {

            algorithm.getFinanceManager().refreshFinance(algorithm, date);

            algorithm.performAlgorithm(date);

            printTradingInfo(date);
        });

        System.out.printf("out-performance: %s%%\n", getOutperformance(fromDate, toDate));

        GraphicsPlotter.plotOutperformance(algorithm);
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
                algorithm.getFinanceManager().getCapitalInvested(),
                algorithm.getFinanceManager().getCapitalNotInvested(),
                algorithm.getFinanceManager().getCashTotal(),
                algorithm.getFinanceManager().getAmountCoins());
    }
}


