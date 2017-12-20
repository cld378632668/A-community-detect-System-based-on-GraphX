import model.{Person, Link}
import org.apache.spark.graphx.{Edge, Graph}
import org.graphstream.graph.implementations._
import org.graphstream.graph.{Graph => GraphStream}

/**
  * Created by mac on 3/25/16.
  * 个性化可视化功能模块.
  */
object Visualize {

  /*
  可视化显示顶点的ID和名称.
   */
  def displayGraphWithIdAndName(graph:Graph[Person,Link],name:String):SingleGraph={

    val graphStream:SingleGraph = new SingleGraph(name);

    // Set up the visual attributes for graph visualization
    graphStream.addAttribute("ui.stylesheet","url(./style/stylesheet.css)")
    graphStream.addAttribute("ui.quality") //to enable slower but better rendering.
    graphStream.addAttribute("ui.antialias") //to enable anti-aliasing(抗锯齿或边缘柔化) of shapes drawn by the viewer

    // Given the egoNetwork, load the graphX vertices into GraphStream
    for ((id,person:Person) <- graph.vertices.collect()) {
      val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
      node.addAttribute("ui.label",id  +"\n"+person.name)
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

  /*
  可视化显示顶点的名称.
   */
  def displayGraphWithName(graph:Graph[Person,Link],name:String):SingleGraph={

    val graphStream:SingleGraph = new SingleGraph(name);

    // Set up the visual attributes for graph visualization
    graphStream.addAttribute("ui.stylesheet","url(./style/stylesheet.css)")
    graphStream.addAttribute("ui.quality") //to enable slower but better rendering.
    graphStream.addAttribute("ui.antialias") //to enable anti-aliasing(抗锯齿或边缘柔化) of shapes drawn by the viewer

    // Given the egoNetwork, load the graphX vertices into GraphStream
    for ((id,person:Person) <- graph.vertices.collect()) {
      val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
      node.addAttribute("ui.label",person.name)
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

  /*
  可视化显示顶点的名称和边的名称.
   */
  def displayGraphWithNameAndLink(graph:Graph[Person,Link],name:String):SingleGraph={

    val graphStream:SingleGraph = new SingleGraph(name);

    // Set up the visual attributes for graph visualization
    graphStream.addAttribute("ui.stylesheet","url(./style/stylesheet.css)")
    graphStream.addAttribute("ui.quality") //to enable slower but better rendering.
    graphStream.addAttribute("ui.antialias") //to enable anti-aliasing(抗锯齿或边缘柔化) of shapes drawn by the viewer

    // Given the egoNetwork, load the graphX vertices into GraphStream
    for ((id,person:Person) <- graph.vertices.collect()) {
      val node = graphStream.addNode(id.toString).asInstanceOf[SingleNode]
      node.addAttribute("ui.label",person.name)
    }
    // Load the graphX edges into GraphStream edges
//    graph.edges.collect().foreach(println)
    for (Edge(x,y,link:Link) <- graph.edges.collect()) {
      val edge = graphStream.addEdge(x.toString ++ y.toString,
        x.toString, y.toString,
        true).
        asInstanceOf[AbstractEdge]
      edge.addAttribute("ui.label",link.relationship)
    }

    graphStream.display()
    graphStream
  }

  /*
   将给定的子图和中心顶点用不同的样式加以区分显示.
   */
  def subgraphMark(graphStream:SingleGraph,subgraph:Graph[Person,Link],centralId:Long)={
    for((id,person:Person) <- subgraph.vertices.collect()){
      val node = graphStream.getNode(id.toString).asInstanceOf[SingleNode]
      node.addAttribute("ui.class","important")
    }

      graphStream.getNode(centralId.toString).asInstanceOf[SingleNode].
        addAttribute("ui.class","central")

    for(Edge(x,y,_)<- subgraph.edges.collect()){
      val edge = graphStream.getEdge(x.toString++y.toString).asInstanceOf[AbstractEdge]
      edge.addAttribute("ui.class","important" )
    }

    graphStream.display()
  }

  /*
  将给定的顶点数组用不同的样式加以区分显示.
   */
  def subgraphMark(graphStream:SingleGraph,array:Array[(Long,Int)])={
    for((id,level)<-array){
      graphStream.getNode(id.toString).asInstanceOf[SingleNode].
        addAttribute("ui.class","important"+level)
    }

    graphStream.display()
  }

}
