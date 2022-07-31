import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class TradingAlgorithm {
    public AlgorithmProperties algorithmProperties;

    public FinanceHelper financeHelper;
    public Map<LocalDate, HistoricalDataLine> data;
    public Indicators indicators;

    public TradingAlgorithm(AlgorithmProperties algorithmProperties, FinanceHelper financeHelper) {
        this.algorithmProperties = algorithmProperties;
        this.financeHelper = financeHelper;
        this.data = BackTestingData.readData();
        this.indicators = new Indicators(algorithmProperties.getIndicatorProperties().getRsiLength());
    }

    /**
     * Please implement the algorithm in this method.
     */
    public void performAlgorithm(LocalDate date) {

        double open = data.get(date).getOpen();
        if (isHighThresholdReached(date)) {
            double sellAmount = financeHelper.getCapitalInvested() * algorithmProperties.getProportionCapitalNotInvested();
            financeHelper.sell(sellAmount, open);
        }
        if (isLowThresholdReached(date)) {
            double buyAmount = financeHelper.getCapitalNotInvested() * algorithmProperties.getProportionCapitalNotInvested();
            financeHelper.buy(buyAmount, open);
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
