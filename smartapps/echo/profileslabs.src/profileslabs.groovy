/* 
 * Message and Control Profile - EchoSistant Add-on 
 *
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
    def text = "R.5.0.1c"
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
	rebuildGroups()
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section ("Name Your Profile") {
            label title:"Profile Name", required:true
        } 
        section("Devices, Groups, Feedback, and Keypads") {
            href "devices", title: "Control and Feedback", description: pSendComplete(), state: pSendSettings()   
        }  
        section("Message Output and Alexa Responses") {
            href "messaging", title: "Outgoing Messages", description: pSendComplete(), state: pSendSettings()   
        }              
        section ("Echo Mailbox, Quick Notes, Reminders") {
            href "mailbox", title: "Incoming Messages", description: pSendComplete(), state: pSendSettings()    
        }
        section ("Settings" , hideable: true, hidden: true ) {
            href "pRestrict", title: "General Restrictions", description: pRestrictComplete(), state: pRestrictSettings()   
            href "pSecurity", title: "PIN Settings", description: pRestrictComplete(), state: pRestrictSettings()
			href "pActions", title: "Profile Actions (to execute when Profile runs)", description: pActionsComplete(), state: pActionsSettings()
            href "pWeatherConfig", title: "Weather Settings", description: pRestrictComplete(), state: pRestrictSettings()   
			href "pSkillConfig", title: "Install AWS Skill", description: pRestrictComplete(), state: pRestrictSettings()   

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
            href "pDevices", title: "Main Profile Control and Feedback", params: [type: "f"]//, description: pRestrictComplete(), state: pRestrictSettings()
            href "pGroups", title: "Create Groups within Profile", required: false //description: pGroupComplete(), state: pGroupSettings()
            href "pKeypads", title: "Keypads and Associated Actions", description: pSendComplete(), state: pSendSettings()
            href "pDefaults", title: "Profile Defaults"//, description: mDefaultsD(), state: mDefaultsS()           
        }        
    }
}
//////////////////////////////////////////////////////////////////////////////
/////////// INDIVIDUAL DEVICE CONTROL AND FEEDBACK 
//////////////////////////////////////////////////////////////////////////////
page name: "pDevices"
def pDevices(params){
    dynamicPage(name: "pDevices", title: "", uninstall: false){
        section("Locks") { //, hideWhenEmpty: true
            input "${params.type}Locks", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false, submitOnChange: true
        }
        section("Garage Doors") { //, hideWhenEmpty: true
            input "${params.type}Garage", "capability.garageDoorControl", title: "Select garage doors", multiple: true, required: false, submitOnChange: true
        	input "${params.type}Relay", "capability.switch", title: "Select Garage Door Relay(s)...", multiple: false, required: false, submitOnChange: true
				if (fRelay) input "cContactRelay", "capability.contactSensor", title: "Allow This Contact Sensor to Monitor the Garage Door Relay(s)...", multiple: false, required: false 
        }
        section("Window Coverings") { //, hideWhenEmpty: true
            input "${params.type}Shades", "capability.windowShade", title: "Select devices that control your Window Coverings", multiple: true, required: false, submitOnChange: true
        }
        section ("Climate Control") { //, hideWhenEmpty: true
            input "${params.type}Tstat", "capability.thermostat", title: "Allow These Thermostat(s)...", multiple: true, required: false
            input "${params.type}Indoor", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Indoor Temperature...", multiple: true, required: false
            input "${params.type}OutDoor", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Outdoor Temperature...", multiple: true, required: false
            input "${params.type}Vents", "capability.switchLevel", title: "Select smart vents", multiple: true, required: false, submitOnChange: true
        }
		section ("Water") { //, hideWhenEmpty: true
			input "${params.type}Valve", "capability.valve", title: "Select Water Valves", required: false, multiple: true, submitOnChange: true
			input "${params.type}Water", "capability.waterSensor", title: "Select Water Sensor(s)", required: false, multiple: true, submitOnChange: true
		}
		section ("Media"){ //, hideWhenEmpty: true
			input "${params.type}Speaker", "capability.musicPlayer", title: "Allow These Media Player Type Device(s)...", required: false, multiple: true
	     	input "${params.type}Synth", "capability.speechSynthesis", title: "Allow These Speech Synthesis Capable Device(s)", multiple: true, required: false
			input "${params.type}Media", "capability.mediaController", title: "Allow These Media Controller(s)", multiple: true, required: false
    	}
        section("Switches, Dimmers") { //, hideWhenEmpty: true
            input "${params.type}Switches", "capability.switch", title: "Select Lights and Bulbs", multiple: true, required: false, submitOnChange: true
            input "${params.type}MiscSwitches", "capability.switch", title: "Select Switches that control misc devices", multiple: true, required: false, submitOnChange: true
            input "${params.type}Fans", "capability.switch", title: "Select devices that control Fans and Ceiling Fans", multiple: true, required: false, submitOnChange: true
        }
        section("Feedback Only Devices") { //, hideWhenEmpty: true
			input "${params.type}Motion", "capability.motionSensor", title: "Select Motion Sensors...", required: false, multiple: true
            input "${params.type}Doors", "capability.contactSensor", title: "Select contacts connected only to Doors", multiple: true, required: false, submitOnChange: true
            input "${params.type}Windows", "capability.contactSensor", title: "Select contacts connected only to Windows", multiple: true, required: false, submitOnChange: true
            input "${params.type}Presence", "capability.presenceSensor", title: "Select These Presence Sensors...", required: false, multiple: true
            input "${params.type}Battery", "capability.battery", title: "Select These Device(s) with Batteries...", required: false, multiple: true
			input "${params.type}CO2", "capability.carbonDioxideMeasurement", title: "Select Carbon Dioxide Sensors (CO2)", required: false            
			input "${params.type}CO", "capability.carbonMonoxideDetector", title: "Select Carbon Monoxide Sensors (CO)", required: false
			input "${params.type}Humidity", "capability.relativeHumidityMeasurement", title: "Select Relative Humidity Sensor(s)", required: false
			input "${params.type}Sound", "capability.soundPressureLevel", title: "Select Sound Pressure Sensor(s) (noise level)", required: false
        }
	}
}   
//////////////////////////////////////////////////////////////////////////////
/////////// GROUP CONTROL AND FEEDBACK
//////////////////////////////////////////////////////////////////////////////
def pGroups() {
	rebuildGroups()
	dynamicPage(name: "pGroups", title: "Groups", install: false, uninstall: false) {
		log.debug "state.groups = ${state.groups}"
        def groups = state.groups
		section("") {
			href "pGroup", title: "Add a new Group", required: false, params: [groupId: 0]
		}
		if (groups.size()) {
			section("Groups") {
				for (group in groups) {
					href "pGroup", title: group.name, required: false, params: [groupId: group.groupId]
				}
			}
		}
	}
}
def pGroup(params) {
	log.debug "params = ${params}"
	def groupId = (int) (params?.groupId != null ? params.groupId : state.groupId)
	if (!groupId) {
		//generate new group id
		groupId = 1
		def existingGroups = settings.findAll{ it.key.startsWith("groupId") }
		for (group in existingGroups) {
			def id = tap.key.replace("groupId", "")
			if (id.isInteger()) {
				id = groupId.toInteger()
				if (id >= groupId) groupId = (int) (id + 1)
			}
		}
	}
	state.groupId = groupId
	dynamicPage(name: "pGroup", title: "Group", install: false, uninstall: false) {
		section("") {
        	input "groupId${groupId}", "string", title: "Name", description: "Enter a name for this Group", required: false, defaultValue: "Group #${groupId}"
			href "pDevices", title: "Groups Control and Feedback", params: [type: "g"]	
		}
	}
}
private rebuildGroups() {
	def groups = settings.findAll{it.key.startsWith("groupId")}
	state.groups = []
	for(group in groups) {
		def groupId = group.key.replace("groupId", "")
		if (groupId.isInteger()) {
			if (group.value != null) {
				def name = group.value
				if (name) {
					def t = [
						groupId: groupId.toInteger(),
						name: name,
					]
					state.groups.push t
				}
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
        def url = "${getApiServerUrl()}/api/smartapps/installations/${app.id}/skillConfig?access_token=${state.accessToken}"
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
    state.lastMessage
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
    // Turn OFF the Color Loop
    unschedule("startLoop")
    unschedule("continueLoop")
    if (garageDoors) {
        subscribe(sLocksGarage,"codeEntered",codeEntryHandler)
    }
    subscribe(sLocksVP, "codeEntered", codeEntryHandler)
    if (sLocksSHM) {
        subscribe(location,"alarmSystemStatus",alarmStatusHandler)
        subscribe(sLocksSHM,"codeEntered",codeEntryHandler)
    }
    //WEATHER UPDATES
    runEvery1Hour(mGetWeatherUpdates)
    state.lastWeatherCheck
    state.lastWeatherUpdate
    //SHM status change and keypad initialize
    subscribe(location, locationHandler)
    subscribe(location, "alarmSystemStatus",alarmStatusHandler)//used for ES speaker feedback
    state.responseTxt = null
    state.lambdaReleaseTxt = "Not Set"
    state.lambdaReleaseDt = "Not Set" 
    state.lambdatextVersion = "Not Set"
    //Alexa Responses
    state.pTryAgain = false
    state.pContCmds = settings.pDisableContCmds == false ? true : settings.pDisableContCmds == true ? false : true
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
    if (fDevice != null) {
        fDevice = fDevice.replaceAll("[^a-zA-Z0-9 ]", "") }
    if (debug){
        log.debug 	"Feedback data: (fDevice) = '${fDevice}', "+
            "(fQuery) = '${fQuery}', (fOperand) = '${fOperand}', (fCommand) = '${fCommand}', (fIntentName) = '${fIntentName}'"}
    def fProcess = true
    state.pTryAgain = false
    if (petNoteAct) {
        log.info "Initializing variables for '${app.label}'"
        if (state.petWalkNotify == null) {state.petWalkNotify = "I'm sorry, I have not been told when the cat was walked" }
        if (state.petShotNotify == null) {state.petShotNotify = "I'm sorry, I have not been told when the cat was shot" }
        if (state.petBathNotify == null) {state.petBathNotify = "I'm sorry, I have not been told when the cat was bathed" }
        if (state.petFedNotify == null) {state.petFedNotify = "I'm sorry, I have not been told when the cat was fed" }
        if (state.petMedNotify == null) {state.petMedNotify = "I'm sorry, I have not been told when the cat was medicated" }
        if (state.petBrushNotify == null) {state.petBrushNotify = "I'm sorry, I have not been told when the cat was brushed" }
    }
    if (litterBoxAct) {
        log.info "Initialing variables for the litter box"
        if (state.litterBoxCleaned == null) {state.litterBoxCleaned = "I'm sorry, I have not been told when the litter box was cleaned" }
        if (state.litterBoxScooped == null) {state.litterBoxScooped = "I'm sorry, I have not been told when the litter box was scooped" }
    }
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
   SPEECH AND TEXT PROCESSING INTERNAL - FEEDBACK
******************************************************************************************************/
def profileFeedbackEvaluate(params) {
    log.info "profileFeedbackEvaluate has been activated"	
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
    if (debug) log.debug "Messaging Profile Data: (ptts) = '${ptts}', (pintentName) = '${pintentName}'"   
    //Sending event to WebCoRE
    sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")

    if (command == "undefined") {
        outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = false
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }    
    if (parent.debug) log.debug "sendNotificationEvent sent to CoRE from ${app.label}"
    if (pSendSettings() == "complete" || pGroupSettings() == "complete" || pDevicesSettings() == "complete"){
        if (intent == childName){
            if (test){
                outputTxt = "Congratulations! Your EchoSistant is now setup properly, good job" 
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
            def getCMD = getFeedbackCommand(tts) 
            deviceType = getCMD.deviceType
            command = getCMD.command
            if(parent.debug) log.debug "I have received a feedback command: ${command}, deviceType:  ${deviceType}"

            //>>>FEEDBACK HANDLER>>>>

            def fDevice = tts.contains("garage") ? fGarage : tts.contains("vent") ? fVents : tts.contains("light") ? fSwitches : tts.contains("door") ? fDoors : tts.contains("window") ? fWindows : tts.contains("fan") ? fFans : 
            tts.contains("lock") ? fLocks : tts.contains("shade") ? fShades : tts.contains("curtains") ? fShades : tts.contains("blinds") ? fShades : tts.startsWith("who") ? fPresence : tts.contains("batteries") ? fBattery : null
            def fValue = tts.contains("garage") ? "contact" : tts.contains("vent") ? "switch" : tts.contains("light") ? "switch" : tts.contains("door") ? "contact" : tts.contains("window") ? "contact" : tts.contains("fan") ? "switch" : 
            tts.contains("lock") ? "lock" : tts.contains("shade") ? "windowShade" : tts.contains("blind") ? "windowShade" : tts.contains("curtains") ? "windowShade" : null
            def fName = tts.contains("vent") ? "vents" : tts.contains("lock") ? "locks" : tts.contains("door") ? "doors" : tts.contains("window") ? "windows" : tts.contains("fan") ? "fans" : 
            tts.contains("light") ? "lights" : tts.contains("shade") ? "shades" : tts.contains("blind") ? "blinds" : tts.contains("curtains") ? "curtains" : null
            if (tts.contains("check") && tts.contains("light")) { command = "on" }
            if (tts.contains("check")) {
                if (tts.contains("lock") || tts.contains("door") || tts.contains("window") || tts.contains("vent") || tts.contains("shades") || tts.contains("blind") || tts.contains("curtain")) {
                    command = "open" }}

            //>>> Mode Status Feedback >>>>
            if (tts.contains("mode")) {
                outputTxt = "The Current Mode is " + location.currentMode      
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
            if (tts.contains("tell me")) {
                if (tts.contains("mode")){
                    outputTxt = "The Current Mode is " + location.currentMode      
                    return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
                }
            }

            //>>> Garage Doors Feedback >>>>
            if (tts.contains("garage")) {
                if (deviceType == "fbGarageOpen" || deviceType == "fbGarageClose") {
                    log.info "garage door method is active"
                    if (fGarage == null) {
                        outputTxt = "I'm sorry, it seems that you have not selected any devices for this query, please configure your feedback devices in the EchoSistant smartapp."
                    }
                    else {
                        fDevice.each { deviceName -> 
                            if (deviceName.currentValue("contact") == "${command}") {
                                String device  = (String) deviceName
                                def status = deviceName.currentValue("contact")
                                if (status == command) {
                                    outputTxt = "Yes, the ${deviceName} is ${command}"
                                }
                            }
                            else if (tts.contains("check") && command == "open") {
                                def status = deviceName.currentValue("contact")
                                outputTxt = "The ${deviceName} is currently " + status
                            }

                            else { outputTxt = "No, the ${deviceName} is not currently ${command}" }
                        }
                    }
                }    
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }   	             
            //>>> Battery Level >>>>                        
            if(tts.contains("batteries") || tts.contains("battery level") || tts.contains("battery")) {
                def cap = "bat"
                def dMatch = deviceMatchHandler(fDevice)	
                if (dMatch.deviceMatch == null) { 		
                    def devList = getCapabilities(cap)
                    if(devList instanceof String){
                        outputTxt = devList
                        log.error " devList = ${devList}"

                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                 	
                    }
                    else {
                        if (tts.startsWith("how") || tts.startsWith("how many") || tts.startsWith("are there") || tts.contains("low") || tts.contains("give") || tts.contains("get")) {
                            if (devList.listSize > 0) {
                                if (devList.listSize == 1) {
                                    outputTxt = "There is one device with low battery level , would you like to know which one"                           			
                                }
                                else {
                                    outputTxt = "There are " + devList.listSize + " devices with low battery levels, would you like to know which devices"
                                }
                                def sdevices = devList.listBat
                                def devListString = sdevices.join(",")
                                data.list = devListString
                                state.lastAction = devListString
                                state.pContCmdsR = "bat"
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                            
                            }
                            else {outputTxt = "There are no devices with low battery levels"}
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                         
                        }
                        else if (tts.contains ("what") || tts.contains ("which")) {
                            if (devList.listSize > 0) {
                                outputTxt = "The following devices have low battery levels " + devList.listBat.sort()//.unique()
                            }
                            else {outputTxt = "There are no devices with low battery levels "
                                 } 
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                         
                        }
                    }
                }
                else {
                    device = dMatch.deviceMatch
                    currState = device.currentState("battery").value
                    stateTime = device.currentState("battery").date.time
                    def timeText = getTimeVariable(stateTime, deviceType)
                    outputTxt = "The battery level of " + fDevice + " is " + currState + " percent and was registered " + timeText.currDate + " at " + timeText.currTime
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }                   
            }
            //>>> Settings Feedback >>>>                                    
            if(tts.endsWith("settings")) {
                outputTxt = settingsFeedback()
            }
            //>>> PetNotes Feedback >>>>
            if (tts.startsWith("when") || tts.startsWith("has")) {
                if (tts.contains("litter box") || tts.contains("litterbox")) {
                    if (fCommand == "scooped" && state.litterBoxScooped != null ) {outputTxt = state.litterBoxScooped}
                    if (fCommand == "cleaned" && state.litterBoxCleaned != null ) {outputTxt = state.litterBoxCleaned}
                }
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
            }
            if(tts.startsWith("when") || tts.startsWith("did")) {
                if(tts.contains("was") || tts.contains("you")) {
                    if(tts.contains("she") || tts.contains("he") || tts.contains("${app.label}") || tts.contains("get") || tts.contains("your")) {
                        if (tts.contains("medicated") && state.petShotNotify.contains("last") && state.petMedNotify.contains("last")) {
                            outputTxt = "I have been told that " + state.petMedNotify + " , I have also been told that " + state.petShotNotify
                        }
                        else if (tts.contains("medicated") && state.petShotNotify.contains("last")) { outputTxt = "I have not been told when ${app.label} was medicated, but " + state.petShotNotify 
                                                                                                    }
                        else if (tts.contains("medicated")) { outputTxt = state.petMedNotify 
                                                            }
                        if (tts.contains("shot") && state.petShotNotify.contains("last") && state.petMedNotify.contains("last")) {
                            outputTxt = "I have been told that " + state.petShotNotify + " , I have also been told that " + state.petMedNotify
                        }
                        else if (tts.contains("shot") && state.petMedNotify.contains("last")) { 
                            outputTxt = "I have not been told when ${app.label} was shot, but " + state.petMedNotify 
                        }
                        else if (tts.contains("shot")) { outputTxt = state.petShotNotify 
                                                       }
                        else if (tts.contains("fed") && state.petFedNotify != null ) {outputTxt = state.petFedNotify}
                        else if (tts.contains("bathed") && state.petBathNotify != null ) {outputTxt = state.petBathNotify}
                        else if (tts.contains("walked") && state.petWalkNotify != null ) {outputTxt = state.petWalkNotify}
                        else if (tts.contains("brushed") && state.petBrushNotify != null ) {outputTxt = state.petBrushNotify}
                    }
                }    
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]    
            }
            //>>> Presence Feedback >>>>
            if (tts.startsWith("who") || tts.contains("people") || tts == "check on my family") {
                if (deviceType == "fbPresence") {
                    state.pTryAgain = false
                    if(fPresence == null) {
                        outputTxt = "I'm sorry, it seems that you have not selected any presence sensors for this query. Please check the configuration of your EchoSistant App"
                        return outputTxt}
                    if(fPresence){
                        def devListP = []
                        def devListNP = []
                        if (fPresence.latestValue("presence").contains("present")) {
                            fPresence.each { deviceName ->
                                if (deviceName.latestValue("presence")=="present") {
                                    String device  = (String) deviceName
                                    devListP += device
                                }
                            }
                        }
                        if (fPresence.latestValue("presence").contains("not present")) {
                            fPresence.each { deviceName ->
                                if (deviceName.latestValue("presence")=="not present") {
                                    String device  = (String) deviceName
                                    devListNP += device
                                }
                            }
                        }
                        if (tts.contains("not")) {
                            if (devListNP.size() > 0) {
                                if (devListNP.size() == 1) {
                                    outputTxt = "Only" + devListNP + "is not home"                         			
                                }
                                else {
                                    outputTxt = "The following " + devListNP.size() + " family members are not at home: " + devListNP
                                }
                            }
                            else outputTxt = "Everyone is at home"
                        }
                        else if (tts.endsWith("here") || tts.endsWith("at home") || tts.endsWith("present") || tts.endsWith("home") || tts.startsWith("check")) {
                            if (devListNP.size() == 0) {
                                outputTxt = "Everyone is at home"
                            }
                            if (devListP.size() > 0) {
                                if (devListP.size() == 1) {
                                    outputTxt = "Only" + devListP + "is at home"                         			
                                }
                                else {
                                    outputTxt = "The following " + devListP.size() + " family members are at home: " + devListP
                                }
                            }
                            else outputTxt = "No one is home"
                        }
                    }
                }
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]}

            /// check the house                        
            if (tts.startsWith("check")) {
                if (tts.endsWith("home") || tts.endsWith("house") || tts.endsWith("my home") || tts.endsWith("my house")) {
                    log.info "this is the house status"
                    def devListDoor = []
                    if (fDoors != null) {
                        if (fDoors.latestValue("contact").contains("open")) {
                            fDoors.each { deviceName ->
                                if (deviceName.latestValue("contact")=="open") {
                                    String device  = (String) deviceName
                                    devListDoor += device
                                }
                            }                
                        }
                    }
                    def devListWindow = []
                    if (fWindows != null) {
                        if (fWindows.latestValue("contact").contains("open")) {
                            fWindows.each { deviceName ->
                                if (deviceName.latestValue("contact")=="open") {
                                    String device  = (String) deviceName
                                    devListWindow += device
                                }
                            }
                        }
                    }
                    def devListLight = []
                    if (fSwitches != null) {
                        if (fSwitches.latestValue("switch").contains("on")) {
                            fSwitches.each { deviceName ->
                                if (deviceName.latestValue("switch")=="on") {
                                    String device  = (String) deviceName
                                    devListLight += device
                                }
                            }
                        }
                    }
                    def devListLock = []
                    if (fLocks != null) {
                        if (fLocks.latestValue("lock").contains("unlocked")) {
                            fLocks.each { deviceName ->
                                if (deviceName.latestValue("lock")=="unlocked") {
                                    String device  = (String) deviceName
                                    devListLock += device
                                }
                            }
                        }
                    }
                    def devListNP = []
                    if (fPresence != null) {
                        if (fPresence.latestValue("presence").contains("not present")) {
                            fPresence.each { deviceName ->
                                if (deviceName.latestValue("presence")=="not present") {
                                    String device  = (String) deviceName
                                    devListNP += device
                                }
                            }
                        }
                    }            
                    if (devListDoor.size() > 0 || devListWindow.size() > 0 || devListLock.size() > 0 || devListNP.size() > 0) {
                        def sSHM = location.currentState("alarmSystemStatus").value      
                        sSHM = sSHM == "off" ? "Disarmed" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Home" : "unknown"
                        outputTxt = "The home has " + devListDoor.size() + " doors open, " + devListWindow.size() + " windows open, " + devListLock.size + " locks unlocked, and " + devListNP.size() + " family members are not home, "
                        outputTxt = outputTxt + " as well as the security system is set to: ${sSHM}"
                    }
                    else if (devListDoor.size() == 0 && devListWindow.size() == 0 && devListLock.size() == 0 && devListNP.size() == 0) {
                        def sSHM = location.currentState("alarmSystemStatus").value      
                        sSHM = sSHM == "off" ? "Disarmed" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Home" : "unknown"
                        outputTxt = "All of the doors and windows are closed, the locks are locked, everyone is home, " +
                            " and the security system is set to: ${sSHM}"
                    }
                }
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }    
            //>>> Security Status Feedback >>>>
            if (tts.contains("smart home monitor") || tts.contains("alarm system") || tts.contains("alarm") || tts.contains("security")){
                def sSHM = location.currentState("alarmSystemStatus").value      
                sSHM = sSHM == "off" ? "Disarmed" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Home" : "unknown"
                outputTxt = "Your Smart Home Monitor Status is " +  sSHM
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]				
            }

            //>>> Misc Devices Feedback >>>>            
            if (tts.contains("window") || tts.contains("vent") || tts.contains("lock") || tts.contains("blind") || tts.contains("curtain") || tts.contains("shade") || tts.contains("door") || tts.contains("light")) {
                if (tts.contains("on") || tts.contains("off") || tts.contains("open") || tts.contains("closed") || tts.startsWith("check")) {
                    def devList = []
                    if ((fDevice == fDoors && fDoors == null) || (fDevice == fWindows && fWindows == null) || (fDevice == fVents && fVents == null) || (fDevice == fLights && fLights == null)) {
                        outputTxt = "I'm sorry, it seems that you have not selected any devices for this query, please configure your feedback devices in the EchoSistant smartapp."
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                    else //if (fDevice != null) {
                        if (fDevice.latestValue("${fValue}").contains(command)) {
                            fDevice.each { deviceName ->
                                if (deviceName.latestValue("${fValue}")=="${command}") {
                                    String device  = (String) deviceName
                                    devList += device
                                    log.info "device = ${fDevice}, value = ${fValue}, deviceType = ${fName}, device list = '${devList}'"
                                }
                            }
                        }
                    //}
                    // RETURNS A NUMBER OF DEVICES //        
                    if (tts.startsWith("how many") || tts.startsWith("check")) { 
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "There is " + devList.size() + " " + fName + " " + command + " in the ${app.label}. "                           			
                            }
                            else {outputTxt = "There are " + devList.size() + " " + fName + " " + command + " in the ${app.label}? "
                                 }
                        }
                        else (outputTxt = "There are no ${fName} " + command + " in the ${app.label} " )
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }    
                    // RETURNS A 'YES, THERE ARE ## OF DEVICES' ~~ Added to keep normal language //        
                    if (tts.startsWith("are")) {
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "Yes, The " + devList + " is the only " + fName + " " + command +  "  in the ${app.label}, would you like anything else? "                           			
                            }
                            else {outputTxt = "Yes, there are " + devList.size() + " " + fName + " " + command + " in the ${app.label}, would you like to know which ones? "
                                 }
                        }    
                        else (outputTxt = "There are no ${fName} " + command + " in the ${app.label} " )
                        return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
                    }
                    // RETURNS A LIST OF DEVICES //        
                    if (tts.startsWith("what") || tts.startsWith("which")) {
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "The " + devList + " is the only " + fName + " " + command +  " in the ${app.label}, would you like anything else? "                           			
                            }
                            else {outputTxt = "The following ${fName} are " + command + " in the ${app.label} " + devList + ", would you like anything else?"
                                 }
                        }
                        else (outputTxt = "There are no ${fName} " + command + " in the ${app.label} " )   
                        return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
                    }
                    // RETURNS STATUS OF A SPECIFIC DEVICE //    
                    if (tts.startsWith("is")) {
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "Yes, the '${devList}'is ${command}"                    			
                            }
                            else {outputTxt = "No, the '${devList}'is not ${command}"
                                 }
                        }
                    }
                    return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
                }
            }
        }
        else {
            outputTxt = "Sorry, you must first set up your profile before trying to execute it."
            pTryAgain = true
            return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
        }
    }
    //}    
    //>>> Temp >>>>      
    if(tts.contains("temperature")) {
        if(fTstat == null){
            outputTxt = "I'm sorry, it seems that you have not selected any temperature devices in the EchoSistant app"
        }
        else if(fTstat){
            //      fTstat.find {s -> 
            //           if(s.label.toLowerCase() == fDevice.toLowerCase()){
            deviceType = "fTstat"
            def currentTMP = s.latestValue("temperature")
            int temp = currentTMP
            stateDate = s.currentState("temperature").date
            stateTime = s.currentState("temperature").date.time
            def timeText = getTimeVariable(stateTime, deviceType)            
            outputTxt = "The temperature " + fTstat + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
            //               }

            if (outputTxt != null) {
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
            //          }            
        }
        if(fMotion){
            fMotion.find {s -> 
                if(s.label.toLowerCase() == fDevice?.toLowerCase()){
                    deviceType = "fTstat"
                    def currentTMP = s.latestValue("temperature")
                    int temp = currentTMP
                    stateDate = s.currentState("temperature").date
                    stateTime = s.currentState("temperature").date.time
                    def timeText = getTimeVariable(stateTime, deviceType)
                    outputTxt = "The temperature in the " + fDevice + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
                }
            }
            if (outputTxt != null) {
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }            
        }
        if(fWater){
            fWater.find {s -> 
                if(s.label.toLowerCase() == fDevice?.toLowerCase()){
                    deviceType = "fWater"
                    def currentTMP = s.latestValue("temperature")
                    int temp = currentTMP
                    stateDate = s.currentState("temperature").date
                    stateTime = s.currentState("temperature").date.time
                    def timeText = getTimeVariable(stateTime, deviceType)            
                    outputTxt = "The temperature of " + fDevice + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
                }
            }
            if (outputTxt != null) {
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
        }            
        if (outputTxt == null && fDevice != "undefined") { 
            outputTxt = "Device named " + fDevice + " doesn't have a temperature sensor" 
            return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
        }
        else {
            if(fIndoor){
                def sensors = fIndoor.size()
                def tempAVG = fIndoor ? getAverage(fIndoor, "temperature") : "undefined device"          
                def currentTemp = tempAVG
                outputTxt = "The indoor temperature is " + currentTemp
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
            else {
                if(state.pShort != true) {
                    outputTxt = "Sorry, I couldn't quite get that, what device would you like to use to get the indoor temperature?"
                }
                else {outputTxt = "I'm sorry, it seems that you have not selected any devices with the temperature attribute"}
                return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
        }
    } 
    //      }
    //>>> Temp >>>>>
    if (tts.contains("temperature inside") || tts.contains("indoor temperature") || tts.contains("temperature is inside")){
        if(fIndoor){
            def sensors = fIndoor.size()
            def tempAVG = fIndoor ? getAverage(fIndoor, "temperature") : "undefined device"          
            def currentTemp = tempAVG
            outputTxt = "The indoor temperature is " + currentTemp
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }
        else {
            outputTxt = "There are no indoor sensors selected, please go to the SmartThings app and select one or more sensors"
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }                            
    }
    //>>> Temp >>>>
    if (tts.contains("temperature outside") || tts.contains("outdoor temperature") || tts.contains("temperature is outside") || tts.contains("hot outside") || tts.contains("cold outside")){
        if(fOutDoor){
            def sensors = fOutDoor.size()
            def tempAVG = fOutDoor ? getAverage(cOutDoor, "temperature") : "undefined device"          
            def currentTemp = tempAVG
            def forecastT = mGetWeatherTemps()
            outputTxt = forecastT + ",. The current temperature is " + currentTemp
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }
        else {
            outputTxt = "There are no outdoor sensors selected, go to the SmartThings app and select one or more sensors"
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }                            
    }
    //>>> Weather >>>>
    if (tts.contains("weather") || tts.contains("forecast")){
        //Full forecast
        if (tts.contains("forecast") || tts.contains("weather") || tts.contains("weather forecast") || tt.contains("outside") || tts.contains("current forecast") || tts.contains("current weather") ){
            outputTxt = mGetWeather()
        }
        if (tts.contains("today") || tts.contains("tonight") || tts.contains("tomorrow")) {
            def period = tts.contains("today") ? "today" : tts.contains("tonight") ? "tonight" : tts.contains("tomorrow") ? "tomorrow" : null
            outputTxt = mGetWeatherShort(period)       
        }
        if (tts.contains("update") ||tts.contains("change") || tts.contains("change")){
            outputTxt = mGetWeatherUpdates()
        }
        if (tts.contains("alert") || tts.contains("warning")){
            outputTxt = mGetWeatherAlerts()
        }
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
    }
    if (tts.contains("wind ") || tts.contains("windy") || tts.contains("rain") || tts.contains("precipitation") || tts.contains("UV ") || tts.contains("condition")){
        def wElement = tts.contains("wind") ? "wind" : tts.contains("rain") ? "rain" : tts.contains("precipitation") ? "precip" : tts.contains("UV ") ? "uv" : tts.contains("weather conditions") ? "cond" : null
        outputTxt = mGetWeatherElements(wElement)
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
    }
    if (tts.contains("humidity") || tts.contains("outside humidity") || tts.contains("humid is outside") || tts.contains("outside humidity") || tts.contains("current conditions")){
        def wElement = tts.contains("humid") ? "humid" : tts.contains("current ") ? "cond" : null
        outputTxt = mGetWeatherElements(wElement)
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }
    else {
        profileEvaluate(params)
    }
}

/******************************************************************************************************
   SPEECH AND TEXT PROCESSING INTERNAL - CONTROL & MESSAGING
******************************************************************************************************/
def profileEvaluate(params) {
	log.info "profile control & messaging Evaluate has been activated"
	def tts = params.ptts.toLowerCase()
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
    /*
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
    // Hue Scenes / Colored Lights   
    def hueSet = tts.startsWith("set the color") ? true : tts.startsWith("set color") ? true : tts.startsWith("set lights color") ? true : tts.startsWith("set the lights to color") ? true : false
    def hueChange = tts.startsWith("change the color") ? true : tts.startsWith("change the lights") ? true : tts.startsWith("change color") ? true : tts.startsWith("change lights to ") ? true : false
    def feelLucky = tts.startsWith("I feel lucky") ? true : tts.startsWith("I am feeling lucky") ? true : tts.startsWith("I'm feeling lucky") ? true : tts.contains("feeling lucky") ? true : tts.startsWith("pick a random color") ? true : false
    def read = tts.contains("reading") ? true : tts.contains("studying") ? true : false 
    def concentrate = tts.contains("cleaning") ? true : tts.contains("working") ? true : tts.contains("concentrate") ? true : tts.contains("concentrating") ? true : false
    def relax = tts.contains("relax") ? true : tts.contains("relaxing") ? true : tts.contains("chilling") ? true : false    
    //Voice Activation Settings
    def muteAll = tts.contains("disable sound") ? "mute" : tts.contains("disable audio") ? "mute" : tts.contains("mute audio") ? "mute" : tts.contains("silence audio") ? "mute" : null
    	muteAll = tts.contains("activate sound") ? "unmute" : tts.contains("enable audio") ? "unmute" : tts.contains("unmute audio") ? "unmute" : muteAll
    def muteAlexa = tts.contains("disable Alexa") ? "mute" : tts.contains("silence Alexa") ? "mute" : tts.contains("mute Alexa") ? "mute" : null
    	muteAlexa = tts.contains("enable Alexa") ? "unmute" : tts.contains("start Alexa") ? "unmute" : tts.contains("unmute Alexa") ? "unmute" : muteAll
    */
    if (parent.debug) log.debug "Message received from Parent with: (tts) = '${tts}', (intent) = '${intent}', (childName) = '${childName}', current app version: ${release()}"  
	
	
    if (command == "undefined") {
		outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = true
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	} 
    
    //Sending event to WebCoRE
	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")    
    if (parent.debug) log.debug "sendNotificationEvent sent to CoRE from ${app.label}"
     
    if (pSendSettings() == "complete" || pGroupSettings() == "complete" || pDevicesSettings() == "complete"){
        if (intent == childName) {
            outputTxt = runCommand(tts)
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]  

            if (true) { /* COMMENTED OUT FOR NOW AND PUT INTO AN IF STATMENT SO I CAN COLLAPSE THE CODE
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
} else {
def numMessages = state.recording2 != null ? "3 messages" : state.recording1 != null ? "2 messages" : state.recording != null ? "one message" : "no messages" 
if (numMessages == "3 messages") outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " , " + state.recording2 + " To delete your messages, just say: delete messages"
else if (numMessages == "2 messages") outputTxt = "You have " + numMessages + " pending, " + state.recording + " , " + state.recording1 + " To delete your messages, just say: delete messages"
else if (numMessages == "one message") outputTxt = "You have " + numMessages + " pending, " + state.recording + " To delete your message, just say: delete messages"
else if (numMessages == "no messages") outputTxt = "You have " + numMessages + " pending "
//"Your last recording was, " + state.recording
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
} else {
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
outputTxt = "Running '${app.label}'"
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
//
//FREE TEXT CONTROL ENGINE 
//
//Feedback for devices
if (gHues?.size()>0) {
if (feedback == true) {
}
}    
//Colored Lights
if (gHues?.size()>0) {
//HUE SCENES
if (read == true || concentrate == true || relax == true || feelLucky == true){
def color = read == true ? "Warm White" : concentrate == true ? "Daylight White" : relax == true ? "Very Warm White" : feelLucky == true ? "random" : "undefined"
if (color != "undefined" && command != "colorloopOn" && command != "colorloopOff" ){
if (color != "random"){
def hueSetVals = getColorName("${color}",level)
gHues?.setColor(hueSetVals)
outputTxt =  "Ok, changing your bulbs to " + color 
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
else  {
setRandomColorName()
outputTxt =  "Ok, changing your bulbs to random colors"
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
}
// CHANGING COLORS
if(hueSet == true || hueChange == true) {
def hueSetVals
tts = tts.replace("set the color to ", "").replace("set lights color to ", "").replace("set the lights to color ", "").replace("set color to ", "")
tts = tts.replace("change the color to ", "").replace("change the lights to ", "").replace("change color to ", "").replace("change lights to ", "")
tts = tts == "day light" ? "Daylight" : tts == "be light" ? "Daylight" : tts
hueSetVals =  getColorName( tts , level)
if (hueSetVals) {
gHues?.setColor(hueSetVals)
outputTxt =  "Ok, changing your bulbs to " + tts
}
else {
outputTxt =  "Sorry, I wasn't able to change the color to " +  tts
pTryAgain = true
}
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain": pTryAgain, "pPIN":pPIN]
}	
if (command == "colorloopOn" || command == "colorloopOff") {
//def loopOn = command == "colorloopOn" ? true : command == "colorloopOff" ? false : null
if(command == "colorloopOn"){ //loopOn == true
outputTxt = profileLoop(app.label)
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
else { 
outputTxt =  profileLoopCancel(app.label)
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
}
if (command != null && deviceType != null && command != "undefined" ) {
//LIGHT SWITCHES && CUSTOM GROUPS
if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
dataSet =  ["command": command, "deviceType": deviceType]
outputTxt = advCtrlHandler(dataSet)
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                                  
}
//DISABLE SWITCHES
if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
dataSet =  ["command": command, "deviceType": deviceType]
outputTxt = advCtrlHandler(dataSet)
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]                                  
}
//DISABLE SWITCHES
if (deviceType == "disable") {
if (gDisable?.size()>0) {
if (command == "on" || command == "off") {
if (reverseDisable == true) { command = command == "on" ? "off" : command == "off" ? "on" : command } // added 2/19/17 per Jason's request 
gDisable?."${command}"()
if (command == "on") {
outputTxt = "Ok, turning " + childName + " automation off" 
}
if (command == "off") {
outputTxt = "Ok, turning " + childName + " automation on "
}    
else if (reverseDisable == false) { command = command == "on" ? "on" : command == "off" ? "off" : command  
outputTxt = "Ok, turning " + childName + " automation " + command
}
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
}           
//FANS CONTROL
if (deviceType == "fan"){
if (gFans?.size()>0) {
if (command == "on" || command == "off") {
gFans?."${command}"()
outputTxt = "Ok, turning the fan " + command
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
else if (command == "decrease" || command == "increase" || command == "high" || command == "medium" || command == "low"){
dataSet =  ["command": command, "deviceType": deviceType]
outputTxt = advCtrlHandler(dataSet)
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}     
} 
//PET NOTES CONTROL
if (deviceType == "petNotification") {
if (tts.startsWith("she") || tts.startsWith("he")) {
if (command == "shot" || command == "medicated" || command == "walked" || command == "bathed" || command == "brushed") {
log.info "Pet Notes Control action executed"
def timeDate = new Date().format("hh:mm aa", location.timeZone)
def dateDate = new Date().format("EEEE, MMMM d", location.timeZone)
if (tts.contains("was") || tts.contains("has") || tts.contains("got") || tts.contains("given")) {
if (tts.contains("shot") || tts.contains("brushed") || tts.contains("fed") || tts.contains("bathed") || tts.contains("walked") || tts.contains("medicated")) {
outputTxt = "Ok, recording that ${app.label} was last ${command} on " + dateDate + " at " + timeDate    
if(command == "shot" || command == "shop") {state.petShotNotify = "${app.label} was last shot on " + dateDate + " at " + timeDate }
if(command == "brushed") {state.petBrushNotify = "${app.label} was last brushed on " + dateDate + " at " + timeDate }
if(command == "fed") {state.petFedNotify = "${app.label} was last fed on " + dateDate + " at " + timeDate }
if(command == "bathed") {state.petBathNotify = "${app.label} was last bathed on " + dateDate + " at " + timeDate }
if(command == "walked") {state.petWalkNotify = "${app.label} was last walked on " + dateDate + " at " + timeDate }
if(command == "medicated") {state.petMedNotify = "${app.label} was last medicated on " + dateDate + " at " + timeDate }
if(psendText) { sendtxt(outputTxt) }
if(pPush) { sendPush outputTxt }
}
}
}
}
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
}
if (deviceType == "petNotification") {
if (tts.contains("litter") || tts.contains("litterbox")) {
if (command == "cleaned" || command == "scooped") {
log.info "Pet Notes Litter Box action executed"
def timeDate = new Date().format("hh:mm aa", location.timeZone)
def dateDate = new Date().format("EEEE, MMMM d", location.timeZone)
if (tts.contains("was") || tts.contains("has") || tts.contains("got")) {
outputTxt = "Ok, recording that the litter box was last ${command} on " + dateDate + " at " + timeDate    
if(command == "cleaned") {state.litterBoxCleaned = "The litter box was last cleaned on " + dateDate + " at " + timeDate }
if(command == "scooped") {state.litterBoxScooped = "The litter box was last scooped on " + dateDate + " at " + timeDate }
}
//        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}    
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
}    
//VENTS AND WINDOWS CONTROL
if (deviceType == "vent" || deviceType == "shade") { 
if (command == "open"  || command == "close") {
if (command == "open") {
if(deviceType == "vent"){
gVents.on()
gVents.setLevel(100)
outputTxt = "Ok, opening the vents in the ${app.label}"
}
else {
gShades.open()
outputTxt = "Ok, opening the window coverings in the ${app.label}"
}
}
else {   
if(deviceType == "vent"){
gVents.off()
outputTxt = "Ok, closing the vents in the ${app.label}"
}
else {
gShades.close()
outputTxt = "Ok, closing the window coverings in the ${app.label}"
}
}  
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
} 
if (deviceType == "tv") {
if(sMedia){
if (command == "startActivity"){
if(state.lastActivity != null){
def activityId = null
def activities = sMedia.currentState("activities").value
def activityList = new groovy.json.JsonSlurper().parseText(activities)
activityList.each { it ->  
def activity = it
if(activity.name == state.lastActivity) {
activityId = activity.id
}    	
}
log.warn "starting activity id = ${activityId}, command = ${command}, lastActivity ${state.lastActivity}"
sMedia."${command}"(activityId)
sMedia.refresh()
outputTxt = "Ok, starting " + state.lastActivity + " activity "
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
else { 
outputTxt = "Sorry for the trouble, but in order for EchoSistant to be able to start where you left off, the last activity must be saved"
pTryAgain = true
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
else {
if (command == "activityoff"){
def activityId = null
def currState = sMedia.currentState("currentActivity").value
def activities = sMedia.currentState("activities").value
def activityList = new groovy.json.JsonSlurper().parseText(activities)
if (currState != "--"){
activityList.each { it ->  
def activity = it
if(activity.name == currState) {
activityId = activity.id
}    	
}
state.lastActivity = currState
sMedia."${command}"()
sMedia.refresh()
outputTxt = "Ok, turning off " + currState
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
else {
outputTxt = sMedia.label + " is already off"
pTryAgain = true
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
}
}
}
if (deviceType == "volume") {
if(sSpeaker || sSynth){
def deviceD = sSpeaker? sSpeaker : sSynth? sSynth : "undefined"
if (command == "increase" || command == "decrease" || command == "mute" || command == "unmute"){
def currLevel = deviceD.latestValue("level")
def currState = deviceD.latestValue("switch")
def newLevel = parent.cVolLevel*10  
if (command == "mute" || command == "unmute") {
deviceD."${command}"()
def volText = command == "mute" ? "muting" : command == "unmute" ? "unmuting" : "adjusting" 
outputTxt = "Ok, " + volText + " the " + deviceD.label
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
if (command == "increase") {
newLevel =  currLevel + newLevel
newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
}
if (command == "decrease") {
newLevel =  currLevel - newLevel
newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
}                        
if (newLevel > 0 && currState == "off") {
deviceD.on()
deviceD.setLevel(newLevel)
}
else {                                    
if (newLevel == 0 && currState == "on") {deviceD.off()}
else {deviceD.setLevel(newLevel)}
} 
outputTxt = "Ok, setting  " + deviceD.label + " volume to " + newLevel + " percent"
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
} 
}
}
}
if (parent.debug) {log.debug "end of control engine, command=${command}, ${deviceType}"}
if (sonosDevice || synthDevice || recipients || sms) { //added 2/19/17 Bobby  
state.lastMessage = tts
state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
outputTxt = ttsHandler(tts)
pContCmdsR = "profile"
return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
}
}
}*/
            } else { // COMMENTED OUT FOR NOW AND PUT INTO AN IF STATMENT SO I CAN COLLAPSE THE CODE
                //outputTxt = "Sorry, you must first set up your profile before trying to execute it."
                //pTryAgain = true
                //return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
        }
    }
 }   
def searchRealDevices(String text) {
			////															Added for groups device search...
    return fSwitches.find({text.contains(it.toString().toLowerCase())}) ?: gSwitches.find({text.contains(it.toString().toLowerCase())}) ?: fFans.find({text.contains(it.toString().toLowerCase())}) ?: fLocks.find({text.contains(it.toString().toLowerCase())}) ?: fDoors.find({text.contains(it.toString().toLowerCase())}) ?: null
}
String getFeedBackWords() 	{"give,for,tell,what,how,is,when,which,are,how many,check,who"}
String getCommandEnable() 	{"on,start,enable,engage,open,begin,unlock,unlocked"}
String getCommandDisable() 	{"off,stop,cancel,disable,disengage,kill,close,silence,lock,locked,quit,end"}
String getCommandMore()		{"increase,more,too dark,not bright enough,brighten,brighter,turn up"}
String getCommandLess()		{"darker,too bright,dim,dimmer,decrease,lower,low,softer,less"}
String getDeviceType()		{"light,switch,fan,lock,door,window,blind,shade,curtain"}
String getDelayCommand()	{"delay,wait,until,after,around,within,in,about"}

String parseWordReturn(String input, String fromList) {
	return fromList.split(",").find({input.contains(it)})
}
Boolean parseWordFound(String input, String fromList) {
	return fromList.split(",").find({input.contains(it)}) ? true : false
}
String getCommand(text) {
	return parseWordFound(text, commandMore) ? "increase" : parseWordFound(text, commandLess) ? "decrease" : parseWordFound(text, commandEnable) ? "on" : parseWordFound(text, commandDisable) ? "off" : null 
}
String getDeviceType(text) {
	return parseWordReturn(text, deviceType) ?: null
}
String runCommand(tts) { 
	//not sure how to implement status here, need to understand how we can get feedback keywords
    String feedBack = parseWordFound(tts, feedBackWords) ? "status" : ""
	String command = getCommand(tts) 
    String deviceType = getDeviceType(tts)
    //if delay keyword, figure out amount... that'll be FUN!
	//Boolean delayAction = parseWordFound(tts, delayCommand)

    def theDelay = 0
    def theDevices
    def theLevel
    def theStatus

    log.debug "deviceType:${deviceType}"
	if (command) {
        switch (deviceType) {
            case "light": 
            theDevices = fSwitches
            break
            case "fan": 
            theDevices = fFans
            break
            case null:
                theDevices = searchRealDevices(tts)
        }
        
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

/******************************************************************************************************
   ADVANCED CONTROL HANDLER
******************************************************************************************************/
def advCtrlHandler(data) {
    def deviceCommand = data.command
    def deviceType = data.deviceType
    def result
    def feedback = data.feedback
    
	if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
        deviceType = deviceType == "light" && fSwitches ? fSwitches : deviceType == "light" && gSwitches ? gSwitches : deviceType == "light1" && gCustom1 ? gCustom1 : deviceType == "light2" && gCustom2 ? gCustom2 : deviceType == "light3" && gCustom3 ? gCustom3 : deviceType == "light4" && gCustom4 ? gCustom4 : deviceType == "light5" && gCustom5 ? gCustom5 : null
        if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "on" || deviceCommand == "off") { 
            deviceType.each {s ->
                if (deviceCommand == "on" || deviceCommand == "off") {
                    s?."${deviceCommand}"()
                    result = "Ok, turning lights " + deviceCommand
                    if (deviceType == gCustom1) {
                        result = "Ok, turning " + gCustom1N + deviceCommand
                    }
                    if (deviceType == gCustom2) {
                        result = "Ok, turning " + gCustom2N + deviceCommand
                    }
                    if (deviceType == gCustom3) {
                        result = "Ok, turning " + gCustom3N + deviceCommand
                    }
                    if (deviceType == gCustom4) {
                        result = "Ok, turning " + gCustom4N + deviceCommand
                    }
                    if (deviceType == gCustom5) {
                        result = "Ok, turning " + gCustom5N + deviceCommand
                    }
                }

                else {
                    def	currLevel = s?.latestValue("level")
                    def currState = s?.latestValue("switch") 
                    if (currLevel) {
                        def newLevel = 3*10     
                        if (deviceCommand == "increase") {
                            if (currLevel == null){
                                s?.on()
                                result = "Ok, turning " + app.label + " lights on"   
                            }
                            else {
                                newLevel =  currLevel + newLevel
                                newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                            }
                        }
                        if (deviceCommand == "decrease") {
                            if (currLevel == null) {
                                s?.off()
                                result = "Ok, turning " + app.label + " lights off"                   
                            }
                            else {
                                newLevel =  currLevel - newLevel
                                newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                            }
                        }            
                        if (newLevel > 0 && currState == "off") {
                            s?.on()
                            s?.setLevel(newLevel)
                        }
                        else {                                    
                            if (newLevel == 0 && currState == "on") {
                                s?.off()
                            }
                            else {
                                s?.setLevel(newLevel)
                            }
                        } 
                    }
                    else if  (deviceCommand == "increase" && currState == "off") {s?.on()}
                    //else if (deviceCommand == "decrease" && currState == "on") {s?.off()} removed as annoying when used in conjunction with dimmable bulbs on ON/OFF switches Bobby 3/14/2017
                    result = "Ok, adjusting the lights in the  " + app.label 
                } 
            }
            return result
        }
    }
    
    if (deviceType == "fan"){
		def cHigh = 99
		def cMedium = 66
        def cLow = 33
        def cFanLevel = 33
        def newLevel
			gFans?.each {deviceD -> 
                def currLevel = deviceD.latestValue("level")
                def currState = deviceD.latestValue("switch")
                	newLevel = cFanLevel     
                if (deviceCommand == "increase") {
                    newLevel =  currLevel + newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }
				else if (deviceCommand == "decrease") {
					newLevel =  currLevel - newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel      
             	}
                else if (deviceCommand == "high") {newLevel = cHigh}
                else if (deviceCommand == "medium") {newLevel = cMedium}
                else if (deviceCommand == "low") {newLevel = cLow}
                deviceD.setLevel(newLevel)
            }
            result = "Ok, adjusting the fans in the  " + app.label 
            return result
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
}      
/***********************************************************************************************************************
	TOGGLE VP STATUS WHEN PROFILE EXECUTES
***********************************************************************************************************************/
private pVirToggle() {
    def vp = getChildDevice("${app.label}")
    if(vp) {
        if (vp?.currentValue('presence').contains('not')) {
            message = "${app.label} has arrived"
            vp.arrived()
        }
        else if (vp?.currentValue('presence').contains('present')) {
            message = "${app.label} has departed"
            vp.departed()
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

/************************************************************************************************************
		Smart Home Monitor Status Change when Profile Executes
************************************************************************************************************/    
def shmStateChange() {
    if (shmState == "stay") {
        sendArmStayCommand()
    }
    if (shmState == "away") {
        sendArmAwayCommand()
    }
    if (shmState == "off") {
        sendDisarmedCommand()
    }
}    
def sendArmAwayCommand() {
    if (shmStateKeypads) {
        shmStateKeypads?.each() { it.acknowledgeArmRequest(3) }
    }
    sendSHMEvent("away")
}
    
def sendDisarmedCommand() {
    if (shmStateKeypads) {
        shmStateKeypads?.each() { it.acknowledgeArmRequest(0) }
    }
    sendSHMEvent("off")
}
    
def sendArmStayCommand() {
    if (shmStateKeypads) {
        shmStateKeypads?.each() { it.acknowledgeArmRequest(1) }
    }
    sendSHMEvent("stay")
}

private sendSHMEvent1(String shmState) {
    def event = [
        name:"alarmSystemStatus",
        value: shmState,
        displayed: true,
        description: "System Status is ${shmState}"
    ]
    sendLocationEvent(event)
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
    //    if (parent.debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
        if (parent.debug) log.debug "Sending sms to selected reipients"
    } 
    else {
        if (push || shmNotification) { 
            sendPushMessage
            if (parent.debug) log.debug "Sending push message to mobile app"
        }
    } 
    if (notify) {
        sendNotificationEvent(message)
        if (parent.debug) log.debug "Sending notification to mobile app"

    }
    if (notifyGdoorOpen) {
        if (message.contains("open")) {
            sendTextGarage(message)
        }
    }
    if (notifyGdoorClose) {
        if (message.contains("close")) {
            sendTextGarage(message)
        }
    }
    if (notifyVPArrive) {
        if (message.contains("check") && message.contains("in")) {
            sendTextvp(message)
            if (vpNotification) {
                sendPush(message)
            }
        }
    }
    if (notifyVPDepart) {
        if (message.contains("check") && message.contains("out")) {
            sendTextvp(message)
            if (vpNotification) {
                sendPush(message)
            }
        }
    }    
    if (notifySHMArm) {
        if (message.contains("Stay") || message.contains("Away")) {
            sendTextshm(sms, message)
        }
    }
    if (notifySHMDisarm) {
        if (message.contains("Disarm")) {
            sendTextshm(sms, message)
        }
    }    
    if (sms) {
        sendText(sms, message)
        if (parent.debug) log.debug "Processing message for selected phones"
    }
    if (psendText) {
        processpsms(psms, message)
    }
}
private void sendTextvp(message) { 
		if (vpPhone != null) {
    	def vpPhones = vpPhone.split("\\,")
        for (phone in vpPhones) {
        	sendSms(vpPhone, message)
            }
        }
	}    
private void sendTextGarage(message) {
    if (garagePhone != null) {
        def garagePhones = garagePhone.split("\\,")
        for (phone in garagePhones) {
            sendSms(garagePhone, message)
        //    if (parent.debug) log.debug "Sending sms to selected phones for the garage door"
        }
    }
}
private void sendTextshm(number, message) {
    if (shmPhone != null) {
        def shmPhones = shmPhone.split("\\,")
        for (phone in shmPhones) {
            sendSms(shmPhone, message)
        //    if (parent.debug) log.debug "Sending sms to selected phones for SHM"
        }
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
private void processpsms(psms, message) {
    if (psendText) {
        def phones = psms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
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
/******************************************************************************************************
   CUSTOM COMMANDS - FEEDBACK
******************************************************************************************************/
private getFeedbackCommand(text) {
    def String command = (String) null
    def String deviceType = (String) null
    text = text.toLowerCase()
    //PET NOTES
    if (text.contains("was") && text.contains("${app.label}")) {
        log.info "pet notes feedback commands method"
        if (text.contains("shot") || text.contains("brushed") || text.contains("fed") || text.contains("bathed") || text.contains("walked") || text.contains("medicated")) {
            command = text.contains("shot") ? "shot" : text.contains("brushed") ? "brushed" : text.contains("fed") ? "fed" : text.contains("walked") ? "walked" : text.contains("medicated") ? "medicated" : "undefined"
            deviceType = "fbPetNotification"
        }
    }
    if (text.contains("litter box") || text.contains("litterbox")) {
        if (text.contains("scooped") || text.contains("cleaned")) {
            command = text.contains("scooped") ? "scooped" : text.contains("cleaned") ? "cleaned" : "undefined"
            deviceType = "fbPetNotification"
        }
    }    
    //Presence Feedback
    if (text.startsWith("who") || text.contains("people") || text.contains("check on my family")) {
        deviceType = "fbPresence"
        command = "present"
    }

    // Fans
    if(fFans) {
        if (text.contains("fan") && text.contains("on")) {
            if (text.contains("on") || text.contains("start")) {
                command = "on" 
                deviceType = "fbFan"
                if (text.contains("fan") && text.contains("off")) {
                    command = "off" 
                    deviceType = "fbFan"
                }
                else if (text.contains("high") || text.contains("medium") || text.contains("low")) {
                    command = text.contains("high") ? "high" : text.contains("medium") ? "medium" : text.contains("low") ? "low" : "undefined"
                    deviceType = "fbFan"
                }
            }
        }
    }
    // Lights
    if (text.contains("light") && text.contains("on")) {  
        command = "on" 
        deviceType = "fbLightOn"
    }
    if (text.contains("light") && text.contains("off")) {
        command = "off" 
        deviceType = "fbLightOff"
    }
    // Temperature
    if (text.contains("temperature") || text.contains("hot") || text.contains("cold")) {
        command = "temp"
        deviceType = "fTstat"
    }

    // Vents
    if (text.contains("vent")) {  // Changed "vents" to "vent" to fix bug.  Jason 2/21/2017
        if (text.contains("open")) {
            command = "on" 
            deviceType = "fbVentOn"
        }
        if (text.contains("close")) {
            command = "off" 
            deviceType = "fbVentOff"
        }
    }
    // Doors
    if (text.contains("door")) {
        if (text.contains("open")) {
            command = "open" 
            deviceType = "fbDoorOpen"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "fbDoorOff"
        }
    }    

    // Locks
    if (text.contains("lock")) {
        if (text.contains("unlocked")) {
            command = "unlocked" 
            deviceType = "fbLocks"
        }
        if (text.contains("locked")) {
            command = "locked" 
            deviceType = "fbLocks"
        }
    }    
    // Garage Doors
    if (text.contains("garage") && text.contains("door")) {  
        if (text.contains("open") || text.contains("check")) {
            command = "open" 
            deviceType = "fbGarageOpen"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "fbGarageClose"
        }
    }

    // Windows
    if (text.contains("window")) {  
        if (text.contains("open")) {
            command = "open" 
            deviceType = "fbWindowOpen"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "fbWindowClose"
        }
    }
    // Shades
    if (text.contains("shade") || text.contains("blinds") || text.contains("curtains") ) {  
        if (text.contains("open")) {
            command = "open" 
            deviceType = "fbShadeOpen"
        }
        else if (text.contains("close")) {
            command = "closed"
            deviceType = "fbShadeClose"
        }    
    }

    //Harmony
    if (text.contains("tv")) {
        if  (text.contains("start") || text.startsWith("turn on") || text.contains("switch to") || text.contains("on")){
            command = "startActivity"
            deviceType = "tv"
        }
        else if  (text.contains("stop") || text.startsWith("turn off") || text.contains("switch off") || text.contains("off")){
            command = "activityoff"
            deviceType = "tv"
        }
        else { 
            command = "undefined"
            deviceType = "tv"
        }
    }
    return ["deviceType":deviceType, "command":command ]
}        
/******************************************************************************************************
   CUSTOM COMMANDS - CONTROL
*******************************************************************************************************/
private OLDgetCommand(text){
    def String command = (String) null
    def String deviceType = (String) null
    text = text.toLowerCase()
    //PET NOTES
    if (text.startsWith("she") || text.startsWith("he")) {
        if (text.contains("was") || text.contains("has been") || text.contains("gave")) {
            if (text.contains("shot") || text.contains("brushed") || text.contains("fed") || text.contains("bathed") || text.contains("walked") || text.contains("medicated")) {
                command = text.contains("shot") ? "shot" : text.contains("brushed") ? "brushed" : text.contains("fed") ? "fed" : text.contains("walked") ? "walked" : text.contains("medicated") ? "medicated" : "undefined"
                deviceType = "petNotification"
            }
        }
    }
    if (text.contains("litter box") || text.contains("litterbox")) {
        if (text.contains("scooped") || text.contains("cleaned")) {
            command = text.contains("scooped") ? "scooped" : text.contains("cleaned") ? "cleaned" : "undefined"
            deviceType = "petNotification"
        }
    }    

    //LIGHT SWITCHES        
    if (gSwitches || gCustom1N || gCustom2N || gCustom3N || gCustom4N || gCustom5N){
        if (gSwitches) {
            command = text.contains(" on") ? "on" : text.contains(" off") ? "off" : "undefined"
            if (command == "undefined") {
                command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
            }
            if (command == "undefined") {
                command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            }
            //      log.warn "command = $command"
            deviceType = "light"
        }
        if (gCustom1N) {
            if (text.contains(settings.gCustom1N.toLowerCase())) {
                command = text.contains("on") ? "on" : text.contains("off") ? "off" : null
                if (command == "undefined") {
                    command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
                }
                if (command == "undefined") {
                    command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
                }
                deviceType = "light1"
            }
        }
        if (gCustom2N) {
            if (text.contains(settings.gCustom2N.toLowerCase())) {
                command = text.contains("on") ? "on" : text.contains("off") ? "off" : null
                if (command == "undefined") {
                    command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
                }
                if (command == "undefined") {
                    command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
                }
                deviceType = "light2"
            }
        }
        if (gCustom3N) {
            if (text.contains(settings.gCustom3N.toLowerCase())) {
                command = text.contains("on") ? "on" : text.contains("off") ? "off" : null
                if (command == "undefined") {
                    command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
                }
                if (command == "undefined") {
                    command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
                }
                deviceType = "light3"
            }
        }
        if (gCustom4N) {
            if (text.contains(settings.gCustom4N.toLowerCase())) {
                command = text.contains("on") ? "on" : text.contains("off") ? "off" : null
                if (command == "undefined") {
                    command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
                }
                if (command == "undefined") {
                    command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
                }
                deviceType = "light4"
            }
        }
        if (gCustom5N) {
            if (text.contains(settings.gCustom5N.toLowerCase())) {
                command = text.contains("on") ? "on" : text.contains("off") ? "off" : null
                if (command == "undefined") {
                    command = text.contains("darker") ? "decrease" : text.contains("too bright")  ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : "undefined"
                }
                if (command == "undefined") {
                    command = text.contains("not bright enough") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("too dark") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
                }
                deviceType = "light5"
            }
        }        
    }
    //Virtual Presence Check In/Out
    if (text.contains ("check") || text.contains ("checking")) {
        //def deviceId = "${app.label}" THIS VARIABLE IS NOT USED - Bobby 3/14/2017
        deviceType = "virPres"
        command = "checking" //text.contains(" checking") ? "checking" : "undefined"
    }
    //Run Profile
    if (text.startsWith ("run profile") || text.startsWith ("execute profile") || text.startsWith("run actions") || text.startsWith ("execute actions")){
        command = "run"
        deviceType = "profile"
    }
    //Run Profile with Delay
    if (text.startsWith ("run profile with delay") || text.startsWith("execute profile with delay") || text.startsWith("run actions with delay") || text.startsWith ("execute actions with delay") || text.startsWith ("delay actions")) {
        command = "delay"
        deviceType = "profile"
    }
    //Color Loop
    if(text.contains(" loop") || text.contains(" looping") || text.contains(" color l") || text.contains(" colored l")) {
        log.warn "color loop is true"
        deviceType = "color"
        if (text.startsWith ("start") || text.startsWith("play") || text.startsWith ("run")) {
            command = "colorloopOn"
        }
        else if (text.startsWith ("stop") || text.startsWith("cancel")){
            command = "colorloopOff" 
        }
        else if  (text.contains("slow down") || text.contains("too fast" )) {
            command = "decrease"
        }
        else if  (text.contains("speed up") || text.contains("too slow")) {
            command = "increase"
        }
        log.warn "deviceType = ${deviceType}, command = ${command}"
    }
    //Disable Switches
    //if (gDisable){
    if (text.startsWith("cut off") || text.startsWith("disengage") || text.startsWith("disable automation") || text.startsWith("stop turning the") || text.startsWith("stop the motion sensor") || text.startsWith ("turn the motion sensor off") || text.startsWith("stop the sensor") || text.startsWith("kill the automation") || text.contains("kill the sensor") || text.contains("sensor off")){
        command = "off"
        deviceType = "disable"
    }
    else if (text.startsWith("cut on") || text.startsWith("engage") ||text.contains("enable automation") || text.startsWith("start turning the") || text.startsWith("start the motion sensor") || text.startsWith("turn the motion sensor on") || text.startsWith ("start the sensor")|| text.contains("sensor on")){
        command = "on"
        deviceType = "disable"
    }
    // Fans
    //if(gFans) {
    if (text.contains("fan") || text.contains("fans")) {
        if (text.contains("on") || text.contains("start")) {
            command = "on" 
            deviceType = "fan"
        }
        else if (text.contains("off") || text.contains("stop")) {
            command = "off" 
            deviceType = "fan"
        }
        else if (text.contains("high") || text.contains("medium") || text.contains("low")) {
            command = text.contains("high") ? "high" : text.contains("medium") ? "medium" : text.contains("low") ? "low" : "undefined"
            deviceType = "fan"
        }
        else if  (text.contains("slow down") || text.contains("too fast" )) {
            command = "decrease"
            deviceType = "fan" 
        }
        else if  (text.contains("speed up") || text.contains("too slow")) {
            command = "increase"
            deviceType = "fan" 
        }
        else {
            command = "undefined"
            deviceType = "fan"
        }      
    }

    // Vents
    if (text.contains("vent")) {  // Changed "vents" to "vent" to fix bug.  Jason 2/21/2017
        if (text.contains("open")) {
            command = "on" 
            deviceType = "vent"
        }
        if (text.contains("close")) {
            command = "off" 
            deviceType = "vent"
        }
        //            else { 
        //                command = "undefined"
        //                deviceType = "vent"
        //            }
    }
    // Doors
    if (text.contains("door")) {
        if (text.contains("open")) {
            command = "open" 
            deviceType = "door"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "door"
        }
        else {
            command = "undefined"
            deviceType = "door"
        }
    }    

    // Locks
    if (text.contains("lock")) {
        if (text.contains("unlocked")) {
            command = "unlocked" 
            deviceType = "locks"
        }
        if (text.contains("locked")) {
            command = "locked" 
            deviceType = "locks"
        }
    }    
    // Windows
    if (text.contains("window")) {  // Added for the feedback module by Jason 5/26/2017
        if (text.contains("open")) {
            command = "open" 
            deviceType = "window"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "window"
        }
        //            else { 
        //                command = "undefined"
        //                deviceType = "window"
        //            }
    }
    // Shades
    if (text.contains("shade") || text.contains("blinds") || text.contains("curtains") ) {  // Changed "vents" to "vent" to fix bug.  Jason 2/21/2017
        if (text.contains("open")) {
            command = "open" 
            deviceType = "shade"
        }
        else if (text.contains("closed")) {
            command = "closed"
            deviceType = "shade"
        }    
        else if (text.contains("close")) {
            command = "close" 
            deviceType = "shade"
        }
        else { 
            command = "undefined"
            deviceType = "shade"
        }
    } 
    //Volume
    if  (text.contains("mute") || text.contains("be quiet") || text.contains("pause speaker")){
        command = "mute"
        deviceType = "volume"
    }
    else if (text.contains("unmute") || text.contains("resume") || text.contains("play")) {
        command = "unmute"
        deviceType = "volume" 
    }
    else if  (text.contains("too loud") || text.startsWith("turn down")) {
        command = "decrease"
        deviceType = "volume" 
    }
    else if (text.contains("not loud enough") || text.contains("too quiet") || text.startsWith("turn up")) {
        command = "increase"
        deviceType = "volume"
    }
    else if  (text.contains("volume")) {
        command = "undefined"
        deviceType = "volume"
    }

    //Harmony
    if (text.contains("tv")) {
        if  (text.contains("start") || text.startsWith("turn on") || text.contains("switch to") || text.contains("on")){
            command = "startActivity"
            deviceType = "tv"
        }
        else if  (text.contains("stop") || text.startsWith("turn off") || text.contains("switch off") || text.contains("off")){
            command = "activityoff"
            deviceType = "tv"
        }
        else { 
            command = "undefined"
            deviceType = "tv"
        }
    }
    return ["deviceType":deviceType, "command":command ]
}
/************************************************************************************************************
	CONTROL SUPPORT - CUSTOM CONTROL COMMANDS
************************************************************************************************************/ 
private getCustomCmd(command, unit, group, num) {
    def result
    if (command == "repeat") {
		result = getLastMessageMain()
		return result
    }
	if (command == "change" || command == "changed" || command == "replace" || command == "replaced") {
		if (unit=="filters") {
        result = scheduleHandler(unit)
      	}
		return result
    }
	if (command == "cancel" || command == "stop" || command == "disable" || command == "deactivate" || command == "unschedule" || command == "disarm") {
    	if(group == "security"){
        	def param = [:]
        		param.command = command
        		param.num = num
				param.pintentName = group
        		result = controlSecurity(param)
                log.warn "security result = ${result}"
                return result.outputTxt
        }
        if (unit == "reminder" || unit == "reminders" || unit == "timer" || unit == "timers" || unit.contains ("reminder") || unit.contains ("timer") || unit.contains ("schedule") ) {
        	if (unit.contains ("reminder") || unit.contains ("schedule")) {
            	if (state.scheduledHandler != null) {
                	if (state.scheduledHandler == "filters") {
                    	unschedule("filtersHandler")
                        state.scheduledHandler = null
		                result = "Ok, canceling reminder to replace HVAC filters"
                    }
                    else {
                    state.pTryAgain = true
                    result = "Sorry, I couldn't find any scheduled reminders"// for " + state.scheduledHandler
                    }
                    return result
            	}
				else {
                	state.pTryAgain = true
					result = "Sorry, I couldn't find any scheduled reminders"// for " + state.scheduledHandler
				}
				return result
            }
            else {
                if (unit.contains ("timer") || unit.contains ("delay")) {
                    unschedule("controlHandler")
                    unschedule("securityHandler")
                    result = "Ok, canceling timer"
                    return result
                }
            }
        }
		if (unit == "conversation" || unit.contains ("conversation")) {
			state.pContCmds = false
            result = "Ok, disabling conversational features. To activate just say, start the conversation"
			return result
        }
		if (unit == "pin number" || unit == "pin") {
			if (state.usePIN_T == true || state.usePIN_D == true || state.usePIN_L == true || state.usePIN_S == true || state.usePIN_SHM == true || state.usePIN_Mode == true) {
			state.lastAction = "disable" + group
			command = "validation"
			num = 0
			def secUnit = group
			def process = false
				if (state.usePIN_T == true && group == "thermostats") 		{process = true}
				else if (state.usePIN_L == true && group == "locks") 		{process = true}
                else if (state.usePIN_D == true && group == "doors") 		{process = true}
                else if (state.usePIN_S == true && group == "switches") 	{process = true}                              
                else if (state.usePIN_SHM == true && group == "security") 	{process = true} 
                else if (state.usePIN_Mode == true && group == "modes") 	{process = true} 
				if(process == true) {
                		result = pinHandler(pin, command, num, secUnit)
                		return result
                    }
                    else {
                    	result = "The pin number for " + group + " is not active"
                        return result
                    }
            }
            else{
            	result = "The pin number for " + group + " is not enabled"
				return result
			}         
		}
        if (unit == "feedback") {
        	state.pMuteAlexa = true
            result = "Ok, disabling Alexa feedback. To activate just say, activate the feedback"
            return result
		}
		if (unit == "short answer" || unit == "short answers") {
        	state.pShort = false
            result = "Ok, disabling short answers. To activate just say, enable the short answers"
            return result
		}        
        if (unit == "undefined" && group == "undefined" ) {
        	state.pContCmdsR = "clear" 
            result = "Ok, I am here when you need me "
            return result
		}        
    }
	if (command == "start" || command == "enable" || command == "activate" || command == "schedule" || command == "arm") {
		if(group == "security"){
        	def param = [:]
        		param.command = command
        		param.num = num
				param.pintentName = group
        		result = controlSecurity(param)
                log.warn "security arm result = ${result}"
                return result.outputTxt
        }
        if (unit == "reminder" || unit == "reminders" || unit == "timer" || unit == "timers" || unit.contains ("reminder") || unit.contains ("timer") ) {
        	state.scheduledHandler = "reminder"
            result = "Ok, reminder scheduled"
           	return result
    	}
		if (unit == "conversation" || unit.contains ("conversation")) {
           state.pContCmds = true        
           result = "Ok, activating conversational features. To disable just say, stop the conversation"
            return result
        }
        if (unit == "feedback") {
        	state.pMuteAlexa = false
            result = "Ok, activating Alexa feedback. To disable just say, stop the feedback"
            return result
		}
		if (unit == "short answer" || unit == "short answers") {
        	state.pShort = true
            result = "Ok, short answers on"
            return result
		}
        if (unit == "pin number" || unit == "pin") {		
			if (group == "thermostats" || group == "locks" || group == "doors" || group == "switches" || group == "security" ) {
				if (group == "thermostats") {state.usePIN_T 	= true}
                else if (group == "locks") 		{state.usePIN_L 	= true}
                else if (group == "doors") 		{state.usePIN_D 	= true}
                else if (group == "switches") 	{state.usePIN_S 	= true}                              
                else if (group == "security") 	{state.usePIN_SHM 	= true} 
                else if (group == "modes") 		{state.usePIN_Mode 	= true} 
                	state.pTryAgain = false
                    result = "Ok, the pin has been activated for " + group + ".  To disable, just say disable the PIN number for " + group
            		return result
            	}
           		else {
                	result = "Sorry, the pin number cannot be enabled for " + group
            		return result
            	}
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
   Keypads and Locks actions handler 
************************************************************************************************************/       
def alarmStatusHandler(event) {
//  log.debug "Keypad manager caught alarm status change: "+event.value
  	if(keypadstatus)
  	{
  	if (event.value == "off"){
      	sLocksSHM?.each() { it.setDisarmed() }
  		}
  		else if (event.value == "away"){
      	sLocksSHM?.each() { it.setArmedAway() }
  		}
  		else if (event.value == "stay") {
      	sLocksSHM?.each() { it.setArmedStay() }
  		}
  	}
}
private sendSHMEvent(String shmState) {
  	def event = [
        name:"alarmSystemStatus",
        value: shmState,
        displayed: true,
        description: "System Status is ${shmState}"
      	]
      	sendLocationEvent(event)
	}
private execRoutine(armMode) {
  	if (armMode == 'away') {
    	location.helloHome?.execute(settings.armRoutine)
  		} 
        else if (armMode == 'stay') {
    		location.helloHome?.execute(settings.stayRoutine)
  		} 
        else if (armMode == 'off') {
    		location.helloHome?.execute(settings.disarmRoutine)
  		}
	}
def codeEntryHandler(evt) {
  	def codeEntered = evt.value as String
  	def data = evt.data as String
  	def armMode = ''
  	def changedMode = 0
  		if (codeEntered == "${shmCode}") {
  			shmCodeEnteredHandler(evt)
            }
        if (codeEntered == "${doorCode1}" || codeEntered == "${doorCode2}" || codeEntered == "${doorCode3}") {
        	garageCodeEnteredhandler(evt)
            }
        if (codeEntered == "${vpCode}") {
        	virtualPersonControlHandler(evt)
            }
        }
def virtualPersonControlHandler(evt) {
	def vp = getChildDevice("${app.label}")
	def codeEntered = evt.value as String
	def data = evt.data as String
	def armMode = ''
	def changedMode = 0
  	def message = " "
    def stamp = state.lastTime = new Date(now()).format("h:mm aa, dd-MMMM-yyyy", location.timeZone) 
    if(vp != null) {
		if (codeEntered == "${vpCode}" && data == "3") {
    		message = "${vp} checked in to the home using the ${evt.displayName} at ${stamp}"
			vp.arrived()
            if (notifyVPArrive) {
            	sendtxt(message)
                }
                if (vpActions) {
                ttsActions(tts)
                }
            }
		if (codeEntered == "${vpCode}" && data == "0") {
            message = "${vp} checked out of the home using the ${evt.displayName} at ${stamp}"
            vp.departed()
            if (notifyVPDepart) {
            	sendtxt(message)
                }
            }
		}
        log.info "'${message}'"
	}
def garageCodeEnteredhandler(evt) {
	def codeEntered = evt.value as String
	def data = evt.data as String
	def armMode = ''
	def changedMode = 0
  	def message = " "
    def stamp = state.lastTime = new Date(now()).format("h:mm aa, dd-MMMM-yyyy", location.timeZone) 
    if (codeEntered == "${doorCode1}" && data == "0") {
        message = "${app.label} used the ${evt.displayName} to close the ${sDoor1} at ${stamp}"
        sDoor1?.close() 
        log.info "${message}"
        }
        else if (codeEntered == "${doorCode2}" && data == "0") {
        	message = "The ${sDoor2} was closed by ${app.label} using the ${evt.displayName} at ${stamp}"
            sDoor2?.close()
            log.info "${message}"
        	}
            else if (codeEntered == "${doorCode3}" && data == "0") {
            	message = "The ${sDoor3} was closed by ${app.label} using the ${evt.displayName} at ${stamp}"
                sDoor3?.close()
                log.info "${message}"
                }
    else if (codeEntered == "${doorCode1}" && data == "3") {
        message = "The ${sDoor1} was opened by ${app.label} using the ${evt.displayName} at ${stamp}"
    		sDoor1?.open()
            if (gd1Actionss) {
            	ttsActions(tts)
                }
            log.info "${message}"
        	}
        	else if (codeEntered == "${doorCode2}" && data == "3") {
        		message = "The ${sDoor2} was opened by ${app.label} using the ${evt.displayName} at ${stamp}"
            		sDoor2?.open()
                    if (gd2Actions) {
                    	ttsActions(tts) 
                        }
                    log.info "${message}"
        			}
            		else if (codeEntered == "${doorCode3}" && data == "3") {
            			message = "The ${sDoor3} was opened by ${app.label} using the ${evt.displayName} at ${stamp}"
							sDoor3?.open()
                            if (gd3Actions) {
                            	ttsActions(tts)
                                }
                            log.info "${message}"
                			}
                        if (garagePush) {
                        	sendPush(message)
                            }
                		if (notifyGdoorOpen || notifyGdoorClose) {
                			sendtxt(message)
                    		}
                        }                    
def shmCodeEnteredHandler(evt) {
	def codeEntered = evt.value as String
	def data = evt.data as String
	def armMode = ''
	def currentarmMode = sLocksSHM.currentValue("armMode")
	def changedMode = 0
    def stamp = state.lastTime = new Date(now()).format("h:mm aa, dd-MMMM-yyyy", location.timeZone )
	if (codeEntered != "${doorCode1}" && codeEntered != "${doorCode2}" && codeEntered != "${doorCode3}" ) {
  		if (data == '0') {
  			armMode = 'Disarmed'
  			}
  		else if (data == '3') {
    		armMode = 'Armed Away'
  			}
  		else if (data == '1') {
    		armMode = 'Armed Stay'
  			}
		else 
        {
        log.error "${app.label}: Unexpected arm mode sent by keypad!: " + data
	return []
  	}
  	def message = " "
    def correctCode = settings."shmCode" as String
    if (codeEntered == correctCode) {
		if (data == "0") {
          	runIn(0, "sendDisarmCommand")
            message = "${app.label} used the ${evt.displayName} to set the SHM to ${armMode} at ${stamp}."
            }
        	else if (data == "1") {
        		if(armDelay && keypadstatus) {
        		sLocksSHM?.each() { it.setExitDelay(armDelay) }
        		}
        		runIn(armDelay, "sendStayCommand")
        		message = "${app.label} used the ${evt.displayName} to set the SHM to ${armMode} at ${stamp}."
        		}
        		else if (data == "3") {
				if(armDelay && keypadstatus) {
            	sLocksSHM?.each() { it.setExitDelay(armDelay) }
				}
        		runIn(armDelay, "sendArmCommand")
       			message = "${app.label} used the ${evt.displayName} to set the SHM to ${armMode} at ${stamp}."
        		}
                if (notifySHMArm || notifySHMDisarm) {
                	sendtxt(message)
                    }
			log.debug "${message}"
      	}
    }
}
def sendArmCommand() {
  	log.debug "Sending Arm Command."
  	if (keypadstatus) {
    	sLocksSHM?.each() { it.acknowledgeArmRequest(3) }
  		}
  	sendSHMEvent("Away")
  	execRoutine("Away")
}
def sendDisarmCommand() {
  	log.debug "Sending Disarm Command."
  	if (keypadstatus) {
    	sLocksSHM?.each() { it.acknowledgeArmRequest(0) }
  		}
  	sendSHMEvent("Disarm")
  	execRoutine("Disarm")
}
def sendStayCommand() {
  	log.debug "Sending Stay Command."
  	if (keypadstatus) {
    	sLocksSHM?.each() { it.acknowledgeArmRequest(1) }
  		}
  	sendSHMEvent("Stay")
  	execRoutine("Stay")
}
/************************************************************************************************************
  PET NOTES FEEDBACK HANDLER 
************************************************************************************************************/
def petNotesFeedback(tts, command) {
	log.info "We made it to the petNotesFeedback handler at line 3557"
    log.info "received info: command = ${command} and tts = ${tts}"
    //OTHER 
    def String deviceType = (String) null
    def String outputTxt = (String) null
    def String result = (String) null
	def currState
    def stateDate
    def stateTime
    if (debug){
    def pProcess = true
    state.pTryAgain = false
                if (tts.contains("when") && tts.contains("was")) {
                	if (tts.contains("she") || tts.contains("he")  ) {
                    if (tts.contains("medicated") && state.petShotNotify.contains("last") && state.petMedNotify.contains("last")) {
                        outputTxt = "I have been told that " + state.petMedNotify + " , I have also been told that " + state.petShotNotify
                        }//	return outputTxt}
                    	else if (tts.contains("medicated") && state.petShotNotify.contains("last")) { outputTxt = "I have not been told when ${app.label} was medicated, but " + state.petShotNotify 
                        }//	return outputTxt}
                        else if (tts.contains("medicated")) { outputTxt = state.petMedNotify 
                        }//	return outputTxt}
                  	if (tts.contains("shot") && state.petShotNotify.contains("last") && state.petMedNotify.contains("last")) {
                  			outputTxt = "I have been told that " + state.petShotNotify + " , I have also been told that " + state.petMedNotify
                        }//	return outputTxt}
                    	else if (tts.contains("shot") && state.petMedNotify.contains("last")) { 
                        	outputTxt = "I have not been told when ${app.label} was shot, but " + state.petMedNotify 
                        }//	return outputTxt}
                        else if (tts.contains("shot")) { outputTxt = state.petShotNotify 
                        }//	return outputTxt}
                    else if (tts.contains("fed") && state.petFedNotify != null ) {outputTxt = state.petFedNotify}
                	else if (tts.contains("bathed") && state.petBathNotify != null ) {outputTxt = state.petBathNotify}
                	else if (tts.contains("walked") && state.petWalkNotify != null ) {outputTxt = state.petWalkNotify}
                	else if (tts.contains("brushed") && state.petBrushNotify != null ) {outputTxt = state.petBrushNotify}
                //    return outputTxt
                   }
                   
				}
                return outputTxt
			}
        }      
		
/******************************************************************************
	 FEEDBACK SUPPORT - GET AVERAGE										
******************************************************************************/
def getAverage(device,type){
	def total = 0
		if(debug) log.debug "calculating average temperature"  
    device.each {total += it.latestValue(type)}
    return Math.round(total/device?.size())
}
/******************************************************************************
	 FEEDBACK SUPPORT - ADDITIONAL FEEDBACK	    
******************************************************************************/
def getMoreFeedback(data) {
    def devices = data.devices
    def deviceType = data.deviceType
    def deviceDoors = data.deviceDoors
    def deviceTypeDoors = data.deviceTypeDoors
    def deviceWindows = data.deviceWindows
    def deviceTypeWindows = data.deviceTypeWindows
    def command = data.cmd
    def outputTxt = ""
	if ( deviceType == "fSwitches") {
    	outputTxt = "The following lights are " + command + "," + devices.sort().unique()
        return outputTxt
    }
	if ( deviceType == "fDoors") {    // Added by Jason to ask "are doors open" on 2/27/2017
    	if (devices?.size() == 1) {
    	outputTxt = "The following door, " + devices + " is " + command 
        return outputTxt
        }
        else if (devices?.size() > 1) { 
        outputTxt = "The following doors are " + command + "," + devices.sort().unique()
    	return outputTxt
        }
    }
	if (deviceType == "fWindows") {    // Added by Jason to ask "are windows open" on 2/27/2017
    	if (devices?.size() == 1) {
    	outputTxt = " The following window, " + devices + " is " + command 
    	}
        else { 
        outputTxt = "The following windows are " + command + "," + devices.sort().unique()
    	return outputTxt
        }
    }
	return outputTxt  
    } 
/******************************************************************************
	 FEEDBACK SUPPORT - DEVICE MATCH											
******************************************************************************/
private deviceMatchHandler(fDevice) {
    def pPIN = false
    def String deviceType = (String) null
	def currState
    def stateDate
    def stateTime
	def deviceMatch
    def result
    	state.pTryAgain = false
		if(fTstat){
           deviceMatch = fTstat?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
			if(deviceMatch){
				deviceType = "fTstat"
                currState = deviceMatch.currentState("thermostatOperatingState").value
                stateDate = deviceMatch.currentState("thermostatOperatingState").date
                stateTime = deviceMatch.currentState("thermostatOperatingState").date.time
                def timeText = getTimeVariable(stateTime, deviceType)            
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "thermostatOperatingState" ]
            }
        }
        if (fSwitches){
//		deviceMatch = fSwitches?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
			if(deviceMatch){
				deviceType = "fSwitches" 
				currState = deviceMatch.currentState("switch").value
				stateDate = deviceMatch.currentState("switch").date
				stateTime = deviceMatch.currentState("switch").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"]
        	}
        }
        if (fContact){
//        deviceMatch =fContact?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fContact" 
				currState = deviceMatch.currentState("contact").value
				stateDate = deviceMatch.currentState("contact").date
				stateTime = deviceMatch.currentState("contact").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "contact"]
            }
        }
        if (fMotion){
//        deviceMatch =fMotion?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fMotion" 
                currState = deviceMatch.currentState("motion").value 
                stateDate = deviceMatch.currentState("motion").date
                stateTime = deviceMatch.currentState("motion").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "motion"]
        	}
        } 
        if (fLocks){
//        deviceMatch =fLocks?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fLocks"
                currState = deviceMatch.currentState("lock").value 
                stateDate = deviceMatch.currentState("lock").date
                stateTime = deviceMatch.currentState("lock").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "lock"]
        	}
        }        
        if (fPresence){
  //      deviceMatch =fPresence.find {d -> d.label?.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fPresence"
                currState = deviceMatch.currentState("presence")?.value 
                stateDate = deviceMatch.currentState("presence")?.date
                stateTime = deviceMatch.currentState("presence")?.date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "presence"]
        	}
        }  
        if (fGarage){
 //       deviceMatch =fGarage.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fGarage"
                currState = deviceMatch.currentState("contact").value 
                stateDate = deviceMatch.currentState("contact").date
                stateTime = deviceMatch.currentState("contact").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "door"]
        	}
        }  
        if (fVent){
//		deviceMatch =fVent?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fVent"
                currState = deviceMatch.currentState("switch").value 
                currState = currState == "on" ? "open" : currState == "off" ? "closed" : "unknown"
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "switch"]
        	}
        }
        if (fWater){
//		deviceMatch =fWater?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fWater"
                currState = deviceMatch.currentState("water").value 
                stateDate = deviceMatch.currentState("water").date
                stateTime = deviceMatch.currentState("water").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "water"]
        	}
        }        
        if (fMedia){
            if (fDevice == "TV") {
                deviceMatch = cMedia.first()
            }
            else {
                deviceMatch =fMedia?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            }   
            if(deviceMatch)	{
                deviceType = "fMedia"
                currState = deviceMatch.currentState("currentActivity").value 
                currState = currState == "--" ? " off " : " running the " + currState + " activity "
                stateDate = deviceMatch.currentState("currentActivity").date
                stateTime = deviceMatch.currentState("currentActivity").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "currentActivity"]
            }
        }        
        if (fFan){
//		deviceMatch =fFan?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fFan"
                currState = deviceMatch.currentState("switch").value 
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"] 
            }
        }         
        if (fRelay){
//		deviceMatch =fRelay?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
				deviceType == "fRelay"
                if (fContactRelay) {
                currState = fContactRelay.currentState("contact").value 
                stateDate = fContactRelay.currentState("contact").date
                stateTime = fContactRelay.currentState("contact").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]

                }
			}
        }
        if (fBattery){
//        deviceMatch = fBattery.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "fBattery"
                currState = fBattery.currentState("battery").value
				stateTime = fBattery.currentState("battery").date.time
                def timeText = getTimeVariable(stateTime, deviceType)  
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "contact"] 
            } 
     	}
	}

/************************************************************************************************************
  SETTINGS FEEDBACK HANDLER
************************************************************************************************************/
def settingsFeedback() {
    //LAMBDA
    def fDevice = params.fDevice
    def fQuery = params.fQuery
    def fOperand = params.fOperand 
    def fCommand = params.fCommand 
    def String deviceType = (String) null
    def String outputTxt = (String) null
    def String result = (String) null
    def String deviceM = (String) null
    def currState
    def stateDate
    def stateTime
    def data = [:]
    fDevice = fDevice?.replaceAll("[^a-zA-Z0-9 ]", "") 
    state.pTryAgain = false                
    def pCmds = state.pContCmds == true ? "enabled" : "disabled"
    def pCmdsR = state.pContCmdsR //last continuation response
    def pMute = state.pMuteAlexa == true ? "Alexa voice is disabled" : "Alexa voice is active"
    //state.scheduledHandler
    def pin_D = state.usePIN_D 			== true ? "active" : "inactive"
    def pin_L = state.usePIN_L 			== true ? "active" : "inactive"
    def pin_T = state.usePIN_T 			== true ? "active" : "inactive"
    def pin_S = state.usePIN_S 			== true ? "active" : "inactive"
    def pin_SHM = state.usePIN_SHM 		== true ? "active" : "inactive"
    def pin_Mode = state.usePIN_Mode 	== true ? "active" : "inactive" 
    def activePin 	= pin_D 	== "active" ? "doors" : null
    activePin  	= pin_L 	== "active" ? activePin + ", locks" : activePin
    activePin  	= pin_S 	== "active" ? activePin + ", switches" : activePin
    activePin  	= pin_T 	== "active" ? activePin + ", thermostats"  : activePin
    activePin  	= pin_SHM 	== "active" ? activePin + ", smart security"  : activePin
    activePin  	= pin_Mode 	== "active" ? activePin + ", modes"  : activePin
    if (activePin == null) { activePin = "no groups"}                
    def inactivePin = pin_D 	== "inactive" ? "doors" : null
    inactivePin  = pin_L 	== "inactive" ? inactivePin + ", locks" : inactivePin
    inactivePin  = pin_S 	== "inactive" ? inactivePin + ", switches" : inactivePin
    inactivePin  = pin_T 	== "inactive" ? inactivePin + ", thermostats" : inactivePin
    inactivePin  = pin_SHM	== "inactive" ? inactivePin + ", smart security" : inactivePin
    inactivePin  = pin_Mode	== "inactive" ? inactivePin + ", location modes" : inactivePin
    if (inactivePin == null) {inactivePin = "no groups"}

    return outputTxt = pMute + " and the conversational module is " + pCmds + ". The pin number is active for: " +  activePin + " and inactive for: " + inactivePin
    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
}

/******************************************************************************
	 FEEDBACK SUPPORT - DEVICE CAPABILITIES											
******************************************************************************/
private getCaps(capDevice,capType, capMainCap, capState) {
    def deviceName = capDevice
    def deviceType = capType
    def deviceCap = capMainCap
    def deviceState = capState
    def result
    def attr = [:]
    state.pContCmdsR = "caps"
    def supportedCaps = deviceName.capabilities
    supportedCaps.each { c ->
        def capName = c.name
        c.attributes.each {a ->
            def attrName = a.name
            def attrValue = deviceName.latestValue(attrName)               
            if (a.name != null && a.name !=checkInterval && a.name !=polling  && a.name !=refresh && attrValue != null ) {
                if (a.name == "temperature") 		{ result = "The " + attrName + " is " + attrValue + " degrees, " }
                if (a.name == "motion") 			{ result = result + attrName + " is " + attrValue +", " }
                if (a.name == "contact") 			{ result = result + attrName + " is " + attrValue +", " }                    
                if (a.name == "humidity") 			{ result = result + attrName + " is " + attrValue + ", " }
                if (a.name == "illuminance") 		{ result = result + "lux level is " + attrValue + ", " }
                if (a.name == "water") 				{ result = result + attrName + " is " + attrValue +", " }                    
                if (a.name == "switch") 			{ result = result + attrName + " is " + attrValue +", " } 
                if (a.name == "presence") 			{ result = result + attrName + " is " + attrValue + ", " }                    
                if (a.name == "heatingSetpoint") 	{ result = result + "Heating Set Point is " + attrValue + " degrees, " }
                if (a.name == "coolingSetpoint") 	{ result = result + "Cooling Set Point is" + attrValue + " degrees, " }
                if (a.name == "thermostatMode") 	{ result = result + "The thermostat Mode is " + attrValue + ", " }
                if (a.name == "thermostatFanMode") 	{ result = result + "The Fan Mode is " + attrValue + ", " }
                if (a.name == "battery") 			{ result = result + attrName + " level is " + attrValue + " percent, " }
                attr << ["${attrName}": attrValue]
            }
        }
    }
    result = result.replace("null", "")
    state.lastAction = result
    state.pContCmdsR = "caps"
    result = attr?.size()
    return result
}
/******************************************************************************
	 FEEDBACK SUPPORT - CAPABILITIES GROUP											
****************************************************************************/
private getCapabilities(cap) {
    def DeviceDetails = [] 
    def batDetails = [] 
    def result = [:] 
    	state.pTryAgain = false	
//try {
//batteries
	if (cap == "bat") {
        fMotion?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName         
             }     
         }
        fContact?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName         
             }
         }
        if (!phones) {
        fPresence?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName         
             	}
             }
         }
        fWater?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName         
             }
         }
        fVent?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName        
             }
         }
        fLocks.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName         
             }
         }    
        fBattery?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < fLowBattery) {
        		batDetails << d.displayName     
             }
         }
        def dUniqueList = batDetails.unique (false)
        dUniqueList = dUniqueList.sort()       
        def listSize = dUniqueList?.size()
        def listBat = dUniqueList
        result = [listSize: listSize, listBat: listBat]
        return result //dUniqueListString
	}
	 
    /*catch (Throwable t) {
        log.error t
        result = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return result
	}
}	*/

//activity	
//try{
    if (cap == "act" && fMotion != null)  {    
        fMotion.each 	{ d ->
        	def stateTime = d.currentState("motion").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fContact?.each 	{ d ->
        def attrValue = d.latestValue("contact") 
        	def stateTime = d.currentState("contact").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fWindows?.each 	{ d ->
        def attrValue = d.latestValue("contact") 
        	def stateTime = d.currentState("contact").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fDoors?.each 	{ d ->
        def attrValue = d.latestValue("door") 
        	def stateTime = d.currentState("door").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fDoor1?.each 	{ d ->
        def attrValue = d.latestValue("contact") 
        	def stateTime = d.currentState("contact").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fPresence?.each 	{ d ->
        def attrValue = d.latestValue("presence") 
        	def stateTime = d.currentState("presence").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fWater?.each 	{ d ->
        def attrValue = d.latestValue("water") 
        	def stateTime = d.currentState("water").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fVent?.each 	{ d ->
        def attrValue = d.latestValue("switch") 
        	def stateTime = d.currentState("switch").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTimeAdj) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        fLock?.each 	{ d ->
        def attrValue = d.latestValue("lock") 
			def stateTime = d.currentState("lock").date.time
            def endTime = now() + location.timeZone.rawOffset
            def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
            int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
            //int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        log.warn "locks devices = $DeviceDetails"
        fSwitch?.each 	{ d ->
        	def attrValue = d.latestValue("switch") 
            if (d?.currentState("switch") != null) {
            def stateTime = d?.currentState("switch").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > fInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
           	}
        } 
        log.warn "switch devices = $DeviceDetails"
        def dUniqueList = DeviceDetails.unique (false)
        dUniqueList = dUniqueList.sort()       
        def listSize = dUniqueList?.size()
        def listDev = dUniqueList
        result = [listSize: listSize, listDev: listDev]
        return result //dUniqueListString
	}
		}
        /*catch(Exception ex) {
         log.error "exception: $ex"
		 result = "Looks like you might have an improper built device type that is missing a standard filed."
      	}
		catch (Throwable t) {
        log.error t
        result = "Looks like you might have an improper built device type that is missing a standard filed."
        state.pTryAgain = true
        return result
		}
}	*/
/***********************************************************************************************************************
 		WEATHER FORECAST (DASH + FULL)
 ***********************************************************************************************************************/
def private mGetWeather(){
	state.pTryAgain = false
    def result ="Today's weather is not available at the moment, please try again later"
//	try {
    	//daily forecast text
        def weather = getWeatherFeature("forecast", settings.wZipCode)
        def todayWeather = 	weather.forecast.txt_forecast.forecastday[0].fcttext 
        def tonightWeather = weather.forecast.txt_forecast.forecastday[1].fcttext 
		def tomorrowWeather = weather.forecast.txt_forecast.forecastday[2].fcttext 
        //simple forecast 
        def sTodayWeather = weather.forecast.simpleforecast.forecastday[0]
        def sTonightWeather = weather.forecast.simpleforecast.forecastday[1]
		def sTomorrowWeather = weather.forecast.simpleforecast.forecastday[2]
        def sHumidity = sTodayWeather.avehumidity + " for " + sTodayWeather.date.weekday + ", " + sTodayWeather.date.monthname + ", " + sTodayWeather.date.day
		def sHumidityTomorrow = sTonightWeather.avehumidity + " for " + sTonightWeather.date.weekday + ", " + sTonightWeather.date.monthname + ", " + sTonightWeather.date.day
		def sLow = sTodayWeather.low.fahrenheit
        def sHigh = sTodayWeather.high.fahrenheit
        def sRainFall = sTodayWeather.qpf_day.in + " inches"
        def sPrecip = sTodayWeather.pop + "percent"
        //conditions
		def condWeather = getWeatherFeature("conditions", settings.wZipCode)
        def condTodayWeather = 	condWeather.current_observation.weather
		def condTodayhumidity = condWeather.current_observation.relative_humidity
		def condTodayUV = condWeather.current_observation.UV
		def condTodayZip = condWeather.current_observation.display_location.zip
        log.warn "reporting zip code: zip ${condTodayZip}"
        if(wMetric){
			result = "Today's forecast is " + weather.forecast.txt_forecast.forecastday[0].fcttext_metric + " Tonight it will be " + weather.forecast.txt_forecast.forecastday[1].fcttext_metric
        	result = result?.toString()
            result = result?.replaceAll(/([0-9]+)C/,'$1 degrees')
            // clean up wind direction (South)
            result = result?.replaceAll(~/ SSW /, " South-southwest ").replaceAll(~/ SSE /, " South-southeast ").replaceAll(~/ SE /, " Southeast ").replaceAll(~/ SW /, " Southwest ")
            // clean up wind direction (North)
            result = result?.replaceAll(~/ NNW /, " North-northwest ").replaceAll(~/ NNE /, " North-northeast ").replaceAll(~/ NE /, " Northeast ").replaceAll(~/ NW /, " Northwest ")
            // clean up wind direction (West)
            result = result?.replaceAll(~/ WNW /, " West-northwest ").replaceAll(~/ WSW /, " West-southwest ")
            // clean up wind direction (East)
            result = result?.replaceAll(~/ ENE /, " East-northeast ").replaceAll(~/ ESE /, " East-southeast ")
            //result = result + " humidity " + sHumidity /// simple weather example
            result = result?.toLowerCase()
        }
        else {
    		result = "Today's forecast is: " + weather.forecast.txt_forecast.forecastday[0].fcttext   + " Tonight it will be " + weather.forecast.txt_forecast.forecastday[1].fcttext
        	result = result?.toString()
            //clean up wind and temps units
            result = result?.replaceAll(/([0-9]+)F/,'$1 degrees').replaceAll(~/mph/, " miles per hour")
            // clean up wind direction (South)
            result = result?.replaceAll(~/ SSW /, " South-southwest ").replaceAll(~/ SSE /, " South-southeast ").replaceAll(~/ SE /, " Southeast ").replaceAll(~/ SW /, " Southwest ")
            // clean up wind direction (North)
            result = result?.replaceAll(~/ NNW /, " North-northwest ").replaceAll(~/ NNE /, " North-northeast ").replaceAll(~/ NE /, " Northeast ").replaceAll(~/ NW /, " Northwest ")
            // clean up wind direction (West)
            result = result?.replaceAll(~/ WNW /, " West-northwest ").replaceAll(~/ WSW /, " West-southwest ")
            // clean up wind direction (East)
            result = result?.replaceAll(~/ ENE /, " East-northeast ").replaceAll(~/ ESE /, " East-southeast ")
            //result = result + " humidity " + sHumidity /// simple weather example
            result = result?.toLowerCase()
        }
        log.info "returning Today's forecast result"
        return result
	}
  /*  catch (Throwable t) {
		log.error t
        state.pTryAgain = true
        return result
	}
}*/
/***********************************************************************************************************************
    WEATHER FORECAST (SHORT)
***********************************************************************************************************************/
def private mGetWeatherShort(period){
	state.pTryAgain = false
    def result ="The weather service is not available at the moment, please try again later"
//	try {
    	//daily forecast text
        def weather = getWeatherFeature("forecast", settings.wZipCode)
        def todayWeather = 	weather.forecast.txt_forecast.forecastday[0].fcttext 
        def tonightWeather = weather.forecast.txt_forecast.forecastday[1].fcttext 
		def tomorrowWeather = weather.forecast.txt_forecast.forecastday[2].fcttext 
        def forecast = period == "today" ? todayWeather : period == "tonight" ? tonightWeather :  period == "tomorrow" ? tomorrowWeather : null
        
        if(wMetric){
        	def todayWeather_m = 	weather.forecast.txt_forecast.forecastday[0].fcttext_metric 
        	def tonightWeather_m = weather.forecast.txt_forecast.forecastday[1].fcttext_metric 
			def tomorrowWeather_m = weather.forecast.txt_forecast.forecastday[2].fcttext_metric 
        	def forecast_metric_m = period == "today" ? todayWeather_m : period == "tonight" ? tonightWeather_m :  period == "tomorrow" ? tomorrowWeather_m : null

			result = period + "'s forecast is " + tomorrowWeather_m
        	result = result.toString()
            result = result.replaceAll(/([0-9]+)C/,'$1 degrees')
            // clean up wind direction (South)
            result = result.replaceAll(~/ SSW /, " South-southwest ").replaceAll(~/ SSE /, " South-southeast ").replaceAll(~/ SE /, " Southeast ").replaceAll(~/ SW /, " Southwest ")
            // clean up wind direction (North)
            result = result.replaceAll(~/ NNW /, " North-northwest ").replaceAll(~/ NNE /, " North-northeast ").replaceAll(~/ NE /, " Northeast ").replaceAll(~/ NW /, " Northwest ")
            // clean up wind direction (West)
            result = result.replaceAll(~/ WNW /, " West-northwest ").replaceAll(~/ WSW /, " West-southwest ")
            // clean up wind direction (East)
            result = result.replaceAll(~/ ENE /, " East-northeast ").replaceAll(~/ ESE /, " East-southeast ")
            //result = result + " humidity " + sHumidity /// simple weather example
            result = result.toLowerCase()
        }
        else {
    		result = period + "'s forecast is " + forecast
        	result = result.toString()
            //clean up wind and temps units
            result = result.replaceAll(/([0-9]+)F/,'$1 degrees').replaceAll(~/mph/, " miles per hour")
            // clean up wind direction (South)
            result = result.replaceAll(~/ SSW /, " South-southwest ").replaceAll(~/ SSE /, " South-southeast ").replaceAll(~/ SE /, " Southeast ").replaceAll(~/ SW /, " Southwest ")
            // clean up wind direction (North)
            result = result.replaceAll(~/ NNW /, " North-northwest ").replaceAll(~/ NNE /, " North-northeast ").replaceAll(~/ NE /, " Northeast ").replaceAll(~/ NW /, " Northwest ")
            // clean up wind direction (West)
            result = result.replaceAll(~/ WNW /, " West-northwest ").replaceAll(~/ WSW /, " West-southwest ")
            // clean up wind direction (East)
            result = result.replaceAll(~/ ENE /, " East-northeast ").replaceAll(~/ ESE /, " East-southeast ")
            //result = result + " humidity " + sHumidity /// simple weather example
            result = result.toLowerCase()
        }
        log.info "returning Today's forecast result"
        return result
	}
/*    catch (Throwable t) {
		log.error t
        state.pTryAgain = true
        return result
	}
}*/
/***********************************************************************************************************************
    WEATHER ELEMENTS
***********************************************************************************************************************/
def private mGetWeatherElements(element){
	state.pTryAgain = false
    def result ="Current weather is not available at the moment, please try again later"
//   	try {
        //hourly updates
        def cWeather = getWeatherFeature("hourly", settings.wZipCode)
        def cWeatherCondition = cWeather.hourly_forecast[0].condition
        def cWeatherPrecipitation = cWeather.hourly_forecast[0].pop + " percent"
        def cWeatherWind = cWeather.hourly_forecast[0].wspd.english + " miles per hour"
        def cWeatherHum = cWeather.hourly_forecast[0].humidity + " percent"
        def cWeatherUpdate = cWeather.hourly_forecast[0].FCTTIME.civil
        
        def condWeather = getWeatherFeature("conditions", settings.wZipCode)
        def condTodayUV = condWeather.current_observation.UV
        
        if(debug) log.debug "cWeatherUpdate = ${cWeatherUpdate}, cWeatherCondition = ${cWeatherCondition}, " +
        					"cWeatherPrecipitation = ${cWeatherPrecipitation}, cWeatherWind = ${cWeatherWind},  cWeatherHum = ${cWeatherHum}, cWeatherHum = ${condTodayUV}  "    

        if(wMetric){
        //hourly metric updates
        def cWeatherWind_m = cWeather.hourly_forecast[0].wspd.metric + " kilometers per hour"        
        	if		(element == "precip" || element == "rain") {result = "The chance of precipitation is " + cWeatherPrecipitation }
        	else if	(element == "wind") {result = "The wind intensity is " + cWeatherWind_m }
        	else if	(element == "uv") {result = "The UV index is " + condTodayUV }
			else if	(element == "hum") {result = "The relative humidity is " + cWeatherHum }        
			else if	(element == "cond") {result = "The current weather condition is " + cWeatherCondition }
        }
        else{
        	if		(element == "precip" || element == "rain") {result = "The chance of precipitation is " + cWeatherPrecipitation }
        	else if	(element == "wind") {result = "The wind intensity is " + cWeatherWind }
        	else if	(element == "uv") {result = "The UV index is " + condTodayUV }
			else if	(element == "hum") {result = "The relative humidity is " + cWeatherHum }        
			else if	(element == "cond") {result = "The current weather condition is " + cWeatherCondition }        
		}        
        return result
	}
/*	catch (Throwable t) {
		log.error t
        state.pTryAgain = true
        return result
	} 
}*/
/***********************************************************************************************************************
    WEATHER TEMPS
***********************************************************************************************************************/
def private mGetWeatherTemps(){
	state.pTryAgain = false
    def result ="Today's temperatures not available at the moment, please try again later"
//	try {
		def weather = getWeatherFeature("forecast", settings.wZipCode)
        def sTodayWeather = weather.forecast.simpleforecast.forecastday[0]
        def tHigh = sTodayWeather.high.fahrenheit//.toInteger()
        def tLow = sTodayWeather.low.fahrenheit//.toInteger()
        	if(wMetric){
                def tHighC = weather.forecast.simpleforecast.forecastday[0].high.celsius//.toInteger()
                def tLowC = weather.forecast.simpleforecast.forecastday[0].low.celsius//.toInteger()
                result = "Today's low temperature is: " + tLowC  + ", with a high of " + tHighC
            }
            else {
                result = "Today's low temperature is: " + tLow  + ", with a high of " + tHigh
        	}
            return result
	}
/*	catch (Throwable t) {
        log.error t
        state.pTryAgain = true
        return result
    }
}   */
/***********************************************************************************************************************
    WEATHER ALERTS
***********************************************************************************************************************/
def private mGetWeatherAlerts(){
	def result = "There are no weather alerts for your area"
//	try {
		def weather = getWeatherFeature("alerts", settings.wZipCode)
        def alert = weather.alerts.description[0]
        def expire = weather.alerts.expires[0]
        	expire = expire?.replaceAll(~/ EST /, " ")?.replaceAll(~/ CST /, " ")?.replaceAll(~/ MST /, " ")?.replaceAll(~/ PST /, " ")
        	log.warn "alert = ${alert} , expire = ${expire}"   	
            if(alert != null) {
                result = alert  + " is in effect for your area, that expires at " + expire            
            }
        return result
    }
/*	catch (Throwable t) {
	log.error t
	return result
	}
}*/
/***********************************************************************************************************************
    HOURLY FORECAST
***********************************************************************************************************************/
def mGetWeatherUpdates(){
    def weatherData = [:]
    def data = [:]
   	def result
    //try {
        //hourly updates
            def cWeather = getWeatherFeature("hourly", settings.wZipCode)
            def cWeatherCondition = cWeather.hourly_forecast[0].condition
            def cWeatherPrecipitation = cWeather.hourly_forecast[0].pop + " percent"
            def cWeatherWind = cWeather.hourly_forecast[0].wspd.english + " miles per hour"
            def cWeatherHum = cWeather.hourly_forecast[0].humidity + " percent"
            def cWeatherUpdate = cWeather.hourly_forecast[0].FCTTIME.civil
            //past hour's forecast
            def pastWeather = state.lastWeatherUpdate
            def lastCheck = state.lastWeatherCheck
            //current forecast
				weatherData.wCond = cWeatherCondition
                weatherData.wWind = cWeatherWind
                weatherData.wHum = cWeatherHum
                weatherData.wPrecip = cWeatherPrecipitation
            def lastUpdated = new Date(now()).format("h:mm aa", location.timeZone)
                if(pastWeather == null) {
                    state.lastWeatherUpdate = weatherData
                    state.lastWeatherCheck = lastUpdated
                }
                else {
                    def wUpdate = pastWeather.wCond != cWeatherCondition ? "current weather condition" : pastWeather.wWind != cWeatherWind ? "wind intensity" : pastWeather.wHum != cWeatherHum ? "humidity" : pastWeather.wPrecip != cWeatherPrecipitation ? "chance of precipitation" : null
                    def wChange = wUpdate == "current weather condition" ? cWeatherCondition : wUpdate == "wind intensity" ? cWeatherWind  : wUpdate == "humidity" ? cWeatherHum : wUpdate == "chance of precipitation" ? cWeatherPrecipitation : null                    
                    //something has changed
                    if(wUpdate != null){
                        // saving update to state
                        state.lastWeatherUpdate = weatherData
                        state.lastWeatherCheck = lastUpdated
                        	def condChanged = pastWeather.wCond != cWeatherCondition
                            def windChanged = pastWeather.wWind != cWeatherWind
                            def humChanged = pastWeather.wHum != cWeatherHum
                            def precChanged = pastWeather.wPrecip != cWeatherPrecipitation
							if(condChanged){
                            	result = "Yes, the weather forecast was last updated at " + lastCheck + ". The weather condition has been changed to "  + cWeatherCondition
                            }
                            if(windChanged){
                            	if(result) {result = result +  " , the wind intensity to "  + cWeatherWind }
                            	else result = "Yes, the weather forecast was last updated at " + lastCheck + ". The wind intensity has been changed to "  + cWeatherWind
							}
                            if(humChanged){
                            	if(result) {result = result +  " , the humidity to "  + cWeatherHum }
                            	else result = "Yes, the weather forecast was last updated at " + lastCheck + ". The humidity has been changed to "  + cWeatherHum
							}
                            if(precChanged){
                            	if(result) {result = result + " , the chance of rain to "  + cWeatherPrecipitation }
                            	else result = "Yes, the weather forecast was last updated at " + lastCheck + ". The chance of rain has been changed to "  + cWeatherPrecipitation
                            }
							return result
                        }
                        else {
                        	result = "No, there have been no updates to the forecast since " + lastCheck
                            return result
                        }
                }
                log.info "refreshed hourly weather forecast: past forecast = ${pastWeather}; new forecast = ${weatherData}"  
    /*
    }
	catch (Throwable t) {
	log.error t
	return result
	}
    */
}
/***********************************************************************************************************************
    MISC. - SCHEDULE HANDLER
***********************************************************************************************************************/
private scheduleHandler(unit) {
//    def rowDate = new Date(date + location.timeZone)
//    def cDay = rowDate.date
//    def cHour= rowDate.hours
//	def cMin = rowDate.minutes   
    def result
    if (unit == "filters") {
    	if (debug) log.debug "Received filter replacement request"
        state.scheduledHandler = "filters"
        def xDays = settings.cFilterReplacement
        def tDays = new Date(now() + location.timeZone.rawOffset) + xDays 
        def schTime = tDays.format("h:mm aa")                       
		def schDate = tDays.format("EEEE, MMMM d")
       		runOnce(new Date() + xDays , "filtersHandler")
        	result = "Ok, scheduled reminder to replace the filters on " + schDate + " at " + schTime
        	state.filterNotif = "The filters need to be changed on  ${schDate}"
    		return result
    }
}
/***********************************************************************************************************************
    MISC. - FILTERS REMINDER
***********************************************************************************************************************/
private filtersHandler() {
    def tts = "It's time to replace your HVAC filters"
	if (synthDevice) {
    	synthDevice?.speak(tts) 
    }
    if (sonosDevice){
    	state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
        def currVolLevel = sonosDevice.latestValue("level")
        def newVolLevel = volume //-(volume*10/100)
        sonosDevice.setLevel(newVolLevel)
        sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
    }
	if(recipients?.size()>0 || sms?.size()>0){        
    	sendtxt(tts)
    }
}
/***********************************************************************************************************************
    MISC. - PET NOTIFICATIONS HANDLER - This sets the variable and repeats the variable
***********************************************************************************************************************/
private petNotifyHandler(tts, command) {
    def String outputTxt = (String) null 
    def result 
    def timeDate = new Date().format("hh:mm aa", location.timeZone)
    def dateDate = new Date().format("EEEE, MMMM d", location.timeZone)
    if (tts.startsWith("she") || tts.startsWith("he")) {
    	if (tts.contains("was") || tts.contains("has been")) {
    	if (tts.contains("shot") || tts.contains("brushed") || tts.contains("fed") || tts.contains("bathed") || tts.contains("walked") || tts.contains("medicated")) {
        outputTxt = "Ok, recording that ${app.label} was last ${command} on " + dateDate + " at " + timeDate    
            if(command == "shot" || command == "shop") {state.petShotNotify = "${app.label} was last shot on " + dateDate + " at " + timeDate }
            if(command == "brushed") {state.petBrushNotify = "${app.label} was last brushed on " + dateDate + " at " + timeDate }
            if(command == "fed") {state.petFedNotify = "${app.label} was last fed on " + dateDate + " at " + timeDate }
            if(command == "bathed") {state.petBathNotify = "${app.label} was last bathed on " + dateDate + " at " + timeDate }
            if(command == "walked") {state.petWalkNotify = "${app.label} was last walked on " + dateDate + " at " + timeDate }
			if(command == "medicated") {state.petMedNotify = "${app.label} was last medicated on " + dateDate + " at " + timeDate }
        if(psendText) { sendtxt(outputTxt) }
		if(pPush) { sendPush outputTxt }
        }
    //	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
      	}
    }
    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
}    
/***********************************************************************************************************************
    Notifications and Reminders Variables Reset Handlers
***********************************************************************************************************************/
page name: "petBrushReset"
def petBrushReset(){
    dynamicPage(name: "petBrushReset", title: "", uninstall: false) {
        section ("${app.label}'s Brushed Note Reset") {
            paragraph "${app.label}'s brushed note has been reset, please tap Done"
            state.petBrushNotify = "I'm sorry, I have not been told when ${app.label} was brushed"
        }
    }
}
page name: "petShotReset"
def petShotReset(){
    dynamicPage(name: "petShotReset", title: "", uninstall: false) {
        section ("${app.label}'s Shot Note Reset") {
            paragraph "${app.label}'s shot note has been reset, please tap Done"
            state.petShotNotify = "I'm sorry, I have not been told when ${app.label} was shot"
        }
    }
}
page name: "petFedReset"
def petFedReset(){
    dynamicPage(name: "petFedReset", title: "", uninstall: false) {
        section ("${app.label}'s Feeding Note Reset") {
            paragraph "${app.label}'s feeding note has been reset, please tap Done"
            state.petFedNotify = "I'm sorry, I have not been told when ${app.label} was fed"
        }
    }
}
page name: "petBathReset"
def petBathReset(){
    dynamicPage(name: "petBathReset", title: "", uninstall: false) {
        section ("${app.label}'s Bath Note Reset") {
            paragraph "${app.label}'s bath note has been reset, please tap Done"
            state.petBathNotify = "I'm sorry, I have not been told when ${app.label} was bathed"
        }
    }
}
page name: "petWalkReset"
def petWalkReset(){
    dynamicPage(name: "petWalkReset", title: "", uninstall: false) {
        section ("${app.label}'s Walk Note Reset") {
            paragraph "${app.label}'s walk note has been reset, please tap Done"
            state.petWalkNotify = "I'm sorry, I have not been told when ${app.label} was walked"
        }
    }
}
page name: "petMedReset"
def petMedReset(){
    dynamicPage(name: "petMedReset", title: "", uninstall: false) {
        section ("${app.label}'s Medication Note Reset") {
            paragraph "${app.label}'s medication note has been reset, please tap Done"
            state.petMedNotify = "I'm sorry, I have not been told when ${app.label} was medicated"
        }
    }
}

/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
def pKeypadSettings() {
    def result = ""
    if (shmConfigure || garageDoors || kpVirPer) {
        result = "complete"
    }
    result
}
def pKeypadComplete() {
    def text = "Tap here to Configure" 
    if (shmConfigure || garageDoors || kpVirPer) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}
def pSendSettings() {
    def result = ""
    if (synthDevice || sonosDevice || sendContactText || sendText || push) {
        result = "complete"}
    result
}
def pSendComplete() {
    def text = "Tap here to Configure" 
    if (synthDevice || sonosDevice || sendContactText || sendText || push) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}
def pConfigSettings() {
    def result = ""
    if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
        result = "complete"}
    result
}
def pConfigComplete() {
    def text = "Tap here to Configure" 
    if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}
def pDevicesSettings() {
    def result = ""
    if (fSwitches || sSwitches || sDimmers || sHues || sFlash) {
        result = "complete"}
    result
}
def pDevicesComplete() {
    def text = "Tap here to Configure" 
    if (fSwitches || sSwitches || sDimmers || sHues || sFlash) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}
def pActionsSettings(){
    def result = ""
    def pDevicesProc = ""
    if (fSwitches || sSwitches || sDimmers || sHues || sFlash || shmState) {
        result = "complete"
        pDevicesProc = "complete"}
    result
}
def pActionsComplete() {
    def text = "Tap here to configure" 
    def pDevicesComplete = pDevicesComplete()
    if (pDevicesProc || pMode || pRoutine || shmState) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}        
def pRestrictSettings(){
    def result = "" 
    if (modes || runDay || hues ||startingX || endingX) {
        result = "complete"}
    result
}
def pRestrictComplete() {
    def text = "Tap here to configure" 
    if (modes || runDay || hues ||startingX || endingX) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}
def pGroupSettings() {
    def result = ""
    if (gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
        result = "complete"}
    result
}
def pGroupComplete() {
    def text = "Tap here to Configure" 
    if (gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
        text = "Configured"}
    else text = "Tap here to Configure"
    text
}