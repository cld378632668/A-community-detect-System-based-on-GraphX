import org.apache.spark.{SparkContext, SparkConf}
import org.graphstream.graph.implementations.{SingleGraph, AbstractEdge, SingleNode}

/**
  * Created by mac on 3/25/16.
  */

object NLayerNetworkTest extends App{
  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("AnalysisTest")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
  //  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/VisualizeTest/SparkTest.jar")
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar8/SparkTest.jar")
  //  (conf,sc) = Configure.setConfAndCotext("VisualizeTest","VisualizeTest")

  case class Person(name:String, sex:String)
  case class Link(relationship:String, happenDate:String)

  //构建图计算对象
  val graph = Create.createGraph(sc,"/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv","/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")
  //构建原始图可视化对象
  val graphStream = Visualize.displayGraphWithName (graph,"原图展示(姓名)")
  //显示
//  graphStream.display()



  //计算N层邻居,返回N层邻居所构成的子图
  val subgraph =NLayerNetwork.nLayerNetwork(graph,3,78000000000008000L)
  //控制台输出
  println("顶点78000000000008000L的3层邻居是:")
  subgraph.vertices.collect().foreach(println)

  //可视化,显示整张图,并把子图和中心点区分显示
  Visualize.subgraphMark(graphStream,subgraph,78000000000008000L)


  //仅显示子图
  //Visualize.displayGraphWithIdAndName(subgraph,"子图")

}
