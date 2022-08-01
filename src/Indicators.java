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

    public void calculateIndicators(TradingAlgorithm algorithm) {
        var fromDate = LocalDate.parse(Main.FROM_DATE);
        var toDate = LocalDate.parse(Main.TO_DATE);
        var data = algorithm.getData();

        calculateRSI(data, fromDate, toDate);
        calculateSMA200(data, fromDate, toDate);
        calculateSMA50(data, fromDate, toDate);
    }

    private void calculateRSI(Map<LocalDate, SingleDayData> data, LocalDate fromDate, LocalDate toDate) {


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

    private void calculateSMA200(Map<LocalDate, SingleDayData> data, LocalDate dateFrom, LocalDate dateTo) {

        int daysBack = 200;
        getSMArecursive(dateFrom, data, daysBack, historicalSMA200s, dateTo);
    }

    private void calculateSMA50(Map<LocalDate, SingleDayData> data, LocalDate dateFrom, LocalDate dateTo) {
        int daysBack = 50;
        getSMArecursive(dateFrom, data, daysBack, historicalSMA50s, dateTo);
    }

    private void getSMArecursive(LocalDate dateFrom, Map<LocalDate, SingleDayData> data, int daysBack, Map<LocalDate, Double> historicalRSIs, LocalDate dateTo) {
        double sma = getSMAon(dateFrom, data, daysBack);
        historicalRSIs.put(dateFrom, sma);

        dateFrom.plusDays(1).datesUntil(dateTo.plusDays(1)).forEach(date -> {
            double sma_new = historicalRSIs.get(date.minusDays(1)) + (data.get(date).getOpen() - data.get(date.minusDays(daysBack)).getOpen()) / daysBack;
            historicalRSIs.put(date, sma_new);
        });
    }

    private double getSMAon(LocalDate date, Map<LocalDate, SingleDayData> data, int daysBack) {
        var lastDays = date.minusDays(daysBack).datesUntil(date.plusDays(1)).collect(Collectors.toList());
        var subMap = new HashMap<>(data);
        subMap.keySet().retainAll(lastDays);
        return subMap.values().stream().map(SingleDayData::getOpen).mapToDouble(Double::doubleValue).average().getAsDouble();
    }

}
