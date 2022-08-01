import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AlgorithmProperties {
    private double lowThreshold;
    private double highThreshold;
    private double proportion;
}
