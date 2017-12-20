/**
  * Created by mac on 3/16/16.
  */

import org.apache.spark.graphx._
import org.apache.spark.graphx.lib.LabelPropagation
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.graphstream.graph.implementations.{AbstractEdge, SingleGraph, SingleNode}

/**
  * 顶点分组并可视化.
  */
object CommunityDetection extends App {

  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("CommunityDetection")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar/SparkTest.jar")

  case class Person(name: String, sex: String)

  case class Link(relationship: String, happenDate: String)

  /**
    * create a graph from files which have specified form
    *
    * @param vertexFilePath file path of vertexs.csv
    * @param edgeFilePath   file path of edges.csv
    * @return
    */
  def createGraph(vertexFilePath: String, edgeFilePath: String): Graph[Person, Link] = {

    val vertices = sc.textFile(vertexFilePath)
    val links = sc.textFile(edgeFilePath)
    //构建边、顶点RDD

    val verticesRDD: RDD[(VertexId, Person)] = vertices map { line
    =>
      val row = line split ','
      (row(0).toLong, Person(row(1), row(2)))
    }

    val linksRDD: RDD[Edge[Link]] = links map { line =>
      val row = line split ','
      Edge(row(0).toLong, row(1).toLong, Link(row(2), row(3)))
    }

    //构建图
    val social: Graph[Person, Link] = Graph(verticesRDD, linksRDD)
    return social

  }

  /**
    * the main graph
    */
  var graph: Graph[Person, Link] = createGraph("/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv", "/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")
  graph.cache()
  /**
    * neighbours' vertexId of each vertexId
    */
  val neighbors: RDD[(VertexId, Array[VertexId])] =
    graph.collectNeighborIds(EdgeDirection.Either) //VertexId是Long的别名
  neighbors.cache()

  val labelGraph = LabelPropagation.run(graph, 20)
  val vv = labelGraph.vertices.collect()
  vv.foreach(x => println("顶点ID:" + x._1 + " 标签:" + x._2))

  val v = labelGraph.vertices
  val vArray = v.map(x => x._2)
  val max = vArray.max()
  val min = vArray.min()
  val delt = max - min


  /**
    * Visualize
    */
  val graphStream: SingleGraph = new SingleGraph("CommunityDetection");

  // Set up the visual attributes for graph visualization
  graphStream.addAttribute("ui.stylesheet", "url(./style/stylesheet.css)")
  graphStream.addAttribute("ui.quality") //to enable slower but better rendering.
  graphStream.addAttribute("ui.antialias") //to enable anti-aliasing(抗锯齿或边缘柔化) of shapes drawn by the viewer

  // load the graphX vertices into GraphStream
  for ((id, person: Person) <- graph.vertices.collect()) {
    val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
    node.addAttribute("ui.label", person.name)
  }
  for ((id, label) <- labelGraph.vertices.collect()) {
    val node = graphStream.getNode(id.toString).asInstanceOf[SingleNode]
    val x: java.lang.Double = (label - min) / delt.toDouble
    node.setAttribute("ui.color", x)
  }
  //Load the graphX edges into GraphStream edges
  graph.edges.collect().foreach(println)
  for (Edge(x, y, link: Link) <- graph.edges.collect()) {
    val edge = graphStream.addEdge(x.toString ++ y.toString,
      x.toString, y.toString,
      true).
      asInstanceOf[AbstractEdge]
    edge.addAttribute("ui.label", link.relationship)
  }

  graphStream.display()


  //  def vertexesCluster(graph:Graph[Person,Link], num:Int):(Graph[VertexId,Link],Array[(VertexId,Long)])

}
