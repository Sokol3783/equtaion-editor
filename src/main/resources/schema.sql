CREATE TABLE equation{
  equation_id SERIAL PRIMARY key,
  record VARCHAR (250)
}

CREATE TABLE root {
    root_id SERIAL PRIMARY key,
    number double precision
}

CREATE TABLE equation_root (
  equation_id INTEGER REFERENCES equation(equation_id),
  root_id INTEGER REFERENCES root(root_id),
  PRIMARY KEY (equation_id, root_id)
);
