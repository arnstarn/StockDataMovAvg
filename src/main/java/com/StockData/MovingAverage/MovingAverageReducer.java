package com.StockData.MovingAverage;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class MovingAverageReducer extends MapReduceBase implements
		Reducer<TimeSeriesKey, TimeSeriesData, Text, Text> {

	static enum PointCounters {
		POINTS_SEEN, POINTS_ADDED_TO_WINDOWS, MOVING_AVERAGES_CALCD
	}

	static long daysInMilliseconds = 24 * 60 * 60 * 1000;
	private JobConf configuration;

	@Override
	public void configure(JobConf job) {
		this.configuration = job;
	}

	public void reduce(TimeSeriesKey key, Iterator<TimeSeriesData> values,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {

		TimeSeriesData nextDataPoint;

		int iWindowSizeInDays = this.configuration.getInt(
				"windowSize", 20);
		int iWindowStepSizeInDays = this.configuration.getInt(
				"windowStepSize", 1);

		long iWindowSizeInMS = iWindowSizeInDays * daysInMilliseconds;
		long iWindowStepSizeInMS = iWindowStepSizeInDays * daysInMilliseconds;

		Text outKey = new Text();
		Text outValue = new Text();

		SlidingWindow slidingWindow = new SlidingWindow(iWindowSizeInMS, iWindowStepSizeInMS, daysInMilliseconds);

		while (values.hasNext()) {
			while (!slidingWindow.isWindowFull() && values.hasNext()) {
				reporter.incrCounter(PointCounters.POINTS_ADDED_TO_WINDOWS, 1);
				nextDataPoint = values.next();

				TimeSeriesData currentDataPoint = new TimeSeriesData();
				currentDataPoint.copy(nextDataPoint);

				try {
					slidingWindow.addDataPoint(currentDataPoint);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (slidingWindow.isWindowFull()) {
				reporter.incrCounter(PointCounters.MOVING_AVERAGES_CALCD, 1);
				LinkedList<TimeSeriesData> outWindow = slidingWindow.getCurrentWindow();
				String strBackDate = outWindow.getLast().getDate();

				//compute the moving average here
				//outKey.set("Group: " + key.getGroup() + ", Date: " + strBackDate);
        outKey.set(key.getGroup() + "," + strBackDate.trim());

        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.setWindowSize(outWindow.size());

				for (int i=0; i<outWindow.size();i++){
					String[] data = outWindow.get(i).getValue().split(",");

          //System.out.println("#################DATA###############");
          //System.out.println(key.getGroup() + "," +outWindow.get(i).getValue());

					if (data.length != 6)
						continue;

          stats.addValue(Double.parseDouble(data[3]));

				}

        double stdDv = stats.getStandardDeviation();
        double mean = stats.getMean();
				//outValue.set("Moving Average: " + closeAvg);
        outValue.set("" + mean + "," +  (mean+stdDv*2) + "," + (mean-stdDv*2) + "," + stdDv);
				output.collect(outKey, outValue);

				slidingWindow.slideWindowForward();
			}
		}
    //outKey.set("");
		//outValue.set(""); //empty line
		//output.collect(outKey, outValue);

	}

}
