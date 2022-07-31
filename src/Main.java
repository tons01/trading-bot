import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    /**
     * Algorithm Properties
     */
    public static final double LOW_THRESHOLD = 0.05;
    public static final double HIGH_THRESHOLD = 0.10;
    public static final double PROPORTION_CAPITAL_NOT_INVESTED = 0.05;

    /**
     * Indicators Properties
     */
    public static final int RSI_LENGTH = 14;

    /**
     * Finance Manager Properties
     */
    public static final int INITIAL_CAPITAL = 10000;

    /**
     * Backtesting Detail Properties
     */
    public static final String FROM_DATE = "2017-09-01";
    public static final String TO_DATE = "2022-07-01";

    public static void main(String[] args) {
        var runTimeStart = System.currentTimeMillis();
        execute();
        var runTimeEnd = System.currentTimeMillis();
        System.out.printf("runtime duration: %sms", runTimeEnd - runTimeStart);
    }

    private static void execute() {

        var algorithmProperties = AlgorithmProperties.builder()
                .lowThreshold(LOW_THRESHOLD)
                .highThreshold(HIGH_THRESHOLD)
                .proportionCapitalNotInvested(PROPORTION_CAPITAL_NOT_INVESTED)
                .build();

        var financeManager = FinanceManager.builder().initialCapital(INITIAL_CAPITAL).build();

        var indicators = Indicators.builder().rsiLength(RSI_LENGTH).build();

        var algorithm = TradingAlgorithm.builder()
                .algorithmProperties(algorithmProperties)
                .financeManager(financeManager)
                .indicators(indicators)
                .build();

        var backTesting = BackTesting.builder().algorithm(algorithm).build();
        backTesting.backtest(LocalDate.parse(FROM_DATE), LocalDate.parse(TO_DATE));
    }
}
