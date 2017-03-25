/* 
 * Reminders and Events Profile - EchoSistant Add-on 
 *
 *
 ************************************ FOR INTERNAL USE ONLY ******************************************************
							
 								DON'T FORGET TO UPDATE RELEASE NUMBER!!!!!
 
 ************************************ FOR INTERNAL USE ONLY ******************************************************
 *
 *		3/24/2017		Version:5.0 R.0.0.1		Alpha Release
 * 
 *  Copyright 2016 Jason Headley & Bobby Dobrescu
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
/**********************************************************************************************************************************************/
definition(
	name			: "Reminders",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Reminders Add-on",
	category		: "My Apps",
    parent			: "Echo:EchoSistant",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.0.1"
}
/**********************************************************************************************************************************************/
preferences {

    page name: "mainProfilePage"
    		page name: "pSend"          
        	page name: "pActions"
        	page name: "pConfig"
        	page name: "pRestrict"

}
//dynamic page methods
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section("General Settings") {
           	href "pSend", title: "Select Audio and Text Delivery Methods", description: pSendComplete(), state: pSendSettings()   
           	href "pActions", title: "Define Global Actions", description: pActionsComplete(), state: pActionsSettings()
        }
        section("On Time Notifications") {
            href "pConfig", title: "On Time Output Settings", description: pConfigComplete(), state: pConfigSettings()
		}
        section("Before Due Date Notifications") {
            href "pConfigDue", title: "Before Due Date Output Settings", description: pConfigComplete(), state: pConfigSettings()
		}
        section ("Defaults Settings") {    
        	href "mDefaults", title: "Change Defaults", description: mDefaultsD(), state: mDefaultsS()
		}
		section ("Notification Restrictions") {
			href "pRestrict", title: "Use these restrictions...", description: pRestComplete(), state: pRestSettings()
        }
	}
}
page name: "pSend"
    def pSend(){
        dynamicPage(name: "pSend", title: "", uninstall: false){
             section ("Speakers", hideWhenEmpty: true){
                input "synthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, required: false
                input "sonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true    
                if (sonosDevice) {
                    input "volume", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
                    input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                }  
            }
            section ("Text Messages" ) {
            	input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true   
                if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
           			input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true     
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                    input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
                }
            }    
            section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
            input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false
            }        
    	}                 
    }   
page name: "pActions"
    def pActions() {
        dynamicPage(name: "pActions", uninstall: false) {
        	def routines = location.helloHome?.getPhrases()*.label 
            if (routines) {routines.sort()}
            section ("Trigger these lights and/or execute these routines when reminder is due...") {
                href "pDeviceControl", title: "Select Devices...", description: pDevicesComplete() , state: pDevicesSettings()
                input "pMode", "enum", title: "Choose Mode to change to...", options: location.modes.name.sort(), multiple: false, required: false 
            	def actions = location.helloHome?.getPhrases()*.label 
                if (actions) {
                    actions.sort()
            	input "pRoutine", "enum", title: "Select a Routine to execute", required: false, options: actions, multiple: false, submitOnChange: true
                if (pRoutine) {
                input "pRoutine2", "enum", title: "Select a Second Routine to execute", required: false, options: actions, multiple: false
            		}
            	}
            }
        }
    }
page name: "pConfig"
    def pConfig(){
        dynamicPage(name: "pConfig", title: "", uninstall: false) {
             section ("Remote Speaker Settings") {
                	input "pRunMsg", "text", title: "Play this predetermined message when a reminder is due...", required: false
                    input "pPreMsg", "text", title: "Play this message before the reminder message...", defaultValue: none, submitOnChange: true, required: false 
                    input "pSound", "enum", title: "Play this sound when a reminder is due...", required: false, submitOnChange: true, 
                        options: [
                        "Bell 1",
                        "Bell 2",
                        "Dogs Barking",
                        "Fire Alarm",
                        "Piano",
                        "Lightsaber"]
             }
             section ("Text and Push Notification Output") {
                	input "pRunTextMsg", "text", title: "Send this predetermined text when reminder is due...", required: false
                    input "pPreTextMsg", "text", title: "Append this text before the reminder message...", defaultValue: none, required: false 
             }             
		}             
	}             
page name: "pConfigDue"
    def pConfigDue(){
        dynamicPage(name: "pConfigDue", title: "", uninstall: false) {
					section ("Before Due Date Reminders") {
                    	input "sDueDate", "enum", title: "Remind me Before Due Date...",
                            options:["15":"15 minutes","30":"30 minutes","60":"One hour", "540":"One day"], multiple: true, required: false
                		input "sRunMsgDue", "text", title: "Use this predetermined message (for text and audio)...", required: false
                    	input "sPreMsgDue", "text", title: "Append this message...", defaultValue: none, submitOnChange: true, required: false 
                    	input "sSoundDue", "enum", title: "Use this custom sound...", required: false, submitOnChange: true, 
                        options: [
                        "Bell 1",
                        "Bell 2",
                        "Dogs Barking",
                        "Fire Alarm",
                        "Piano",
                        "Lightsaber"]
             		}            
		}             
	}             
page name: "mDefaults"
        def mDefaults(){
                dynamicPage(name: "mDefaults", title: "", uninstall: false){           
                    section ("Use Google Calendar to Add Events") {
                    	input "iGCal", "bool", title: "Enable GCal Integration", required: false, defaultValue: false, submitOnChange:true
                        if(iGCal){
                        	input "sGCal", "enum", title: "Select Calendars...",
                            options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: true, required: false
                    	}                     
                	}
                    section ("Other Defaults") {
						input "cFilterReplacement", "number", title: "Alexa Automatically Schedules HVAC Filter Replacement in this number of days (default is 90 days)", defaultValue: 90, required: false
                     
                     }                    
        	}
        }
page name: "pDeviceControl"
    def pDeviceControl() {
            dynamicPage(name: "pDeviceControl", title: "",install: false, uninstall: false) {
                 section ("Switches", hideWhenEmpty: true){
                    input "sSwitches", "capability.switch", title: "Select Lights and Switches...", multiple: true, required: false, submitOnChange: true
                        if (sSwitches) {
                        	input "sSwitchCmd", "enum", title: "Command To Send",
                        		options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true
                        	input "delaySwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                        	if (delaySwitches) {
                        		input "sSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        		input "sSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                            }
                        	if (sSwitchCmd) input "sOtherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true                        
                        	if (sOtherSwitch) input "sOtherSwitchCmd", "enum", title: "Command To Send to these other switches", 
                        					options: ["on1":"Turn on","off1":"Turn off","toggle1":"Toggle"], multiple: false, required: false, submitOnChange: true
                        	if (sOtherSwitchCmd)	input "delayOtherSwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                                if (delayOtherSwitches) {
                                    input "sOtherSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                                    input "sOtherSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                                }
                	}
                }
                section ("Dimmers", hideWhenEmpty: true){
                    input "sDimmers", "capability.switchLevel", title: "Select Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (sDimmers) { input "sDimmersCmd", "enum", title: "Command To Send",
                        					options:["on":"Turn on","off":"Turn off", "set":"Set level"], multiple: false, required: false, submitOnChange:true
                        }
                        if (sDimmersCmd) {                       
                       		input "sDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true	
                        	input "delayDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true      
                            if (delayDimmers) {
                            	input "sSecondsDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                                input "sSecondsDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                            }
                       		input "sOtherDimmers", "capability.switchLevel", title: "...and these other Dimmers...", multiple: true, required: false , submitOnChange:true
                        		if (sOtherDimmers) { 
                                	input "sOtherDimmersCmd", "enum", title: "Command To Send to these other Dimmers", 
                        				options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
                        		}
                        		if (sOtherDimmersCmd) {
                                   	input "sOtherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true
									input "delayOtherDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange: true
									if (delayOtherDimmers) {
                                       	input "sSecondsOtherDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                                        input "sSecondsOtherDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                                	}
                     			}
                		}
                }
				section ("Colored lights", hideWhenEmpty: true){
            		input "sHues", "capability.colorControl", title: "Select These Colored Lights...", multiple: true, required: false, submitOnChange:true
            			if (sHues) {
                        	input "sHuesCmd", "enum", title: "Command To Send ", 
                            				options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							if(sHuesCmd == "setColor") {
                            input "sHuesColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
							}
                            if(sHuesCmd == "setColor" || sHuesCmd == "on") {
                            input "sHuesLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], submitOnChange:true                        
        					}
                        }
                        if (sHuesLevel)	input "sHuesOther", "capability.colorControl", title: "...and these other Colored Lights?", multiple: true, required: false, submitOnChange:true
            			if (sHuesOther) {
                        	input "sHuesOtherCmd", "enum", title: "Command To Send to these other Colored Lights", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							if(sHuesOtherCmd == "setColor") {
                            input "sHuesOtherColor", "enum", title: "Which Color?", required: false, multiple:false, options: fillColorSettings().name
                            }
                            if(sHuesOtherCmd == "on" || sHuesOtherCmd == "setColor") {
							input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
        				}
					}
                }
                section ("Flash These Switches", hideWhenEmpty: true) {
                    input "sFlash", "capability.switch", title: "Flash Switch(es)", multiple: true, required: false, submitOnChange:true
                    if (sFlash) {
                    	input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                    	input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true			
                    	input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
                    }
                }
			}
		}     
page name: "pRestrict"
    def pRestrict(){
        dynamicPage(name: "pRestrict", title: "", uninstall: false) {
			section ("Mode Restrictions") {
                input "modes", "mode", title: "Only when mode is", multiple: true, required: false
            }        
            section ("Days - Audio only on these days"){	
                input "days", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                    "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
            }
            section ("Time - Audio only during these times"){
                href "certainTime", title: "Only during a certain time", description: timeIntervalLabel ?: "Tap to set", state: timeIntervalLabel ? "complete" : null
            }   
	    }
	}
page name: "certainTime"
    def certainTime() {
        dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
            section("Beginning at....") {
                input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
                if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
                else {
                    if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
            section("Ending at....") {
                input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
                if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
                else {
                    if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
        }
    }
/************************************************************************************************************
		Base Process
************************************************************************************************************/    
def installed() {
	log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    state.ProfileRelease ="Profile: "  + release()
}

def updated() {
	log.debug "Updated with settings: ${settings}, current app version: ${release()}"
    state.ProfileRelease = "Profile: " + release()
	unsubscribe()
	initialize()
}

def initialize() {
    	state.lastTime
        state.recording = null
        state.recording1
        state.recording2
        state.recording3
        state.lastAction = null
        state.lastActivity
        state.reminderAnsPend = 0
        state.delayAnsPend = 0
        state.reminder1
        state.reminder2
        state.reminder3
        //Alexa Voice Settings
		state.pContCmds = settings.pContCmdsProfile == false ? true : settings.pContCmdsProfile == true ? false : true
        state.pContCmdsR = "init"
        state.pTryAgain = false
        //Sound Cancellation    
        state.pMuteAlexa = settings.pDisableAlexaProfile ?: false
        state.pMuteAll = settings.pDisableALLProfile ?: false
}

/******************************************************************************************************
   PARENT STATUS CHECKS
******************************************************************************************************/
def checkState() {
return state.pMuteAlexa
}
def checkRelease() {
return state.ProfileRelease
}
/******************************************************************************************************
   SPEECH AND TEXT PROCESSING INTERNAL
******************************************************************************************************/
def profileEvaluate(params) {
	def tts = params.ptts
	def intent = params.pintentName        
	def childName = app.label       
	//Data for CoRE 
	def data = [args: tts]
    def dataSet = [:]
    //Output Variables
    def pTryAgain = false
    def pPIN = false
    def String pContCmdsR = (String) "tts"
	def String outputTxt = (String) null 
	def String scheduler = (String) null     
	def String ttsR = (String) null
	def String command = (String) null
	def String deviceType = (String) null
    def String colorMatch = (String) null 

    //Recorded Messages
	def repeat = tts.startsWith("repeat last message") ? true : tts.contains("repeat last message") ? true : tts.startsWith("repeat message") ? true : false
    def whatsUP = "what's up"
	def play = tts.startsWith("play message") ? true : tts.startsWith("play the message") ? true : tts.startsWith("play recording") ? true : tts.startsWith("play recorded") ? true : false
	def recordingNow = tts.startsWith("record a message") ? "record a message" : tts.startsWith("record message") ? "record message" : tts.startsWith("leave a message") ? "leave a message" : tts.startsWith("leave message") ? "leave message" : null
    def whatMessages = tts.startsWith("what messages") ? true : tts.startsWith("how many messages") ? true : tts.contains("have messages") ? true : tts.contains("have any messages") ? true : false
    def deleteMessages = tts.startsWith("delete message 1") ?  "recording" : tts.startsWith("delete message 2") ? "recording1" : tts.startsWith("delete message 3") ? "recording2" : tts.startsWith("delete all messages") ? "all" : tts.startsWith("delete messages") ? "all" : null
	log.warn "Delete messages = ${deleteMessages}"
    //Reminders
    def reminder = tts.startsWith("set a reminder ") ? "set a reminder " : tts.startsWith("set reminder ") ? "set reminder" : tts.startsWith("remind me ") ? "remind me " : tts.startsWith("set the reminder") ? "set the reminder" : null
    def cancelReminder = tts.startsWith("cancel reminder") ? true : tts.startsWith("cancel the reminder") ? true : tts.startsWith("cancel a reminder") ? true : false
    def whatReminders = tts.startsWith("what reminders")
    def cancelReminderNum = tts.startsWith("cancel reminder 1") ?  "reminder1" : tts.startsWith("cancel reminder 2") ? "reminder2" : tts.startsWith("cancel reminder 3") ? "reminder3" : null

    //Voice Activation Settings
    def muteAll = tts.contains("disable sound") ? "mute" : tts.contains("disable audio") ? "mute" : tts.contains("mute audio") ? "mute" : tts.contains("silence audio") ? "mute" : null
    	muteAll = tts.contains("activate sound") ? "unmute" : tts.contains("enable audio") ? "unmute" : tts.contains("unmute audio") ? "unmute" : muteAll
    def muteAlexa = tts.contains("disable Alexa") ? "mute" : tts.contains("silence Alexa") ? "mute" : tts.contains("mute Alexa") ? "mute" : null
    	muteAlexa = tts.contains("enable Alexa") ? "unmute" : tts.contains("start Alexa") ? "unmute" : tts.contains("unmute Alexa") ? "unmute" : muteAll
	def test = tts.contains("this is a test") ? true : tts.contains("a test") ? true : false
    
    if (parent.debug) log.debug "Message received from Parent with: (tts) = '${tts}', (intent) = '${intent}', (childName) = '${childName}', current app version: ${release()}"  
    
    if (pSendSettings() == "complete" || pGroupSettings() == "complete"){
        if (intent == childName){
			if (test){
				outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
				return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]       
    		}
            //Voice Activated Commands
            if(muteAll == "mute" || muteAll == "unmute"){
                if(muteAll == "mute"){
                    state.pMuteAll = true
                    outputTxt = "Ok, audio messages have been disabled"       
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                
                }
                else { 
                    state.pMuteAll = false
                    outputTxt = "Ok, audio messages have been enabled"       
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
                }
            }
            if(muteAlexa == "mute" || muteAlexa == "unmute"){
                if(muteAlexa == "mute"){
                    state.pMuteAlexa = true
                    outputTxt = "Ok, Alexa Feedback Responses have been disabled"       
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                
                }
                else { 
                    state.pMuteAlexa = false
                    outputTxt = "Ok, Alexa Feedback Responses have been enabled"       
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
                }
            } 
            //Repeat Message
            if (repeat == true || play == true  || tts == whatsUP) {
                if (tts == repeat || tts == whatsUP) {
                    outputTxt = getLastMessage()          
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
                else {
                    def numMessages = state.recording2 != null ? "3 messages" : state.recording1 != null ? "2 messages" : state.recording != null ? "one message" : "no messages" 
                    if (numMessages == "3 messages") outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " , " + state.recording2 + " To delete your messages, just say: delete messages"
                    else if (numMessages == "2 messages") outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " To delete your messages, just say: delete messages"
                    else if (numMessages == "one message") outputTxt = "You have " + numMessages + " pending, " + state.recording + " To delete your message, just say: delete messages"
                    else if (numMessages == "no messages") outputTxt = "You have " + numMessages + " pending "
                    //"Your last recording was, " + state.recording
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
            }  
            else {
                //Schedule Reminders
                if(state.reminderAnsPend >0){
                    int iLength
                    def unit = tts.endsWith("minutes") ? "minutes" : tts.endsWith("hours") ? "hours" : tts.endsWith("hour") ? "hours" : tts.endsWith("day") ? "days" : tts.endsWith("days") ? "days" : "undefined"
                    def length = tts.findAll( /\d+/ )*.toInteger()
                    if(length[0] !=null) {
                        iLength = (int)length.get(0)                    
                    }
                    else {
                        outputTxt = "sorry, I was unable to get the number,  "
                        state.reminderAnsPend = 0
                        pTryAgain = true
                        return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }	
                    if(unit !="undefined" && iLength != null){
                        if (state.reminderAnsPend == 1)	{
                            ttsR = state.reminder1
                            scheduler = "reminderHandler1"
                            outputTxt = "I have scheduled a reminder " + ttsR + " in " + tts
                            if(parent.debug) log.debug "scheduling reminder 1 with outputTxt = ${outputTxt}"
                        }
                        else {
                            if (state.reminderAnsPend == 2)	{
                                ttsR = state.reminder2
                                scheduler = "reminderHandler2"
                                outputTxt = "I have scheduled a reminder " + ttsR + " in " + tts
                                if(parent.debug) log.debug "scheduling reminder 2 with outputTxt = ${outputTxt}"
                            }
                            else {
                                if (state.reminderAnsPend == 3)	{
                                    tts = state.reminder3
                                    scheduler = "reminderHandler3"
                                    outputTxt = "I have scheduled a reminder " + ttsR + " in " + tts
                                    if(parent.debug) log.debug "scheduling reminder 3 with outputTxt = ${outputTxt}"
                                }
                            }
                        }
                        if (unit == "minutes" && iLength>0 ) {runIn(iLength*60, scheduler)}
                        else {
                            if (unit == "hours" && iLength>0 ) { runIn(iLength*3600, scheduler)}
                                else{
                                    if(unit == "days"){
                                        def currDate = new Date(now() + location.timeZone.rawOffset)
                                        runOnce(currDate + iLength , scheduler)
                                    }
                                }
                        }
                        state.reminderAnsPend = 0
                        return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                    else {
                        outputTxt = "sorry, I was unable to schedule your reminder, "
                        state.reminderAnsPend = 0
                        pTryAgain = true
                        return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }
                //Cancel Reminders
                 if (cancelReminder == true || cancelReminderNum != null) {
                    def String cancelMeText = (String) null
                    if (cancelMe == "reminder2" || cancelReminderNum == "reminder2") {
                                unschedule("reminderHandler2")
                                cancelMeText = state.reminder2
                                state.reminder2 = null
                                state.reminderAnsPend = 0
                            }
                     if(cancelMe != "undefined" || cancelReminderNum != null) {
                        if (cancelMe == "reminder1" || cancelReminderNum == "reminder1") {                        
                            unschedule("reminderHandler1")
                            cancelMeText = state.reminder1
                            state.reminder1 = null
                            state.reminderAnsPend = 0
                         }
                         else {
                            if (cancelMe == "reminder2" || cancelReminderNum == "reminder2") {
                                unschedule("reminderHandler2")
                                cancelMeText = state.reminder2
                                state.reminder2 = null
                                state.reminderAnsPend = 0
                            }
                            else {
                                if (cancelMe == "reminder3" || cancelReminderNum == "reminder3") {
                                unschedule("reminderHandler3")
                                cancelMeText = state.reminder3
                                state.reminder3 = null
                                state.reminderAnsPend = 0
                                }
                            }
                        }
                        outputTxt = "Ok, canceling reminder to " + cancelMeText
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                    else {
                        outputTxt = "sorry, I was unable to cancel your reminder "
                        state.reminderAnsPend = 0
                        pTryAgain = true
                        return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }
                //Record a Message
                if (recordingNow || reminder || whatReminders == true || whatMessages == true || deleteMessages != null) {  
                    if (recordingNow) {
                    def record
                    record = tts.replace("record a message", "").replace("record message", "").replace("leave a message", "").replace("leave message", "")
                    if (parent.debug) log.debug "Recording: (record) = '${record}' for (intent) = '${intent}'" 
                    //state.recording = record
                    if (state.recording == null || state.recording1 == null || state.recording2 == null) {    
                            if(state.recording == null || state.recording == "" ) {
                                state.recording = record
                                //state.reminderAnsPend = 1
                            }
                            else if(state.recording1 == null || state.recording1 == "") {
                                state.recording1 = record
                                //state.reminderAnsPend = 2
                            }  
                            else if(state.recording2 == null || state.recording2 == "") {
                                state.recording2 = record
                                //state.reminderAnsPend = 3
                            }
                            outputTxt = "Ok, message recorded. To play it later, just say: play message."
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                        else {
                            pTryAgain = true
                            outputTxt = "You have reached the maximum allowed number of recordings. Please delete one or more messages before recording another one, "
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }            
                    }                        
					if (whatMessages == true) {
                        def numMessages = state.recording2 != null ? "3 messages" : state.recording1 != null ? "2 messages" : state.recording != null ? "one message" : "no messages" 
						if(numMessages == "3 messages") outputTxt = outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " , " + state.recording2 + " To delete your messages, just say: delete messages"
                    	else if (numMessages == "2 messages") outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " To delete your messages, just say: delete messages"
                    	else if (numMessages == "one message") outputTxt = "You have " + numMessages + " pending, " + state.recording + " To delete your message, just say: delete messages"
                    	else if (numMessages == "no messages") outputTxt = "You have " + numMessages + " pending "
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    } 
                    //Delete Messages
					if (deleteMessages != null) {
                        def String deleteMeText = (String) null
                        if (deleteMessages == "recording") {
							deleteMeText = state.recording
                            state.recording = null
                       	}	
                        else {
                        	if (deleteMessages == "recording1") {
                                deleteMeText = state.recording1
                                state.recording1 = null
                         	}
							else {
                            	if (deleteMessages == "recording2") {
                                    deleteMeText = state.recording2
                                    state.recording2 = null
                                 }
                                 else if (deleteMessages == "all"){
									deleteMeText = "all messages"
                                    state.recording = null
                                    state.recording1 = null
                                    state.recording2 = null
                            	}
                            }
                            outputTxt = "Ok, deleted " + deleteMeText
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                    }                    
                    //Set a reminder        	
                    if (reminder) {
                    def remindMe = tts.replace("${reminder}", "")
                    if (parent.debug) log.debug "Setting Reminder: (remindMe) = '${remindMe}' for (intent) = '${intent}'" 
                        if (state.reminder1 == null || state.reminder2 == null || state.reminder3 == null) {
                            if(state.reminder1 == null || state.reminder1 == "" ) {
                                state.reminder1 = remindMe
                                state.reminderAnsPend = 1

                            }
                            else if(state.reminder2 == null || state.reminder2 == "") {
                                state.reminder2 = remindMe
                                state.reminderAnsPend = 2
                            }  
                            else if(state.reminder3 == null || state.reminder3 == "") {
                                state.reminder3 = remindMe
                                state.reminderAnsPend = 3
                            }
                            outputTxt = "For how long?"
                            pContCmdsR = "reminder"
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                        else {
                            pTryAgain = true
                            outputTxt = "You have reached the maximum allowed numbers of reminders. Please cancel a reminder before scheduling another one."
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }            
                    }
                    if (whatReminders == true) {
                            def numReminders = state.reminder3 != null ? "3 reminders" : state.reminder2 != null ? "2 reminders" : state.reminder1 != null ? "one reminder" : "no reminders" 
                            outputTxt = "You have " + numReminders + "scheduled, " + state.reminder1 + " , " + state.reminder2 + " , " + state.reminder3
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }    
                }
                //EXECUTE PROFILE ACTIONS
                 if (command == "run" && deviceType == "profile"){    	
                    outputTxt = "Running profile"
                    ttsActions(tts)
                    pContCmdsR = "run"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
                //EXECUTE PROFILE ACTIONS WITH DELAY
                if (command == "delay" && deviceType == "profile"){ 
                    state.lastAction = "Running scheduled actions"
                    state.delayAnsPend = 1
                    outputTxt = "For how long?"
                    pContCmdsR = "reminder"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
                //SCHEDULE ACTIONS WITH DELAY
                if(state.delayAnsPend >0 ){
                    int iLength
                    def unit = tts.endsWith("minutes") ? "minutes" : tts.endsWith("hours") ? "hours" : tts.endsWith("hour") ? "hours" : tts.endsWith("day") ? "days" : tts.endsWith("days") ? "days" : "undefined"
                    def length = tts.findAll( /\d+/ )*.toInteger()
                        if(length[0] !=null) {
                            iLength = (int)length.get(0)                    
                        }
                        else {
                            outputTxt = "sorry, I was unable to get the number,  "
                            state.delayAnsPend = 0
                            pTryAgain = true
                            return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }	
                    if(unit !="undefined" && iLength != null){ 
                        outputTxt = "I have scheduled the actions for " + app.label + " to run in " + tts
                        if(parent.debug) log.debug "scheduling delay with outputTxt = ${outputTxt}"
                        if (unit == "minutes" && iLength>0 ) {runIn(iLength*60, "ttsHandler")}
                        else {
                            if (unit == "hours" && iLength>0 ) { runIn(iLength*3600, "ttsHandler")}
                            else{
                                if(unit == "days"){
                                    def currDate = new Date(now() + location.timeZone.rawOffset)
                                    runOnce(currDate + iLength , "ttsHandler")
                                }
                            }
                         }
                    }
                    else {
                        outputTxt = "sorry, I was unable to schedule your reminder, "
                        state.reminderAnsPend = 0
                        pTryAgain = true
                        return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }       
                    state.reminderAnsPend = 0
                    return  ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
			}
      	}
	}
}
/******************************************************************************************************
   SPEECH AND TEXT ALEXA RESPONSE
******************************************************************************************************/
def ttsHandler(tts) {
	def result = tts
    def cm = app.label
	//Preparing Alexa Response
    if(parent.debug) log.debug " ttshandler settings: pAlexaCustResp=${pAlexaCustResp},pAlexaRepeat=${pAlexaRepeat},tts=${tts}"
    if (pAlexaCustResp) {
			result = settings.pAlexaCustResp
	}
	else {
    	if (pAlexaRepeat) {
        	result = "I have delivered the following message to " + cm + " , " + tts
        }
        else {
       	    result = "Message sent to " + cm + " , " 
        }
    }
	ttsActions(tts)
    if(parent.debug) log.debug "running actions, sending result to Parent = ${result}"
    return result
}
/******************************************************************************************************
   SPEECH AND TEXT ACTION
******************************************************************************************************/
def ttsActions(tts) {
	def String ttx = (String) null 	
	//Seding Data to CoRE 
	def data = [args: tts ]
	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
	if (parent.debug) log.debug "sendNotificationEvent sent to CoRE was '${app.label}' from the TTS process section"
    //define audio message
    if(pRunMsg){
    	tts = settings.pRunMsg
    }
    else {
    	if (pPreMsg) {
			tts = pPreMsg + tts
		}
        else {
			tts = tts
		}
    if(parent.debug) log.debug "defined tts = ${tts}"
    }
	//define text message
    if(pRunTextMsg){
  		ttx = settings.pRunTextMsg
    }  
    else {
    	if (pPreTextMsg) {
			ttx = pPreTextMsg + tts
		}
        else {
			ttx = tts
		}
    if(parent.debug) log.debug "defined sms = ${ttx}"
    }
    if(state.pMuteAll == false){
        if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            if (synthDevice) {
                synthDevice?.speak(tts) 
                if (parent.debug) log.debug "Sending message to Synthesis Devices"
            }
            if (tts) {
                state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
            }
            else {
                state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
                if (parent.debug) log.debug "You selected the custom message option but did not enter a message"
            }
            if (sonosDevice){ // 2/22/2017 updated Sono handling when speaker is muted
                def currVolLevel = sonosDevice.latestValue("level")
            	def currMuteOn = sonosDevice.latestValue("mute").contains("muted")
                if (parent.debug) log.debug "currVolSwitchOff = ${currVolSwitchOff}, vol level = ${currVolLevel}, currMuteOn = ${currMuteOn} "
                    if (currMuteOn) { 
                        if (parent.debug) log.warn "speaker is on mute, sending unmute command"
                        sonosDevice.unmute()
                    }
                def sVolume = settings.volume ?: 20
                sonosDevice?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume)
                if (parent.debug) log.info "Playing message on the music player '${sonosDevice}' at volume '${volume}'" 
            }
        }
		if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
			sendtxt(ttx)
        }
	}
	else {
			if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
			if (parent.debug) log.debug "Only sending sms because disable voice message is ON"
            sendtxt(ttx)
		}
    }   
   if (sHues) {               
		processColor()
	}
	if (sFlash) {
		flashLights()
	}
	profileDeviceControl()
	if (pRoutine) {
		location.helloHome?.execute(settings.pRoutine)
    }
	if (pRoutine2) {
		location.helloHome?.execute(settings.pRoutine2)
	}
	if (pMode) {
		setLocationMode(pMode)
	}
    if (push && pPreTextMsg) {
			tts = pPreTextMsg + tts
            sendPushMessage(tts)
		}
        else if (push) {
        	sendPushMessage(tts)
            }
}
        
/***********************************************************************************************************************
    LAST MESSAGE HANDLER
***********************************************************************************************************************/
def getLastMessage() {
	def cOutputTxt = "The last message sent to " + app.label + " was," + state.lastMessage + ", and it was sent at, " + state.lastTime
	return  cOutputTxt 
	if (parent.debug) log.debug "Sending last message to parent '${cOutputTxt}' "
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getAllOk() {
	modeOk && daysOk && timeOk
}
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	if(parent.debug) log.debug "modeOk = $result"
    result
} 
private getDayOk() {
    def result = true
if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	if(parent.debug) log.debug "daysOk = $result"
	result
}
private getTimeOk() {
	def result = true
	if ((starting && ending) ||
	(starting && endingX in ["Sunrise", "Sunset"]) ||
	(startingX in ["Sunrise", "Sunset"] && ending) ||
	(startingX in ["Sunrise", "Sunset"] && endingX in ["Sunrise", "Sunset"])) {
		def currTime = now()
		def start = null
		def stop = null
		def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: startSunriseOffset, sunsetOffset: startSunsetOffset)
		if(startingX == "Sunrise") start = s.sunrise.time
		else if(startingX == "Sunset") start = s.sunset.time
		else if(starting) start = timeToday(starting,location.timeZone).time
		s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: endSunriseOffset, sunsetOffset: endSunsetOffset)
		if(endingX == "Sunrise") stop = s.sunrise.time
		else if(endingX == "Sunset") stop = s.sunset.time
		else if(ending) stop = timeToday(ending,location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	if (parent.debug) log.trace "getTimeOk = $result."
    }
    return result
}
private hhmm(time, fmt = "h:mm a") {
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
private offset(value) {
	def result = value ? ((value > 0 ? "+" : "") + value + " min") : ""
}
private timeIntervalLabel() {
	def result = ""
	if      (startingX == "Sunrise" && endingX == "Sunrise") result = "Sunrise" + offset(startSunriseOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunrise" && endingX == "Sunset") result = "Sunrise" + offset(startSunriseOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunset" && endingX == "Sunrise") result = "Sunset" + offset(startSunsetOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunset" && endingX == "Sunset") result = "Sunset" + offset(startSunsetOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunrise" && ending) result = "Sunrise" + offset(startSunriseOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (startingX == "Sunset" && ending) result = "Sunset" + offset(startSunsetOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (starting && endingX == "Sunrise") result = hhmm(starting) + " to Sunrise" + offset(endSunriseOffset)
	else if (starting && endingX == "Sunset") result = hhmm(starting) + " to Sunset" + offset(endSunsetOffset)
	else if (starting && ending) result = hhmm(starting) + " to " + hhmm(ending, "h:mm a z")
}
/***********************************************************************************************************************
    SMS HANDLER
***********************************************************************************************************************/
private void sendtxt(message) {
    if (parent.debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
            if (parent.debug) log.debug "Sending sms to selected reipients"
    } 
    else {
    	if (push) { 
    		sendPushMessage
            	if (parent.debug) log.debug "Sending push message to selected reipients"
        }
    } 
    if (notify) {
        sendNotificationEvent(message)
             	if (parent.debug) log.debug "Sending notification to mobile app"

    }
    if (sms) {
        sendText(sms, message)
        if (parent.debug) log.debug "Processing message for selected phones"
	}
}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}
/***********************************************************************************************************************
    MISC. - REMINDERS HANDLER
***********************************************************************************************************************/
private reminderHandler1() {
def text = state.reminder1
state.reminder1 = null
ttsActions(text)
}
private reminderHandler2() {
def text = state.reminder2
state.reminder2 = null
ttsActions(text)
}
private reminderHandler3() {
def text = state.reminder3
state.reminder3 = null
ttsActions(text)
}
/************************************************************************************************************
   Switch/Color/Dimmer/Toggle Handlers
************************************************************************************************************/
// Used for delayed devices
def turnOnSwitch() { sSwitches?.on() }  
def turnOffSwitch() { sSwitches?.off() }
def turnOnOtherSwitch() { sOtherSwitch?.on() }
def turnOffOtherSwitch() { sOtherSwitch?.off() }  
def turnOnDimmers() { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
	sDimmers?.setLevel(sDimmersLVL) }
def turnOffDimmers() { sDimmers?.off() }
def turnOnOtherDimmers() { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
	sOtherDimmers?.setLevel(sOtherDimmersLVL) }
def turnOffOtherDimmers() { sOtherDimmers?.off() }   

// Primary control of profile triggered lights/switches when delayed
def profileDeviceControl() {
	if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sOtherSecondsOn)  { runIn(sOtherSecondsOn,turnOnOtherSwitch)}
    if (sOtherSecondsOff) { runIn(sOtherSecondsOff,turnOffOtherSwitch)}
	if (sSecondsDimmers) { runIn(sSecondsDimmers,turnOnDimmers)}
	if (sSecondsDimmersOff) { runIn(sSecondsDimmersOff,turnOffDimmers)}
    if (sSecondsOtherDimmers) { runIn(sSecondsOtherDimmers,turnOnOtherDimmers)}
	if (sSecondsOtherDimmersOff) { runIn(sSecondsOtherDimmersOff,turnOffOtherDimmers)}
// Control of Lights and Switches when not delayed            
    if (!sSecondsOn) {
		if  (sSwitchCmd == "on") { sSwitches?.on() }
			else if (sSwitchCmd == "off") { sSwitches?.off() }
		if (sSwitchCmd == "toggle") { toggle() }
		if (sOtherSwitchCmd == "on") { sOtherSwitch?.on() }
			else if (sOtherSwitchCmd == "off") { sOtherSwitch?.off() }
		if (otherSwitchCmd == "toggle") { toggle() }
		
        if (sDimmersCmd == "set" && sDimmers) { def level = sDimmersLVL < 0 || !sDimmersLVL ?  0 : sDimmersLVL >100 ? 100 : sDimmersLVL as int
			sDimmers?.setLevel(level) }
		if (sOtherDimmersCmd == "set" && sOtherDimmers) { def otherLevel = sOtherDimmersLVL < 0 || !sOtherDimmersLVL ?  0 : sOtherDimmersLVL >100 ? 100 : sOtherDimmersLVL as int
			sOtherDimmers?.setLevel(otherLevel) }
	}
}

private toggle() {
	if (sSwitches) {
        if (sSwitches?.currentValue('switch').contains('on')) {
            sSwitches?.off()
            }
        else if (sSwitches?.currentValue('switch').contains('off')) {
            sSwitches?.on()
            }
    }
    if (sOtherSwitch) {
        if (sOtherSwitch?.currentValue('switch').contains('on')) {
            sOtherSwitch?.off()
        }
        else if (sOtherSwitch?.currentValue('switch').contains('off')) {
            sOtherSwitch?.on()
            }
	}
}
/************************************************************************************************************
   Flashing Lights Handler
************************************************************************************************************/
private flashLights() {
 	if (parent.debug) log.debug "The Flash Switches Option has been activated"
	def doFlash = true
	def onFor = onFor ?: 60000/60
	def offFor = offFor ?: 60000/60
	def numFlashes = numFlashes ?: 3
	
    if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
	}
	if (doFlash) {
		state.lastActivated = now()
		def initialActionOn = sFlash.collect{it.currentflashSwitch != "on"}
		def delay = 0L
		
        numFlashes.times {
			sFlash.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
                }
				else {
					s.off(delay:delay)                   
                } 
			}
			delay += onFor
			sFlash.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.off(delay: delay)
				}
				else {
					s.on(delay:delay)
                }
			}
			delay += offFor
		}
	}
}
/************************************************************************************************************
   Custom Color Filter
************************************************************************************************************/
private setRandomColorName(){
	for (bulb in gHues) {    
		int hueLevel = !level ? 100 : level
		int hueHue = Math.random() *100 as Integer
		def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
        bulb.setColor(randomColor)
    }
}
private processColor() {
    if (sHuesCmd == "on") { sHues?.on() }
	if (sHuesCmd == "off") { sHues?.off() }
    if (sHuesOtherCmd == "on") { sHuesOther?.on() }
    if (sHuesOtherCmd == "off") { sHuesOther?.off() }
		def hueSetVals = getColorName("${sHuesColor}",level)
        	sHues?.setColor(hueSetVals)
        hueSetVals = getColorName("${sHuesOtherColor}",level)
        	sHuesOther?.setColor(hueSetVals)
}
private getColorName(cName, level) {
    for (color in fillColorSettings()) {
		if (color.name.toLowerCase() == cName.toLowerCase()) {
        	int hueVal = Math.round(color.h / 3.6)
            int hueLevel = !level ? color.l : level
			def hueSet = [hue: hueVal, saturation: color.s, level: hueLevel]
            return hueSet
		}
	}
}
def fillColorSettings() {
	return [
		[ name: "Soft White",				rgb: "#B6DA7C",		h: 83,		s: 44,		l: 67,	],
		[ name: "Warm White",				rgb: "#DAF17E",		h: 51,		s: 20,		l: 100,	],
        [ name: "Very Warm White",			rgb: "#DAF17E",		h: 51,		s: 60,		l: 51,	],
		[ name: "Daylight White",			rgb: "#CEF4FD",		h: 191,		s: 9,		l: 90,	],
		[ name: "Daylight",					rgb: "#CEF4FD",		h: 191,		s: 9,		l: 90,	],        
		[ name: "Cool White",				rgb: "#F3F6F7",		h: 187,		s: 19,		l: 96,	],
		[ name: "White",					rgb: "#FFFFFF",		h: 0,		s: 0,		l: 100,	],
		[ name: "Alice Blue",				rgb: "#F0F8FF",		h: 208,		s: 100,		l: 97,	],
		[ name: "Antique White",			rgb: "#FAEBD7",		h: 34,		s: 78,		l: 91,	],
		[ name: "Aqua",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
		[ name: "Aquamarine",				rgb: "#7FFFD4",		h: 160,		s: 100,		l: 75,	],
		[ name: "Azure",					rgb: "#F0FFFF",		h: 180,		s: 100,		l: 97,	],
		[ name: "Beige",					rgb: "#F5F5DC",		h: 60,		s: 56,		l: 91,	],
		[ name: "Bisque",					rgb: "#FFE4C4",		h: 33,		s: 100,		l: 88,	],
		[ name: "Blanched Almond",			rgb: "#FFEBCD",		h: 36,		s: 100,		l: 90,	],
		[ name: "Blue",						rgb: "#0000FF",		h: 240,		s: 100,		l: 50,	],
		[ name: "Blue Violet",				rgb: "#8A2BE2",		h: 271,		s: 76,		l: 53,	],
		[ name: "Brown",					rgb: "#A52A2A",		h: 0,		s: 59,		l: 41,	],
		[ name: "Burly Wood",				rgb: "#DEB887",		h: 34,		s: 57,		l: 70,	],
		[ name: "Cadet Blue",				rgb: "#5F9EA0",		h: 182,		s: 25,		l: 50,	],
		[ name: "Chartreuse",				rgb: "#7FFF00",		h: 90,		s: 100,		l: 50,	],
		[ name: "Chocolate",				rgb: "#D2691E",		h: 25,		s: 75,		l: 47,	],
		[ name: "Coral",					rgb: "#FF7F50",		h: 16,		s: 100,		l: 66,	],
		[ name: "Corn Flower Blue",			rgb: "#6495ED",		h: 219,		s: 79,		l: 66,	],
		[ name: "Corn Silk",				rgb: "#FFF8DC",		h: 48,		s: 100,		l: 93,	],
		[ name: "Crimson",					rgb: "#DC143C",		h: 348,		s: 83,		l: 58,	],
		[ name: "Cyan",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
		[ name: "Dark Blue",				rgb: "#00008B",		h: 240,		s: 100,		l: 27,	],
		[ name: "Dark Cyan",				rgb: "#008B8B",		h: 180,		s: 100,		l: 27,	],
		[ name: "Dark Golden Rod",			rgb: "#B8860B",		h: 43,		s: 89,		l: 38,	],
		[ name: "Dark Gray",				rgb: "#A9A9A9",		h: 0,		s: 0,		l: 66,	],
		[ name: "Dark Green",				rgb: "#006400",		h: 120,		s: 100,		l: 20,	],
		[ name: "Dark Khaki",				rgb: "#BDB76B",		h: 56,		s: 38,		l: 58,	],
		[ name: "Dark Magenta",				rgb: "#8B008B",		h: 300,		s: 100,		l: 27,	],
		[ name: "Dark Olive Green",			rgb: "#556B2F",		h: 82,		s: 39,		l: 30,	],
		[ name: "Dark Orange",				rgb: "#FF8C00",		h: 33,		s: 100,		l: 50,	],
		[ name: "Dark Orchid",				rgb: "#9932CC",		h: 280,		s: 61,		l: 50,	],
		[ name: "Dark Red",					rgb: "#8B0000",		h: 0,		s: 100,		l: 27,	],
		[ name: "Dark Salmon",				rgb: "#E9967A",		h: 15,		s: 72,		l: 70,	],
		[ name: "Dark Sea Green",			rgb: "#8FBC8F",		h: 120,		s: 25,		l: 65,	],
		[ name: "Dark Slate Blue",			rgb: "#483D8B",		h: 248,		s: 39,		l: 39,	],
		[ name: "Dark Slate Gray",			rgb: "#2F4F4F",		h: 180,		s: 25,		l: 25,	],
		[ name: "Dark Turquoise",			rgb: "#00CED1",		h: 181,		s: 100,		l: 41,	],
		[ name: "Dark Violet",				rgb: "#9400D3",		h: 282,		s: 100,		l: 41,	],
		[ name: "Deep Pink",				rgb: "#FF1493",		h: 328,		s: 100,		l: 54,	],
		[ name: "Deep Sky Blue",			rgb: "#00BFFF",		h: 195,		s: 100,		l: 50,	],
		[ name: "Dim Gray",					rgb: "#696969",		h: 0,		s: 0,		l: 41,	],
		[ name: "Dodger Blue",				rgb: "#1E90FF",		h: 210,		s: 100,		l: 56,	],
		[ name: "Fire Brick",				rgb: "#B22222",		h: 0,		s: 68,		l: 42,	],
		[ name: "Floral White",				rgb: "#FFFAF0",		h: 40,		s: 100,		l: 97,	],
		[ name: "Forest Green",				rgb: "#228B22",		h: 120,		s: 61,		l: 34,	],
		[ name: "Fuchsia",					rgb: "#FF00FF",		h: 300,		s: 100,		l: 50,	],
		[ name: "Gainsboro",				rgb: "#DCDCDC",		h: 0,		s: 0,		l: 86,	],
		[ name: "Ghost White",				rgb: "#F8F8FF",		h: 240,		s: 100,		l: 99,	],
		[ name: "Gold",						rgb: "#FFD700",		h: 51,		s: 100,		l: 50,	],
		[ name: "Golden Rod",				rgb: "#DAA520",		h: 43,		s: 74,		l: 49,	],
		[ name: "Gray",						rgb: "#808080",		h: 0,		s: 0,		l: 50,	],
		[ name: "Green",					rgb: "#008000",		h: 120,		s: 100,		l: 25,	],
		[ name: "Green Yellow",				rgb: "#ADFF2F",		h: 84,		s: 100,		l: 59,	],
		[ name: "Honeydew",					rgb: "#F0FFF0",		h: 120,		s: 100,		l: 97,	],
		[ name: "Hot Pink",					rgb: "#FF69B4",		h: 330,		s: 100,		l: 71,	],
		[ name: "Indian Red",				rgb: "#CD5C5C",		h: 0,		s: 53,		l: 58,	],
		[ name: "Indigo",					rgb: "#4B0082",		h: 275,		s: 100,		l: 25,	],
		[ name: "Ivory",					rgb: "#FFFFF0",		h: 60,		s: 100,		l: 97,	],
		[ name: "Khaki",					rgb: "#F0E68C",		h: 54,		s: 77,		l: 75,	],
		[ name: "Lavender",					rgb: "#E6E6FA",		h: 240,		s: 67,		l: 94,	],
		[ name: "Lavender Blush",			rgb: "#FFF0F5",		h: 340,		s: 100,		l: 97,	],
		[ name: "Lawn Green",				rgb: "#7CFC00",		h: 90,		s: 100,		l: 49,	],
		[ name: "Lemon Chiffon",			rgb: "#FFFACD",		h: 54,		s: 100,		l: 90,	],
		[ name: "Light Blue",				rgb: "#ADD8E6",		h: 195,		s: 53,		l: 79,	],
		[ name: "Light Coral",				rgb: "#F08080",		h: 0,		s: 79,		l: 72,	],
		[ name: "Light Cyan",				rgb: "#E0FFFF",		h: 180,		s: 100,		l: 94,	],
		[ name: "Light Golden Rod Yellow",	rgb: "#FAFAD2",		h: 60,		s: 80,		l: 90,	],
		[ name: "Light Gray",				rgb: "#D3D3D3",		h: 0,		s: 0,		l: 83,	],
		[ name: "Light Green",				rgb: "#90EE90",		h: 120,		s: 73,		l: 75,	],
		[ name: "Light Pink",				rgb: "#FFB6C1",		h: 351,		s: 100,		l: 86,	],
		[ name: "Light Salmon",				rgb: "#FFA07A",		h: 17,		s: 100,		l: 74,	],
		[ name: "Light Sea Green",			rgb: "#20B2AA",		h: 177,		s: 70,		l: 41,	],
		[ name: "Light Sky Blue",			rgb: "#87CEFA",		h: 203,		s: 92,		l: 75,	],
		[ name: "Light Slate Gray",			rgb: "#778899",		h: 210,		s: 14,		l: 53,	],
		[ name: "Light Steel Blue",			rgb: "#B0C4DE",		h: 214,		s: 41,		l: 78,	],
		[ name: "Light Yellow",				rgb: "#FFFFE0",		h: 60,		s: 100,		l: 94,	],
		[ name: "Lime",						rgb: "#00FF00",		h: 120,		s: 100,		l: 50,	],
		[ name: "Lime Green",				rgb: "#32CD32",		h: 120,		s: 61,		l: 50,	],
		[ name: "Linen",					rgb: "#FAF0E6",		h: 30,		s: 67,		l: 94,	],
		[ name: "Maroon",					rgb: "#800000",		h: 0,		s: 100,		l: 25,	],
		[ name: "Medium Aquamarine",		rgb: "#66CDAA",		h: 160,		s: 51,		l: 60,	],
		[ name: "Medium Blue",				rgb: "#0000CD",		h: 240,		s: 100,		l: 40,	],
		[ name: "Medium Orchid",			rgb: "#BA55D3",		h: 288,		s: 59,		l: 58,	],
		[ name: "Medium Purple",			rgb: "#9370DB",		h: 260,		s: 60,		l: 65,	],
		[ name: "Medium Sea Green",			rgb: "#3CB371",		h: 147,		s: 50,		l: 47,	],
		[ name: "Medium Slate Blue",		rgb: "#7B68EE",		h: 249,		s: 80,		l: 67,	],
		[ name: "Medium Spring Green",		rgb: "#00FA9A",		h: 157,		s: 100,		l: 49,	],
		[ name: "Medium Turquoise",			rgb: "#48D1CC",		h: 178,		s: 60,		l: 55,	],
		[ name: "Medium Violet Red",		rgb: "#C71585",		h: 322,		s: 81,		l: 43,	],
		[ name: "Midnight Blue",			rgb: "#191970",		h: 240,		s: 64,		l: 27,	],
		[ name: "Mint Cream",				rgb: "#F5FFFA",		h: 150,		s: 100,		l: 98,	],
		[ name: "Misty Rose",				rgb: "#FFE4E1",		h: 6,		s: 100,		l: 94,	],
		[ name: "Moccasin",					rgb: "#FFE4B5",		h: 38,		s: 100,		l: 85,	],
		[ name: "Navajo White",				rgb: "#FFDEAD",		h: 36,		s: 100,		l: 84,	],
		[ name: "Navy",						rgb: "#000080",		h: 240,		s: 100,		l: 25,	],
		[ name: "Old Lace",					rgb: "#FDF5E6",		h: 39,		s: 85,		l: 95,	],
		[ name: "Olive",					rgb: "#808000",		h: 60,		s: 100,		l: 25,	],
		[ name: "Olive Drab",				rgb: "#6B8E23",		h: 80,		s: 60,		l: 35,	],
		[ name: "Orange",					rgb: "#FFA500",		h: 39,		s: 100,		l: 50,	],
		[ name: "Orange Red",				rgb: "#FF4500",		h: 16,		s: 100,		l: 50,	],
		[ name: "Orchid",					rgb: "#DA70D6",		h: 302,		s: 59,		l: 65,	],
		[ name: "Pale Golden Rod",			rgb: "#EEE8AA",		h: 55,		s: 67,		l: 80,	],
		[ name: "Pale Green",				rgb: "#98FB98",		h: 120,		s: 93,		l: 79,	],
		[ name: "Pale Turquoise",			rgb: "#AFEEEE",		h: 180,		s: 65,		l: 81,	],
		[ name: "Pale Violet Red",			rgb: "#DB7093",		h: 340,		s: 60,		l: 65,	],
		[ name: "Papaya Whip",				rgb: "#FFEFD5",		h: 37,		s: 100,		l: 92,	],
		[ name: "Peach Puff",				rgb: "#FFDAB9",		h: 28,		s: 100,		l: 86,	],
		[ name: "Peru",						rgb: "#CD853F",		h: 30,		s: 59,		l: 53,	],
		[ name: "Pink",						rgb: "#FFC0CB",		h: 350,		s: 100,		l: 88,	],
		[ name: "Plum",						rgb: "#DDA0DD",		h: 300,		s: 47,		l: 75,	],
		[ name: "Powder Blue",				rgb: "#B0E0E6",		h: 187,		s: 52,		l: 80,	],
		[ name: "Purple",					rgb: "#800080",		h: 300,		s: 100,		l: 25,	],
		[ name: "Red",						rgb: "#FF0000",		h: 0,		s: 100,		l: 50,	],
		[ name: "Rosy Brown",				rgb: "#BC8F8F",		h: 0,		s: 25,		l: 65,	],
		[ name: "Royal Blue",				rgb: "#4169E1",		h: 225,		s: 73,		l: 57,	],
		[ name: "Saddle Brown",				rgb: "#8B4513",		h: 25,		s: 76,		l: 31,	],
		[ name: "Salmon",					rgb: "#FA8072",		h: 6,		s: 93,		l: 71,	],
		[ name: "Sandy Brown",				rgb: "#F4A460",		h: 28,		s: 87,		l: 67,	],
		[ name: "Sea Green",				rgb: "#2E8B57",		h: 146,		s: 50,		l: 36,	],
		[ name: "Sea Shell",				rgb: "#FFF5EE",		h: 25,		s: 100,		l: 97,	],
		[ name: "Sienna",					rgb: "#A0522D",		h: 19,		s: 56,		l: 40,	],
		[ name: "Silver",					rgb: "#C0C0C0",		h: 0,		s: 0,		l: 75,	],
		[ name: "Sky Blue",					rgb: "#87CEEB",		h: 197,		s: 71,		l: 73,	],
		[ name: "Slate Blue",				rgb: "#6A5ACD",		h: 248,		s: 53,		l: 58,	],
		[ name: "Slate Gray",				rgb: "#708090",		h: 210,		s: 13,		l: 50,	],
		[ name: "Snow",						rgb: "#FFFAFA",		h: 0,		s: 100,		l: 99,	],
		[ name: "Spring Green",				rgb: "#00FF7F",		h: 150,		s: 100,		l: 50,	],
		[ name: "Steel Blue",				rgb: "#4682B4",		h: 207,		s: 44,		l: 49,	],
		[ name: "Tan",						rgb: "#D2B48C",		h: 34,		s: 44,		l: 69,	],
		[ name: "Teal",						rgb: "#008080",		h: 180,		s: 100,		l: 25,	],
		[ name: "Thistle",					rgb: "#D8BFD8",		h: 300,		s: 24,		l: 80,	],
		[ name: "Tomato",					rgb: "#FF6347",		h: 9,		s: 100,		l: 64,	],
		[ name: "Turquoise",				rgb: "#40E0D0",		h: 174,		s: 72,		l: 56,	],
		[ name: "Violet",					rgb: "#EE82EE",		h: 300,		s: 76,		l: 72,	],
		[ name: "Wheat",					rgb: "#F5DEB3",		h: 39,		s: 77,		l: 83,	],
		[ name: "White Smoke",				rgb: "#F5F5F5",		h: 0,		s: 0,		l: 96,	],
		[ name: "Yellow",					rgb: "#FFFF00",		h: 60,		s: 100,		l: 50,	],
		[ name: "Yellow Green",				rgb: "#9ACD32",		h: 80,		s: 61,		l: 50,	],
	]
}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
def mDefaultsS() {def result = ""
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cLowBattery || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	result = "complete"}
   		result}
def mDefaultsD() {def text = "Tap here to configure settings" 
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cLowBattery || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text} 
def pSendSettings() {def result = ""
    if (synthDevice || sonosDevice || sendContactText || sendText || push) {
    	result = "complete"}
        result}
def pSendComplete() {def text = "Tap here to Configure" 
    if (synthDevice || sonosDevice || sendContactText || sendText || push) {
    	text = "Configured"}
        else text = "Tap here to Configure"
    	text}
def pConfigSettings() {def result = ""
    if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
    	result = "complete"}
        result}
def pConfigComplete() {def text = "Tap here to Configure" 
    if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
    	text = "Configured"}
    	else text = "Tap here to Configure"
		text}
def pDevicesSettings() {def result = ""
    if (sSwitches || sDimmers || sHues || sFlash) {
    	result = "complete"}
    	result}
def pDevicesComplete() {def text = "Tap here to Configure" 
    if (sSwitches || sDimmers || sHues || sFlash) {
    	text = "Configured"}
        else text = "Tap here to Configure"
        text}
def pActionsSettings(){def result = ""
	def pDevicesProc = ""
    if (sSwitches || sDimmers || sHues || sFlash) {
    	result = "complete"
        pDevicesProc = "complete"}
    	result}
def pActionsComplete() {def text = "Configured" 
	def pDevicesComplete = pDevicesComplete()
    if (pDevicesProc || pMode || pRoutine) {
    	text = "Tap here to Configure"}
        else text = "Tap here to Configure"
        text}        
def pRestSettings() {def result = ""
    if (modes || days ||pTimeSettings() || onceDaily || everyXmin) {
    	result = "complete"}
   		result}
def pRestComplete() {def text = "Tap here to configure settings" 
    if (modes || days || pTimeSettings() || everyXmin ) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}     
def pTimeSettings() {def result = ""
    if (startingX || endingX) {
    	result = "complete"}
   		result}
def pTimeComplete() {def text = "Tap here to configure settings" 
    if (startingX || endingX) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}