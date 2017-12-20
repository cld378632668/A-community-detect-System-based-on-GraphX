import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by mac on 3/25/16.
  * Visualize的测试
  */
object VisualizeTest extends App{
  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("VisualizeTest")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
//  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/VisualizeTest/SparkTest.jar")
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar7/SparkTest.jar")
//  (conf,sc) = Configure.setConfAndCotext("VisualizeTest","VisualizeTest")

  val graph = Create.createGraph(sc,"/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv","/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")

  val graphStream = Visualize.displayGraphWithIdAndName(graph,"全图展示(ID和姓名)")
//  Visualize.displayGraphWithName (graph,"全图展示(姓名)")
//  Visualize.displayGraphWithNameAndLink(graph,"全图展示(姓名和关系)")
//  val subgraph =Analysis.nLayerNetwork(graph,3,78000000000008000L)

  val subgraph =NLayerNetwork.nLayerNetwork(graph,2,66000000000006689L)
  Visualize.subgraphMark(graphStream,subgraph,66000000000006689L)


}
