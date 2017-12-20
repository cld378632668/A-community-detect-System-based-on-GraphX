import model.{Link, Person}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, VertexId, Graph}
import org.apache.spark.rdd.RDD

/**
  * Created by mac on 3/24/16.
  */
object Create {

  /**
    * create a graph from files which have specified form
    * @param vertexFilePath file path of vertexs.csv
    * @param edgeFilePath file path of edges.csv
    * @return
    */
  def createGraph(sc:SparkContext ,vertexFilePath:String, edgeFilePath:String): Graph[Person,Link] ={

    val vertices = sc.textFile(vertexFilePath)
    val links= sc.textFile(edgeFilePath)
    //构建边、顶点RDD
    links.foreach(println)
    val verticesRDD: RDD[(VertexId,Person)] = vertices map {line
    =>
      val row = line split ','
      (row(0).toLong,Person(row(1),row(2)))
    }

//    links.collect().foreach(println)
    val linksRDD:RDD[Edge[Link]] = links map {line =>
      val row = line split ','
      Edge(row(0).toLong, row(1).toLong, Link(row(4), row(5)))
    }
//    linksRDD.collect().foreach(println)
    //构建图
    val social: Graph[Person,Link] = Graph(verticesRDD, linksRDD)
    println("Create a Graph successfully!")
    return social
  }
}
