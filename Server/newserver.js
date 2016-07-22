var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var server_port = process.env.OPENSHIFT_NODEJS_PORT || 3000
var mysql = require("mysql");
var num=0;
var con = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database :'chat'

});
con.connect();
var now = new Date();
var nxt = new Date();
server.listen(server_port, function(){
	console.log('Server listening at port %d', server_port);
  app.get('/', function(req, res){
    res.sendFile(__dirname + '/index.html');

  });
  //console.log(now.setTime(now.getMinutes()+30));
  now = now.addMinutes(100);
  var timeDiff = Math.abs(now.getTime() - nxt.getTime());
  var diffMins = Math.round(timeDiff / 60000);
  if(nxt<now){
  console.log(diffMins);}
  //console.log(now.setTime(now.getMinutes()+30));
});

var suspendedUsers = [];
var usersArray = [];
var numUsers = 0;

Date.prototype.addMinutes = function(minutes) {
          this.setMinutes(this.getMinutes() + minutes);
          return this;
      };


io.on('connection', function (socket) {

  var addedUser = false;
  var client;

function onlineCheck(checkid){
  var feed;
  for(var i in usersArray){
    var useri = usersArray[i].id;
    if (useri == checkid){
      feed = true;
    }else{
      feed=false;
    }  }
  return feed;
}


  socket.on('login', function(userdata) {
	  var usertype=userdata.type;
      var userid= userdata.userid;
      client = userid;
      var pass=userdata.pass;
      var Query = "select * from users where type ='"+usertype+"' and id="+userid+" and pass='"+pass+"'";
      if(onlineCheck(userid)){
        socket.emit('notlog' , 'User Already Logged');
      }else {

	  var query = con.query(Query, function(err,result){

if(err){
console.log(query.sql);
console.error(err);
return;
}
if(result.length>0){
if(result[0].id == userid){
  var suspend = false;
  var TimeLeft;
  for(var i in suspendedUsers){
    var useri = suspendedUsers[i].susid;
    if(useri == userid){
      var d = new Date();
      if(d<suspendedUsers[i].susTime){
        TimeLeft = Math.abs(suspendedUsers[i].susTime.getTime() - d.getTime());
        console.log(TimeLeft / 60000);
        socket.emit('notlog' , 'Cant Login before '+Math.round(TimeLeft / 60000)+' Minutes');
        suspend = true;
      }
      else{
        suspendedUsers.splice(i,1);
      }
    }
  }
if(!suspend){
socket.RoomName="public";
socket.username = result[0].user;
socket.join(socket.RoomName);
usersArray.push({'socketid':socket.id,'id':userid,'name':socket.username});
socket.broadcast.to(socket.RoomName).emit('user left',usersArray);

socket.emit('logged',{
username:socket.username,
jarr:usersArray
});
	console.log(usertype +": " +socket.username+" connected "+socket.id);

  //console.log(usersArray);
}
}

}
else{
	socket.emit('notlog' , 'wrong');
}

});
}

    });
    socket.on('privateRequest' , function(data){
    var fromUser= data.fromUser;
    var roomname= data.room;
    var toUser= data.receiver;
    socket.username
    for(var i in usersArray){
      var useri = usersArray[i].id;
      if (useri == toUser){
          io.to(usersArray[i].socketid).emit('private', data);
      }
    }


    });


//private chat rooms
socket.on('sprivate' , function(data){
  var reqType = data.requestType;
  var fromUser= data.fromUser;
  var roomname= data.room;
  var toUser= data.receiver;
    for(var i in usersArray){
  var useri = usersArray[i].id;
  if (useri == toUser){
      io.to(usersArray[i].socketid).emit('private', data);
  }

 }
});
  // end private chat rooms



socket.on('roomleft', function(data){
  var room = data.room;
console.log(socket.username + " Left "+room);

});
//Admin Check When Modify

socket.on('modify', function() {
var check = "SELECT * FROM users";
var query = con.query(check, function(err,result){
if(err){
console.log(query.sql);
console.error(err);
return;
}else{
  socket.emit('modifyfeed' , result);
}
});

});

//Admin End Checking

//User change Personal information
socket.on('change' , function(data){
  var id = data.id;
  var type = data.type;
  if(type == 1){
    var name = data.uname;
    var q = "UPDATE users SET user = '"+name+"' WHERE id ="+id+"";
    var query = con.query(q, function(err,result){
    if(err){
    console.log(query.sql);
    console.error(err);
    return;
    }else{
      socket.emit('change feed' , "Username Updated Successfully");
    }
    });
  }else{
    var Query = "select * from users where id="+id+"";
    var query = con.query(Query, function(err,result){
    if(err){
    console.log(query.sql);
    console.error(err);
    return;
    }else{
      if(result.length>0){
        if(result[0].pass == data.oldpass){
            if(type == 2){
              var q = "UPDATE users SET pass = '"+data.newpass+"' WHERE id ="+id+"";
              var query = con.query(q, function(err,result){
              if(err){
              console.log(query.sql);
              console.error(err);
              return;
              }else{
                socket.emit('change feed' , "Password Updated Successfully");
              }
              });
            }
            else if (type == 3){
              var q = "UPDATE users SET user ='"+data.uname+"', pass = '"+data.newpass+"' WHERE id ="+id+"";
              var query = con.query(q, function(err,result){
              if(err){
              console.log(query.sql);
              console.error(err);
              return;
              }else{
                socket.emit('change feed' , "Username and Password Updated Successfully");
              }
              });
            }
        }
        else{
          socket.emit('change feed' , "Your Old Password is not Entered Correctly");
        }
      }

    }
    });

  }

});
//User change Personal information

//Admin Suspend a User

socket.on('Suspend' , function(data){
  var susId = data.id;
  var susTime = parseInt(data.time);
  var sust = new Date();
  sust = sust.setMinutes(sust.getMinutes()+susTime);
  for(var i in usersArray){
    var useri = usersArray[i].id;
    if (useri == susId){
      console.log(new Date().addMinutes(susTime));
      console.log(susTime);
      suspendedUsers.push({'susid':susId,'susTime':new Date().addMinutes(susTime)});
      io.to(usersArray[i].socketid).emit('suspend me');
    }
  }


});

//Admin Suspend a User


//Admin Delete a User
socket.on('delete', function(data) {
var check = "DELETE FROM users WHERE id="+data+"";
var query = con.query(check, function(err,result){

if(err){
console.log(query.sql);
console.error(err);
return;
}else{
  socket.emit('submitdata','User Deleted Successfully');
}
});

});
//Admin Delete a User


//Admin Commit Modify
socket.on('Commit modify', function(data) {
var id = data.index;
var column = data.columnname;
var changed = data.newValue;
var columnSwitch;
if(column=='NAME'){
  columnSwitch = 'user';
}else if(column=='PASSWORD'){
  columnSwitch = 'pass';
}else if(column == 'Type'){
  columnSwitch = 'type';
}
var query = "UPDATE users SET "+columnSwitch+" = '"+changed+"' WHERE id ="+id+"";

var Execute = con.query(query, function(err,result){

if(err){
console.log(query.sql);
console.error(err);
return;
}else{
  console.log('Admin: '+socket.username+' Changed Data');
  socket.emit('submitdata','Data Updated Successfully');
}
});

});
//Admin Commit Modify


//Admin add Event

socket.on('add', function (data) {
  var userType= data.type;
  var id= data.index;
  var uName= data.name;
  var pass=data.password;

  var check = "SELECT * FROM users WHERE id="+id+"";
  var Query = "INSERT INTO users (`type` ,`id` ,`user` ,`pass`)VALUES ('"+userType+"',"+id+",'"+uName+"','"+pass+"')";
var queryCheck = con.query(check, function(err,result){
  if(err){
  console.log(query.sql);
  console.error(err);
  return;
  }
if(result.length==0){
  var query = con.query(Query, function(err,result){

if(err){
console.log(query.sql);
console.error(err);
return;
}
socket.emit('verify' , 'Submit');
});
}else{
  socket.emit('verify' , 'Already Exist');
}


});

});
//END ADD EVENT

  socket.on('new message', function (data) {

    socket.broadcast.to(data.room).emit('new message', {
      username: socket.username,
      message: data.message,
      roomname: data.room
    });
	console.log('message from ', socket.username, ' in room ', data.room);
  });


	socket.on('enterroom',function(data){
	socket.RoomName=data.room;

	var room = io.sockets.adapter.rooms[data];
	if(room==null){
	socket.numusers=0;
	}else{
	socket.numusers=Object.keys(room).length;}


	//socket.emit('users in room', {
      //numusers: socket.numusers
   // });

	socket.broadcast.to(data.room).emit('user joined', {
      username: socket.username
    });
	socket.join(data.room);
	//console.log(usersArray);
	console.log(socket.username+' logged to ', socket.RoomName);
});


  socket.on('disconnect', function () {

   console.log(socket.username,' Left');


    for(var i in usersArray){
      var useri = usersArray[i].id;
      if (useri == client){
        delete usersArray[i];
        usersArray.splice(i,1);
      }
    }
    //console.log(usersArray);


      socket.broadcast.to(socket.RoomName).emit('user left',usersArray);

  });
});
