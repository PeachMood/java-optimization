package com.example.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LineChart {
    private final XYSeries series;
    private final JFreeChart chart;

    public LineChart(String title, String xAxisLabel, String yAxisLabel) {
        series = new XYSeries(title);
        chart = createChart(title, xAxisLabel, yAxisLabel);
    }

    public void addData(double x, double y) {
        series.add(x, y);
    }

    public void saveChart(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
        ChartUtils.saveChartAsJPEG(path.toFile(), chart, 1200, 600);
    }

    private JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                new XYSeriesCollection(series),
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(232, 232, 232));
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        plot.getRangeAxis().setAxisLineVisible(false);
        return chart;
    }
}