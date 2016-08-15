package wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCount extends Configured implements Tool {
	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		public void map(LongWritable lineOffSet, Text record, Context context)
				throws IOException, InterruptedException {
			for (String str : record.toString().split(" "))
				context.write(new Text(str), new LongWritable(1));
		}
	}

	public int run(String[] arg0) throws Exception {
		Job job = Job.getInstance(getConf());
		job.setJarByClass(getClass());

		FileInputFormat.setInputPaths(job, new Path(arg0[0]));

		job.setMapperClass(WordCountMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setReducerClass(LongSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		// TODO Auto-generated method stub
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new WordCount(), args));
	}
}
