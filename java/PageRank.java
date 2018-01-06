import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class PageRank {
    
    public static void main(String[] args) throws Exception {
        double damping=0.85;
        ArrayList<String> ouputList=new ArrayList<String>();
        Configuration conf1 = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf1, args).getRemainingArgs();
            if (otherArgs.length != 3) {
                System.err.println("Usage: PageRank <num> <in> <out>");
                System.exit(2);
            }
        if (check(otherArgs[0]) == false){
            System.err.println("Please use interger as num");
            System.exit(2);
        }
        String num = otherArgs[0];
        String input = otherArgs[1];
        String OriOutput = otherArgs[2];
        String output = otherArgs[2] + "1";

        for(int i = 1; i <= 20; i++){
            Configuration conf = new Configuration();
            conf.set("num", num);
            conf.set("damping", String.valueOf(damping));
            Job job = Job.getInstance(conf, "PageRank");
            job.setJarByClass(PageRank.class);
            job.setMapperClass(PRMapper.class);
            job.setReducerClass(PRReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            FileInputFormat.setInputPaths(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output));
            input = output;
            ouputList.add(output);
            output = OriOutput + String.valueOf(i+1);
            
            System.out.println("the "+i+"th step is finished");
            job.waitForCompletion(true);
        }

        for(int i=0;i<ouputList.size()-1;i++){ //删除前面的输出文件夹只保留最后一次的
            Configuration conf=new Configuration();
            Path path=new Path(ouputList.get(i));
            FileSystem fs=path.getFileSystem(conf);
            fs.delete(path,true);
        }

    }

    public static boolean check(String str) {
        for (int i = str.length();--i>=0;){  
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}