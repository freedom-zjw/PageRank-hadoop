import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Kmeans {
	private int k; // �ֳɶ��ٴ�
	private int step; // ��������
	private int dataNum; // ���ݸ���
	private ArrayList<float[]> dataSet; // ���ݼ�����
	private ArrayList<float[]> centroids; // ��������
	private ArrayList<float[]> Lastcentroids;//��һ�ε�����
	private ArrayList<ArrayList<float[]>> cluster; // �ֳɵĴ�
	private Random random;

	// ��ʼ������,�����ݼ������ѡȡK����
	private ArrayList<float[]> initCentroids() {
		ArrayList<float[]> NewCentroids = new ArrayList<float[]>();
		int[] randoms = new int[k];
		int temp = random.nextInt(dataNum);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

		map.put(temp, 1);
		randoms[0] = temp;
		for (int i = 1; i < k; i++) {
			temp = random.nextInt(dataNum);
			while (map.containsKey(temp))
				temp = random.nextInt(dataNum);
			map.put(temp, 1);
			randoms[i] = temp;
		}
		for (int i = 0; i < k; i++)
			NewCentroids.add(dataSet.get(randoms[i]));

		return NewCentroids;
	}

	// ��������ŷ����þ���
	private float getDis(float[] x, float[] y) {
		float deltaX = x[0] - y[0];
		float deltaY = x[1] - y[1];
		return deltaX * deltaX + deltaY * deltaY;
	}

	// ���࣬��ÿ����ŵ�������������������Ĵ���
	private void clusterSet() {
		float[] dis = new float[k];
		float minDistance = 0.0f;
		int nearstCluster = 0;
		for (int i = 0; i < dataNum; i++) {
			for (int j = 0; j < k; j++) {
				dis[j] = getDis(dataSet.get(i), centroids.get(j));
				if (j == 0) {
					minDistance = dis[j];
					nearstCluster = j;
				} else {
					if (dis[j] < minDistance) {
						minDistance = dis[j];
						nearstCluster = j;
					} else if (dis[j] == minDistance) {
						// ������������ѡ��һ���ؼ���
						if (random.nextInt(10) < 5)
							nearstCluster = j;
					}
				}
			}
			cluster.get(nearstCluster).add(dataSet.get(i));
		}
	}

	// �жϵ�ǰ���� �Ƿ� ����һ�ε�������ȫһ��
	private boolean finishClustering() {
		for (int i = 0; i < k; i++) {
			boolean flag = false;
			float[] A = centroids.get(i); 
			for (int j = 0; j < k; j++) {
				float [] B  = Lastcentroids.get(j);
				if (B[0] == A[0] && B[1] == A[1]) {
					flag = true;
					break;
				}
			}
			if (flag == false)return false;
		}
		return true;
	}

	// �����µ��ʵ� ����ÿ�����е� �� ƽ������
	private void setNewCentroids() {
		for (int i = 0; i < k; i++) {
			int n = cluster.get(i).size();
			if (n != 0) {
				float[] NewCentroids = { 0, 0 };
				for (int j = 0; j < n; j++) {
					NewCentroids[0] += cluster.get(i).get(j)[0];
					NewCentroids[1] += cluster.get(i).get(j)[1];
				}
				// ����һ��ƽ��ֵ
				NewCentroids[0] = NewCentroids[0] / n;
				NewCentroids[1] = NewCentroids[1] / n;
				centroids.set(i, NewCentroids);
			}
		}
	}

	// ���캯��������Ҫ�ֳɶ��ٸ�cluster
	public Kmeans(int k) {
		this.k = k > 0 ? k : 1;
	}

	// ����ԭʼ���ݼ�
	public void setDataSet(ArrayList<float[]> dataSet) {
		this.dataSet = dataSet;
	}

	// ��÷�����
	public ArrayList<ArrayList<float[]>> getCluster() {
		return cluster;
	}

	// ��ʼ��
	public void init() {
		step = 0;
		random = new Random();
		if (dataSet == null || dataSet.size() == 0) {
			// ���ݼ�����Ϊ�գ�û����������һ�����õġ�
			dataSet = new ArrayList<float[]>();
			float[][] dataSetArray = new float[][] { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 }, { 3, -1 },
					{ 5, -8 }, { 4, -1 }, { 3, 1 }, { 2, 7 }, { 8, -3 }, { 4, 0 }, { 0, 2 }, { 7, 4 }, { -2, -1 } };
			for (int i = 0; i < dataSetArray.length; i++)
				dataSet.add(dataSetArray[i]);
		}
		dataNum = dataSet.size();
		k = k > dataNum ? dataNum : k; // �ֵĴ��������ܱ����ݵ�������
		centroids = initCentroids(); // �����ʼ������
		Lastcentroids = new ArrayList<float[]>();
		for (int i = 0; i < k; i++)
			Lastcentroids.add(centroids.get(i));
		cluster = new ArrayList<ArrayList<float[]>>();  
		for (int i = 0; i < k; i++) // ��ʼ��K���մ�
			cluster.add(new ArrayList<float[]>());
	}

	// ִ��K-means�㷨
	public void execute() {
		System.out.println("Kmeans begins...");
		long startTime = System.currentTimeMillis();
		
		/////////// k-means �㷨����

		init(); // ��һ����ʼ��
		while (true) {
			clusterSet();
			if (step > 0) {
				// �����Ĳ��ٱ��ˣ��������
				if ( finishClustering())break;
			}	
			for (int i = 0; i < k; i++) Lastcentroids.set(i, centroids.get(i));
			step++;
			setNewCentroids();
			cluster.clear();
			for (int i = 0; i < k; i++)
				cluster.add(new ArrayList<float[]>());
		}

		//////////////////////

		long endTime = System.currentTimeMillis();
		System.out.println("Time used: " + (endTime - startTime) + "ms");
		System.out.println("Kmeans ends");
	}

	public void printDataArray(ArrayList<float[]> dataArray, String dataArrayName) {
		try {
			OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream("Output.txt"),"UTF-8");
			BufferedWriter bw = new BufferedWriter(output);
			for (int i = 0; i < dataArray.size(); i++) {
					bw.write(dataArrayName + "[" + i + "]={" + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "}");
					bw.newLine();
			}
			bw.flush();  
			bw.close();
			output.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}

}
