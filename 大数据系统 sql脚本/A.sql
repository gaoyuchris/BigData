select cluster, avg(consumer_level) ,min(consumer_level), max(consumer_level) from (select card_id, cluster, count(*) / 2 as frequency_count, sum(trans_amount)/ 2 as consumer_level from (SELECT business_cluster.business_id, business_cluster.cluster, business_cluster.business_name,
business_cluster.business_type, trans.card_id, trans.card_type, trans.trans_amount from business_cluster inner join
trans on business_cluster.original_business_id = trans.business_id) temp group by card_id, cluster having
cluster != 0) temp group by cluster