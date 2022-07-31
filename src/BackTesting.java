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
            algorithm.getFinanceHelper().refreshCapitalInvested(open);

            algorithm.getIndicators().calculateRSI(algorithm.getData(), algorithm.getFinanceHelper(), date);

            algorithm.performAlgorithm(date);

            printTradingInfo(date);
        });
    }

    private void printTradingInfo(LocalDate date) {
        var tradingInfo = String.format("date: %s, invested: %s, not invested: %s, total: %s, coins amount: %s",
                date,
                algorithm.getFinanceHelper().getCapitalInvested(),
                algorithm.getFinanceHelper().getCapitalNotInvested(),
                algorithm.getFinanceHelper().getCashTotal(),
                algorithm.getFinanceHelper().getAmountCoins());
        System.out.println(tradingInfo);
    }
}


