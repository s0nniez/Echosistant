/* 
 * Profile - EchoSistant Add-on 
 *
 *		02/14/2017		Release 4.1.4	Removed weather alerts and added mode change notifications
 *		02/12/2017		Release 4.1.3	Bug fix: Scheduled events not executing properly
 *		02/07/2017		Release 4.1.2	Updates... lots and lots of updates
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
	name			: "NotificationProfile",
    namespace		: "EchoLabs",
    author			: "JH/BD",
	description		: "EchoSistant Add-on",
	category		: "My Apps",
    parent			: "EchoLabs:EchoSistantLabs", 
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/


preferences {

    page name: "mainProfilePage"
    		page name: "pNotifyScene"          
        	page name: "pNotifications"
        	page name: "pRestrict"
            page name: "pNotifyConfig"
            page name: "SMS"
            page name: "severeWeatherAlertsPage"
            page name: "customSounds"
            page( name: "timeIntervalInput", title: "Only during a certain time")

}

//dynamic page methods
page name: "mainProfilePage"
    def mainProfilePage() {
        dynamicPage (name: "mainProfilePage", install: true, uninstall: true) {
	        section ("Create a Notification") {
                input "actionType", "enum", title: "Choose the message output...", required: false, defaultValue: "", submitOnChange: true, options: [
				"Custom",
				"Bell 1",
				"Bell 2",
				"Dogs Barking",
				"Fire Alarm",
				"The mail has arrived",
				"A door opened",
				"There is motion",
				"Smartthings detected a flood",
				"Smartthings detected smoke",
				"Someone is arriving",
				"Piano",
				"Lightsaber"]
			}

        if (actionType == "Custom") {
        section ("Send this message...") {
            input "message", "text", title: "Play this message...", required:false, multiple: false, defaultValue: ""
        	}
        }
        section ("Using These Triggers") {
            input "timeOfDay", "time", title: "At this time every day", required: false
            input "mySwitch", "capability.switch", title: "Choose Switches...", required: false, multiple: true, submitOnChange: true
            input "myContact", "capability.contactSensor", title: "Choose Doors and Windows..", required: false, multiple: true, submitOnChange: true
            input "myLocks", "capability.lock", title: "Choose Locks..", required: false, multiple: true, submitOnChange: true
            input "myMotion", "capability.motionSensor", title: "Choose Motion Sensors..", required: false, multiple: true, submitOnChange: true
            input "myPresence", "capability.presenceSensor", title: "Choose Presence Sensors...", required: false, multiple: true, submitOnChange: true
            input "pMode", "enum", title: "Choose Modes...", options: location.modes.name.sort(), multiple: true, required: false 
            	def actions = location.helloHome?.getPhrases()*.label 
                }    
        section ("and these output methods...") {    
			input "sonos", "capability.musicPlayer", title: "On this Sonos type music player", required: false, multiple: true, submitOnChange: true
            if (sonos) {
			input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
				}
			input "speechSynth", "capability.speechSynthesis", title: "Speech Synthesis Device (may not work)", required: false, multiple: true, submitOnChange: true
          	href "SMS", title: "Send SMS & Push Messages...", description: pSendComplete(), state: pSendSettings()
                        }
        section ("Using these Restrictions") {
            href "pRestrict", title: "Use these restrictions...", description: pRestComplete(), state: pRestSettings()
            }
            section ("Name and/or Remove this Profile") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "Notification Profile"  
        	} 
		}
	}
page name: "SMS"
    def SMS(){
        dynamicPage(name: "SMS", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
            input "timeStamp", "bool", title: "Add time stamp to Push Messages", required: false, defaultValue: false  
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
                href "certainTime", title: "Only during a certain time", description: pTimeComplete(), state: pTimeSettings(),
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
		
************************************************************************************************************/
def installed() {
	log.debug "Installed with settings: ${settings}"
}
def updated() { 
	log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}
def initialize() {
    	subscribeToEvents()
        subscribe(location, modeChangeHandler)
	    subscribe(location, locationHandler) 
}    
/************************************************************************************************************
		Subscriptions
************************************************************************************************************/
def subscribeToEvents() {
	loadText()
	if (timeOfDay) {
    log.debug "Time of Day subscribed to for ${timeOfDay}"
		schedule(timeOfDay, scheduledTimeHandler)
	} 
    if (runDay) {
   		subscribe(runDay, location.day, location.currentDay)
	}
    if (actionType) {
    if (pMode) {subscribe(location, "mode", locationHandler)}
   	if (mySwitch) {subscribe(mySwitch, "switch.on", alertsHandler)}
    if (myContact) {subscribe(myContact, "contact.open", alertsHandler)}
    if (myLocks) {subscribe(myLocks, "lock.locked", alertsHandler)}
    if (myLocks) {subscribe(myLocks, "lock.unlocked", alertsHandler)}
    if (myPresence) {subscribe(myPresence, "presenceSensor", alertsHandler)}
	}
}
private dayString(Date date) {
	def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	df.format(date)
}
/***********************************************************************************************************************
    MODE CHANGE HANDLER
***********************************************************************************************************************/
def locationHandler(evt) {
	if (pMode) {
  		log.debug "Location Mode changed to: ${evt.value}"
//    	def result = !modes || modes?.contains(location.mode)
		takeAction()
	}
} 
/***********************************************************************************************************************
    CUSTOM SOUNDS HANDLER
***********************************************************************************************************************/
private takeAction(evt) {
def CustomMessage = message
	log.trace "takeAction()"
    	if (speechSynth) {
        speechSynth?.playSoundAndTrack(state.sound.uri, state.sound.duration, state.selectedSong)
    	log.info "Playing this message: '${message}', on the speech synthesizer'${speechSynth}'"
        }
        if (sonos) {
        sonos?.playSoundAndTrack(state.sound.uri, state.sound.duration, state.selectedSong, volume)
		log.info "Playing message: '${CustomMessage}', on the music player '${sonos}' at volume '${volume}'"
		}
        if (resumePlaying){
		sonos.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
		}
    log.trace "Exiting takeAction()"
	}
private loadText() {
	switch (actionType) {
		case "Bell 1":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
		case "Bell 2":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell2.mp3", duration: "10"]
			break;
		case "Dogs Barking":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/dogs.mp3", duration: "10"]
			break;
		case "Fire Alarm":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/alarm.mp3", duration: "17"]
			break;
		case "The mail has arrived":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/the+mail+has+arrived.mp3", duration: "1"]
			break;
		case "A door opened":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/a+door+opened.mp3", duration: "1"]
			break;
		case "There is motion":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/there+is+motion.mp3", duration: "1"]
			break;
		case "Smartthings detected a flood":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/smartthings+detected+a+flood.mp3", duration: "2"]
			break;
		case "Smartthings detected smoke":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/smartthings+detected+smoke.mp3", duration: "1"]
			break;
		case "Someone is arriving":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/someone+is+arriving.mp3", duration: "1"]
			break;
		case "Piano":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/piano2.mp3", duration: "10"]
			break;
		case "Lightsaber":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/lightsaber.mp3", duration: "10"]
			break;
		case "Custom":
			if (message) {
            	state?.sound = textToSpeech(message instanceof List ? message[0] : message) // not sure why this is (sometimes) needed)
				}
            else {
				state?.sound = textToSpeech("Attention, Attention. You selected the custom message option but did not enter a message in the $app.label Smart App")
			}
			break;
		default:
			state?.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
	}
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getAllOk() {
	modeOk && daysOk && timeOk
}
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	log.debug "modeOk = $result"
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
	log.debug "daysOk = $result"
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
    log.debug "timeOk = $result"
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
def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
    if (debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) {
       log.debug "Sending sms to selected reipients"
    } 
    if (push && !timeStamp) {
	    sendPush message
        }
	if (push && timeStamp) {
     	sendPush (message + " at " + stamp)
       	log.debug "Sending push message to selected reipients with timestamp"
        }
	if (sms || sendContactText) {
        sendText(sms, message)
        log.debug "Processing message for selected phones"
		}
	}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
            log.debug "Sending sms to selected phones"
        	}
    	}        
	}
/************************************************************************************************************
   Time of Day Scheduler Handler
************************************************************************************************************/
def scheduledTimeHandler() {
	if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
		sendtxt(message)
    	takeAction()
    	}
    }    
/************************************************************************************************************
   Alerts Handler
************************************************************************************************************/
def alertsHandler(evt) {
	def eVal = evt.value
    def eName = evt.name
    def eDev = evt.device
    def eTxt = " "
    log.debug "Received event name ${evt.name} with value:  ${evt.value}, from: ${evt.device}"

	if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
	def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
        if (eVal == "present" || eVal == "open" || eVal == "locked" || eVal == "active" || eVal == "on") {
        	takeAction(evt)
            sendtxt(message)
        }
	}        
}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
def pSendSettings() {def result = ""
    if (sendContactText || sendText || push) {
    	result = "complete"}
   		result}
def pSendComplete() {def text = "Tap here to configure settings" 
    if (sendContactText || sendText || push) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}
def pRestSettings() {def result = ""
    if (modes || days) {
    	result = "complete"}
   		result}
def pRestComplete() {def text = "Tap here to configure settings" 
    if (modes || days) {
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
