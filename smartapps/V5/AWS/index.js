/**
 *  EchoSistant - Lambda Code
 *
 *  Version 5.4.00 - 6/30/2017 Complete overhaul!
 *  Version 5.3.00 - 6/21/2017 Added US Skill
 *  Version 5.1.00 - 3/21/2017 Added Reminders Profile
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
'use strict';
const Alexa = require('alexa-sdk');
const https = require('https');
const STtoken = process.env.STtoken;
const STurl = process.env.STurl;
const versionTxt = '5.4';

var keywords = {
    'feedback': ['give', 'for', 'tell', 'what', 'how', 'is', 'when', 'which', 'are', 'how many', 'check', 'who', 'status'],
    'enable': ['on', 'start', 'enable', 'engage', 'open', 'begin', 'unlock', 'unlocked'],
    'disable': ['off', 'stop', 'cancel', 'disable', 'disengage', 'kill', 'close', 'silence', 'lock', 'locked', 'quit', 'end'],
    'more': ['increase', 'more', 'too dark', 'not bright enough', 'brighten', 'brighter', 'turn up'],
    'less': ['darker', 'too bright', 'dim', 'dimmer', 'decrease', 'lower', 'low', 'softer', 'less'],
    'delay': ['delay', 'wait', 'until', 'after', 'around', 'within', 'in', 'about']
};

//will eventually be a var...
var deviceTypes = {
    'switch': ['light', 'switch', 'fan', 'outlet', 'relay'],
    'lock': ['lock'],
    'door': ['door', 'garage'], //'window', 'shade', 'curtain', 'blind', 'tstat', 'indoor', 'outdoor', 'vent', 'valve', 'water', 'speaker', 'synth', 'media', 'relay'
    'contact': ['window', 'door', 'window']
};

const SKILL_NAME = 'EchoSistant';
const WELCOME_MESSAGE = 'Yes';
const WELCOME_REPROMT = 'Welcome reprompt';
const REPROMPT_SPEECH = 'Anything else?';
const EXIT_SKILL_MESSAGE = 'Goodbye';
const HELP_MESSAGE = 'Examples of things to say';
const HELP_REPROMT = 'Need more Help?';
const STOP_MESSAGE = 'I am here if you need me';
const SETTINGS_UPDATED = 'I have updated your settings.';
const ERROR = 'Something went wrong';


exports.handler = function (event, context, callback) {

    //this.event.context.System.device.deviceId
    //console.log('Intent ' + event.request.intent.name);


    if (event.header === undefined) { //Custom Skill

        const alexa = Alexa.handler(event, context, callback);
        alexa.dynamoDBTableName = 'EchoSistant';
        alexa.registerHandlers(handlers);
        alexa.execute();

    } else { //Smart Home Skill
        switch (event.header.namespace) {
            case "Alexa.ConnectedHome.Discovery":
                console.log('Discovery');
                handleDiscovery(event, context, callback);
                break;
            case "Alexa.ConnectedHome.Control":
                console.log('Control');
                handleControl(event, context, callback);
                break;
            case "Alexa.ConnectedHome.Query":
                console.log('Query');
                handleQuery(event, context, callback);
                break;
            case "Alexa.ConnectedHome.System":
                console.log('System');
                //handleDiscovery(event, context, callback);
                break;
            default:
                console.log("Error, unsupported namespace: " + event.header.namespace);
                //context.fail("Something went wrong");
                break;
        }
    }
};

function handleDiscovery(request, context, callback) {
    var payloads = {
        discoveredAppliances: SAMPLE_APPLIANCES
    };
    var headers = JSON.parse(JSON.stringify(request.header));
    headers.name = "DiscoverAppliancesResponse";

    var result = {
        header: headers,
        payload: payloads
    };

    callback(null, result);
}

function handleControl(request, context, callback) {
    var payload = {};
    var appliance_id = request.payload.appliance.applianceId;
    var message_id = request.header.messageId;
    var request_name = request.header.name;
    console.log(request_name);
    var response_name = '';

    if (appliance_id === '5d86039e-1c2a-471e-a77f-7f950d7d153d') {
        if (request_name === 'SetLockStateRequest') {
            response_name = 'SetLockStateConfirmation';
            payload = {
                'lockState': request.payload.lockState
            };
        } else if (request_name === 'TurnOnRequest') {
            response_name = 'DependentServiceUnavailableError';
            //payload = {"currentDeviceMode":"AWAY"};
            //payload = {"errorInfo": {"code":"DEVICE_AJAR","description":"I can't do that Dave."}};
            payload = {
                "dependentServiceName": "I can't do that Dave."
            };
        } else if (request_name === 'TurnOffRequest') {
            response_name = 'TurnOffConfirmation';
            payload = {};
        }
    }

    var header = generateResponseHeader(request, response_name);
    var response = generateResponse(header, payload);
    callback(null, response);
}

function handleQuery(request, context, callback) {
    var payload = {};
    var appliance_id = request.payload.appliance.applianceId;
    var message_id = request.header.messageId;
    var request_name = request.header.name;

    var response_name = '';

    if (appliance_id === '5d86039e-1c2a-471e-a77f-7f950d7d153d') {
        if (request_name === 'GetLockStateRequest') {
            response_name = 'GetLockStateResponse';
            payload = {
                'lockState': 'UNLOCKED',
                'applianceResponseTimestamp': getUTCTimestamp()
            };
        }
    }

    var header = generateResponseHeader(request, response_name);
    var response = generateResponse(header, payload);
    callback(null, response);
}

function generateResponseHeader(request, response_name) {
    var header = {
        'namespace': request.header.namespace,
        'name': response_name,
        'payloadVersion': '2',
        'messageId': request.header.messageId
    };
    return header;
}

function generateResponse(header, payload) {
    var response = {
        'header': header,
        'payload': payload,
    };
    return response;
}

function getUTCTimestamp() {
    return new Date().toISOString();
}

const SAMPLE_APPLIANCES = [{
    "applianceId": "5d86039e-1c2a-471e-a77f-7f950d7d153d",
    "manufacturerName": "Schlage",
    "modelName": "Lock",
    "version": "1",
    "friendlyName": "Front Door",
    "friendlyDescription": "EchoSistant - Front Door Lock",
    "isReachable": true,
    "actions": [
        "setLockState",
        "getLockState",
        "turnOn",
        "turnOff"
    ],
    "applianceTypes": [
        "SMARTLOCK", "SWITCH"
    ],
    "additionalApplianceDetails": {}
}];

const handlers = {
    'NewSession': function () {
        console.log('NewSession');
        if (Object.keys(this.attributes).length === 0) {
            //First time user has called so we need to update settings
            this.emitWithState('UpdateSettings');
        } else {
            //Settings have already been setup, so we call the request type
            this.emitWithState(this.event.request.type);
        }
    },
    'LaunchRequest': function () {
        console.log('LaunchRequest');
        //Called the Invocation word without an intent....


        this.attributes.speechOutput = WELCOME_MESSAGE;
        // If the user either does not reply to the welcome message or says something that is not
        // understood, they will be prompted again with this text.
        this.attributes.repromptSpeech = WELCOME_REPROMT;
        this.emit(':ask', this.attributes.speechOutput, this.attributes.repromptSpeech);
    },
    'IntentRequest': function () {
        console.log('IntentRequest');
        //Called the invocation word with an intent...
        // this.event.request.intent.name = the profile name

        this.attributes.speechOutput = WELCOME_MESSAGE;
        // If the user either does not reply to the welcome message or says something that is not
        // understood, they will be prompted again with this text.
        this.attributes.repromptSpeech = WELCOME_REPROMT;
        this.emit(':ask', this.attributes.speechOutput, this.attributes.repromptSpeech);
    },
    'UpdateSettings': function () {
        console.log('UpdateSettings');
        var beginURL = STurl + 'update?access_token=' + STtoken;
        this.attributes.allSettings = '{"appSettings":{}}';
        var self = this;
        https.get(beginURL, function (res) {
            console.error("Got response: " + res.statusCode);
            res.on("data", function (data) {
                var getJSON = JSON.parse(data);
                console.log(getJSON.data);
                self.attributes.allSettings = getJSON.data;
                if (self.event.request.intent.name == 'UpdateSettings') {
                    self.emit(':tell', SETTINGS_UPDATED);
                } else {
                    self.emitWithState(self.event.request.type);
                }
            });
        }).on('error', function (e) {
            console.error("Got error: " + e.message);
            self.emit(':tell', ERROR);
        });
    },
    'AMAZON.HelpIntent': function () {
        console.log('HelpIntent');
        this.attributes.speechOutput = HELP_MESSAGE;
        this.attributes.repromptSpeech = HELP_REPROMT;
        this.emit(':ask', this.attributes.speechOutput, this.attributes.repromptSpeech);
    },
    'AMAZON.RepeatIntent': function () {
        console.log('RepeatIntent');
        this.emit(':ask', this.attributes.speechOutput, this.attributes.repromptSpeech);
    },
    'AMAZON.StopIntent': function () {
        console.log('StopIntent');
        this.emit('SessionEndedRequest');
    },
    'AMAZON.CancelIntent': function () {
        console.log('CancelIntent');
        this.emit('SessionEndedRequest');
    },
    'SessionEndedRequest': function () {
        console.log('SessionEndedRequest');
        this.emit(':saveState', true);
        this.emit(':tell', STOP_MESSAGE);
    },
    'Unhandled': function () {
        console.log('Unhandled');
        if (Object.keys(this.attributes).length === 0) {
            this.emitWithState('UpdateSettings');
        } else {
            /*check for real device
            let profiles = [...new Set(settings.deviceList.map(p => p.profile.toLowerCase()))];
            let realDevices = [...new Set(settings.deviceList.map(p => p.name.toLowerCase()))];
            let theCommand = this.event.request.intent.slots.ttstext.value;
            for (let [key, value] of realDevices.entries()) {
                if (theCommand.match('\\b' + value + '\\b')) {
                    console.log('realDevice ' + value);
                }
            }

            for (let [key, value] of profiles.entries()) {
                if (theCommand.match('\\b' + value + '\\b')) {
                    console.log('profile ' + value);
                }
            }

            for (let keyword in keywords) {
                if (keywords[keyword].find((it) => theCommand.includes(it))) {
                    console.log('keyword ' + keyword);
                }
            }

            for (let device in deviceTypes) {
                if (deviceTypes[device].find((it) => theCommand.includes(it))) {
                    console.log('device ' + device);
                }
            }*/

            this.attributes.speechOutput = HELP_MESSAGE;
            this.attributes.repromptSpeech = HELP_REPROMT;
            this.emit(':ask', this.attributes.speechOutput, this.attributes.repromptSpeech);
        }
    },
};