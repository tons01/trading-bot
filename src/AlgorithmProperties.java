import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AlgorithmProperties {
    private double lowThreshold;
    private double highThreshold;
    private double proportionCapitalNotInvested;

    private IndicatorProperties indicatorProperties;

    @Builder
    @Getter
    @Setter
    public static class IndicatorProperties {
        private int rsiLength;
    }
}
