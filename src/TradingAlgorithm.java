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
        }



        double open = data.get(date).getOpen();
        if (isHighThresholdReached(date)) {
            double sellAmount = financeManager.getMoneyInvested() * algorithmProperties.getProportionCapitalNotInvested();
            financeManager.sell(sellAmount, open);
        }
        if (isLowThresholdReached(date)) {
            double buyAmount = financeManager.getMoneyNoInvested() * algorithmProperties.getProportionCapitalNotInvested();
            financeManager.buy(buyAmount, open);
        }

    }


    private boolean isLowThresholdReached(LocalDate date) {
        double openDifference = data.get(date.minusDays(1)).getOpen() - data.get(date).getOpen();
        return openDifference > data.get(date.minusDays(1)).getOpen() * algorithmProperties.getLowThreshold();
    }

    private boolean isHighThresholdReached(LocalDate date) {
        double openDifference = data.get(date).getOpen() - data.get(date.minusDays(1)).getOpen();
        return openDifference > data.get(date.minusDays(1)).getOpen() * algorithmProperties.getHighThreshold();
    }
}
