/* 
* Message and Control Profile - EchoSistant Add-on 
*
*		6/12/2017		Version:5.0 R.0.0.1		Alpha Release
*		2/17/2017		Version:4.0 R.0.0.1		Public Release
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
	name			: "Shortcuts Profile",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Profiles2 Add-on - only publish if using secondary accounts",
	category		: "My Apps",
    parent			: "Echo:Profiles",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.3.4"
}
/**********************************************************************************************************************************************/
preferences {

    page name: "mainProfilePage"
    page name: "pSend"          
    page name: "pActions"
    page name: "pConfig"
    page name: "pGroups"
    page name: "pRestrict"
    page name: "pDeviceControl"
    page name: "pPerson"
    page name: "pVirPerAction"
}

// MAIN PROFILE - HOME PAGE
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section ("Name Your Shortcut") {
            label title:"Shortcut Name", required:true
        	}
        def routines = location.helloHome?.getPhrases()*.label 
        if (routines) {routines.sort()}
        section ("Trigger these lights and/or execute these routines when the Profile runs...") {
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
            input "shmState", "enum", title: "Set Smart Home Monitor to...", options:["stay":"Armed Stay","away":"Armed Away","off":"Disarmed"], multiple: false, required: false, submitOnChange: true
            if (shmState) {
                input "shmStateKeypads", "capability.lockCodes",  title: "Send status change to these keypads...", multiple: true, required: false, submitOnChange: true
            }
            input "pVirPer", "bool", title: "Toggle the Virtual Person State Automatically when this Profile Runs", default: false, submitOnChange: true, required: false
        		if (pVirPer) {
                	href "pPerson", title: "Configure the Virtual Person for ${app.label}", description: VPCreateComplete(), state: VPCreateSettings()
                    }
        		}
    		}
		}

// DEVICE CONTROL SELECTION PAGE
page name: "pDeviceControl"
def pDeviceControl() {
    dynamicPage(name: "pDeviceControl", title: "",install: false, uninstall: false) {
    	section ("Garage Doors", hideWhenEmpty: true){
        	input "gDoor1", "capability.garageDoorControl", title: "Select Garage Doors...", multiple: true, required: false, submitOnChange: true
            }
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
    //SHM status change and keypad initialize
    subscribe(location, locationHandler)
    state.responseTxt = null
    state.lambdaReleaseTxt = "Not Set"
    state.lambdaReleaseDt = "Not Set" 
    state.lambdatextVersion = "Not Set"
    //Alexa Responses
    state.pTryAgain = false
    state.pContCmds = settings.pContCmdsProfile == false ? true : settings.pContCmdsProfile == true ? false : true
    state.pMuteAlexa = settings.pEnableMuteAlexa
    state.pShort = settings.pUseShort
    state.pContCmdsR = "init"       
    //PIN Settings
    state.usePIN_T = settings.uPIN_T
    state.usePIN_L = settings.uPIN_L
    state.usePIN_D = settings.uPIN_D
    state.usePIN_S = settings.uPIN_S             
    state.usePIN_SHM = settings.uPIN_SHM
    state.usePIN_Mode = settings.uPIN_Mode
    state.savedPINdata = null
    state.pinTry = null
    //OTHER 
    def String deviceType = (String) null
    def String outputTxt = (String) null
    def String result = (String) null
    def String deviceM = (String) null
    def currState
    def stateDate
    def stateTime
    def data = [:]
    def fProcess = true
    state.pTryAgain = false
}
/******************************************************************************************************
SPEECH AND TEXT ACTION
******************************************************************************************************/
def ttsActions() {

/*    def String ttx = (String) null 	
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
  //      if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
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
 //       }
        if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
            sendtxt(ttx)
        }
    }
    else {
        if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
            if (parent.debug) log.debug "Only sending sms because disable voice message is ON"
            sendtxt(ttx)
        }
    }*/
    if (pVirPer) {
        pVirToggle()
    }
    if (shmState) {
        shmStateChange()
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
    state.lastMessage = tts
    log.info "the grandchild shortcut - $app.label, has executed"
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

// Primary control of profile triggered lights/switches when delayed handler
def profileDeviceControl() {
if (parent.debug) {"grandchild activated"}
    if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sOtherSecondsOn)  { runIn(sOtherSecondsOn,turnOnOtherSwitch)}
    if (sOtherSecondsOff) { runIn(sOtherSecondsOff,turnOffOtherSwitch)}
    if (sSecondsDimmers) { runIn(sSecondsDimmers,turnOnDimmers)}
    if (sSecondsDimmersOff) { runIn(sSecondsDimmersOff,turnOffDimmers)}
    if (sSecondsOtherDimmers) { runIn(sSecondsOtherDimmers,turnOnOtherDimmers)}
    if (sSecondsOtherDimmersOff) { runIn(sSecondsOtherDimmersOff,turnOffOtherDimmers)}
        if (sDimmersCmd == "set" && sDimmers) { def level = sDimmersLVL < 0 || !sDimmersLVL ?  0 : sDimmersLVL >100 ? 100 : sDimmersLVL as int
            sDimmers?.setLevel(level) }

// Control of Lights and Switches when not delayed handler         
    if (!sSecondsOn) {
        if  (sSwitchCmd == "on") { sSwitches?.on() }
        else if (sSwitchCmd == "off") { sSwitches?.off() }
        if (sSwitchCmd == "toggle") { toggle() }
        if (sOtherSwitchCmd == "on") { sOtherSwitch?.on() }
        else if (sOtherSwitchCmd == "off") { sOtherSwitch?.off() }
        if (otherSwitchCmd == "toggle") { toggle() }

        if (sOtherDimmersCmd == "set" && sOtherDimmers) { def otherLevel = sOtherDimmersLVL < 0 || !sOtherDimmersLVL ?  0 : sOtherDimmersLVL >100 ? 100 : sOtherDimmersLVL as int
            sOtherDimmers?.setLevel(otherLevel) }
    }
}

// SWITCHES TOGGLE HANDLER
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
def profileLoop(child) {
    def childName = app.label
    def result
    if(childName == child){
        if(gHues){
            int hueLevel = !level ? 100 : level
            int hueHue = Math.random() *100 as Integer
            def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
            gHues.setColor(randomColor)
            runIn(60, "startLoop")
            result =  "Ok, turning the color loop on, in the " + childName

        }
        else result = "Sorry, I wasn't able to turn the color loop on in the " + childName
    }
    return result
}
private startLoop() {
    def device =  state.lastDevice
    def deviceMatch = cSwitch.find {s -> s.label.toLowerCase() == device.toLowerCase()}	
    int hueLevel = !level ? 100 : level
    int hueHue = Math.random() *100 as Integer
    def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
    gHues.setColor(randomColor)
    runIn(60, "continueLoop")
}


private continueLoop() {
    int hueLevel = !level ? 100 : level
    int hueHue = Math.random() *100 as Integer
    def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
    gHues.setColor(randomColor)
    runIn(60, "startLoop")
}
def profileLoopCancel(child) {
    def childName = app.label 
    def result
    if(childName == child){
        unschedule("startLoop")
        unschedule("continueLoop")
        result =  "Ok, turning the color loop off in the " + childName
    }
    else result = "Sorry, I wasn't able to turn the color loop off"
    return result
}
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
    if (sc1BulbsCmd == "on") { sc1Bulbs?.on() }
    if (sc1BulbsCmd == "off") { sc1Bulbs?.off() }
    if (sc2BulbsCmd == "on") { sc2Bulbs?.on() }
    if (sc2BulbsCmd == "off") { sc2Bulbs?.off() }
    if (sc3BulbsCmd == "on") { sc3Bulbs?.on() }
    if (sc3BulbsCmd == "off") { sc3Bulbs?.off() }
    def sc1HueSetVals = getColorName("${sc1BulbsColor}",level)
    	sc1Bulbs?.setColor(sc1HueSetVals)
    def sc2HueSetVals = getColorName("${sc2BulbsColor}",level)
    	sc2Bulbs?.setColor(sc2HueSetVals)
    def sc3HueSetVals = getColorName("${sc3BulbsColor}",level)
    	sc3Bulbs?.setColor(sc3HueSetVals)
    def hueSetVals = getColorName("${sHuesColor}",level)
    	sHues?.setColor(hueSetVals)
    def	hueSetValsOther = getColorName("${sHuesOtherColor}",level)
    	sHuesOther?.setColor(hueSetValsOther)
}
private getColorName(sHuesColor, level) {
    for (color in fillColorSettings()) {
		if (color.name.toLowerCase() == sHuesColor.toLowerCase()) {
        log.warn "found a color match"
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

/******************************************************************************
SHORTCUT HANDLERS									
******************************************************************************/
// SC-ONE ACTIONS HANDLER
def scOneActions(){
    if (sc1Switches) {
    	if (sc1SwitchesCmd == "on") {sc1Switches.on()}
        	else if (sc1SwitchesCmd == "off") {sc1Switches.off()}
            }
	if (sc1Bulbs) {
        processColor()
    	}
	if (sc1Dimmers) {
		def level = sc1DimmersLevel < 0 || !sc1DimmersLevel ?  0 : sc1DimmersLevel >100 ? 100 : sc1DimmersLevel as int
            sc1Dimmers?.setLevel(level) 
            }		
    if (sc1Garage) {        
        if (sc1GarageCmd == "open") {sc1Garage.open()}
       	if (sc1GarageCmd == "close") {sc1Garage.close()}
        }
    if (sc1Fans) {        
        if (sc1FansCmd == "on") {sc1Fans.on()}
       	if (sc1FansCmd == "off") {sc1Fans.off()}
        if (sc1FansCmd == "low") {sc1Fans.setLevel(33)}
        if (sc1FansCmd == "med") {sc1Fans.setLevel(66)}
        if (sc1FansCmd == "high") {sc1Fans.setLevel(99)}
            }
    if (sc1Vents) {        
        if (sc1VentsCmd == "open") {sc1Vents.on()}
       	if (sc1VentsCmd == "close") {sc1Vents.off()}
        if (sc1VentsCmd == "25") {sc1Vents.setLevel(25)}
        if (sc1VentsCmd == "50") {sc1Vents.setLevel(50)}
        if (sc1VentsCmd == "75") {sc1Vents.setLevel(75)}
            }
    if (sc1Shades) {        
        if (sc1ShadesCmd == "open") {sc1Shades.setLevel(100)}
       	if (sc1ShadesCmd == "close") {sc1Shades.setLevel(0)}
        if (sc1ShadesCmd == "25") {sc1Shades.setLevel(25)}
        if (sc1ShadesCmd == "50") {sc1Shades.setLevel(50)}
        if (sc1ShadesCmd == "75") {sc1Shades.setLevel(75)}
            }
    if (sc1Locks) {        
        if (sc1LocksCmd == "lock") {sc1Locks.lock()}
       	if (sc1LocksCmd == "unlock") {sc1Locks.unlock()}
        }
    if (sc1Modes) {
    	setLocationMode(sc1Modes)
        }
}    

/************************************************************************************************************
Page status and descriptions 
************************************************************************************************************/       
def pKeypadSettings() {def result = ""
                       if (shmConfigure || garageDoors || kpVirPer) {
                           result = "complete"}
                       result}
def pKeypadComplete() {def text = "Tap here to Configure" 
                       if (shmConfigure || garageDoors || kpVirPer) {
                           text = "Configured"}
                       else text = "Tap here to Configure"
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
def pActionsSettings(){def result = ""
                       def pDevicesProc = ""
                       if (sSwitches || sDimmers || sHues || sFlash || shmState) {
                           result = "complete"
                           pDevicesProc = "complete"}
                       result}
def pActionsComplete() {def text = "Tap here to configure" 
                        def pDevicesComplete = pDevicesComplete()
                        if (pDevicesProc || pMode || pRoutine || shmState) {
                            text = "Configured"}
                        else text = "Tap here to Configure"
                        text}        
def pRestrictSettings(){ def result = "" 
                        if (modes || runDay || hues || startingX || endingX) { 
                            result = "complete"}
                        result}
def pRestrictComplete() {def text = "Tap here to configure" 
                         if (modes || runDay || hues || startingX || endingX) {
                             text = "Configured"}
                         else text = "Tap here to Configure"
                         text}
def pGroupSettings() {def result = ""
                      if (gDisable || gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
                          result = "complete"}
                      result}
def pGroupComplete() {def text = "Tap here to Configure" 
                      if (gDisable || gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
                          text = "Configured"}
                      else text = "Tap here to Configure"
                      text
                     }
// PROFILE ACTIONS
def pDevicesSettings() {def result = ""
                     if (gDoor1 || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash) {
                         result = "complete"}
                     result}
def pDevicesComplete() {def text = "Tap here to select devices" 
                     if (gDoor1 || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash) {
                         text = "Configured"}
                     else text = "Tap here to select devices"
                     text}


// VIRTUAL PERSON  
def VPCreateSettings() {def result = ""
                     def deviceId = "${app.label}"
                     def d = getChildDevice("${app.label}")
                     if ("${d}"== ("${app.label}")) {
                         result = "complete"}
                     result}
def VPCreateComplete() {def text = "Tap here to Configure" 
                     def deviceId = "${app.label}"
                     def d = getChildDevice("${app.label}")
                     if ("${d}"== ("${app.label}")) {
                         text = "Virtual Person Created"}
                     else text = "Tap here to Configure"
                     text}

def VPNotifySettings() {def result = ""
                     if (vpNotification || vpPhone) {
                         result = "complete"}
                     result}
def VPNotifyComplete() {def text = "Tap here to Configure" 
                     if (vpNotification || vpPhone) {
                         text = "Configured"}
                     else text = "Tap here to Configure"
                     text}
                     
def pleaseWork(params) {
	log.info "Please Work method called by the profile parent"
    }