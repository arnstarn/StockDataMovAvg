package com.StockData.MovingAverage;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class NaturalKeyGroupingComparator extends WritableComparator {

	protected NaturalKeyGroupingComparator() {
		super(TimeSeriesKey.class, true);
	}

	@Override
	public int compare(WritableComparable o1, WritableComparable o2) {

		TimeSeriesKey tsK1 = (TimeSeriesKey) o1;
		TimeSeriesKey tsK2 = (TimeSeriesKey) o2;

		return tsK1.getGroup().compareTo(tsK2.getGroup());

	}

}
