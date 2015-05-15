package com.StockData.MovingAverage;

import java.util.LinkedList;

/**
 * SlidingWindow
 * 
 * Very simple sliding window for timeseries processing
 * 
 * Assumes incoming points were already sorted, throws exception if value is out of order
 *
 * Window is based on the delta between the timestamps on the front and back points in the window, not the number of samples.
 * 
 * 
 * 
 */
public class SlidingWindow {

	LinkedList<TimeSeriesData> currentWindow;
	long windowSize;
	long slideIncrement;
	long currentTime;
	long sampleSize;
	
	public SlidingWindow(long WindowSizeInMS, long SlideIncrement, long sample_size) {
		windowSize = WindowSizeInMS;
		slideIncrement = SlideIncrement;
		currentTime = 0;
		sampleSize = sample_size;
		
		currentWindow = new LinkedList<TimeSeriesData>();
	}
	
	public long getWindowStepSize() {
		return slideIncrement;
	}
	
	public long getWindowSize() {
		return windowSize;
	}
	
	
	public boolean isWindowFull() {
		if (getWindowDelta() >= windowSize)
			return true;
		
		return false;
	}
	
	public long getWindowDelta() {
		if (currentWindow.size() > 0)
			return currentWindow.getLast().getDateTime() - currentWindow.getFirst().getDateTime() + sampleSize;

		return 0;
	}
	
	public void addDataPoint(TimeSeriesData point) throws Exception{
		// look at back of window
		
		// if back of window is greater than this point, throw exception
		if (currentWindow.size() > 0){
			if (point.getDateTime() <= currentWindow.getLast().getDateTime()){
				throw new Exception("Data point is out of order!");
			}
		}
		
		currentWindow.add(point);
	}
	
	public int getNumberPointsInWindow() {
		return currentWindow.size();
	}

	public void slideWindowForward() {
		long lCurrentFrontTS = currentWindow.getFirst().getDateTime(); //.GetCalendar().getTimeInMillis();
		currentTime = lCurrentFrontTS + slideIncrement;
		
		// now burn off the tail
		while (currentWindow.getFirst().getDateTime() < currentTime)
			currentWindow.removeFirst();
	}	

	public LinkedList<TimeSeriesData> getCurrentWindow() {
		return currentWindow;
	}

}
