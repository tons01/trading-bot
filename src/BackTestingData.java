import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;


public class BackTestingData {

    public static final String DATA_CSV = "ethereum_data.csv";

    public static LinkedList<HistoricalDataLine> readData() {

        var data = new LinkedList<HistoricalDataLine>();

        try {
            var reader = new BufferedReader(new FileReader(DATA_CSV));
            String line;

            reader.readLine(); // skip first line

            while ((line = reader.readLine()) != null) {

                var values = line.split(",");

                var timestamp = Instant.ofEpochSecond(Long.parseLong(values[0]));
                var open = Double.parseDouble(values[1]);

                HistoricalDataLine historicalDataLine = HistoricalDataLine.builder()
                        .timestamp(timestamp)
                        .open(open)
                        .build();

                data.addFirst(historicalDataLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
