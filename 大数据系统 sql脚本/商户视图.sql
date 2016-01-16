
create view business

as


select business_id ,business_name, business_type, count(business_id) as frequency, sum(amount) as  'total_amount' from beijing_sample_trans 
group by business_id, business_name, business_type 