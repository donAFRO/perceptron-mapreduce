/*
 *  Copyright (C) 2015 Karl R. Wurst
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Recognize extends Perceptron {

    private static String templateFilename;
    // replace with configuration variable in context.

        public static class RecognizerMapper
	    extends Mapper<Object, Text, IntWritable, Text> {

	    private Text letter = new Text();

	    public void setup(Context context) {
		try {
		    FileInputStream fileIn = new FileInputStream(templateFilename);
		    ObjectInputStream in = new ObjectInputStream(fileIn);
		    templates = (Letter[]) in.readObject();
		    in.close();
		    fileIn.close();
		} catch(IOException i) {
		    i.printStackTrace();
		    return;
		} catch(ClassNotFoundException c) {
		    System.out.println("Letter[] class not found");
		    c.printStackTrace();
		    return;
		}
	    }

	    public void map(Object key, Text value, Context context)
		throws IOException, InterruptedException {

		String line = value.toString();
		Letter pattern = Letter.getNewLetterFromRecognitionPatternString(line);
		int guess = guess(pattern);
		letter.set(templates[guess].getLetter());
		int offset = Integer.parseInt(line.substring(SIZE, line.length()));
		context.write(new IntWritable(offset), letter);
	    }
	}

        public static class RecognizerReducer
	    extends Reducer<IntWritable, Text, IntWritable, Text> {

	    public void reduce(IntWritable key, Iterable<Text> values,
			                       Context context
			       ) throws IOException, InterruptedException {
		for (Text val : values) {
		    context.write(val);
		}
	    }
	}

    public static void main(String[] args) {
	Configuration conf = new Configuration();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	if (otherArgs.length < 3) {
	    System.err.println("Usage: Recognize <in> <templates> <out>");
	    System.exit(2);
	}

	Job job = new Job(conf, "perceptron recognize");
	job.setJarByClass(Perceptron.class);
	job.setMapperClass(RecognizerMapper.class);
	job.setReducerClass(RecognizerReducer.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);

	FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	templateFilename = otherArgs[1];
	FileOutputFormat.setOutputPath(job,
				       new Path(otherArgs[otherArgs.length - 1]));

	System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
