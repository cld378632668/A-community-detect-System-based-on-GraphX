import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, Graph, VertexId, EdgeDirection}
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Created by mac on 3/3/16.
  */
object PeopleCorrelationAnalysis extends App{
  /**
    * configuration of spark
    */
  val conf = new SparkConf().setAppName("PeopleCorrelationAnalysis")
  conf.setMaster("spark://10.10.18.226:7077")
  val sc = new SparkContext(conf)
  sc.addJar("/Users/mac/Documents/GraphXSurvey/SparkTest/out/artifacts/..")

  case class Person(name:String, sex:String)
  case class Link(relationship:String, happenDate:String)

  /**
    * create a graph from files which have specified form
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

  /**
    * find the n-layer neighbours of specified VertexId
    *
    * @param n the number of layers
    * @param srcId the specified VertexId
    * @return the set of n-layer neighbors
    */
  def findNLayers(n:Int, srcId:Long):mutable.Set[Long] = {

    var temp = Array[Long](srcId);
    val tempSet =  mutable.Set[Long]()
    val result = mutable.Set[Long]()
    val set = Set[Long]()
    var m = 0
    while(m<n){
//      print("temp:")
//      temp.foreach(x => print(x+" "))
//      println("temp.size : " + temp.size )
      for( i <- 0 until temp.size ) {
        //取出temp(i)对应的唯一的Array[VertexId]
        val idsArray = neighbors.lookup(temp(i))(0)
        //把Array中的值全部加入tempSet
        tempSet ++= idsArray.toSet
      }
//      println("tempSet:" + tempSet)
      result ++= tempSet
//      println("result:"+result)
//      println()
      //去重后,赋值给新一轮的temp
      tempSet --= temp
      temp = tempSet.toArray

      tempSet.clear()
      m=m+1
    }

    return result
  }

  println(findNLayers(3,78000000000008000L))

  /**
    * Run PageRank with an error tolerance of 0.0001,and
    * get the top 10 vertices
    */
  def getTop10Vertices() ={
    val ranks = social.pageRank(0.0001).vertices.sortBy(_._2,false).take(10)
    ranks.foreach(x => println(x._1+" "+x._2))
    ranks
  }




}
