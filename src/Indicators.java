import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public class Indicators {
    private final int rsiLength;

    private Map<LocalDate, Double> historicalRSIs;
    private Map<LocalDate, Double> historicalSMA200s;
    private Map<LocalDate, Double> historicalSMA50s;

    @Builder
    public Indicators(int rsiLength) {
        this.rsiLength = rsiLength;
        this.historicalRSIs = new HashMap<>();
        this.historicalSMA200s = new HashMap<>();
        this.historicalSMA50s = new HashMap<>();
    }

    public void refreshIndicators(TradingAlgorithm algorithm, LocalDate date) {
        var data = algorithm.getData();

        calculateRSI(data, date);
        calculateSMA200(data, date);
        calculateSMA50(data, date);
    }

    private void calculateRSI(Map<LocalDate, SingleDayData> data, LocalDate date) {

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

    private void calculateSMA200(Map<LocalDate, SingleDayData> data, LocalDate date) {
        double sma200 = getSMA(date, data, 200);
        historicalSMA200s.put(date, sma200);
    }

    private void calculateSMA50(Map<LocalDate, SingleDayData> data, LocalDate date) {
        double sma200 = getSMA(date, data, 50);
        historicalSMA200s.put(date, sma200);
    }

    private double getSMA(LocalDate date, Map<LocalDate, SingleDayData> data, int daysBack) {
        var daysLast200 = date.minusDays(daysBack).datesUntil(date.plusDays(1)).collect(Collectors.toList());
        var subMap = new HashMap<>(data);
        subMap.keySet().retainAll(daysLast200);
        return subMap.values().stream().map(SingleDayData::getOpen).mapToDouble(Double::doubleValue).average().getAsDouble();
    }

}
