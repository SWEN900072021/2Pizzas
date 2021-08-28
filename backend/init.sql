DROP TABLE IF EXISTS test;
CREATE TABLE test(
  id SERIAL,
  name VARCHAR(255)
);

INSERT INTO test(name)
VALUES ('hello');
