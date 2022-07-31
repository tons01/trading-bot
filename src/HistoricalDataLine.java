import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class HistoricalDataLine {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private double volume;
}
