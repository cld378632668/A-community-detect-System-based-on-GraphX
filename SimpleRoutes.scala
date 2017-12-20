import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, Graph, VertexId, EdgeDirection}
import org.apache.spark.rdd.RDD
import org.graphstream.graph.implementations.{AbstractEdge, SingleNode, SingleGraph}

import scala.collection.{immutable, mutable}

/**
  * Created by mac on 3/15/16.
  */
object SimpleRoutes extends App {

  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("PeopleCorrelationAnalysis")
  conf.setMaster("spark://MacdeMacBook-Pro-3.local:7077")
  val sc = new SparkContext(conf)
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/SparkTest_jar5/SparkTest.jar")

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
  var social:Graph[Person,Link] = createGraph("/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/vertexs.csv","/Users/mac/Documents/GraphXSurvey/GraphX/SocialNetwork/edges.csv")
  social.cache()
  /**
    * neighbours' vertexId of each vertexId
    */
  val neighbors:RDD[(VertexId,Array[VertexId])] =
    social.collectNeighborIds(EdgeDirection.Either) //VertexId是Long的别名
  neighbors.cache()


  var stack = immutable.Stack[Long]() //存储当前路径
  var result = immutable.List[Array[Long]]() //存储所有路径
  var resultVertexSet = immutable.Set[Long]() //result中所有的点

  /**
    * 显示一条路径
    *
    * @param array 存储路径的数组
    */
  def showPath(array:Array[Long]): Unit ={
    for(i <- 0 to array.length - 1){
      if(i == array.length - 1 )
        println(array(i))
      else
        print(array(i) + "-->")
    }
  }

  /**
    * 显示所有路径
    */
  def showAllRoutes(): Unit ={
    println("所有的简单路径是:")
    for(i <- result)
      showPath(i)
  }

  /**
    * 将result中的所有点存入一个集合resultVertexSet中,以便于可视化时作样式区分
    */
  def getVertexes(): Unit ={
    for(array <- result){
      for(i <- array )
        resultVertexSet = resultVertexSet+i
    }

  }


  def simpleRoutesBetween(src:Long, dest:Long): List[Array[Long]] ={
    if(src == dest)
      println("src == dest!")
      else{
        stack = stack.push(src)
        /*get the array of the src's neighbours*/
        val neighbourArray = neighbors.lookup(src)(0)
        for(id <- neighbourArray){
          var flag = true
          if(id == dest){
            stack = stack.push(id)
            result = result :+ stack.toArray.reverse
//            showPath(stack.toArray.reverse)//show the path
            stack = stack.pop
            flag = false
          }
          else if(flag  && !stack.toArray.contains(id) ){/*环路检查*/
            simpleRoutesBetween(id,dest)
          }
          else{
//            println("构成环路,尝试下一个邻居节点.")
          }
        }
        /*回溯前还原现场*/
        stack = stack.pop //"当前节点"+src+"的所有邻居节点都已尝试,当前节点出栈,回退到上一个节点"

    }
    result
  }

  /**
    * debug用的函数
    * @param src
    * @param dest
    * @return
    */
  def simpleRoutesBetweenPrint(src:Long, dest:Long): List[Array[Long]] ={
    if(src == dest)
      println("src == dest!")
    else{
      stack = stack.push(src)
      /*get the array of the src's neighbours*/
      val neighbourArray = neighbors.lookup(src)(0)
      /**/
      print("当前路径是"); showPath(stack.toArray.reverse);
      print(src+"的邻居节点有")
      neighbourArray.foreach(x => print(x + ","))
      println()
      /**/
      for(id <- neighbourArray){
        /**/
        print("当前路径是"); showPath(stack.toArray.reverse);
        print(src+"下一个邻居节点是"+id+",")
        var flag = true
        if(id == dest){
          println("到达终点96000000000009876L.")
          stack = stack.push(id)
          result = result :+ stack.toArray.reverse
          print("一条完整路径:")
          showPath(stack.toArray.reverse)//show the path
          stack = stack.pop
          flag = false
        }
        else if(flag  && !stack.toArray.contains(id) ){/*环路检查*/
          println("加入路径,成为新的当前节点.")
          simpleRoutesBetween(id,dest)
        }
        else{
          println("构成环路,尝试下一个邻居节点.")
        }
      }
      println("当前节点"+src+"的所有邻居节点都已尝试,当前节点出栈,回退到上一个节点")
      /*回溯前还原现场*/
      stack = stack.pop

    }
    result
  }

  /**
    * 进行所有简单路径的计算
    */
  simpleRoutesBetween(78000000000008000L,96000000000009876L)
  //  simpleRoutesBetween(67000000000008932L,32000000000000027L)
  /**
    * end of  进行所有简单路径的计算
    */


  /**
    * 控制台输出开始
    */
  /**
    * 在控制台打印所有简单路径
    */
  print("从顶点78000000000008000L到顶点96000000000009876L")
  showAllRoutes()
  /**
    * 将result中的所有点存入一个集合resultVertexSet中,以便于可视化时作样式区分
    */
  getVertexes()

  /**
    * 在控制台打印所有简单路径中涉及的点
    */
  println("所有简单路径上涉及的顶点有:")
  resultVertexSet.foreach(println)
  /**
    * 控制台输出结束
    */


  /**
    * 可视化图,显示顶点ID和名称,resultVertexSet中的顶点区分显示
    * @param graph
    * @return
    */
  def displayGraphWithIdAndName(graph:Graph[Person,Link]):SingleGraph={

    val graphStream:SingleGraph = new SingleGraph("SimpleRoutes");

    // Set up the visual attributes for graph visualization
    graphStream.addAttribute("ui.stylesheet","url(./style/stylesheet.css)")
    graphStream.addAttribute("ui.quality") //to enable slower but better rendering.
    graphStream.addAttribute("ui.antialias") //to enable anti-aliasing(抗锯齿或边缘柔化) of shapes drawn by the viewer

    // Given the egoNetwork, load the graphX vertices into GraphStream
    for ((id,person:Person) <- graph.vertices.collect()) {
      val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
      node.addAttribute("ui.label",id  +"\n"+person.name)
      if(resultVertexSet.contains(id))
        node.addAttribute("ui.class","important")
    }
    // Load the graphX edges into GraphStream edges
    for (Edge(x,y,link:Link) <- graph.edges.collect()) {
      val edge = graphStream.addEdge(x.toString ++ y.toString,
        x.toString, y.toString,
        true).
        asInstanceOf[AbstractEdge]
    }

    graphStream.display()

    graphStream
  }

  /**
    * 调用可视化命令
    */
  displayGraphWithIdAndName(social)

}
