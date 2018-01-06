import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PRReducer extends Reducer<Text, Text, Text, Text> {
    //对于一个页面KEY，计算其新的PR值
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
       
        Configuration conf = context.getConfiguration();
        double damping = Double.parseDouble(conf.get("damping"));//阻尼系数
        int num = Integer.parseInt(conf.get("num"));//网站数量
        
        Map<String, Double> PR = new HashMap<String,Double>();
        Map<String, Double> M = new HashMap<String,Double>();
        
        String nextPage = "";
        for (Text val : values) {
            String[] line = val.toString().split(":");
            if(line[0].compareTo("nextPage") == 0){
                //取出当前页可跳转的页面方便后面按照mapper输入文件格式输出
                nextPage = line[1];
            }
            else {
                String[] tmp = line[1].split(" ");
                double p = Double.parseDouble(tmp[1]);
                if(line[0].compareTo("PR") == 0){ //取出tmp的PR值
                    PR.put(tmp[0], p);
                }else if(line[0].compareTo("M") == 0){//取出从tmp转移到当前页的转移概率
                    M.put(tmp[0], p);
                }
            }
        }
        double sum=0;
        for(Map.Entry<String,Double> entry: PR.entrySet()){
            sum += M.get(entry.getKey()) * entry.getValue(); //M*PR
        }
        double newPR = damping*sum + (1-damping)/num; //根据公式计算当前页的新PR值

        System.out.println("    ");
        System.out.println(String.valueOf(key) + "'s New PageRank is " + String.valueOf(newPR));
        /*
            按照MAPPER中的输入格式输入
            当前页 当前页的PR值：可跳转页1，可跳转页2，....
        */
        context.write(key, new Text(String.valueOf(newPR)+":"+nextPage));
    }
}