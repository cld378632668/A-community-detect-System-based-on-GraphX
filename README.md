#  Related Link

Spark·Shuffle调优指南
![Spark·Shuffle调优指南](http://note.youdao.com/noteshare?id=59701d10a35ac27548a883c5b64c5820&sub=wcp1515727395139560)

# Abstract

This is my 2015 undergraduate graduation design based on GraphX().

Common Interfaces implementedd in this system for upper users:


效率比较高的算法有标签传播(LPA)，lovain method, infomap等，其中以infomap综合优势最好，因为infomap通吃所有类型的网络(有向无向有权无权)，且是线性时间，发现的社区质量也比较。


社区发现发展到现在，领域的拼图基本完善了，可是实际应用一直是困扰这个领域的痛点。所以我觉得在现有评价体系下，再求准没意义了接下来的重点研究方向是scalabl。Louvain则是将Modularity的优化进行了scalable，可以快速的应用在大规模的网络上. This project implements Lonvain on GraphX.

# Summary Design
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/System_Architecture.png)


# Implemention Details.

## GraphBuild
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/GraphBuild_flow_chart.png)
## N Degree Neighbours
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/flow_chart_of_find_n-layer_neighbors_algorithm.png)



# Visualization

## custom attributes  要展示的属性标签客制化
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/带有人物姓名和关系的图构建可视化结果.png)


## Community Detection
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/顶点分组可视化结果.png)

## PageRank
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/顶点重要程度可视化结果.png)


## Second Degree Neighbours
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/节点邻居计算可视化结果.png)

# Related Work
大规模图数据的分布式处理 作者：谷峪、于戈、鲍玉斌
第1章大规模图数据处理： 问题与挑战
1.1大图数据处理的背景
1.2图数据的表示
1.3传统的大图数据管理方法
1.4云计算环境处理大图数据的优势
1.5新型大图计算系统面临的挑战
1.6关键技术问题
第2章大图分布式处理的计算模型和执行机制
2.1大图分布式处理的基本计算框架
2.1.1基于MapReduce的计算框架
2.1.2基于BSP的计算框架与GAS模型
2.1.3MapReduce与BSP对比
2.1.4其他处理框架
2.2图查询处理的遍历模式
2.2.1以顶点为中心
2.2.2以子图为中心
2.2.3以边和路径为中心
2.3消息通信
2.3.1消息发送时序控制
2.3.2消息交换模式
2.3.3网络通信平台
2.3.4上层消息优化技术
2.4同步控制
2.4.1同步模式
2.4.2异步模式
2.4.3混合模式
2.4.4跨步模式
2.5容错管理
2.5.1故障恢复技术
2.5.2故障侦测技术
2.6任务调度
2.7可扩展性
第3章大图数据划分技术
3.1图数据划分技术综述
3.1.1离线划分算法
3.1.2在线划分算法
3.1.3动态划分算法
3.2大图划分定义
3.2.1处理流程和定义
3.2.2真实图的局部性分析
3.3OnFlyP划分算法
3.3.1Range划分
3.3.2OnFlyP划分
3.3.3负载均衡控制机制
3.3.4计算接口描述
3.3.5动态调整机制
3.4性能评价
3.5小结
第4章大图数据分布式存储与索引技术
4.1大图数据的存储索引技术...


