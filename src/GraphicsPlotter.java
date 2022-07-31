import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GraphicsPlotter extends JPanel {

    //initialize coordinates
    private final List<Double> historicalMoneyTotal;
    private final List<Double> outperformIndex;
    int marg = 60;

    public static void plotOutperformance(TradingAlgorithm algorithm) {
        var historicalFinanceData = algorithm.getFinanceManager().getHistoricalFinanceData();

        var from_date = LocalDate.parse(Main.FROM_DATE);
        var to_date = LocalDate.parse(Main.TO_DATE);

        var cashTotalHistoricalData = getCashTotalHistoricalData(historicalFinanceData, from_date, to_date);
        var outperformance = getOutperformIndex(algorithm, from_date, to_date);

        //create an instance of JFrame class
        JFrame frame = new JFrame();
        // set size, layout and location for frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GraphicsPlotter(cashTotalHistoricalData, outperformance));
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        frame.setVisible(true);

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

        //set color for points
        graph.setPaint(Color.RED);

        // set points to the graph
        for (int i = 0; i < historicalMoneyTotal.size(); i++) {
            double x1 = marg + i * x;
            double y1 = height - marg - scale * historicalMoneyTotal.get(i);
            graph.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
        }

        //set color for points
        graph.setPaint(Color.BLUE);

        // set points to the graph
        for (int i = 0; i < outperformIndex.size(); i++) {
            double x1 = marg + i * x;
            double y1 = height - marg - scale * outperformIndex.get(i);
            graph.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
        }
    }

    //create getMax() method to find maximum value
    private double getMax() {
        return Math.max(historicalMoneyTotal.stream().mapToDouble(Double::doubleValue).max().getAsDouble(),
                outperformIndex.stream().mapToDouble(Double::doubleValue).max().getAsDouble());
    }
}
