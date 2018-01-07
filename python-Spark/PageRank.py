from  pyspark import SparkContext, SparkConf


def calc(x):
    # x = (page, (links, rank))
    links = x[1][0]
    PageRank = x[1][1]
    links = links.split(",")
    n = len(links)
    result = []
    for x in links:
        result.append((x, PageRank*1.0/n))  # M[i][j]*PR[j]
    return result


if __name__ == "__main__":

    sc = SparkContext("local", "PR")
    inputfile = "/sparkinput/b.txt"
    inputData = sc.textFile(inputfile)
    link = inputData.map(lambda x: (x.split(":")[0], x.split(":")[1])) # links
    n = inputData.count()  # get number of web pages
    PageRank = link.mapValues(lambda x: 1.0/n)  # initial PR value

    for i in range(20):
        PageRank = link.join(PageRank).flatMap(calc)
	PageRank = PageRank.reduceByKey(lambda x, y: x+y)
	PageRank = PageRank.mapValues(lambda x: 0.15/n + 0.85*x)
    print PageRank.collect()

