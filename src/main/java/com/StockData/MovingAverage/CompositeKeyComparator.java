package com.StockData.MovingAverage;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CompositeKeyComparator extends WritableComparator {

	protected CompositeKeyComparator() {
		super(TimeSeriesKey.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {

		TimeSeriesKey ip1 = (TimeSeriesKey) w1;
		TimeSeriesKey ip2 = (TimeSeriesKey) w2;

		int cmp = ip1.getGroup().compareTo(ip2.getGroup());
		if (cmp != 0) {
			return cmp;
		}

		return ip1.getTimeStamp() == ip2.getTimeStamp() ? 0 : (ip1
				.getTimeStamp() < ip2.getTimeStamp() ? -1 : 1);

	}

}
