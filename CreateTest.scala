import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by mac on 3/16/16.
  */
object CreateTest extends App{

    /**
  * configuration of spark
    */
  val conf = new SparkConf().setAppName("Main")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar6/SparkTest.jar")

  Create.createGraph(sc,"/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv","/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")



}
