import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class TradingAlgorithm {
    private final AlgorithmProperties algorithmProperties;
    private final FinanceManager financeManager;
    private final Indicators indicators;
    private Map<LocalDate, SingleDayData> data;

    @Builder
    public TradingAlgorithm(AlgorithmProperties algorithmProperties, FinanceManager financeManager, Indicators indicators) {
        this.algorithmProperties = algorithmProperties;
        this.financeManager = financeManager;
        this.indicators = indicators;
        this.data = BacktestData.readData();
    }


    /**
     * Please implement the algorithm in this method.
     */
    public void performAlgorithm(LocalDate date) {

        if (date.equals(LocalDate.parse(Main.FROM_DATE))) {
            financeManager.buy(10000, data.get(date).getOpen());
            return;
        }

        double open = data.get(date).getOpen();
        Map<LocalDate, Double> sma50s = indicators.getHistoricalSMA50s();
        Map<LocalDate, Double> sma200s = indicators.getHistoricalSMA200s();

        if (sma50s.get(date) > sma200s.get(date) &&
            sma50s.get(date.minusDays(1)) < sma200s.get(date.minusDays(1))) {
            double buyAmount = financeManager.getMoneyNoInvested()*1.0;
            financeManager.buy(buyAmount, open);
        }
        if (sma50s.get(date) < sma200s.get(date) &&
            sma50s.get(date.minusDays(1)) > sma200s.get(date.minusDays(1))) {
            double sellAmount = financeManager.getMoneyInvested()*1.0;
            financeManager.sell(sellAmount, open);
        }

    }
}
