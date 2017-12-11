import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Test {

	public static void main(String[] args) {
		Kmeans k = new Kmeans(34);
		ArrayList<float[]> dataSet=new ArrayList<float[]>();  
		//System.out.println(System.getProperty("user.dir"));
		try {
			Scanner sc = new Scanner(new File("In.txt"));
			while (sc.hasNextLine()) {
				String s = sc.nextLine().trim();
				String[] sa = s.split(" ");
				float x = (float) Integer.valueOf(sa[0]);
				float y = (float) Integer.valueOf(sa[1]);
				//System.out.println(x + " " + y);
				dataSet.add(new float[] {x,y});
			}
			sc.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		/*	
	    for (int i = 0; i < 200000; i++) {  
	        int x = (int)(Math.random() * 600) + 1;  
	        int y = (int)(Math.random() * 400) + 1;  
	        dataSet.add(new float[]{x,y}); 
	    } */
        //����ԭʼ���ݼ�  
        k.setDataSet(dataSet);  
        //ִ���㷨  
        k.execute();  
        //�õ�������  
        ArrayList<ArrayList<float[]>> cluster=k.getCluster();  
        //*�鿴���
        
        for(int i=0;i<cluster.size();i++)  
        {  
            k.printDataArray(cluster.get(i), "cluster["+i+"]");  
        } 
         
	}

}
