# PageRank

###Environment

 	1. install jdk 
 	2. install hadoop
 	3. install spark on yarn

### run

* Assume that you add the hadoop/bin to environment path 

  than use the flow commend to run

  `hadoop jar PageRank.jar Pagerank numberOfWebPages inputdir outputdir`

  the result is at      `outputdir20`

  * example:

    * inputdata format (0.25 is 1/ numberOfWebPages， numberOfWebPages is 4 in here): 

      ~~~
      A 0.25:B,C,D
      B 0.25:A,D
      C 0.25:A
      D 0.25:B,C
      ~~~

      input the above data into `a.txt`

    * make directory on hdfs

      ~~~shell
      hdfs dfs -mkdir /PageRank_input
      ~~~

    * put a.txt to input directory

      ~~~shell
      hdfs dfs -put a.txt /PageRank_input
      ~~~

    * than you can run the program as：

      ~~~~shell
      hadoop jar PageRank.jar Pagerank 4 /PageRank_input /PageRank_Output/output
      ~~~~

    * the result will be at `/PageRank_Output/output20` on hdfs, use the following commend to get it

      ~~~shell
      hdfs dfs -cat /PageRank_Output/output20/*
      ~~~

       

* Assume that you add the spark/bin to environment path

  modify the input path at code in row 19

  example: `inputfile = "/sparkinput/b.txt"`  the input path is on hdfs acquiescently

  than run the code as：

  `pyspark PageRank.py > result.txt`

  the result will be found in `result.txt`

  * example

    * dataformat b.txt

      ~~~
      A:B,C,D
      B:A,D
      C:A
      D:B,C
      ~~~

    * make directory on hdfs

      ```shell
      hdfs dfs -mkdir /sparkinput
      ```

    * put b.txt to input directory

      ```shell
      hdfs dfs -put b.txt /sparkinput
      ```

    * modify the input path in PageRank.py, row 19：

      ```shell
      inputfile = "/sparkinput/b.txt"
      ```

    * than run the code as:

      ```shell
      pyspark PageRank.py > result.txt
      ```

    * the result will be found in `result.txt`

  ​

  ​