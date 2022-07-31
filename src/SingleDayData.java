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
    private Map<LocalTime, TimeDetailData> timeDetailData;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private double volume;

    @Builder
    @Getter
    @Setter
    private static class TimeDetailData {
        LocalTime time;
    }
}
