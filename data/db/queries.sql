CREATE TABLE IF NOT EXISTS ucb
(
        id_rule integer PRIMARY KEY, 
        qtdUsed int
)

select * from ucb

DELETE FROM UCB

ALTER TABLE ucb add qtdUsed int;

drop table ucb
drop table logUCB

CREATE TABLE logUCB(
        id integer primary key AUTOINCREMENT,
        reward integer not null,
        id_rule integer NOT NULL,
        FOREIGN KEY (id_rule) REFERENCES ucb(id_rule)
);

insert into sqlite_sequence (name,seq) values ("logUCB",1);

DELETE FROM UCB

DELETE FROM logUCB


    WITH RECURSIVE
      for(i) AS (VALUES(1) UNION ALL SELECT i+1 FROM for WHERE i < 60088)
    INSERT INTO UCB SELECT i,0 FROM for;
    
 CREATE UNIQUE INDEX index_UCB_ID ON UCB (id_rule);
 
 CREATE INDEX index_log_FK ON logUCB(id_rule)
 
 update ucb set qtdused = 0
 
 select * from ucb where id_rule = 0
 
 insert into ucb values (0,0)
    
