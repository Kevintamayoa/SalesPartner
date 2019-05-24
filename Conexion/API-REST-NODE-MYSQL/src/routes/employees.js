
const express = require('express');

const router = express.Router();

 const mysqlConnection = require('../database');
 router.get('/', (req, res) => {
   mysqlConnection.query('SELECT * FROM employees;', (err, rows, fields) => {
    if(!err){
      res.json(rows);
      res.end();


    } else{
      console.log(err);
    }
   });

 });

// router.get('/:id', (req, res) => {
//     id =req.id;
//     mysqlConnection.query('SELECT name FROM employees where' + id ;', (err, rows, fields) => {
//         if(!err){
//             res.json(rows);
//             res.end();
//
//         } else{
//             console.log(err);
//         }
//
//     });
// });
let obj={id:0,name:'peluna',salary:50}
router.get('/add', (req, res) => {
    mysqlConnection.query('INSERT INTO employees values (null,"'+obj.name+'",'+obj.salary+');', (err, rows, fields) => {
    if(!err){

        res.send();
        res.end();

    } else{
        console.log(err);
    }

});

});

module.exports = router;
