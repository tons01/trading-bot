import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
public class GraphicsPlotter extends JPanel {
    //margin
    private static final int marg = 60;
    // plot boolean
    private static boolean plotHistoricalMoneyTotal = true;
    private static boolean plotOutperformIndex = true;

    private static boolean plotSma50 = true;
    private static boolean plotSma200 = false;

    private static boolean plotLowerBB = true;
    private static boolean plotMiddleBB = false;
    private static boolean plotHigherBB = true;
    // data
    private final List<Double> historicalMoneyTotal;

    private final List<Double> outperformIndex;

    private final List<Double> sma50;
    private final List<Double> sma200;

    private final List<Double> lowerBB;
    private final List<Double> middleBB;
    private final List<Double> higherBB;


    public static void plotOutperformance(TradingAlgorithm algorithm) {
        var historicalFinanceData = algorithm.getFinanceManager().getHistoricalFinanceData();

        var fromDate = LocalDate.parse(Main.FROM_DATE);
        var toDate = LocalDate.parse(Main.TO_DATE);

        // calculate ArrayLists to plot
        var cashTotalHistoricalDataList = getCashTotalHistoricalData(historicalFinanceData, fromDate, toDate);
        var outperformIndexList = getOutperformIndex(algorithm, fromDate, toDate);

        var sma50List = getListOfIndicator(algorithm.getIndicators().getHistoricalSMA50s(), fromDate, toDate);
        var sma200List = getListOfIndicator(algorithm.getIndicators().getHistoricalSMA200s(), fromDate, toDate);

        var lowerBBList = getListOfIndicator(algorithm.getIndicators().getHistoricalLowerBB(), fromDate, toDate);
        var middleBBList = getListOfIndicator(algorithm.getIndicators().getHistoricalMiddleBB(), fromDate, toDate);
        var higherBBList = getListOfIndicator(algorithm.getIndicators().getHistoricalSHigherBB(), fromDate, toDate);

        //create an instance of JFrame class
        JFrame frame = new JFrame();
        // set size, layout and location for frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(GraphicsPlotter.builder()
                .historicalMoneyTotal(cashTotalHistoricalDataList)
                .outperformIndex(outperformIndexList)
                .sma50(sma50List)
                .sma200(sma200List)
                .lowerBB(lowerBBList)
                .middleBB(middleBBList)
                .higherBB(higherBBList)
                .build());
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        frame.setVisible(true);

    }

    private static ArrayList<Double> getListOfIndicator(Map<LocalDate, Double> indicatorMap, LocalDate fromDate, LocalDate toDate) {
        var indicatorList = new ArrayList<Double>();
        fromDate.datesUntil(toDate.plusDays(1)).forEach(date -> {
            indicatorList.add(indicatorMap.get(date));
        });
        return indicatorList;
    }

    private static ArrayList<Double> getOutperformIndex(TradingAlgorithm algorithm, LocalDate from_date, LocalDate to_date) {
        var outperformList = new ArrayList<Double>();

        var data = algorithm.getData();
        var initialCapital = algorithm.getFinanceManager().getInitialCapital();
        var initialOpen = algorithm.getData().get(from_date).getOpen();
        var amountCoins = initialCapital / initialOpen;
        from_date.datesUntil(to_date)
                .forEach(date ->
                {
                    double open = data.get(date).getOpen();
                    var possibleCapital = amountCoins * open;
                    outperformList.add(possibleCapital);
                });

        return outperformList;
    }

    private static ArrayList<Double> getCashTotalHistoricalData(Map<LocalDate, FinanceManager.HistoricalFinanceDataLine> historicalFinanceData, LocalDate from_date, LocalDate to_date) {
        var cashList = new ArrayList<Double>();
        from_date.datesUntil(to_date)
                .forEach(date -> {
                    cashList.add(historicalFinanceData.get(date).getMoneyTotal());
                });
        return cashList;
    }

    protected void paintComponent(Graphics grf) {
        //create instance of the Graphics to use its methods
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D) grf;

        //Sets the value of a single preference for the rendering algorithms.
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // get width and height
        int width = getWidth();
        int height = getHeight();

        // draw graph
        graph.draw(new Line2D.Double(marg, marg, marg, height - marg));
        graph.draw(new Line2D.Double(marg, height - marg, width - marg, height - marg));

        //find value of x and scale to plot points
        double x = (double) (width - 2 * marg) / (historicalMoneyTotal.size() - 1);
        double scale = (double) (height - 2 * marg) / getMax();

        if (plotHistoricalMoneyTotal) {
            plotData(graph, Color.RED, historicalMoneyTotal, x, height, scale);
        }
        if (plotOutperformIndex) {
            plotData(graph, Color.blue, outperformIndex, x, height, scale);
        }
        if (plotSma50) {
            plotData(graph, Color.GREEN, sma50, x, height, scale);
        }
        if (plotSma200) {
            plotData(graph, Color.cyan, sma200, x, height, scale);
        }
        if (plotLowerBB) {
            plotData(graph,Color.DARK_GRAY, lowerBB,x,height,scale);
        }
        if (plotMiddleBB) {
            plotData(graph,Color.DARK_GRAY, middleBB,x,height,scale);
        }
        if (plotHigherBB) {
            plotData(graph,Color.DARK_GRAY, higherBB,x,height,scale);
        }
    }

    private void plotData(Graphics2D graph, Color color, List<Double> dataList, double x, int height, double scale) {
        //set color for points
        graph.setPaint(color);
        // set points to the graph
        for (int i = 0; i < dataList.size(); i++) {
            double x1 = marg + i * x;
            double y1 = height - marg - scale * dataList.get(i);
            graph.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
        }
    }


    //create getMax() method to find maximum value
    private double getMax() {
        return Stream.of(outperformIndex, historicalMoneyTotal)
                .flatMap(Collection::stream)
                .mapToDouble(Double::doubleValue)
                .max().getAsDouble();
    }
}
