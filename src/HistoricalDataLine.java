import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Builder
@Getter
@Setter
public class HistoricalDataLine {
    private Instant timestamp;
    private double open;
    private double close;
}
