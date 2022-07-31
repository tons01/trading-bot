import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CustomDate {
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

}
