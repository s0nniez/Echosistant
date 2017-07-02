/* 
 * Message and Control Profile - EchoSistant Add-on 
 *
 *  Copyright 2017 Jason Headley & Bobby Dobrescu
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
import groovy.json.JsonSlurper

definition(
	name			: "ProfilesLabs",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Profiles Add-on - only publish if using secondary accounts",
	category		: "My Apps",
    parent			: "Echo:EchoSistantLabs",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private def textVersion() {
	def text = "5.0"
}
private release() {
    def text = "R.5.0.2"
}
/**********************************************************************************************************************************************/
preferences {
    page name: "mainProfilePage"
    //"Devices, Groups, Feedback, and Keypads"//
    page name: "devices"
    page name: "pDevices"
    page name: "pGroups"
    page name: "pGroup"
    page name: "pKeypads"
    page name: "pPerson"
    page name: "pPersonCreate"
    page name: "pPersonDelete"
    page name: "pVPNotifyPage"
    page name: "pShmNotifyPage"
    page name: "pGarageDoorNotify"            
    page name: "pDefaults"      
    //"Message Output and Alexa Responses"//	
    page name: "messaging"
    //"Echo Mailbox, Quick Notes, Reminders"//    
    page name: "mailbox"
    page name: "pPetNotes"
    page name: "pFamilyNotes"
    //SETTINGS//
    page name: "pRestrict"
    page name: "certainTime"    
    page name: "pSecurity"    
    page name: "pActions"
    page name: "pDeviceControl"
    page name: "pWeatherConfig"
    page name: "pSkillConfig"
}
//////////////////////////////////////////////////////////////////////////////
/////////// MAIN PAGE
//////////////////////////////////////////////////////////////////////////////
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section("Name Your Profile") {
            label title:"Profile Name", required:true
        } 
        section("Devices, Groups, Feedback, and Keypads") {
            href "devices", title: "Control and Feedback"//, description: pSendComplete(), state: pSendSettings()  
        }  
        section("Message Output and Alexa Responses") {
            href "messaging", title: "Outgoing Messages"//, description: pSendComplete(), state: pSendSettings()   
        }              
        section("Echo Mailbox, Quick Notes, Reminders") {
            href "mailbox", title: "Incoming Messages"//, description: pSendComplete(), state: pSendSettings()    
        }
        section("Settings" , hideable: true, hidden: true ) {
            href "pRestrict", title: "General Restrictions"//, description: pRestrictComplete(), state: pRestrictSettings()   
            href "pSecurity", title: "PIN Settings"//, description: pRestrictComplete(), state: pRestrictSettings()
			href "pActions", title: "Profile Actions (to execute when Profile runs)"//, description: pActionsComplete(), state: pActionsSettings()
            href "pWeatherConfig", title: "Weather Settings"//, description: pRestrictComplete(), state: pRestrictSettings()   
			href "pSkillConfig", title: "Install AWS Skill"//, description: pRestrictComplete(), state: pRestrictSettings()   

        }        
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// "Devices, Groups, Feedback, and Keypads"
//////////////////////////////////////////////////////////////////////////////
page name: "devices"
def devices(){
    dynamicPage(name: "devices", title: "", uninstall: false){  
        section("") {
            href "pDevices", title: "Main Profile Control and Feedback"//, description: pRestrictComplete(), state: pRestrictSettings()
            href "pGroups", title: "Create Groups within Profile", required: false //description: pGroupComplete(), state: pGroupSettings()
            href "pShortcuts", title: "Create Shortcuts within Profile", required: false //description: pGroupComplete(), state: pGroupSettings()
            href "pKeypads", title: "Keypads and Associated Actions"//, description: pSendComplete(), state: pSendSettings()
            href "pDefaults", title: "Profile Defaults"//, description: mDefaultsD(), state: mDefaultsS()           
        }        
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// INDIVIDUAL DEVICE CONTROL AND FEEDBACK 
//////////////////////////////////////////////////////////////////////////////
page name: "pDevices"
def pDevices(){
    dynamicPage(name: "pDevices", title: "", uninstall: false){
        section("Locks") { //, hideWhenEmpty: true
            input "lock", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false//, submitOnChange: true
        }
        section("Garage/Doors") { //, hideWhenEmpty: true
            input "garageDoorControl", "capability.garageDoorControl", title: "Select garage doors", multiple: true, required: false//, submitOnChange: true
        	input "doorControl", "capability.doorControl", title: "Select doors", multiple: true, required: false//, submitOnChange: true
            //input "relay", "capability.switch", title: "Select Garage Door Relay(s)...", multiple: false, required: false//, submitOnChange: true
			//if (fRelay) {
            //	input "contactRelay", "capability.contactSensor", title: "Allow This Contact Sensor to Monitor the Garage Door Relay(s)...", multiple: false, required: false
        	//}
        }
        section("Window Coverings") { //, hideWhenEmpty: true
            input "windowShade", "capability.windowShade", title: "Select devices that control your Window Coverings", multiple: true, required: false//, submitOnChange: true
        }
        section("Climate Control") { //, hideWhenEmpty: true
            input "thermostat", "capability.thermostat", title: "Allow These Thermostat(s)...", multiple: true, required: false
            input "temperatureMeasurement", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Indoor Temperature...", multiple: true, required: false
            //input "outdoor", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Outdoor Temperature...", multiple: true, required: false
            //input "vent", "capability.switchLevel", title: "Select smart vents", multiple: true, required: false//, submitOnChange: true
        }
		section("Water") { //, hideWhenEmpty: true
			input "valve", "capability.valve", title: "Select Water Valves", required: false, multiple: true//, submitOnChange: true
			input "waterSensor", "capability.waterSensor", title: "Select Water Sensor(s)", required: false, multiple: true//, submitOnChange: true
		}
		section("Media"){ //, hideWhenEmpty: true
			input "musicPlayer", "capability.musicPlayer", title: "Allow These Media Player Type Device(s)...", required: false, multiple: true
	     	input "speechSynthesis", "capability.speechSynthesis", title: "Allow These Speech Synthesis Capable Device(s)", multiple: true, required: false
			input "mediaController", "capability.mediaController", title: "Allow These Media Controller(s)", multiple: true, required: false
    	}
        section("Lights, Switches, Dimmers, Fans, Vents") { //, hideWhenEmpty: true
            input "light", "capability.light", title: "Select Lights and Bulbs..", multiple: true, required: false//, submitOnChange: true
            input "switch", "capability.switch", title: "Select Switches...", multiple: true, required: false//, submitOnChange: true
            input "switchLevel", "capability.switchLevel", title: "Select devices that can take a level..", multiple: true, required: false//, submitOnChange: true
        }
        section("Feedback Only Devices") { //, hideWhenEmpty: true
			input "motionSensor", "capability.motionSensor", title: "Select Motion Sensors...", required: false, multiple: true
            input "contactSensor", "capability.contactSensor", title: "Select contacts connected to Doors and Windows", multiple: true, required: false//, submitOnChange: true
            input "presenceSensor", "capability.presenceSensor", title: "Select These Presence Sensors...", required: false, multiple: true
            input "battery", "capability.battery", title: "Select These Device(s) with Batteries...", required: false, multiple: true
			input "carbonDioxideMeasurement", "capability.carbonDioxideMeasurement", title: "Select Carbon Dioxide Sensors (CO2)", required: false            
			input "carbonMonoxideDetector", "capability.carbonMonoxideDetector", title: "Select Carbon Monoxide Sensors (CO)", required: false
			input "relativeHumidityMeasurement", "capability.relativeHumidityMeasurement", title: "Select Relative Humidity Sensor(s)", required: false
			input "soundPressureLevel", "capability.soundPressureLevel", title: "Select Sound Pressure Sensor(s) (noise level)", required: false
        }
	}
}   
//////////////////////////////////////////////////////////////////////////////
/////////// GROUP CONTROL AND FEEDBACK
//////////////////////////////////////////////////////////////////////////////
page name: "pGroups"    
    def pGroups() {
        dynamicPage (name: "pGroups", title: ""){//, install: true, uninstall: false) {
            if (childApps.size()) {  
            	section("Group",  uninstall: false){
                	app(name: "group", appName: "Groups", namespace: "Echo", title: "Create a new group for this Profile", multiple: true,  uninstall: false)
            	}
            }
            else {
            	section("Group",  uninstall: false){
            		paragraph "NOTE: Looks like you have no Groups yet, please make sure you have installed the Groups Smart App Add-on before creating a new Group!"
            		app(name: "group", appName: "Groups", namespace: "Echo", title: "Create a new group for this Profile", multiple: true,  uninstall: false)
        		}
            }
		}
	}
//////////////////////////////////////////////////////////////////////////////
/////////// SHORTCUTS
//////////////////////////////////////////////////////////////////////////////
page name: "pShortcuts"    
    def pShortcuts() {
        dynamicPage (name: "pShortcuts", title: "", install: true, uninstall: false) {
            if (childApps.size()) {  
            	section("Shortcuts",  uninstall: false){
                	app(name: "shortcut", appName: "Shortcuts", namespace: "Echo", title: "Create a new Shortcut", multiple: true,  uninstall: false)
            	}
            }
            else {
            	section("Shortcuts",  uninstall: false){
            		paragraph "NOTE: Looks like you have no Shortcut yet, please make sure you have installed the Shortcut Smart App Add-on before creating a new Shortcut!"
            		app(name: "shortcut", appName: "Shortcuts", namespace: "Echo", title: "Create a new Shortcut", multiple: true,  uninstall: false)
        		}
            }
		}
	}
//////////////////////////////////////////////////////////////////////////////
/////////// KEYPADS, SHM, GARAGE DOORS, AND VIRTUAL PERSON ACTIONS 
//////////////////////////////////////////////////////////////////////////////
page name: "pKeypads"
def pKeypads(){
    dynamicPage(name: "pKeypads", title: "", uninstall: false){
        def deviceId = "${app.label}" 
        def d = getChildDevice("${app.label}")
        section("Configure the Virtual Person Device") {
            input "kpVirPer", "bool", title: "Virtual Person Device Configuration", refreshAfterSelection: true
            if (kpVirPer) {
                href "pPerson", title: "Create/Delete Virtual Person Device"
                if(d) {
                    input "sLocksVP","capability.lockCodes", title: "Select Keypads", required: true, multiple: true, submitOnChange: true
                    input "vpCode", "number", title: "Code (4 digits)", required: false, refreshAfterSelection: true
                    input "vpActions", "bool", title: "Perform this profiles actions when the Virtual Person arrives via Keypad", required: false, submitOnChange: true
                    input "notifyVPArrive", "bool", title: "Notify when Virtual Person Arrives", required: false, submitOnChange: true
                    input "notifyVPDepart", "bool", title: "Notify when Virtual Person Departs", required: false, submitOnChange: true
                    if (notifyVPArrive || notifyVPDepart) {
                        href "pVPNotifyPage", title: "Virtual Person Notification Settings"
                    }
                }
            }    
        }
        section("Configure Smart Home Monitor Settings") {
            input "SHMConfigure", "bool", title: "Smart Home Monitor Controls", refreshAfterSelection: true
            if (SHMConfigure) {
                input "sLocksSHM","capability.lockCodes", title: "Select Keypads", required: true, multiple: true, submitOnChange: true
                input "shmCode", "number", title: "Code (4 digits)", required: false, refreshAfterSelection: true
                input "keypadstatus", "bool", title: "Send status to keypad?", required: true, defaultValue: false
                href "pShmNotifyPage", title: "SHM Profile Notification Settings"//, description: notificationPageDescription(), state: notificationPageDescription() ? "complete" : "")
            }
        }    
        if (SHMConfigure) {
            def hhPhrases = location.getHelloHome()?.getPhrases()*.label
            hhPhrases?.sort()
            section("Execute These Routines") {
                input "armRoutine", "enum", title: "Arm/Away routine", options: hhPhrases, required: false
                input "disarmRoutine", "enum", title: "Disarm routine", options: hhPhrases, required: false
                input "stayRoutine", "enum", title: "Arm/Stay routine", options: hhPhrases, required: false
                input "armDelay", "number", title: "Arm Delay (in seconds)", required: false
                input "notifyIncorrectPin", "bool", title: "Notify you when incorrect code is used?", required: false, defaultValue: false, submitOnChange: true
            }

        }
        section("Garage Door Control via Keypad") {
            input "garageDoors", "bool", title: "Garage Door Access Controls", refreshAfterSelection: true
            if (garageDoors) {
                paragraph "This code is for opening and closing the garage door only, this code will not change SHM Status. This code MUST be different than the SHM code above"
                input "sLocksGarage","capability.lockCodes", title: "Select Locks/Keypads", required: true, multiple: true, submitOnChange: true
                href "pGarageDoorNotify",title: "Garage Door Notification Settings"//, description: notificationPageDescription(), state: notificationPageDescription() ? "complete" : "")
                input "sDoor1", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
                input "doorCode1", "number", title: "Code (4 digits)", required: false, refreshAfterSelection: true
                input "gd1Actions", "bool", title: "Perform this profiles actions when this Garage Door is opened via Keypad", required: false, submitOnChange: true
                if (doorCode1) {
                    input "sDoor2", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
                    input "doorCode2", "number", title: "Code (4 digits)", required: false, refreshAfterSelection: true
                    input "gd2Actions", "bool", title: "Perform this profiles actions when this Garage Door is opened via Keypad", required: false, submitOnChange: true
                    if (doorCode2) {
                        input "gd3Actions", "bool", title: "Perform this profiles actions when this Garage Door is opened via Keypad", required: false, submitOnChange: true
                        input "sDoor3", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
                        input "doorCode3", "number", title: "Code (4 digits)", required: false, refreshAfterSelection: true
                    }
                }
            }
        }
    }    
}
//////////////////////////////////////////////////////////////////////////////
/////////// KEYPAD ACTIONS NOTIFICATIONS       
//////////////////////////////////////////////////////////////////////////////
page name: "pVPNotifyPage"
def pVPNotifyPage() {
    dynamicPage(name: "pVPNotifyPage", title: "Notification Settings") {
        section {
            input "vpPhone", "phone", title: "Text This Number", description: "Phone number", required: false, submitOnChange: true
            paragraph "For multiple SMS recipients, separate phone numbers with a comma"
            input "vpNotification", "bool", title: "Send A Push Notification", description: "Notification", required: false, submitOnChange: true
        }
    }
}    
page name: "pShmNotifyPage"
def pShmNotifyPage() {
    dynamicPage(name: "pShmNotifyPage", title: "Notification Settings") {
        section {
            input "shmPhone", "phone", title: "Text This Number", description: "Phone number", required: false, submitOnChange: true
            paragraph "For multiple SMS recipients, separate phone numbers with a comma"
            input(name: "notifySHMArm", title: "Notify when arming SHM", type: "bool", required: false)
            input(name: "notifySHMDisarm", title: "Notify when disarming SHM", type: "bool", required: false)
            input "shmNotification", "bool", title: "Send A Push Notification", description: "Notification", required: false, submitOnChange: true
        }
    }
}        
page name: "pGarageDoorNotify"
def pGarageDoorNotify() {
    dynamicPage(name: "pGarageDoorNotify", title: "Notification Settings") {
        section {
            input(name: "garagePhone", type: "text", title: "Text This Number", description: "Phone number", required: false, submitOnChange: true)
            paragraph "For multiple SMS recipients, separate phone numbers with a semicolon(;)"
            input(name: "garagePush", type: "bool", title: "Send A Push Notification", description: "Notification", required: false, submitOnChange: true)
            if (phone != null || notification) {
                input(name: "notifyGdoorOpen", title: "Notify when opening", type: "bool", required: false)
                input(name: "notifyGdoorClose", title: "Notify when closing", type: "bool", required: false)
            }
        }
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// VIRTUAL PRESENCE           
//////////////////////////////////////////////////////////////////////////////
page name: "pPerson"
def pPerson(){
    dynamicPage(name: "pPerson", title: "", uninstall: false){
        section ("Manage the Profile Virtual Person Device", hideWhenEmpty: true){
            href "pPersonCreate", title: "Tap Here to Create the Virtual Person Device ~ '${app.label}'"
            href "pPersonDelete", title: "Tap Here to Delete the Virtual Person Device ~ '${app.label}'"
        }
    }
}
//// CREATE VIRTUAL PRESENCE             
page name: "pPersonCreate"
def pPersonCreate(){
    dynamicPage(name: "pPersonCreate", title: "", uninstall: false) {
        section ("") {
            paragraph "You have created a virtual presence sensor device. You will now see this device in your 'Things' list " +
                " in the SmartThings Mobile App.  You will also see it in the 'MyDevices' tab of the IDE"
        }
        virtualPerson()
    }
}
def virtualPerson() {
    log.trace "Creating EchoSistant Virtual Person Device"
    def deviceId = "${app.label}" 
    def d = getChildDevice("${app.label}")
    if(!d) {
        d = addChildDevice("EchoSistant", "EchoSistant Simulated Presence Sensor", deviceId, null, [label:"${app.label}"])
        log.trace "Echosistant Virtual Person Device - Created ${app.label} "
    }
    else {
        log.trace "NOTICE!!! Found that the EVPD ${d.displayName} already exists. Only one device per profile permitted"
    }
} 
//// DELETE VIRTUAL PRESENCE
page name: "pPersonDelete"
def pPersonDelete(){
    dynamicPage(name: "pPersonDelete", title: "", uninstall: false) {
        section ("") {
            paragraph "You have deleted a virtual presence sensor device. You will no longer see this device in your " +
                " SmartThings Mobile App.  "
        }
        removeChildDevices(getAllChildDevices())
    }
}
private removeChildDevices(delete) {
    log.debug "The Virtual Person Device '${app.label}' has been deleted from your SmartThings environment"
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}  
//////////////////////////////////////////////////////////////////////////////
/////////// PROFILE DEFAULTS 
//////////////////////////////////////////////////////////////////////////////
page name: "pDefaults"
def pDefaults(){
    dynamicPage(name: "pDefaults", title: "", uninstall: false){
        section ("General Control") {            
            input "cLevel", "number", title: "Alexa Adjusts Light Levels by using a scale of 1-10 (default is +/-3)", defaultValue: 3, required: false
            input "cVolLevel", "number", title: "Alexa Adjusts the Volume Level by using a scale of 1-10 (default is +/-2)", defaultValue: 2, required: false
            input "cTemperature", "number", title: "Alexa Automatically Adjusts temperature by using a scale of 1-10 (default is +/-1)", defaultValue: 1, required: false						
        }
        section ("Fan Control") {            
            input "cHigh", "number", title: "Alexa Adjusts High Level to 99% by default", defaultValue: 99, required: false
            input "cMedium", "number", title: "Alexa Adjusts Medium Level to 66% by default", defaultValue: 66, required: false
            input "cLow", "number", title: "Alexa Adjusts Low Level to 33% by default", defaultValue: 33, required: false
            input "cFanLevel", "number", title: "Alexa Automatically Adjusts Ceiling Fans by using a scale of 1-100 (default is +/-33%)", defaultValue: 33, required: false
        }
        section ("Activity Defaults") {            
            input "cLowBattery", "number", title: "Alexa Provides Low Battery Feedback when the Bettery Level falls below... (default is 25%)", defaultValue: 25, required: false
            input "cInactiveDev", "number", title: "Alexa Provides Inactive Device Feedback when No Activity was detected for... (default is 24 hours) ", defaultValue: 24, required: false
        }
        section ("Alexa Voice Settings") {            
            input "pDisableContCmds", "bool", title: "Disable Conversation (Alexa no longer prompts for additional commands except for 'try again' if an error ocurs)?", required: false, defaultValue: false
            input "pEnableMuteAlexa", "bool", title: "Disable Feedback (Silence Alexa - it no longer provides any responses)?", required: false, defaultValue: false
            input "pUseShort", "bool", title: "Use Short Alexa Answers (Alexa provides quick answers)?", required: false, defaultValue: false
        }
    }
} 
//////////////////////////////////////////////////////////////////////////////
/////////// OUTGOING MESSAGING   
//////////////////////////////////////////////////////////////////////////////
page name: "messaging"
def messaging(){
    dynamicPage(name: "messaging", title: "", uninstall: false){    
        section ("Audio Messages", hideWhenEmpty: true){
            input "synthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, required: false, submitOnChange: true 
            input "sonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice) {
                input "volume", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            }  
            if (synthDevice || sonosDevice) {
                input "pRunMsg", "Text", title: "Play this predetermined message when this profile executes...", required: false
                input "pPreMsg", "text", title: "Play this message before your spoken message...", defaultValue: none, submitOnChange: true, required: false 
                input "pDisableALLProfile", "bool", title: "Disable Audio Output on the Remote Speaker(s)?", required: false
            }
        }    
        section ("SMS Messages" ) {
            input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true   
            if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
            input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true     
            if (sendText){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
                input "pRunTextMsg", "Text", title: "Send this predetermined text when this profile executes...", required: false
                input "pPreTextMsg", "text", title: "Append this text before the text message...", defaultValue: none, required: false 
            }
        }    
        section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
            input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false
        }        
        section ("Alexa Responses") {
            input "pAlexaCustResp", "text", title: "Custom Response from Alexa...", required: false, defaultValue: none
            input "pAlexaRepeat", "bool", title: "Alexa repeats the sent message to the sender as the response...", defaultValue: false, submitOnChange: true
            if (pAlexaRepeat) {			
                if (pAlexaRepeat && pAlexaCustResp){
                    paragraph 	"NOTE: only one custom Alexa response can"+
                        " be delivered at once. Please only enable Custom Response OR Repeat Message"
                }				
            }
            input "pContCmdsProfile", "bool", title: "Disable Conversation? (Alexa no longer prompts for additional commands, after a message is sent to a remote speaker, except for 'try again' if an error ocurs)", defaultValue: false
            input "pDisableAlexaProfile", "bool", title: "Disable Alexa Feedback Responses (silence Alexa - overrides all other Alexa Options)?", defaultValue: false
        }                 
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// ECHOSISTANT MAILBOX  
//////////////////////////////////////////////////////////////////////////////
page name: "mailbox"
def mailbox(){
    dynamicPage(name: "mailbox", title: "", uninstall: false){  
        section("") {
            paragraph "This space will be filled at a later time"
            href "pPetNotes", title: "Configure the Pets Notes"//, description: mPetNotesD(), state: mPetNotesS()
            href "pFamilyNotes", title: "Configure the Family Notes"//, description: mKidNotesD(), state: mKidNotesS()
        }        
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// QUICK NOTES            
//////////////////////////////////////////////////////////////////////////////
page name: "pPetNotes" 
def pPetNotes(){
    dynamicPage(name: "pPetNotes", title: "", install: false, uninstall: false) {
        section ("Family Pets Notes") {
            input "petNoteAct", "bool", title: "Activate your Pets Notes", required: false, default: false, submitOnChange: true
            if (petNoteAct) {
                input "litterBoxAct", "bool", title: "Does ${app.label} use a litter box?", required: false, default: false, submitOnChange: true
                paragraph "Your Family Pet's notes are now active for ${app.label}."
                paragraph "The following notes have been set for ${app.label}:"
                paragraph "${state.petShotNotify}"
                paragraph "${state.litterBoxCleaned}"
            }
            input "pSMS", "bool", title: "Configure Notifications for ${app.label}", required: false, defaultValue: false, submitOnChange: true
            if (pSMS) {
                input "psendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
                if (psendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
                input "psendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true 
                if (psendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                    input name: "psms", title: "Send text notification to (optional):", type: "phone", required: false
                }
                input "pPush", "bool", title: "Do you want to send a Push message when notes are made?", required: false, defaultValue: false, submitOnChange: true
            }
        }    
    }	    
}                	
page name: "pFamilyNotes" 
def pFamilyNotes(){
    dynamicPage(name: "pFamilyNotes", title: "", install: false, uninstall: false) {
        section ("Family Notes") {
            input "famNoteAct", "bool", title: "Activate your Family Notes", required: false, default: false, submitOnChange: true
            if (famNoteAct) {
                input "litterBoxAct", "bool", title: "Does ${app.label} use a litter box?", required: false, default: false, submitOnChange: true
                paragraph "Your Family notes are now active for ${app.label}."
                paragraph "The following notes have been set for ${app.label}:"
                paragraph "${state.petShotNotify}"
                paragraph "${state.litterBoxCleaned}"
            }
            input "fSMS", "bool", title: "Configure Notifications for ${app.label}", required: false, defaultValue: false, submitOnChange: true
            if (fSMS) {
                input "fSendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
                if (fSendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
                input "fSendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true 
                if (fSendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                    input name: "fsms", title: "Send text notification to (optional):", type: "phone", required: false
                }
                input "fPush", "bool", title: "Do you want to send a Push message when notes are made?", required: false, defaultValue: false, submitOnChange: true
            }
        }    
    }	    
}   
//////////////////////////////////////////////////////////////////////////////
/////////// PROFILE RESTRICTIONS
//////////////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////////
/////////// SECURITY - PIN SETTINGS 
//////////////////////////////////////////////////////////////////////////////
page name: "pSecurity"
	def pSecurity(){
		dynamicPage(name: "pSecurity", title: "",install: false, uninstall: false) {
			section ("Set PIN Number to Unlock Security Features") {
            	input "cPIN", "password", title: "Use this PIN for ALL Alexa Controlled Controls", default: false, required: false, submitOnChange: true
                	//input "cTempPIN", "password", title: "Guest PIN (expires in 24 hours)", default: false, required: false, submitOnChange: true
        	}
            section ("Configure Security Options for Alexa") {
				def routines = location.helloHome?.getPhrases()*.label.sort()
                input "cMiscDev", "capability.switch", title: "Allow these Switches to be PIN Protected...", multiple: true, required: false, submitOnChange: true
                input "cRoutines", "enum", title: "Allow these Routines to be PIN Protected...", options: routines, multiple: true, required: false
                input "uPIN_SHM", "bool", title: "Enable PIN for Smart Home Monitor?", default: false, submitOnChange: true
                if(uPIN_SHM == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Security"} 
                	input "uPIN_Mode", "bool", title: "Enable PIN for Location Modes?", default: false, submitOnChange: true
                if(uPIN_Mode == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Location Modes"} 
				if (cMiscDev) 			{input "uPIN_S", "bool", title: "Enable PIN for Switch(es)?", default: false, submitOnChange: true}
               	if(uPIN_S == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Switches"} 
               	if (cTstat) 			{input "uPIN_T", "bool", title: "Enable PIN for Thermostats?", default: false, submitOnChange: true}
				if(uPIN_T == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Thermostats"}                             
				if (cDoor || cRelay) 	{input "uPIN_D", "bool", title: "Enable PIN for Doors?", default: false, submitOnChange: true}
				if(uPIN_D == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Doors"}                             
				if (cLock) 				{input "uPIN_L", "bool", title: "Enable PIN for Locks?", default: false, submitOnChange: true}
				if(uPIN_L == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Locks"}                             
         	}
		}
	}
//////////////////////////////////////////////////////////////////////////////
/////////// PROFILE ACTIONS - TRIGGERED WHEN PROFILE EXECUTES 
//////////////////////////////////////////////////////////////////////////////
page name: "pActions"
def pActions() {
    dynamicPage(name: "pActions", uninstall: false) {
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
        }
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// DEVICE ACTIONS CONTROL 
//////////////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////////
/////////// WEATHER SETTINGS  
//////////////////////////////////////////////////////////////////////////////
page name: "pWeatherConfig"
def pWeatherConfig() {
	dynamicPage(name: "pWeatherConfig") {
		section () {
    		input "wMetric", "bool", title: "Report Weather In Metric Units\n(°C / km/h)", required: false
            input "wZipCode", "text", title: "Zip Code (If Location Not Set)", required: false
		}
	}
}
//////////////////////////////////////////////////////////////////////////////
/////////// SKILL CONFIGURATION  
//////////////////////////////////////////////////////////////////////////////
page name: "pSkillConfig"
def pSkillConfig() {
	dynamicPage(name: "pSkillConfig") {
		section () {
        def token = parent.checkToken()
        log.debug "token = $token"
        def url = "${getApiServerUrl()}/api/smartapps/installations/${token.appId}/skill?access_token=${token.token}" // NEEDS UPDATE TO LOOK UP PARENT TOKEN
		log.debug "url = $url"
        paragraph ("Grab skill details from here")
        href "", title: "Open Skill Settings in a Browser", style: "external", url: url, required: false, description: "Click here" 
		}
	}
}
/*************************************************************************************************************
   DATA MAPPING
************************************************************************************************************/
mappings {
	path("/skill") {action: [GET: "skillConfig"]}	
}
/*************************************************************************************************************
   SKILL CONFIGURATION HANDLER
************************************************************************************************************/
def skillConfig() {
	def html = """
			<!DOCTYPE HTML>
				<html>
					<head><title>Intent Schema</title></head>
						<body>	<p>"json formatted text goes here"  </p></body>
                        		<p>"json formatted text goes here"  </p>
					<head><title>Sample Utterances</title></head>
						<body>	<p>" more json formatted text goes here" </p></body>                        
                </html>
		"""
	render contentType: "text/html", data: html                             
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
	//subscriptions()
    //Alexa Voice Settings
    state.pContCmds = settings.pContCmdsProfile == false ? true : settings.pContCmdsProfile == true ? false : true
    state.pContCmdsR = "init"
    state.pTryAgain = false
    //Sound Cancellation    
    state.pMuteAlexa = settings.pDisableAlexaProfile ?: false
    state.pMuteAll = settings.pDisableALLProfile ?: false 
    log.debug "Init with settings: ${settings}, current app version: ${release()}"
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
def getProfileData() {
//	NEED THIS TO RETURN JSON DATA FROM THE CURRENT PROFILE, AND THE GROUPS.
/*	def groupData = new groovy.json.JsonBuilder()
	def root = builder {
              name "Devin"
              data {
                 type "Test"
                 note "Dummy"
              }
              addUrn(delegate, "gender", "male")
              addUrn(delegate, "zip", "43230")
           }
	def groupData = 
    def groups = getChildApps()
	for(group in groups) {
        groupData += "'group':'${group.label}'" + group.getGroupData()
    }
    def getSettingData() {     
    	def res = getSettings()     
        def resultJson = new groovy.json.JsonOutput().toJson(res)     
        render contentType: "application/json", data: resultJson 
    }
    return new groovy.json.JsonBuilder(settings) //+ groupData
    
    def res = getSettings()     
   	def resultJson = new groovy.json.JsonOutput().toJson(res)     
    render contentType: "application/json", data: resultJson 
    */
    return getSettings()
}
/******************************************************************************************************
   TEXT TO SPEECH PROCESS PROFILE
******************************************************************************************************/
def processText(params) {
	log.info "profile control & messaging Evaluate has been activated"
	def tts = params.ptts.toLowerCase()
	def intent = params.pintentName        
	def childName = app.label       
	//Data for CoRE 
	def data = [args: tts]
    //Output Variables
    def pTryAgain = false
    def pPIN = false
    def String pContCmdsR = (String) "tts"
	def String outputTxt = (String) null    
	def String command = (String) null
	def String deviceType = (String) null
    if (parent.debug) log.debug "Message received from Parent with: (tts) = '${tts}', (intent) = '${intent}', (childName) = '${childName}', current app version: ${release()}"  
    //Sending event to WebCoRE
	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")    
    if (parent.debug) log.debug "sendNotificationEvent sent to CoRE from ${app.label}"
    
    if (intent == childName) {
		outputTxt = runCommand(tts)
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]  
   	}
}   
def searchRealDevices(String input) {
    return settings.collect{k, devices -> devices}.flatten().unique().find{input.find(~/(?i)\b${it.toString()}\b/)}
}
def searchDeviceTypes(String input) {
	return settings.collect{k, devices -> k}.find{it == "p" + getDeviceType(input)}
}
/*Map searchGroupDevices(String input, ArrayList fromList) {
	Map allDevices = [:]		////															Added for groups device search...
	for (device in fromList) {
    	allDevices << settings.findAll{it.key.toLowerCase().endsWith(device)}
	}
    return allDevices
}*/
ArrayList getFeedBackWords() 	{["give","for","tell","what","how","is","when","which","are","how many","check","who"]}
ArrayList getCommandEnable() 	{["on","start","enable","engage","open","begin","unlock","unlocked"]}
ArrayList getCommandDisable() 	{["off","stop","cancel","disable","disengage","kill","close","silence","lock","locked","quit","end"]}
ArrayList getCommandMore()		{["increase","more","too dark","not bright enough","brighten","brighter","turn up"]}
ArrayList getCommandLess()		{["darker","too bright","dim","dimmer","decrease","lower","low","softer","less"]}
ArrayList getDeviceTypes()		{["light","switch","fan","lock","garage","door","window","shade","curtain","blind","tstat","indoor","outdoor","vent","valve","water","speaker","synth","media","relay"]}
ArrayList getDelayCommand()		{["delay","wait","until","after","around","within","in","about"]}

String parseWordReturn(String input, ArrayList fromList) {
	for (item in fromList) {
    	if (input.contains(item)) {
        	return item
        }
    }
}
Boolean parseWordFound(String input, ArrayList fromList) {
	for (item in fromList) {
    	if (input.contains(item)) {
        	return true
        }
    }
}
String getCommand(text) {
	return parseWordFound(text, commandMore) ? "increase" : parseWordFound(text, commandLess) ? "decrease" : parseWordFound(text, commandEnable) ? "on" : parseWordFound(text, commandDisable) ? "off" : null 
}
def getDeviceType(text) {
	return parseWordReturn(text, deviceTypes) ?: null
}
String runCommand(tts) { 
	//not sure how to implement status here, need to understand how we can get feedback keywords
    String feedBack = parseWordFound(tts, feedBackWords) ? "status" : ""
	String command = getCommand(tts) 
    String deviceType = null
    Boolean foundRealDevice = false
    Boolean foundGroup = false
    def theDevices = null
    
    /*String groupDevice = settings.findAll{it.key.startsWith("groupId")}.find{tts.contains(it.value.toLowerCase())}.key.replace("groupId", "g")
	if (groupDevices) {
    	log.debug "groupFound: ${groupDevices}"
        foundGroup = true
    	//Loop through groups groupDevice + deviceTypes = g1Light << g1Switch << g1Fan... etc...
    }
    */
    
	def realDevice = searchRealDevices(tts) //Search for real devive in Profile first... 
    if (realDevice) {
        theDevices = realDevice
        foundRealDevice = true
        // Need to go through and replace Group names like Office/Kitchen/etc... they conflict with real device names, if they really meant a group.
    } 
    //if delay keyword, figure out amount... that'll be FUN!
	//Boolean delayAction = parseWordFound(tts, delayCommand)

    def theDelay = 0
    def theLevel
    def theStatus
	
    //if (!foundGroup && !foundRealDevice) {
	//	theDevices = searchDeviceTypes(tts)
    //}
    
    log.debug "deviceType:${deviceType}, theDevices:${theDevices}, command:${command}, feedBack${feedBack}"

    if (command) {
        switch ("${command}${feedBack}") {
            case "on":
            theDevices*.on([delay: theDelay])
            return "Ok, turning ${theDevices} ${command}"
            break
            case "off":
            theDevices*.off([delay: theDelay])
            return "Ok, turning ${theDevices} ${command}"
            break
            case "increase":
            //check device(s) for capabilities and adjust accodrinly (theDevices*.capabilities)
            theDevices*.on([delay: theDelay])
            return "Ok, increasing ${theDevices} ${command}"
            break
            case "decrease":
            //check device(s) for capabilities and adjust accodrinly (theDevices*.capabilities)
            theDevices*.off([delay: theDelay])
            return "Ok, lowering ${theDevices} ${command}"
            break
            case "onstatus":
            theStatus = theDevices*.latestValue("switch")
            return "${theDevices} is currently ${theStatus}"
            break
            case "offstatus":
            theStatus = theDevices*.latestValue("switch")
            return "${theDevices} is currently ${theStatus}"
            break
        }
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