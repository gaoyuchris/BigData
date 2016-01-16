select cluster, count(*) from (select  * from business_cluster  order by total_amount  desc limit 1000) temp group by cluster
