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
    /**
     * RSI
     */
    private final int rsiLength;

    /**
     * BB
     */
    private final int daysbackBB;
    private final int k;


    private final LocalDate fromDate;
    private final LocalDate toDate;

    private Map<LocalDate, Double> historicalRSIs;
    private Map<LocalDate, Double> historicalSMA50s;
    private Map<LocalDate, Double> historicalSMA200s;
    private Map<LocalDate, Double> historicalLowerBB;
    private Map<LocalDate, Double> historicalMiddleBB;
    private Map<LocalDate, Double> historicalSHigherBB;

    @Builder
    public Indicators(int rsiLength, int daysbackBB, int k, LocalDate fromDate, LocalDate toDate) {
        this.rsiLength = rsiLength;
        this.daysbackBB = daysbackBB;
        this.k = k;
        this.fromDate = fromDate;
        this.toDate = toDate;

        this.historicalRSIs = new HashMap<>();
        this.historicalSMA200s = new HashMap<>();
        this.historicalSMA50s = new HashMap<>();
        this.historicalLowerBB = new HashMap<>();
        this.historicalMiddleBB = new HashMap<>();
        this.historicalSHigherBB = new HashMap<>();
    }

    public void calculateIndicators(TradingAlgorithm algorithm) {

        var data = algorithm.getData();

        calculateHistoricalRSIs(data, fromDate, toDate);
        calculateHistoricalSMA200s(data, fromDate, toDate);
        calculateHistoricalSMA50s(data, fromDate, toDate);

        calculateHistoricalBBs(data, fromDate, toDate, 20, 2);

    }

    private void calculateHistoricalBBs(Map<LocalDate, SingleDayData> data, LocalDate fromDate, LocalDate toDate, int daysBack, int k) {

        fromDate.datesUntil(toDate.plusDays(1)).forEach(date -> {

            var middleBB = calculateSmaRegular(date, data, daysBack);

            double variance = date.minusDays(daysBack - 1).datesUntil(date)
                                      .mapToDouble(daysVariance -> middleBB - data.get(daysVariance).getOpen())
                                      .map(diff -> diff * diff).sum() / daysBack;
            var standardDeviation = Math.sqrt(variance);

            var lowerBB = middleBB - standardDeviation * k;
            var higherBB = middleBB + standardDeviation * k;

            historicalLowerBB.put(date, lowerBB);
            historicalMiddleBB.put(date, middleBB);
            historicalSHigherBB.put(date, higherBB);
        });
    }


    private void calculateHistoricalRSIs(Map<LocalDate, SingleDayData> data, LocalDate fromDate, LocalDate toDate) {

        fromDate.datesUntil(toDate.plusDays(1)).forEach(date -> {
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
        });
    }

    private void calculateHistoricalSMA200s(Map<LocalDate, SingleDayData> data, LocalDate dateFrom, LocalDate dateTo) {

        int daysBack = 200;
        calculateSmaRecursive(dateFrom, data, daysBack, historicalSMA200s, dateTo);
    }

    private void calculateHistoricalSMA50s(Map<LocalDate, SingleDayData> data, LocalDate dateFrom, LocalDate dateTo) {
        int daysBack = 50;
        calculateSmaRecursive(dateFrom, data, daysBack, historicalSMA50s, dateTo);
    }

    private void calculateSmaRecursive(LocalDate dateFrom, Map<LocalDate, SingleDayData> data, int daysBack, Map<LocalDate, Double> historicalRSIs, LocalDate dateTo) {
        double sma = calculateSmaRegular(dateFrom, data, daysBack);
        historicalRSIs.put(dateFrom, sma);

        dateFrom.plusDays(1).datesUntil(dateTo.plusDays(1)).forEach(date -> {
            double sma_new = historicalRSIs.get(date.minusDays(1)) + (data.get(date).getOpen() - data.get(date.minusDays(daysBack)).getOpen()) / daysBack;
            historicalRSIs.put(date, sma_new);
        });
    }

    private double calculateSmaRegular(LocalDate date, Map<LocalDate, SingleDayData> data, int daysBack) {
        var lastDays = date.minusDays(daysBack).datesUntil(date.plusDays(1)).collect(Collectors.toList());
        var subMap = new HashMap<>(data);
        subMap.keySet().retainAll(lastDays);
        return subMap.values().stream().map(SingleDayData::getOpen).mapToDouble(Double::doubleValue).average().getAsDouble();
    }

}
