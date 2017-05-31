/* 
 * Message and Control Profile - EchoSistant Add-on 
 ************************************ FOR INTERNAL USE ONLY ******************************************************
							
 								DON'T FORGET TO UPDATE RELEASE NUMBER!!!!!
 
 ************************************ FOR INTERNAL USE ONLY ******************************************************
 *
 *		5/25/2017		Version:4.0 R.0.3.5		Keypad support, Virtual Person enhancement, Garage Door Support
 *		4/20/2017		Version:4.0 R.0.3.4 	WebCoRE integration
 *		4/20/2017		Version:4.0 R.0.3.2c	Added SHM state change when profile runs option
 *		4/10/2017		Version:4.0 R.0.3.2b	Added Virtual Person status change when profile runs option
 *		4/5/2017		Version:4.0 R.0.3.2a	Added "Cut on" and "Cut off" commands for lights and Automation Disable
 *		4/03/2017		Version:4.0 R.0.3.2		Fixed Alexa output when controlling groups and custom groups
 *		3/21/2017		Version:4.0 R.0.3.1 	added window covering group
 *		3/15/2017		Version:4.0 R.0.3.0 	minor bug fixes
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
	name			: "Profiles",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Profiles Add-on - only publish if using secondary accounts",
	category		: "My Apps",
    parent			: "Echo:EchoSistant",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.3.5"
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

//dynamic page methods
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
		section ("Name Your Profile (must match the Intent Name)") {
 		   	label title:"Profile Name", required:false, defaultValue: "New Profile"  
        } 
        section("Audio and Text Message Settings") {
           	href "pSend", title: "Send These Message Types", description: pSendComplete(), state: pSendSettings()   
        }
        if(pSendSettings() == "complete"){
            section ("Output Settings and Profile Actions") {    
                href "pConfig", title: "Message Output Settings", description: pConfigComplete(), state: pConfigSettings()
                href "pActions", title: "Select Location and Device Actions (to execute when Profile runs)", description: pActionsComplete(), state: pActionsSettings()
            }
        }
        section("Select Devices physically located in this Profile <Room Control Feedback>") {
       		href "fDevices", title: "Feedback devices located in this Room", description: pConfigComplete(), state: pConfigSettings()
            }
        section("Keypads") {
        	href "pKeypads", title: "Keypad Configuration and Actions Control Settings", description: pSendComplete(), state: pSendSettings()
            }
       	section("Devices/Group Control Settings and Restrictions") {
	    	href "pGroups", title: "Create Groups and Select Devices", description: pGroupComplete(), state: pGroupSettings()
			if (pGroupSettings() == "complete" || pSendSettings() == "complete" ){
            	href "pRestrict", title: "General Profile Restrictions", description: pRestrictComplete(), state: pRestrictSettings()
			}
        }
		}
	}
page name: "fDevices"
	def fDevices(){
    	dynamicPage(name: "fDevices", title: "", uninstall: false){
        	section("Lights, Bulbs, and Switches") {
            input "fSwitches", "capability.switch", title: "Select Lights, Bulbs, and Switches...", multiple: true, required: false, submitOnChange: true
            }
            section("Doors") {
            input "fDoors", "capability.contactSensor", title: "Select contacts connected only to Doors...", multiple: true, required: false, submitOnChange: true
            }
            section("Windows") {
            input "fWindows", "capability.contactSensor", title: "Select contacts connected only to Windows...", multiple: true, required: false, submitOnChange: true
            }
            section("Fans") {
            input "fFans", "capability.switchLevel", title: "Select devices that control Fans and Ceiling Fans...", multiple: true, required: false, submitOnChange: true
            }
            section("Smart Vents") {
            input "fVents", "capability.switchLevel", title: "Select smart vents in this profiles room...", multiple: true, required: false, submitOnChange: true
            }
            section("Window Coverings") {
            input "fShades", "capability.windowShade", title: "Select devices that control your Window Coverings...", multiple: true, required: false, submitOnChange: true
            }
            section("Locks") {
            input "fLock", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false, submitOnChange: true
			}
            section("Batteries"){
            input "fBattery", "capability.battery", title: "Allow These Device(s) with Batteries...", required: false, multiple: true
            } 
        }
    }    
            	
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
   
page name: "pPerson"
    def pPerson(){
        dynamicPage(name: "pPerson", title: "", uninstall: false){
        	section ("Manage the Profile Virtual Person Device", hideWhenEmpty: true){
                    href "pPersonCreate", title: "Tap Here to Create the Virtual Person Device ~ '${app.label}'"
                    href "pPersonDelete", title: "Tap Here to Delete the Virtual Person Device ~ '${app.label}'"
                    }
                }
            }
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
/************************************************************************************************************
	Virtual Presence Sensor Creation Handler
************************************************************************************************************/
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

/************************************************************************************************************
	Virtual Presence Sensor Deletion Handler
************************************************************************************************************/
private removeChildDevices(delete) {
log.debug "The Virtual Person Device '${app.label}' has been deleted from your SmartThings environment"
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
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
page name: "pConfig"
    def pConfig(){
        dynamicPage(name: "pConfig", title: "", uninstall: false) {
            section ("Alexa Responses") {
                    input "pAlexaCustResp", "text", title: "Custom Response from Alexa...", required: false, defaultValue: none
                    input "pAlexaRepeat", "bool", title: "Alexa repeats the message to the sender as the response...", defaultValue: false, submitOnChange: true
                        if (pAlexaRepeat) {			
                        	if (pAlexaRepeat && pAlexaCustResp){
                           		paragraph 	"NOTE: only one custom Alexa response can"+
                            				" be delivered at once. Please only enable Custom Response OR Repeat Message"
                            }				
                        }
                    input "pContCmdsProfile", "bool", title: "Disable Conversation? (Alexa no longer prompts for additional commands, after a message is sent to a remote speaker, except for 'try again' if an error ocurs)", defaultValue: false
             }
             section ("Remote Speaker Settings") {
                	input "pRunMsg", "Text", title: "Play this predetermined message when this profile executes...", required: false
                    input "pPreMsg", "text", title: "Play this message before your spoken message...", defaultValue: none, submitOnChange: true, required: false 
             }
             section ("Sound Cancellation") {
					input "pDisableAlexaProfile", "bool", title: "Disable Alexa Feedback Responses (silence Alexa - overrides all other Alexa Options)?", defaultValue: false
                    input "pDisableALLProfile", "bool", title: "Disable Audio Output on the Remote Speaker(s)?", required: false
             }
             section ("Text and Push Notification Output") {
                	input "pRunTextMsg", "Text", title: "Send this predetermined text when this profile executes...", required: false
                    input "pPreTextMsg", "text", title: "Append this text before the text message...", defaultValue: none, required: false 
             }             
		}             
	}             
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
    page name: "pGroups"
        def pGroups() {
            dynamicPage(name: "pGroups", title: "",install: false, uninstall: false) {
                section ("Group These Switches", hideWhenEmpty: true){
                        input "gSwitches", "capability.switch", title: "Group Dimmers and Switches...", multiple: true, required: false, submitOnChange: true
                        if (gSwitches) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa, turn on/off the lights in the " + app.label
                        }
                        input "gDisable", "capability.switch", title: "Group Disable Automation Switches (disable = off, enable = on)", multiple: true, required: false, submitOnChange: true
						if (gDisable) {
                            input "reverseDisable", "bool", title: "Reverse Disable Command (disable = on, enable = off)", required: false, defaultValue: false
                            paragraph "You can now use this group by speaking commands to Alexa:  \n" +
                            " E.G: Disable Automation in the " + app.label
                        }
						input "gFans", "capability.switch", title: "Group Ceiling Fans...", multiple: true, required: false, submitOnChange: true
                        if (gFans) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa turn on/off the fan in the " + app.label
                        }
						input "gHues", "capability.colorControl", title: "Group Colored Lights...", multiple: true, required: false, submitOnChange: true
                        if (gHues) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa set the color to red in the " + app.label
                        }
                    	href "gCustom", title: "Create Custom Groups", description: "Tap to set"               
                }       
               section ("Vents and Window Coverings", hideWhenEmpty: true){ 
                    input "gVents", "capability.switchLevel", title: "Group Smart Vent(s)...", multiple: true, required: false, submitOnChange: true
						if (sVent) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa open/close the vents in the " + app.label
                        }
					input "gShades",  "capability.windowShade", title: "Group These Window Covering Devices...", multiple: true, required: false   
                    	if (gShades) {
                            paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                            " E.G: Alexa open/close the blinds/curtains/window coverings in the " + app.label
                        }
                }                
                section ("Media" , hideWhenEmpty: true){
					input "sMedia", "capability.mediaController", title: "Use This Media Controller", multiple: false, required: false, submitOnChange: true
                    	if (sMedia) {
                            paragraph "You can now control this device by speaking commands to Alexa:  \n" +
                            " E.G: Alexa start < Harmony Activity Name > in the " + app.label
                        }
						if (sSpeaker || sSynth) {
                            paragraph "You can now control this device by speaking commands to Alexa:  \n" +
                            " E.G: Alexa mute/unmute < Media Device Name > in the " + app.label
                        }
                    input "sSpeaker", "capability.musicPlayer", title: "Use This Media Player Device For Volume Control", required: false, multiple: false, submitOnChange: true
					input "sSynth", "capability.speechSynthesis", title: "Use This Speech Synthesis Capable Device", multiple: false, required: false, submitOnChange: true
                }             
            }
      	}
        page name: "gCustom"    
            def gCustom(){
                dynamicPage(name: "gCustom", title: "",install: false, uninstall: false) {
                    section ("Create a Group", hideWhenEmpty: true){
                        input "gCustom1", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true
                        input "gCustom1N", "text", title: "Name this Group...", multiple: false, required: false
                    }
                    if (gCustom1) {
                        section ("+ Create another Group", hideWhenEmpty: true){
                            input "gCustom2", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true
                            input "gCustom2N", "text", title: "Name this Group...", multiple: false, required: false
                        }
                    }
                    if (gCustom2) {
                        section ("+ Create another Group", hideWhenEmpty: true){
                            input "gCustom3", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true
                            input "gCustom3N", "text", title: "Name this Group...", multiple: false, required: false
                        }
                    }
                    if (gCustom3) {    
                        section ("+ Create another Group", hideWhenEmpty: true){
                            input "gCustom4", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true
                            input "gCustom4N", "text", title: "Name this Group...", multiple: false, required: false
                        }
                    }
                    if (gCustom4) {    
                        section ("+ Create another Group", hideWhenEmpty: true){
                            input "gCustom5", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true
                            input "gCustom5N", "text", title: "Name this Group...", multiple: false, required: false
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
		Virtual Person Check In/Out Automatically Handler
************************************************************************************************************/    
// Check in VP when profile runs

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
    //Feedback
    def feedback = tts.startsWith("how many") ? "feedbackModule" : tts.startsWith("which") ? "feedbackModule" : tts.startsWith("what") ? "feedbackModule" : "feedbackModule"
		feedback = tts.startsWith("are") ? "feedbackModule" : tts.startsWith("check") ? "feedbackModule" : "feedbackModule"
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
	def test = tts.contains("this is a test") ? true : tts.contains("a test") ? true : false
    if (parent.debug) log.debug "Message received from Parent with: (tts) = '${tts}', (intent) = '${intent}', (childName) = '${childName}', current app version: ${release()}"  
	//Sending event to WebCoRE
    sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
	
    if (command == "undefined") {
		outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = true
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	}    
	
    if (parent.debug) log.debug "sendNotificationEvent sent to CoRE from ${app.label}"
    
    if (pSendSettings() == "complete" || pGroupSettings() == "complete"){
        if (intent == childName){
			if (test){
				outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
				return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]       
    		}
            def getCMD = getCommand(tts) 
                deviceType = getCMD.deviceType
                command = getCMD.command
                if(parent.debug) log.debug "I have received a control command: ${command}, deviceType:  ${deviceType}"
//>>>FEEDBACK>>>>

		if (feedback == "feedbackModule") { // Variables for device current values ie - open/close/on/off
                def fDevice = tts.contains("vent") ? fVents : tts.contains("light") ? fSwitches : tts.contains("door") ? fDoors : tts.contains("window") ? fWindows : tts.contains("fan") ? fFans : 
                tts.contains("lock") ? fLocks : tts.contains("shade") ? fShades : tts.contains("curtains") ? fShades : tts.contains("blinds") ? fShades : null
                def fValue = tts.contains("vent") ? "switch" : tts.contains("light") ? "switch" : tts.contains("door") ? "contact" : tts.contains("window") ? "contact" : tts.contains("fan") ? "switchLevel" : 
                tts.contains("lock") ? "lock" : tts.contains("shade") ? "windowShade" : tts.contains("blind") ? "windowShade" : tts.contains("curtains") ? "windowShade" : null
                def fName = tts.contains("vent") ? "vent" : tts.contains("lock") ? "locks" : tts.contains("door") ? "door" : tts.contains("window") ? "window" : tts.contains("fan") ? "fan" : 
            	tts.contains("light") ? "lights" : tts.contains("shade") ? "shades" : tts.contains("blind") ? "blinds" : tts.contains("curtains") ? "curtains" : null
				if (tts.contains("check") && tts.contains("light")) { command = "on" }
                if (tts.contains("check")) {
                	if (tts.contains("lock") || tts.contains("door") || tts.contains("window") || tts.contains("vent") || tts.contains("shades") || tts.contains("blind") || tts.contains("curtain")) {
					command = "open" }}
//>>> Mode Status Feedback >>>>
            if (tts.contains("mode")){
                    outputTxt = "The Current Mode is " + location.currentMode      
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]			
            }
//>>> Security Status Feedback >>>>
            //TO DO: restrict security based on command
            if (tts.contains("smart home monitor") || tts.contains("alarm system") || tts.contains("alarm")){
                    def sSHM = location.currentState("alarmSystemStatus")?.value       
                    sSHM = sSHM == "off" ? "disarmed" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Home" : "unknown"
                    outputTxt = "Your Smart Home Monitor Status is " +  sSHM
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]				
            }
//>>> Misc Devices Feedback - Returns a Number Of Devices >>>>            
				if (tts.contains("window") || tts.contains("vent") || tts.contains("lock") || tts.contains("blind") || tts.contains("curtain") || tts.contains("shade") || tts.contains("fan") || tts.contains("door") || tts.contains("light")) {
                    def devList = []
                    if (fDevice == null) {
                    	outputTxt = "I'm sorry, it seems that you have not selected any devices for this query, please configure your feedback devices in the EchoSistant smartapp."}
                    else if (fDevice != null) {
                    if (fDevice.latestValue("${fValue}").contains(command)) {
                        fDevice.each { deviceName ->
                                    if (deviceName.latestValue("${fValue}")=="${command}") {
                                        String device  = (String) deviceName
                                        devList += device
                                        log.info "device = ${fDevice}, value = ${fValue}, deviceType = ${fName}, device list = '${devList}'"
                                    	}
                        			}
                            	}
                            }
                            if (tts.startsWith("how many") || tts.startsWith("check")) { 
                            if (devList.size() > 0) {
                            	if (devList.size() == 1) {
                            		outputTxt = "There is " + devList.size() + " " + fName + " " + command + " in the ${app.label}, would you like anything else? "                           			
                            		}
                            		else {outputTxt = "There are " + devList.size() + " " + fName + "'s " + command + " in the ${app.label}, would you like to know which ones? "
                            		}
                        		}
                            else (outputTxt = "There are no ${fName}'s " + command + " in the ${app.label} " )
                            }    
                            if (tts.startsWith("are there")) {
                    		if (devList.size() > 0) {
                            	if (devList.size() == 1) {
                            		outputTxt = "Yes, The " + devList + " is the only " + fName + " " + command +  "  in the ${app.label}, would you like anything else? "                           			
                            		}
                            		else {outputTxt = "Yes, there are " + devList.size() + " " + fName + "'s " + command + " in the ${app.label}, would you like to know which ones? "
                                	}
                        		}    
                        	else (outputTxt = "There are no ${fName}'s " + command + " in the ${app.label} " )
                            }
                            if (tts.startsWith("what") || tts.startsWith("which")) {
                    		if (devList.size() > 0) {
                            	if (devList.size() == 1) {
                            		outputTxt = "The " + devList + " is the only " + fName + " " + command +  "  in the ${app.label}, would you like anything else? "                           			
                            		}
                            		else {outputTxt = "The following ${fName}'s are " + command + " in the ${app.label} " + devList + ", would you like anything else?"
                        			}
                        		}
                            else (outputTxt = "There are no ${fName}'s " + command + " in the ${app.label} " )   
                        	}
                        }    
                    	else {outputTxt = "I'm sorry, There are no ${fName}'s " + command }
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                    	}
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
                /**********************
                FREE TEXT CONTROL ENGINE 
                ***********************/
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
                //VENTS AND WINDOWS CONTROL
                if (deviceType == "vent" || deviceType == "shade") { 
                        if (command == "open"  || command == "close") {
                            if (command == "open") {
                            	if(deviceType == "vent"){
                                gVents.on()
                                	gVents.setLevel(100)
                                	outputTxt = "Ok, opening the vents"
                                }
                                else {
                                	gShades.open()
                                	outputTxt = "Ok, opening the window coverings"
                            	}
                            }
                            else {   
                            	if(deviceType == "vent"){
                                	gVents.off()
                                	outputTxt = "Ok, closing the vents"
                                }
                                else {
                                	gShades.close()
                                    outputTxt = "Ok, closing the window coverings"
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
        	}
			else {
		outputTxt = "Sorry, you must first set up your profile before trying to execute it."
		pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
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
    deviceType = deviceType == "light" && gSwitches ? gSwitches : deviceType == "light1" && gCustom1 ? gCustom1 : deviceType == "light2" && gCustom2 ? gCustom2 : deviceType == "light3" && gCustom3 ? gCustom3 : deviceType == "light4" && gCustom4 ? gCustom4 : deviceType == "light5" && gCustom5 ? gCustom5 : null
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
   CUSTOM COMMANDS - CONTROL
******************************************************************************************************/
private getCommand(text){
   	def String command = (String) null
	def String deviceType = (String) null
    	text = text.toLowerCase()
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
		if(cTstat){
           deviceMatch = cTstat?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
			if(deviceMatch){
				deviceType = "cTstat"
                currState = deviceMatch.currentState("thermostatOperatingState").value
                stateDate = deviceMatch.currentState("thermostatOperatingState").date
                stateTime = deviceMatch.currentState("thermostatOperatingState").date.time
                def timeText = getTimeVariable(stateTime, deviceType)            
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "thermostatOperatingState" ]
            }
        }
        if (fSwitches){
		deviceMatch = fSwitches?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
			if(deviceMatch){
				deviceType = "fSwitches" 
				currState = deviceMatch.currentState("switch").value
				stateDate = deviceMatch.currentState("switch").date
				stateTime = deviceMatch.currentState("switch").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"]
        	}
        }
        if (cContact){
        deviceMatch =cContact?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cContact" 
				currState = deviceMatch.currentState("contact").value
				stateDate = deviceMatch.currentState("contact").date
				stateTime = deviceMatch.currentState("contact").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "contact"]
            }
        }
        if (cMotion){
        deviceMatch =cMotion?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cMotion" 
                currState = deviceMatch.currentState("motion").value 
                stateDate = deviceMatch.currentState("motion").date
                stateTime = deviceMatch.currentState("motion").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "motion"]
        	}
        } 
        if (cLock){
        deviceMatch =cLock?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cLock"
                currState = deviceMatch.currentState("lock").value 
                stateDate = deviceMatch.currentState("lock").date
                stateTime = deviceMatch.currentState("lock").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "lock"]
        	}
        }        
        if (cPresence){
        deviceMatch =cPresence.find {d -> d.label?.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cPresence"
                currState = deviceMatch.currentState("presence")?.value 
                stateDate = deviceMatch.currentState("presence")?.date
                stateTime = deviceMatch.currentState("presence")?.date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "presence"]
        	}
        }  
        if (cDoor){
        deviceMatch =cDoor?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cDoor"
                currState = deviceMatch.currentState("door").value 
                stateDate = deviceMatch.currentState("door").date
                stateTime = deviceMatch.currentState("door").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "door"]
        	}
        }  
        if (cVent){
		deviceMatch =cVent?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cVent"
                currState = deviceMatch.currentState("switch").value 
                currState = currState == "on" ? "open" : currState == "off" ? "closed" : "unknown"
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "switch"]
        	}
        }
        if (cWater){
		deviceMatch =cWater?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cWater"
                currState = deviceMatch.currentState("water").value 
                stateDate = deviceMatch.currentState("water").date
                stateTime = deviceMatch.currentState("water").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "water"]
        	}
        }        
        if (cMedia){
            if (fDevice == "TV") {
                deviceMatch = cMedia.first()
            }
            else {
                deviceMatch =cMedia?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            }   
            if(deviceMatch)	{
                deviceType = "cMedia"
                currState = deviceMatch.currentState("currentActivity").value 
                currState = currState == "--" ? " off " : " running the " + currState + " activity "
                stateDate = deviceMatch.currentState("currentActivity").date
                stateTime = deviceMatch.currentState("currentActivity").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "currentActivity"]
            }
        }        
        if (cFan){
		deviceMatch =cFan?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cFan"
                currState = deviceMatch.currentState("switch").value 
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"] 
            }
        }         
        if (cRelay){
		deviceMatch =cRelay?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
				deviceType == "cRelay"
                if (cContactRelay) {
                currState = cContactRelay.currentState("contact").value 
                stateDate = cContactRelay.currentState("contact").date
                stateTime = cContactRelay.currentState("contact").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "contact"] 
                }
			}
        }
        if (cBattery){
        deviceMatch =cBattery?.find {d -> d.label.toLowerCase() == fDevice?.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cBattery"
                currState = cBattery.currentState("battery").value
				stateTime = cBattery.currentState("battery").date.time
                def timeText = getTimeVariable(stateTime, deviceType)  
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": "", "tText": timeText.tText,  "mainCap": "battery"]
            } 
     	}
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
	if (modes || runDay || hues ||startingX || endingX) {
    	result = "complete"}
        result}
def pRestrictComplete() {def text = "Tap here to configure" 
    if (modes || runDay || hues ||startingX || endingX) {
    	text = "Configured"}
    	else text = "Tap here to Configure"
        text}
def pGroupSettings() {def result = ""
    if (gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
    	result = "complete"}
    	result}
def pGroupComplete() {def text = "Tap here to Configure" 
    if (gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
    	text = "Configured"}
        else text = "Tap here to Configure"
        text}        
