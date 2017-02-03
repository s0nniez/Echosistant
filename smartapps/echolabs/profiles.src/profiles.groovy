/* 
 * Rooms - EchoSistant Add-on 
 *
 *		12/31/2016		Release 4.1.1	New features: status updates, custom commands, weather alerts, message reminders 
 *										Improvements: streamlined UI and processing
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
	name			: "Profiles",
    namespace		: "EchoLabs",
    author			: "JH/BD",
	description		: "EchoSistant Profiles Add-on",
	category		: "My Apps",
    parent			: "EchoLabs:EchoSistantLabs",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/


preferences {

    page name: "mainProfilePage"
    		page name: "pSend"          
        	page name: "pActions"
        	page name: "pConfig"
            
            page name: "pGroups"
        	page name: "pRestrict"
  			page name: "pDeviceControl"
}

//dynamic page methods
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section("Audio and Text Message Settings") {
           	href "pSend", title: "Send Messages... "
   				//image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"   			
            href "pConfig", title: "Audio Settings..."
            	//image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"             
        	href "pActions", title: "Execute Actions when Profile runs..."//, description: pActionsComplete(), state: pActionsSettings(),
            	//image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"   
        }   
       	section("Profile Control Settings") {
	    	href "pGroups", title: "Create Groups and Select Devices"
                //,image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"     
			href "pRestrict", title: "General Profile Restrictions" 
            	//image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png" 
		}
        section ("Name and/or Remove this Profile") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "New Profile"  
        }    
	}
}
page name: "pSend"
    def pSend(){
        dynamicPage(name: "pSend", title: "", uninstall: false){
             section ("Speakers", hideWhenEmpty: true){
                input "synthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, required: false,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                input "sonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true,    
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                if (sonosDevice) {
                    input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                }  
            }
            section ("Text Messages" ) {
            	input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true,    
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
           			input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true,      
                        image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
                    input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
                }
            }    
            section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
            input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
            }        
    	}                 
    }   
page name: "pConfig"
    def pConfig(){
        dynamicPage(name: "pConfig", title: "", uninstall: false) {
            section ("Alexa Responses") {
                    input "pAcustResp", "text", title: "Custom Response from Alexa", required: false, defaultValue: "Message sent,   "
                    input "pArepeat", "bool", title: "Alexa repeats the message sent to remote speaker...", defaultValue: false, submitOnChange: true
                        if (pArepeat) {			
                        	if (pArepeat && pAcustom){
                           		paragraph 	"NOTE: only one custom Alexa response can"+
                            				" be delivered at once. Please only enable Custom Response OR Repeat Message"
                            }				
                        }
                    input "pContCmds", "bool", title: "Allow Alexa to prompt for additional commands after a message is sent to a remote speaker", defaultValue: false, submitOnChange: true
             }
             section ("Remote Speaker Settings") {
                	input "pRunMsg", "Text", title: "Play this message when this profile executes", description: none, required: false
                    input "pPreMsg", "text", title: "Remote Pre-Message...", defaultValue: none, submitOnChange: true, required: false 
             }
             section ("Sound Cancelation") {
                    //formerly pAfeedBack // pDisableAlexa = state.pMuteAlexa
					input "pDisableAlexa", "bool", title: "Turn on to disable Alexa Feedback Responses (silence Alexa) Overrides all other Alexa Options...", defaultValue: false, submitOnChange: true
                    input "pDisableALL", "bool", title: "Disable All spoken notifications (No voice output from the speakers or Alexa)", required: false, submitOnChange: true  
             }
		}             
	}             
page name: "pActions"
    def pActions() {
        dynamicPage(name: "pActions", uninstall: false) {
        	def routines = location.helloHome?.getPhrases()*.label 
            if (routines) {routines.sort()}
            section ("Trigger these lights and/or execute these routines when the Profile runs...") {
                href "pDeviceControl", title: "Select Devices..." //description: DevConDescr() , state: completeDevCon()
                    //image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"
                input "pMode", "enum", title: "Choose Mode to change to...", options: location.modes.name.sort(), multiple: false, required: false 
					//image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
            	input "pRoutine", "enum", title: "Select Routines to execute", required: false, options: routines, multiple: false, submitOnChange: true
                //,image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
                if (runRoutine) {
                	input "pRoutine2", "enum", title: "Select a Second Routine to execute", required: false, options: actions, multiple: false
                		//,image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png" 
            	}
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
							input "sHuesColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
							input "sHuesLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], submitOnChange:true                        
        				}
                        if (sHuesLevel)	input "sHuesOther", "capability.colorControl", title: "...and these other Colored Lights?", multiple: true, required: false, submitOnChange:true
            			if (sHuesOther) {
                        	input "sHuesOtherCmd", "enum", title: "Command To Send to these other Colored Lights", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							input "sHuesOtherColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
							input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
        				}
				}
                section ("Flash These Switches", hideWhenEmpty: true) {
                    input "sFlash", "capability.switch", title: "Flash Switch(es)", multiple: true, required: false, submitOnChange:true
                    if (sFlash) {
                    	input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                    	input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true			
                            if (onFor) {
                                input "sFlashColorA", "enum", title: "Flasher Color?", required: false, multiple:false, options: fillColorSettings().name
                                input "sFlashLevelA", "enum", title: "Light Flash Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
                            }
                    	input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
                            if (offFor) {
                                input "sFlashColorB", "enum", title: "Flasher Second Color?", required: false, multiple:false, options: fillColorSettings().name
                                input "sFlashLevelB", "enum", title: "Light Flash Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]] 					
                            }
                	}
                }
			}
		}    
    page name: "pGroups"
        def pGroups() {
            dynamicPage(name: "pGroups", title: "Allow Devices to be used by this Profile",install: false, uninstall: false) {
                section ("Use These Lights (switches and dimmers)", hideWhenEmpty: true){
                        input "gSwitches", "capability.switch", title: "Group Lights and Switches...", multiple: true, required: false, submitOnChange: true
                        if (gSwitches) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa tell Main Skill, to turn on/off the lights in the Profile Name"
                        }
                        input "gHues", "capability.colorControl", title: "Use These Colored Lights...", multiple: true, required: false, submitOnChange: true
                        if (gSwitches) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa tell Main Skill, to set the lights to red in the Profile Name"
                        }
                }       
               section ("Climate Control", hideWhenEmpty: true){ 
                 	input "sTstat", "capability.thermostat", title: "Use These Thermostat(s)...", multiple: true, required: false
                    input "sRoomTemp", "capability.temperatureMeasurement", title: "Use These Device(s) to Report the Room Temperature...", multiple: true, required: false
                    input "sVent", "capability.switchLevel", title: "Use These Smart Vent(s)...", multiple: true, required: false
                }                
                section ("Sensors", hideWhenEmpty: true) {
                 	input "sMotion", "capability.motionSensor", title: "Use These Motion Sensor(s)...", multiple: true, required: false
                    input "sContact", "capability.contactSensor", title: "Use These Contact Sensor(s)...", multiple: true, required: false      
            		input "sWater", "capability.waterSensor", title: "Use These Water Sensor(s)...", multiple: true, required: false                       
                }
                section ("Media" , hideWhenEmpty: true){
                    input "sSpeaker", "capability.musicPlayer", title: "Use These Media Player Type Device(s)...", required: false, multiple: true
                    input "sSynth", "capability.speechSynthesis", title: "Use These Speech Synthesis Capable Device(s)", multiple: true, required: false
                    input "sMedia", "capability.mediaController", title: "Use These Media Controller(s)", multiple: true, required: false
                }             
            }
      	}
page name: "pRestrict"
    def pRestrict(){
        dynamicPage(name: "pRestrict", title: "", uninstall: false) {
			section ("Mode Restrictions") {
                input "modes", "mode", title: "Only when mode is", multiple: true, required: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
            }        
            section ("Days - Audio only on these days"){	
                input "days", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                    "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
            }
            section ("Time - Audio only during these times"){
                href "certainTime", title: "Only during a certain time", description: timeIntervalLabel ?: "Tap to set", state: timeIntervalLabel ? "complete" : null,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
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
	log.debug "Installed with settings: ${settings}"
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
    def cScenes = getChildApps()
    if (parent.debug) "cScenes=${cScenes}"
}

def initialize() {
        state.lastMessage = null
    	state.lastTime  = null
        state.recording = null
}
/************************************************************************************************************
		CoRE Integration
************************************************************************************************************/

/******************************************************************************************************
   SPEECH AND TEXT PROCESSING
******************************************************************************************************/
def profileEvaluate(params) {
        def tts = params.ptts
        def txt = params.pttx
        def intent = params.pintentName        
        def childName = app.label       
        def data = [args: tts ]
        
        if (intent.toLowerCase() == childName.toLowerCase()){
        	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
      		if (parent.debug) log.debug "sendNotificationEvent sent to CoRE was '${app.label}' from the TTS process section"

			if (!disableTts){
        			if (PreMsg) 
        				tts = PreMsg + tts
        				if (parent.debug) log.debug "tts with PreMsg = '${tts}'"
                    else {
            			tts = tts
            			if (parent.debug) log.debug "tts without PreMsg = '${tts}'"
                    }
            			if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            					if (synthDevice) {
                                synthDevice?.speak(tts) 
        			    			if (parent.debug) log.debug "Sending message to Synthesis Devices"
                                    }
                				if (mediaDevice) {
                                	mediaDevice?.speak(tts) 
                					if (parent.debug) log.debug "Sending message to Media Devices"
                                    }
            						if (tts) {
										state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
									}
									else {
										state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
                                        if (parent.debug) log.debug "You selected the custom message option but did not enter a message"
									}
								if (sonosDevice) {
										sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
                    						if (parent.debug) log.debug "Sending message to Sonos Devices"
                                            }
    								}
    					sendtxt(txt) 
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
            			if (parent.debug) log.debug "Sending sms and voice message to selected phones and speakers" 
				}
				else {
    					sendtxt(txt)
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
           					if (parent.debug) log.debug "Only sending sms because disable voice message is ON"
				}
 		
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
	if(debug) log.debug "modeOk = $result"
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
	if(debug) log.debug "daysOk = $result"
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
    		sendPush message
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
        def phones = sms.split("\\;")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}
private txtFormat (message, eDev, eVal) {
    def eTxt = " " 
        if(message) {
        	message = message.replace('$device', "${eDev}")
        	message = message.replace('$action', "${eVal}")
	  	    eTxt = message
        }  	
            if (debug) log.debug "Processed Alert: ${eTxt} "
    		
            return eTxt
}
/************************************************************************************************************
   Play Sonos Alert
************************************************************************************************************/
def playAlert(message, speaker) {
    state.sound = textToSpeech(message instanceof List ? message[0] : message)
    speaker.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
    if (debug) log.debug "Sending message: ${message} to speaker: ${speaker}"

}

/************************************************************************************************************
   Switch/Color/Dimmer/Toggle Handlers
************************************************************************************************************/
// Used for delayed devices
def turnOnSwitch() { switches?.on() }  
def turnOffSwitch() { switches?.off() }
def turnOnOtherSwitch() { otherSwitch?.on() }
def turnOffOtherSwitch() { otherSwitch?.off() }  
def turnOnDimmers() { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
			dimmers?.setLevel(dimmersLVL) }
def turnOffDimmers() { dimmers?.off() }
def turnOnOtherDimmers() { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
			otherDimmers?.setLevel(otherDimmersLVL) }
def turnOffOtherDimmers() { otherDimmers?.off() }            

// Primary control of profile triggered lights/switches when delayed
def profileDeviceControl() {
	if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sSecondsOn1)  { runIn(sSecondsOn1,turnOnOtherSwitch)}
    if (sSecondsOff1) { runIn(sSecondsOff1,turnOffOtherSwitch)}
	if (sSecondsOn2) { runIn(sSecondsOn2,turnOnDimmers)}
	if (sSecondsOff2) { runIn(sSecondsOff2,turnOffDimmers)}
    if (sSecondsOn3) { runIn(sSecondsOn3,turnOnOtherDimmers)}
	if (sSecondsOff3) { runIn(sSecondsOff3,turnOffOtherDimmers)}

// Control of Lights and Switches when not delayed            
    if (!sSecondsOn) {
            	if  (switchCmd == "on") { switches?.on() }
	            	else if (switchCmd == "off") { switches?.off() }
	           		if (switchCmd == "toggle") { toggle() }
                if (otherSwitchCmd == "on") { otherSwitch?.on() }
            		else if (otherSwitchCmd == "off") { otherSwitch?.off() }
                    if (otherSwitchCmd == "toggle") { toggle() }
                if (dimmersCmd == "set" && dimmers) { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
        				dimmers?.setLevel(level) }
            	if (otherDimmersCmd == "set" && otherDimmers) { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
        				otherDimmers?.setLevel(otherlevel) }
                }
			}

private colorB() { 
	if (hueCmd == "off") { hues?.off() }
    if (hueCmd1 == "off") { hues1?.off() }
		if (debug) log.debug "color bulbs initiated"
		def hueColor = 0
        def hueColor1 = 0
        fillColorSettings()
        if (color == "White")hueColor = 48
        if (color == "Red")hueColor = 0
        if (color == "Blue")hueColor = 70//60  
        if (color == "Green")hueColor = 39//30
        
        if(color == "Yellow")hueColor = 25//16
        if(color == "Orange")hueColor = 11
        if(color == "Purple")hueColor = 75
        if(color == "Pink")hueColor = 83
        
        
        
	def colorB = [hue: hueColor, hue1: hueColor1, saturation: 100, level: (lightLevel as Integer) ?: 100]
    hues*.setColor(colorB)
	}
 
 
 def fillColorSettings(){
	def colorData = []
    colorData << [name: "White", hue: 0, sat: 0] << [name: "Orange", hue: 11, sat: 100] << [name: "Red", hue: 100, sat: 100] << [name: "Purple", hue: 77, sat: 100]
    colorData << [name: "Green", hue: 30, sat: 100] << [name: "Blue", hue: 66, sat: 100] << [name: "Yellow", hue: 16, sat: 100] << [name: "Pink", hue: 95, sat: 100]
    colorData << [name: "Cyan", hue: 50, sat: 100] << [name: "Chartreuse", hue: 25, sat: 100] << [name: "Teal", hue: 44, sat: 100] << [name: "Magenta", hue: 92, sat: 100]
	colorData << [name: "Violet", hue: 83, sat: 100] << [name: "Indigo", hue: 70, sat: 100]<< [name: "Marigold", hue: 16, sat: 75]<< [name: "Raspberry", hue: 99, sat: 75]
    colorData << [name: "Fuchsia", hue: 92, sat: 75] << [name: "Lavender", hue: 83, sat: 75]<< [name: "Aqua", hue: 44, sat: 75]<< [name: "Amber", hue: 11, sat: 75]
    colorData << [name: "Carnation", hue: 99, sat: 50] << [name: "Periwinkle", hue: 70, sat: 50]<< [name: "Pistachio", hue: 30, sat: 50]<< [name: "Vanilla", hue: 16, sat: 50]
    if (customName && (customHue > -1 && customerHue < 101) && (customSat > -1 && customerSat < 101)) colorData << [name: customName, hue: customHue as int, sat: customSat as int]
    return colorData    
}    
    
private toggle() {
    if (parent.debug) log.debug "The selected device is toggling now"
	if (switches) {
	if (switches?.currentValue('switch').contains('on')) {
		switches?.off()
		}
    else if (switches?.currentValue('switch').contains('off')) {
		switches?.on()
		}
    }
    if (otherSwitch) {
	if (otherSwitch?.currentValue('switch').contains('on')) {
		otherSwitch?.off()
	}
	else if (otherSwitch?.currentValue('switch').contains('off')) {
		otherSwitch?.on()
		}
	}
	if (lock) {
	if (lock?.currentValue('lock').contains('locked')) {
		lock?.unlock()
		}
    }
	if (parent.debug) log.debug "The selected device has toggled"
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
		def initialActionOn = flashSwitches.collect{it.currentflashSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			flashSwitches.eachWithIndex {s, i ->
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