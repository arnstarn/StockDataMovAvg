package com.StockData.MovingAverage;

import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class MovingAverageMapper extends MapReduceBase implements
		Mapper<LongWritable, Text, TimeSeriesKey, TimeSeriesData> {

	static enum Parse_Counters {
		BAD_PARSE
	};

	private JobConf configuration;
	private final TimeSeriesKey key = new TimeSeriesKey();
	private final TimeSeriesData val = new TimeSeriesData();
	private String filename;

	private static final Logger logger = Logger
			.getLogger(MovingAverageMapper.class);

	//public void close() {}

	public void configure(JobConf conf) {
		configuration = conf;
		filename = conf.get("map.input.file");
	}

	@Override
	public void map(LongWritable inkey, Text value,
			OutputCollector<TimeSeriesKey, TimeSeriesData> output,
			Reporter reporter) throws IOException {

		String line = value.toString();
		String symbol = FilenameUtils.removeExtension(new File(filename).getName());

		StockData record = StockData.parse("S&P500," + symbol + "," + line.trim());

		if (record != null) {

			// set both parts of the key
			key.set(record.getSymbol(), record.getDate());

			StringBuilder sb = new StringBuilder();
			sb.append(record.getOpen().replaceAll("[^\\d.+-]", "")+",");
			sb.append(record.getHigh()+",");
			sb.append(record.getLow()+",");
			sb.append(record.getClose()+",");
			sb.append(record.getVolume()+",");
			sb.append(record.getAdjClose());

			val.setValue(sb.toString());
			val.setDateTime(record.getDate());

			// now that its parsed, we send it through the shuffle for sort,
			output.collect(key, val);

		} else {
			reporter.incrCounter(Parse_Counters.BAD_PARSE, 1);
		}

	}

}
