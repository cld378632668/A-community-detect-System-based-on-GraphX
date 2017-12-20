/**
  * Created by mac on 3/11/16.
  */

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.graphstream.graph.{Graph => GraphStream}
import org.graphstream.graph.implementations._

/**
  * 显示原始图可视化对象
  */
object GraphStreamTest extends App{
  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("GraphStreamTest")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar3/SparkTest.jar")

  case class Person(name:String, sex:String)
  case class Link(relationship:String, happenDate:String)
  /**
    * create a graph from files which have specified form
    *
    * @param vertexFilePath file path of vertexs.csv
    * @param edgeFilePath file path of edges.csv
    * @return
    */
  def createGraph(vertexFilePath:String, edgeFilePath:String): Graph[Person,Link] ={

    val vertices = sc.textFile(vertexFilePath)
    val links= sc.textFile(edgeFilePath)
    //构建边、顶点RDD

    val verticesRDD: RDD[(VertexId,Person)] = vertices map {line
    =>
      val row = line split ','
      (row(0).toLong,Person(row(1),row(2)))
    }

    val linksRDD:RDD[Edge[Link]] = links map {line =>
      val row = line split ','
      Edge(row(0).toLong, row(1).toLong, Link(row(2), row(3)))
    }

    //构建图
    val social: Graph[Person,Link] = Graph(verticesRDD, linksRDD)
    return social

  }
  /**
    * the main graph
    */
  var graph:Graph[Person,Link] = createGraph("/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv","/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")
  graph.cache()



  //创建原始可视化对象
  val graphStream:SingleGraph = new SingleGraph("GraphStream")

  // 设置graphStream全局属性. Set up the visual attributes for graph visualization
  graphStream.addAttribute("ui.stylesheet","url(./style/stylesheet.css)")
  graphStream.addAttribute("ui.quality")
  graphStream.addAttribute("ui.antialias")


  // 加载顶点到可视化图对象中
  for ((id,person:Person) <- graph.vertices.collect()) {
    val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
    node.addAttribute("ui.label",id  +"\n"+person.name)
  }
  //加载边到可视化图对象中
  for (Edge(x,y,link:Link) <- graph.edges.collect()) {
    val edge = graphStream.addEdge(x.toString ++ y.toString,
      x.toString, y.toString,
      true).
      asInstanceOf[AbstractEdge]

  }
  //显示
  graphStream.display()



//  def subgraphMark(graphStream:SingleGraph,subgraph:Graph[Person,Link]): Unit ={
//    for((id,person:Person)<- subgraph.vertices.collect()){
//      graph.getNode(id.toString).changeAttribute("","")
//    }
//  }

}
