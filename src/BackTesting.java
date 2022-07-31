import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class BackTesting {
    public TradingAlgorithm algorithm;

    public BackTesting(TradingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void backtest(int initialDay, Date startDate, Date endDate) {

        for (int dayIndex = initialDay; dayIndex < algorithm.getData().size(); dayIndex++) {

            double open = algorithm.getData().get(dayIndex).getOpen();
            algorithm.getFinancialHelper().refreshCapitalInvested(open);

            algorithm.performIteration(dayIndex);

            printTradingInfo(dayIndex);

        }
    }

    private void printTradingInfo(int dayIndex) {
        var tradingInfo = String.format("date: %s, invested: %s, not invested: %s, total: %s, coins amount: %s",
                Date.from(algorithm.getData().get(dayIndex).getTimestamp()),
                algorithm.getFinancialHelper().getCapitalInvested(),
                algorithm.getFinancialHelper().getCapitalNotInvested(),
                algorithm.getFinancialHelper().getCashTotal(),
                algorithm.getFinancialHelper().getAmountCoins());
        System.out.println(tradingInfo);
    }
}


