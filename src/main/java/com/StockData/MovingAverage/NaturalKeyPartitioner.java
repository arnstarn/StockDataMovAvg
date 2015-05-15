package com.StockData.MovingAverage;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

public class NaturalKeyPartitioner implements
		Partitioner<TimeSeriesKey, TimeSeriesData> {

	@Override
	public int getPartition(TimeSeriesKey key, TimeSeriesData value,
			int numPartitions) {
		return Math.abs(key.getGroup().hashCode() * 127) % numPartitions;
	}

	@Override
	public void configure(JobConf arg0) {
		// TODO Auto-generated method stub

	}
}
