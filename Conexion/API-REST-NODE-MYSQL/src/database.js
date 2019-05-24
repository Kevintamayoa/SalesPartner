const mysql = require('mysql');

const mysqlConnection = mysql.createConnection({

  host: '209.59.155.254',
  port: '3306',
  user: 'firmatio_proyecto',
  password: 'PROY#123456789#',
  database: 'firmatio_test'
});

mysqlConnection.connect(function(err){

  if (err){
    console.log(err);
    return;
  } else {
    console.log('Db is conect!');
  }


});






module.exports = mysqlConnection;
