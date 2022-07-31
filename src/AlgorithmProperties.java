import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AlgorithmProperties {
    public double threshold;
    public double proportionCapitalNotInvested;

    public AlgorithmProperties(double threshold, double proportionCapitalNotInvested) {
        this.threshold = threshold;
        this.proportionCapitalNotInvested = proportionCapitalNotInvested;
    }
}
