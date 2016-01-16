SELECT business_cluster.cluster, cluster_area.area, COUNT(*) as business_num, count(*) / cluster_area.area as business_density from 
business_cluster inner join  cluster_area on business_cluster.cluster = cluster_area.cluster
group by business_cluster.cluster, cluster_area.area having business_cluster.cluster != 0