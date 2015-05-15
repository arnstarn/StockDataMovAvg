package com.StockData.MovingAverage;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TimeSeriesKey implements WritableComparable<TimeSeriesKey> {

	private String group = "";
	private long timeStamp = 0;

	public void set(String strGroup, long longTimeStamp) {
		group = strGroup;
		timeStamp = longTimeStamp;
	}

	public String getGroup() {
		return this.group;
	}

	public long getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		group = in.readUTF();
		timeStamp = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(group);
		out.writeLong(this.timeStamp);
	}

	@Override
	public int compareTo(TimeSeriesKey timeSeriesKey) {
		if (this.group.compareTo(timeSeriesKey.group) != 0) {
			return this.group.compareTo(timeSeriesKey.group);
		} else if (this.timeStamp != timeSeriesKey.timeStamp) {
			return timeStamp < timeSeriesKey.timeStamp ? -1 : 1;
		} else {
			return 0;
		}

	}

	public static class TimeSeriesKeyComparator extends WritableComparator {
		public TimeSeriesKeyComparator() {
			super(TimeSeriesKey.class);
		}

		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			return compareBytes(b1, s1, l1, b2, s2, l2);
		}
	}

	static { // register this comparator
		WritableComparator.define(TimeSeriesKey.class,
				new TimeSeriesKeyComparator());
	}

}
