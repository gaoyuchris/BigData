use BigDataSystem
select beijing_sample_trans.business_id,beijing_sample_trans.business_name, beijing_sample_trans.amount from (select business_id from business group by business_id having count(business_id) > 1) as a
inner join beijing_sample_trans on a.business_id = beijing_sample_trans.business_id