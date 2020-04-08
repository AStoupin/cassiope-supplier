--liquibase formatted sql

--changeset Art:1586032267021-27
-- set FULL_REPAYMENT_DATE
update car car 
set FULL_REPAYMENT_DATE = (SELECT max(date) FROM REPAYMENT_ITEM where car_id = car.id )
where state in( 'REPAID', 'ARCHIVED') and id in (SELECT car_id FROM REPAYMENT_ITEM ) and FULL_REPAYMENT_DATE is null ;

