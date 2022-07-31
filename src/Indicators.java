import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.KeyPair;
import java.time.Instant;
import java.util.*;


@Getter
@Setter
public class Indicators {

    public LinkedList<Map.Entry<Instant,Double>> historicalRSIs = new LinkedList<>();

    public void calculateRSI(LinkedList<HistoricalDataLine> data, FinancialHelper financialHelper, int dayIndex, int rsiLength) {

        double losses = 0;
        double gains = 0;
        for (int i = 0; i < rsiLength; i++) {
            losses += Math.max(data.get(dayIndex - i -1).getOpen() - data.get(dayIndex-i).getOpen(), 0);
            gains += Math.max(data.get(dayIndex-i).getOpen()-data.get(dayIndex-i-1).getOpen(), 0);
        }
        double lossAvg = losses/rsiLength;
        double gainAvg = gains/rsiLength;

        double rs = gainAvg/lossAvg;
        double rsi = 100 - 100/(1+rs);

        var date = data.get(dayIndex).getTimestamp();
        historicalRSIs.addFirst(Map.entry(date,rs));
    }

    public void refreshIndicators() {

    }

}
