/**
  * Created by mac on 3/25/16.
  */
import model.{Person, Link}
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

import scala.collection.mutable


object NLayerNetwork {

//  def subgraphWithVertexes( graph:Graph[Person,Link], set:Set[Long] ) : Graph[Person,Link]={
//    graph.subgraph(vpred = (id,attr) => set.contains(id))
//  }
  /**
    * 计算N层邻居,返回N层邻居所构成的子图
    * find the n-layer neighbours of specified VertexId
    *
    * @param n the number of layers
    * @param srcId the specified VertexId
    * @return the set of n-layer neighbors
    */
  def nLayerNetwork(graph:Graph[Person,Link], n:Int, srcId:Long): Graph[Person,Link]={
    /**
      * neighbours' vertexId of each vertexId
      */
    val neighbors:RDD[(VertexId,Array[VertexId])] =
      graph.collectNeighborIds(EdgeDirection.Either) //VertexId是Long的别名
    neighbors.cache()

    var temp = Array[Long](srcId)
    val tempSet =  mutable.Set[Long]()
    val result = mutable.Set[Long]()//最后的结果集
    val set = Set[Long]()
    var m = 0
    while(m<n){
      for( i <- 0 until temp.size ) {
        val idsArray = neighbors.lookup(temp(i))(0)

        tempSet ++= idsArray.toSet
      }

      result ++= tempSet
      tempSet --= temp
      temp = tempSet.toArray

      tempSet.clear()
      m=m+1
    }

    graph.subgraph(vpred = (id,attr) => result.contains(id))
  }

  def nLayerNetworkWithDebug(graph:Graph[Person,Link], n:Int, srcId:Long): Graph[Person,Link]={
    /**
      * neighbours' vertexId of each vertexId
      */
    val neighbors:RDD[(VertexId,Array[VertexId])] =
      graph.collectNeighborIds(EdgeDirection.Either) //VertexId是Long的别名
      neighbors.cache()

    var temp = Array[Long](srcId);
    val tempSet =  mutable.Set[Long]()
    val result = mutable.Set[Long]()//最后的结果集
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

    graph.subgraph(vpred = (id,attr) => result.contains(id))
  }




}
