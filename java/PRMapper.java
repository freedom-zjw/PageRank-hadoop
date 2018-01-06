import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PRMapper extends Mapper<LongWritable, Text, Text, Text>{
    //mapper的作用是从输入文件中获取转移矩阵的转移概率以及当前PR值
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
            /*
                当前页i PRi: 可跳转页1，可跳转页2, ... 
            */
            //将输入按：切开，line[0] = 当前页 Vn  line[1] = 可跳转页1，可跳转页2, ... 
            String[] line = value.toString().split(":"); 
            String nextPage = line[1]; 
            int N = nextPage.split(",").length; //当前页有N个可跳转页
            String nowPage = line[0].split("\\s+")[0];
            String PR = line[0].split("\\s+")[1];
            context.write(new Text(nowPage), new Text("nextPage:"+nextPage));//当前页可跳转的页
            for(String subpage:nextPage.split(",")){
                //转移矩阵中从nowPage跳转到一个subpage的概率
                context.write(new Text(subpage), new Text("M:"+nowPage+" "+String.valueOf(1.0/N))); 
                //当前subpage的PR值
                context.write(new Text(subpage), new Text("PR:"+nowPage+" "+PR));
            }        
    }
}