package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieRatingCount {

  public static class JoinCountMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private Map<String, String> movieGenres = new HashMap<>();
    private final static IntWritable one = new IntWritable(1);
    private Text outKey = new Text();

    @Override
    protected void setup(Context context) throws IOException {
      // 从 Distributed Cache 加载 movies.dat
      URI[] cacheFiles = context.getCacheFiles();
      if (cacheFiles != null && cacheFiles.length > 0) {
        for (URI uri : cacheFiles) {
          Path path = new Path(uri.getPath());
          if (path.getName().equals("movies.dat")) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path.getName()))) {
              String line;
              while ((line = reader.readLine()) != null) {
                // movies.dat 格式：MovieID::Title::Genres
                String[] parts = line.split("::");
                if (parts.length == 3) {
                  String movieId = parts[0];
                  String genres = parts[2];
                  movieGenres.put(movieId, genres);
                }
              }
            }
          }
        }
      }
    }

    @Override
    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      // ratings.dat 格式：UserID::MovieID::Rating::Timestamp
      String[] parts = value.toString().split("::");
      if (parts.length == 4) {
        String movieId = parts[1];
        String genres = movieGenres.getOrDefault(movieId, "UNKNOWN");
        // key = movieId\tgenres
        outKey.set(movieId + "\t" + genres);
        context.write(outKey, one);
      }
    }
  }

  public static class SumReducer
      extends Reducer<Text, IntWritable, Text, IntWritable> {

    private IntWritable result = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Usage: MovieRatingCount <movies.dat> <ratings_input> <output>");
      System.exit(2);
    }
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Movie Rating Count with Join");
    job.setJarByClass(MovieRatingCount.class);

    // 1) 把 movies.dat 加到 Distributed Cache
    job.addCacheFile(new Path(args[0]).toUri());

    // 2) 设置 Mapper/Reducer
    job.setMapperClass(JoinCountMapper.class);
    job.setReducerClass(SumReducer.class);
    job.setCombinerClass(SumReducer.class);

    // 3) 设置输出类型
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    // 4) 输入ratings路径 & 输出路径
    FileInputFormat.addInputPath(job, new Path(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(args[2]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
