import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        var runTimeStart = System.currentTimeMillis();
        execute("2017-09-01", "2022-07-01");
        var runTimeEnd = System.currentTimeMillis();
        System.out.printf("runtime duration: %sms", runTimeEnd - runTimeStart);
    }

    private static void execute(String startDate, String endDate) {

        var algorithmProperties = AlgorithmProperties.builder()
                .lowThreshold(0.05)
                .highThreshold(0.10)
                .proportionCapitalNotInvested(0.05)
                .indicatorProperties(
                        AlgorithmProperties.IndicatorProperties.builder()
                                .rsiLength(14)
                                .build())
                .build();

        var financialData = new FinanceManager(10000);

        var algorithm = new TradingAlgorithm(algorithmProperties, financialData);

        var backTesting = new BackTesting(algorithm);
        backTesting.backtest(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}
