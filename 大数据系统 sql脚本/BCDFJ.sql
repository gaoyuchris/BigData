SELECT business_cluster.cluster, cluster_area.area, COUNT(*) as business_num, 
count(*) / cluster_area.area as business_density,
sum(business_cluster.total_amount) / 2 / cluster_area.area as capital_density,
sum(business_cluster.total_amount / 2) as cluster_scale,
sum(business_cluster.frequency)/14 as cluster_frequency_dayavg 

from 
business_cluster inner join  cluster_area on business_cluster.cluster = cluster_area.cluster
group by business_cluster.cluster, cluster_area.area having business_cluster.cluster != 0