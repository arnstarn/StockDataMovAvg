package com.StockData.MovingAverage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StockData {

	private String exchange;
	private String symbol = "";
	private long date = 0;
	private String open = "";
	private String high = "";
	private String low = "";
	private String close = "";
	private String volume = "";
	private String adjClose = "";

	// public String segment = ""; // lookup

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

	private static final String DATE_FORMAT_YYMM = "yyyyMM";

	private static SimpleDateFormat sdf_ym = new SimpleDateFormat(
			DATE_FORMAT_YYMM);

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(String adjClose) {
		this.adjClose = adjClose;
	}

	public String getYearMonth() {
		return sdf_ym.format(this.date);
	}

	public String getDateString() {
    return sdf.format(this.date);
	}

	public float getClose() {
		// System.out.println( "close: " + this.close );
		return Float.parseFloat(this.close);
	}

	public float getAdjCloseFloat() {
		// System.out.println( "close: " + this.close );
		return Float.parseFloat(this.adjClose);
	}

	public static StockData parse(String line) {

		StockData stockData = new StockData();

		String[] values = line.split(",");

		if (values.length != 9) {
			return null;
		}

		stockData.exchange = values[0].trim();
		stockData.symbol = values[1].trim();

		String stockDate = values[2].trim();

		try {
      stockData.date = sdf.parse(stockDate.replaceAll("\\p{Cntrl}", "")).getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		stockData.open = values[3].trim();
		stockData.high = values[4].trim();
		stockData.low = values[5].trim();
		stockData.close = values[6].trim();
		stockData.volume = values[7].trim();
		stockData.adjClose = values[8].trim();

		return stockData;

	}

}
