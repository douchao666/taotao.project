# taotao.project
本项目是黑马程序员经典电商项目
  开发框架：springmvc spring mybatis（可以使用springboot工程对该项目进行改进）
  搜索框架：solr（该项目使用solr集群支持商品的搜索服务）
  缓存：redis （该项目中使用的是redis集群进行缓存数据）
  使用ssh框架，支持单点登陆
  使用ftp服务器对项目中所使用的图片进行保存
  负载均衡：该项目对服务端使用nginx进行负载均衡（后期可以使用ribbon对客户端进行负载均衡）
  服务发现：使用zookeeper集群实现对该项目的服务发现
启动时候先启动服务端，在启动客户端

