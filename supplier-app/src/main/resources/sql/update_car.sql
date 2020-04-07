-- update_car script
-- 2020-04-07
-- ver. 1.0

select state, count(*) from car group by state ;

ALTER TABLE PAYLOAD ALTER COLUMN DATE TIMESTAMP ;

update PAYLOAD set state = 'PROCESSED' where state in ('SUBMITTED', 'RECIEVED') ;

-- update CFL without 11
update car c set state = 'SUBMITTED CFL'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' and EVENT_CODE = '22' 
) and state not in (
'SUBMITTED CFL',
'SUBMITTED',
'READY TO FINANCE',
'FINANCE REQUESTED',
'FINANCED',
'REPAID', 'CANCEL', 'ARCHIVED'
) and DEALER_ID is not null ;

-- update F150 without CFL-11
update car c set state = 'SUBMITTED'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'F150' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.f150.F150Item' and EVENT_CODE = 'F150' 
) and state not in (
'SUBMITTED',
'READY TO FINANCE',
'FINANCE REQUESTED',
'FINANCED',
'REPAID', 'CANCEL', 'ARCHIVED', 'NEW'
) and DEALER_ID is not null ;

-- update F950 without CFL-11
update car c set state = 'READY TO FINANCE'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'F950' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.f950.F950Item' and EVENT_CODE in('950','READY')
) and state not in (
'READY TO FINANCE',
'FINANCE REQUESTED',
'FINANCED',
'REPAID', 'CANCEL', 'ARCHIVED', 'NEW'
) and DEALER_ID is not null ;

-- update F120 without CFL-11
update car c set state = 'FINANCE REQUESTED'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'F120' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.f120.F120Item' and EVENT_CODE in('F120')
) and state not in (
'FINANCE REQUESTED',
'FINANCED',
'REPAID', 'CANCEL', 'ARCHIVED', 'NEW'
) and DEALER_ID is not null ;

-- update F910 without CFL-11
update car c set state = 'FINANCED'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'F910' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.f910.F910Item' and EVENT_CODE in('910')
) and state not in (
'FINANCED',
'REPAID', 'CANCEL', 'ARCHIVED', 'NEW'
) and DEALER_ID is not null ;

-- update F920 without CFL-11
update car c set state = 'FINANCED'
where id not in(
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'CFL' and pi.CAR_ID is not null and (SOURCE_TYPE != 'ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item' OR EVENT_CODE = '11' ) 
) and id in (
SELECT pi.CAR_ID FROM PAYLOAD_ITEM pi
join PAYLOAD p on p.id = pi.PAYLOAD_id
where state  = 'PROCESSED'  and PAYLOAD_TYPE = 'F920' and pi.CAR_ID is not null and SOURCE_TYPE = 'ru.cetelem.cassiope.supplier.io.f910.F910Item' and EVENT_CODE in('920')
) and state not in (
'REPAID', 'CANCEL', 'ARCHIVED', 'NEW'
) and DEALER_ID is not null ;

commit;

select state, count(*) from car group by state ;
