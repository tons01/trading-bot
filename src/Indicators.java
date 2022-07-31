import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public class Indicators {

    private int rsiLength;

    public Indicators(int rsiLength) {
        this.rsiLength = rsiLength;
    }

    private Map<LocalDate, Double> historicalRSIs = new HashMap<>();

    public void calculateRSI(Map<LocalDate, HistoricalDataLine> data, FinanceManager financeManager, LocalDate date) {

        double losses = 0;
        double gains = 0;
        for (int daysBack = 0; daysBack < rsiLength; daysBack++) {
            losses += Math.max(data.get(date.minusDays(daysBack + 1)).getOpen() - data.get(date.minusDays(daysBack)).getOpen(), 0);
            gains += Math.max(data.get(date.minusDays(daysBack)).getOpen() - data.get(date.minusDays(daysBack + 1)).getOpen(), 0);
        }
        double lossAvg = losses / rsiLength;
        double gainAvg = gains / rsiLength;

        double rs = gainAvg / lossAvg;
        double rsi = 100 - 100 / (1 + rs);

        historicalRSIs.put(date, rsi);
    }

    public void refreshIndicators() {

    }

}
