package com.StockData.MovingAverage;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class TimeSeriesData implements Writable,
		Comparable<TimeSeriesData> {

	private long dateTime;
	private String value;


	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

	public void readFields(DataInput in) throws IOException {
		dateTime = in.readLong();
		value = in.readLine();
	}

	public static TimeSeriesData read(DataInput in) throws IOException {
		TimeSeriesData p = new TimeSeriesData();
		p.readFields(in);
		return p;
	}

  public void setValue(String value){
    this.value = value;
  }

  public String getValue(){
    return value;
  }

  public void setDateTime(long dateTime){
    this.dateTime = dateTime;
  }

  public long getDateTime(){
    return dateTime;
  }

	public String getDate() {
		return sdf.format(this.dateTime);
	}

	public void copy(TimeSeriesData source) {
		dateTime = source.dateTime;
		value = source.value;

	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(dateTime);
		out.writeUTF(value);
	}

	@Override
	public int compareTo(TimeSeriesData oOther) {
		if (this.dateTime < oOther.dateTime) {
			return -1;
		} else if (this.dateTime > oOther.dateTime) {
			return 1;
		}

		// default -- they are equal
		return 0;
	}

}
