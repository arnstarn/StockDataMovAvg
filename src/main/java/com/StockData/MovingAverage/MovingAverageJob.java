package com.StockData.MovingAverage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Arrays;
import java.util.List;

public class MovingAverageJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {

		System.out.println("\nMovingAverageJob\n");

    JobConf conf = new JobConf(getConf(), MovingAverageJob.class);
    conf.setJobName("MovingAverageJob");
    conf.set("mapred.textoutputformat.separator", ",");

    conf.setMapOutputKeyClass(TimeSeriesKey.class);
    conf.setMapOutputValueClass(TimeSeriesData.class);

    conf.setMapperClass(MovingAverageMapper.class);
    conf.setReducerClass(MovingAverageReducer.class);

    conf.setPartitionerClass(NaturalKeyPartitioner.class);
    conf.setOutputKeyComparatorClass(CompositeKeyComparator.class);
    conf.setOutputValueGroupingComparator(NaturalKeyGroupingComparator.class);

    List<String> arguments = Arrays.asList(args);

    if (arguments.size() != 2) {
     System.out.println("ERROR: Wrong number of parameters: " + arguments.size() + " instead of 2.");
    }
		   		
		
		conf.setInputFormat(TextInputFormat.class);

		conf.setOutputFormat(TextOutputFormat.class);
		conf.setCompressMapOutput(true);

		FileInputFormat.setInputPaths(conf, arguments.get(0));
		FileOutputFormat.setOutputPath(conf, new Path(arguments.get(1)));

		JobClient.runJob(conf);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new MovingAverageJob(), args);
		System.exit(res);

	}

}
