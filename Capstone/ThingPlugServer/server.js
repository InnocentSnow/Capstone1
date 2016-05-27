'use strict';

var thingPlugDir	= '../thing'

var colors		= require('colors');
var async		= require('async');

var config		= require(thingPlugDir + '/config');
var api			= require(thingPlugDir + '/lib/api');
var MQTTClient		= require(thingPlugDir + '/lib/mqtt_client');

var http		= require('http');
var path		= require('path');
var express		= require('express');
var cookieParser        = require('cookie-parser');
var bodyParser          = require('body-parser');

var app			= express();
var PORT                = 2000;

app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

var resID;

async.waterfall([
  function createNode(cb){
    console.log(colors.blue('1. node 생성 요청'));
    api.createNode(config.nodeID, cb);
  },
  function createRemoteCSE(nodeRI, cb){
    console.log(colors.blue('2. remoceCSE 생성 요청'));
    config.nodeRI = nodeRI;
    api.createRemoteCSE(config.nodeID, config.nodeRI, config.passCode, cb);
  },
  function createContainer(dKey, cb){
    console.log(colors.blue('3. container 생성 요청'));
    config.dKey = dKey;
    api.createContainer(config.nodeID, config.containerName, dKey, cb);
  },
  function createMgmtCmd(res, cb){
    console.log(colors.blue('4. mgmtCmd 생성 요청'));
    var mgmtCmd = config.mgmtCmdPrefix + config.nodeID;
    api.createMgmtCmd(mgmtCmd, config.dKey, config.cmdType, config.nodeRI, cb);
  }
], function(err,resourceID) {
  if(err) return console.log(err);

  app.listen(PORT, function() {
    console.log('Waiting for data');
  });

  var mqttClient = new MQTTClient(config.nodeID);
  mqttClient.on('message', function(topic, message){
      var msgs = message.toString().split(',');
      console.log(colors.red('#####################################'));
      console.log(colors.red('MQTT 수신'));
      xml2js.parseString( msgs[0], function(err, xmlObj){
        if(!err){
          console.log(xmlObj['m2m:req']['pc'][0]['exin'][0]['ri'][0]);
          console.log(xmlObj['m2m:req']['pc'][0]['exin'][0]['exra'][0]);
          try{
            var req = JSON.parse(xmlObj['m2m:req']['pc'][0]['exin'][0]['exra'][0]);
          }
          catch(e){
            console.error(xmlObj['m2m:req']['pc'][0]['exin'][0]['exra'][0]);
            console.error(e);
          }
          processCMD(req);
          var ei = xmlObj['m2m:req']['pc'][0]['exin'][0]['ri'][0];
          api.updateExecInstance(config.nodeID, config.mgmtCmdPrefix, config.dKey, ei);
        }
      });
      console.log(colors.red('#####################################'));
  });
});

app.get('/', function(req, res)
{
  var value = req.query.q

  if(value) {
    console.log('get data : ' + value);
    api.createContentInstance(config.nodeID, config.containerName, config.dKey, value);
  }
  else console.log('Connected, but it contains No values');

  res.end();
});

