CREATE DATABASE IF NOT EXISTS prueba;
USE prueba;

CREATE TABLE employees (
	id INT(11) NOT NULL AUTO_INCREMENT,
	name VARCHAR(45) DEFAULT NULL,
    salary INT(11) DEFAULT NULL,
    PRIMARY KEY(id)
);

DESCRIBE employees;

SELECT * FROM employees;

INSERT INTO employees values
		(1, 'Eduardo Perez', 3000),
        (2, 'Saul Lopez', 5000),
        (3,'Margarita Suarez', 2000);
        
        