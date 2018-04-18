delimiter |

CREATE TRIGGER status_update_bid
AFTER INSERT
   ON bid FOR EACH ROW
   
BEGIN
      
   UPDATE advertisement a set a.current_bid=New.bid_amount where a.adv_id=New.ad_id;
   
END; 
|

delimiter |

CREATE TRIGGER status_update_buy
AFTER INSERT
   ON direct_buy FOR EACH ROW
   
BEGIN

   -- Update record in ad table
   UPDATE advertisement a set a.status='SOLD' where a.adv_id=New.ad_id;
   
END;

|