
# About Me

图计算和图存储在国内兴起于2014年，笔者在2015有幸成为中科院的客座实习生开始基础图计算，从2015年12月开始分别完成了图计算的综述研究、GraphX的源码剖析，随后参与了图计算的相关项目，受益匪浅，乐趣无穷，不想离开，因毕业需要，2016年5月返回本科学校花了几周时间完成了这个简单的毕业设计，作为毕业之用。


# Abstract

This is my 2015 undergraduate graduation design based on GraphX().

Common Interfaces implementedd in this system for upper users:



# Summary Design
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/System_Architecture.png)


# Implemention Details

## GraphBuild
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/GraphBuild_flow_chart.png)
## N Degree Neighbours
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/design/flow_chart_of_find_n-layer_neighbors_algorithm.png)



# Visualization

## Custom attributes  要展示的属性标签客制化
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/带有人物姓名和关系的图构建可视化结果.png)


## Community Detection
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/顶点分组可视化结果.png)

## PageRank
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/顶点重要程度可视化结果.png)


## Second Degree Neighbours
![Alt text](https://github.com/cld378632668/A-community-detect-System-based-on-GraphX/raw/master/visualization/节点邻居计算可视化结果.png)


# Performance Tuning Guide

Spark·Shuffle调优指南
[Spark·Shuffle调优指南](http://note.youdao.com/noteshare?id=59701d10a35ac27548a883c5b64c5820&sub=wcp1515727395139560)


# Future Work

    基于图的社区发现效率比较高的算法有标签传播(LPA)，lovain method, infomap等，其中以infomap综合优势最好，因为infomap通吃所有类型的网络(有向无向有权无权)，且是线性时间，发现的社区质量也比较高。
    社区发现发展到现在，领域的拼图基本完善了，可是实际应用一直是困扰这个领域的痛点。所以我觉得在现有评价体系下，再求准意义不大，接下来的重点研究方向是scalabl。Louvain则是将Modularity的优化进行了scalable，可以快速的应用在大规模的网络上. We will implements Lonvain on GraphX.

