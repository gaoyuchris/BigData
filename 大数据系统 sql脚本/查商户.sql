use BigDataSystem

select business_id ,business_name, business_type, count(business_id) as frequency, sum(trans_amount) as  'total_amount' from trans 
group by business_id, business_name, business_type 