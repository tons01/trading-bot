import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        var runTimeStart = System.currentTimeMillis();

        execute();

        var runTimeEnd = System.currentTimeMillis();
        System.out.printf("runtime duration: %sms", runTimeEnd-runTimeStart);
    }

    private static void execute() {
        var algorithmProperties = AlgorithmProperties.builder()
                .threshold(0.01)
                .proportionCapitalNotInvested(0.05)
                .build();

        var financialData = new FinancialHelper(10000);

        var algorithm = new TradingAlgorithm(algorithmProperties, financialData);

        var backTesting = new BackTesting(algorithm);

        backTesting.backtest(15, Date.valueOf("2021-12-01"), Date.valueOf("2022-07-29"));
    }
}
