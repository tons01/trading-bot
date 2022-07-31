import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TradingAlgorithm {

    public AlgorithmProperties algorithmProperties;
    public FinancialHelper financialHelper;
    public LinkedList<HistoricalDataLine> data;
    public Indicators indicators;

    public TradingAlgorithm(AlgorithmProperties algorithmProperties, FinancialHelper financialHelper) {
        this.algorithmProperties = algorithmProperties;
        this.financialHelper = financialHelper;
        this.data = BackTestingData.readData();
        this.indicators = new Indicators();

    }

    private void performAlgorithm(int dayIndex) {
         double open = data.get(dayIndex).getOpen();
        if (isHighThresholdReached(dayIndex)) {
            double sellAmount = financialHelper.getCapitalInvested() * 0.05;
            financialHelper.sell(sellAmount, open);
        }
        if (isLowThresholdReached(dayIndex)) {
            double buyAmount = financialHelper.getCapitalNotInvested() * 0.05;
            financialHelper.buy(buyAmount, open);
        }
    }

    public void performIteration(int day) {
        performAlgorithm(day);
        indicators.calculateRSI(data, financialHelper, day, 14);
    }


    private boolean isLowThresholdReached(int dayIndex) {
        double openDifference = data.get(dayIndex - 1).getOpen() - data.get(dayIndex).getOpen();
        return openDifference > data.get(dayIndex - 1).getOpen() * algorithmProperties.getThreshold();
    }

    private boolean isHighThresholdReached(int iteration) {
        double openDifference = data.get(iteration).getOpen() - data.get(iteration - 1).getOpen();
        return openDifference > data.get(iteration - 1).getOpen() * algorithmProperties.getThreshold();
    }


}
