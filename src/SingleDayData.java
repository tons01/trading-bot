import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Builder
@Getter
@Setter
public class SingleDayData {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private double volume;
    private Map<LocalTime, TimeDetailData> timeDetailData;

    @Builder
    @Getter
    @Setter
    private static class TimeDetailData {
        LocalTime time;
    }
}
