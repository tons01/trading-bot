import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class BackTestingData {

    public static final String DATA_CSV = "BTC-USD.csv";

    public static Map<LocalDate, HistoricalDataLine> readData() {
        var data = new HashMap<LocalDate, HistoricalDataLine>();
        try {
            var reader = new BufferedReader(new FileReader(DATA_CSV));

            reader.readLine(); // skip first line

            String line;
            while ((line = reader.readLine()) != null) {
                var values = line.split(",");
                var date = LocalDate.parse(values[0]);
                var historicalDataLine = buildHistoricalDataLine(values, date);
                data.put(date, historicalDataLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static HistoricalDataLine buildHistoricalDataLine(String[] values, LocalDate date) {
        return HistoricalDataLine.builder()
                .date(date)
                .open(Double.parseDouble(values[1]))
                .high(Double.parseDouble(values[2]))
                .low(Double.parseDouble(values[3]))
                .close(Double.parseDouble(values[4]))
                .adjClose(Double.parseDouble(values[5]))
                .volume(Double.parseDouble(values[6]))
                .build();


    }
}
