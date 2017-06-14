/* 
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
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
 * //UPDATE VERSION
/**********************************************************************************************************************************************/
definition(
	name			: "EchoSistantLabs",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "The Ultimate Voice Controlled Assistant Using Alexa Enabled Devices.",
	category		: "My Apps",
    singleInstance	: true,
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************
	UPDATE LINE 38 TO MATCH RECENT RELEASE
**********************************************************************************************************************************************/
private def textVersion() {
	def text = "5.0"
}
private release() {
    def text = "R.5.0.0"
}
/**********************************************************************************************************************************************/
preferences {   
    page name: "mainParentPage"
    	page name: "mProfiles"
        page name: "mSettings"
                page name: "mTokens"
                    page name: "mConfirmation"            
                    	page name: "mTokenReset"
            page name: "mBonus"
            	page name: "mDashboard"
                	page name: "mDashConfig"
                    page name: "pageTwo"
                    page name: "mWeatherConfig"
                    page name: "scheduled"
}            
//dynamic page methods
page name: "mainParentPage"
    def mainParentPage() {	
       dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
       		section ("") { 

				href "mProfiles", title: "Configure Profiles", description: mRoomsD(), state: mRoomsS(),
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
				href "mSettings", title: "General Settings", description: mSettingsD(), state: mSettingsS(),
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"                               
            }    
                //Dashboard
                section ("Echo Dashboard") { 
                    //href "mBonus", title: 
				def shmLocation = location.currentState("alarmSystemStatus")?.value
            	def shmStatus = shmLocation == "off" ? "Disarmed" : shmLocation == "away" ? "Armed (Away)" : shmLocation == "stay" ? "Armed (Stay)" : null
                paragraph 	"Location:\nCurrent Mode: ${location.currentMode} \n"  +
                        	"Smart Home Monitor Status: ${shmStatus}"
                    if (mLocalWeather) paragraph "Weather: ${state.todayWeather}"
                    if (activeAlert) paragraph "Active Weather Alerts: ${state.activeAlert}"
                def tStat1 = ThermoStat1
                def temp1 = (tStat1?.currentValue("temperature"))
                def setPC1 = (tStat1?.currentValue("coolingSetpoint"))
                def setPH1 = (tStat1?.currentValue("heatingSetpoint"))
                def mode1 = (tStat1?.currentValue("thermostatMode"))
                def oper1 = (tStat1?.currentValue("thermostatOperatingState"))
                def tStat2 = ThermoStat2
                def temp2 = (tStat2?.currentValue("temperature"))
                def setPC2 = (tStat2?.currentValue("coolingSetpoint"))
                def setPH2 = (tStat2?.currentValue("heatingSetpoint"))
                def mode2 = (tStat2?.currentValue("thermostatMode"))
                def oper2 = (tStat2?.currentValue("thermostatOperatingState"))
            if ("${mode1}" == "auto") 
                paragraph "The ${tStat1} is ${temp1}°. The thermostat is in ${mode1} mode, the heat is set to ${setPH1}°, the cooling is set to ${setPC1}°, and it is currently ${oper1}."
            if ("${mode1}" == "cool")
                paragraph "The ${tStat1} is ${temp1}°. The thermostat is set to ${setPC1}°, is in ${mode1} mode and is currently ${oper1}."
            if ("${mode1}" == "heat")
                paragraph "The ${tStat1} is ${temp1}°. The thermostat is set to ${setPH1}°, is in ${mode1} mode and is currently ${oper1}."
            if ("${mode1}" == "off")
                paragraph "The ${tStat1} thermostat is currently ${mode1}, and the temperature is ${temp1}°." 
            if ("${mode2}" == "auto") 
                paragraph "The ${tStat2} is ${temp2}°. The thermostat is in ${mode2} mode, the heat is set to ${setPH2}°, the cooling is set to ${setPC2}°, and it is currently ${oper2}."
            if ("${mode2}" == "cool")
                paragraph "The ${tStat2} is ${temp2}°. The thermostat is set to ${setPC2}°, is in ${mode2} mode and is currently ${oper2}."
            if ("${mode2}" == "heat")
                paragraph "The ${tStat2} is ${temp2}°. The thermostat is set to ${setPH2}°, is in ${mode2} mode and is currently ${oper2}."
            if ("${mode2}" == "off")
                paragraph "The ${tStat2} thermostat is currently ${mode2}, and the temperature is ${temp2}°." 


                def Sens1temp = (tempSens1?.currentValue("temperature"))
                def Sens2temp = (tempSens2?.currentValue("temperature"))
                def Sens3temp = (tempSens3?.currentValue("temperature"))
                def Sens4temp = (tempSens4?.currentValue("temperature"))
                def Sens5temp = (tempSens5?.currentValue("temperature"))
                if (tempSens1)
                    paragraph "The temperature of the ${tempSens1} is ${Sens1temp}°."
                if (tempSens2)
                    paragraph "The temperature of the ${tempSens2} is ${Sens2temp}°."
                if (tempSens3)
                    paragraph "The temperature of the ${tempSens3} is ${Sens3temp}°."
                if (tempSens4)
                    paragraph "The temperature of the ${tempSens4} is ${Sens4temp}°."
                if (tempSens5)
                    paragraph "The temperature of the ${tempSens5} is ${Sens5temp}°."
                
                href "scheduled", title: "Echo Mailbox", description: "Tap here to check your mailbox", state: complete
                
                }
            }

    }
	page name: "mProfiles"    
            def mProfiles() {
                dynamicPage (name: "mProfiles", title: "", install: true, uninstall: false) {
                    if (childApps?.size()>0) {  
                        section("Profiles",  uninstall: false){
                            app(name: "ProfilesLabs", appName: "ProfilesLabs", namespace: "Echo", title: "Create a New Profile", multiple: true,  uninstall: false)
                        }
                    }
                    else {
                        section("Profiles",  uninstall: false){
                            paragraph "NOTE: Looks like you haven't created any Profiles yet.\n \nPlease make sure you have installed the  Echo : Profiles Add-on before creating a new Profile!"
                            app(name: "ProfilesLabs", appName: "ProfilesLabs", namespace: "Echo", title: "Create a New Profile", multiple: true,  uninstall: false)
						}
					}
				}
            }  
page name: "mSettings"  
	def mSettings(){
        dynamicPage(name: "mSettings", uninstall: true) {
				section ("Directions, How-to's, and Troubleshooting") { 
 					href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "Tap to go to the EchoSistant Wiki", description: none,
                		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/wiki.png"
                	} 
                section ("Home Status Dashboard") {
                    input "activateDashboard", "bool", title: "Activate the DashBoard on the Home Page", required: false, default: false, submitOnChange: true
                    if (activateDashboard) {
                        href "mDashConfig", title: "Tap here to configure Dashboard", description: "", state: complete
                    }
                }
                section("Debugging") {
                    input "debug", "bool", title: "Enable Debug Logging", default: true, submitOnChange: true 
                    }
                section ("Apache License"){
                    input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
                    def msg = textLicense()
                        if (ShowLicense) paragraph "${msg}"
                    }
                section ("Security Token", hideable: true, hidden: true) {
                	paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here, and open this section")
                    paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
    				log.trace 	"\nLAMBDA CREDENTIALS (copy/paste in Lambda code (between the breaks): \n" +
                    			"\n---------------------------------------------------------------------------------------\n" +
                    			"\nvar STappID = '${app.id}'; \n var STtoken = '${state.accessToken}';\n" +
                   				"var url= '${apiServerUrl("/api/smartapps/installations/")}' + STappID + '/' ;\n" +
                                "\n---------------------------------------------------------------------------------------"
                    paragraph 	"Access token:\n"+
                                                "${state.accessToken}\n"+
                                                "Application ID:\n"+
                                                "${app.id}"
                    href "mTokens", title: "Revoke/Reset Security Access Token", description: none
                }
                section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){
                }	
			}             
		}
        page name: "mTokens"
            def mTokens(){
                    dynamicPage(name: "mTokens", title: "Security Tokens", uninstall: false){
                        section(""){
                            paragraph "Tap below to Reset/Renew the Security Token. You must log in to the IDE and open the Live Logs tab before tapping here. "+
                            "Copy and paste the displayed tokens into your Amazon Lambda Code."
                            if (!state.accessToken) {
                                OAuthToken()
                                paragraph "You must enable OAuth via the IDE to setup this app"
                                }
                            }
                                def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. "+
                                "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
                        section ("Reset Access Token / Application ID"){
                            href "mConfirmation", title: "Reset Access Token and Application ID", description: none
                            }
                        }
                    } 
            page name: "mConfirmation"
                def mConfirmation(){
                        dynamicPage(name: "mConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
                            section {
                                href "mTokenReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
                                paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
                                "You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
                                "Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
                                }
                            section(" "){
                                href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
                                }
                            }
                        }
                    page name: "mTokenReset"
                        def mTokenReset(){
                                dynamicPage(name: "mTokenReset", title: "Access Token Reset", uninstall: false){
                                    section{
                                        revokeAccessToken()
                                        state.accessToken = null
                                        OAuthToken()
                                        def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
                                        "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
                                        paragraph "${msg}"
                                        paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
                                        log.info "New IDs: STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                                    }
                                    section(" "){ 
                                        href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
                                        }
                                    }
                                }
       
page name: "scheduled"  // display scheduled events on the dashboard add by JH 3/26/2017
	def scheduled(){
    	dynamicPage(name: "scheduled", uninstall: false) {
    	def remMsg = state.esEvent.eText
        def remDate = state.esEvent.eStartingDate
        def remTime = state.esEvent.eStartingTime
    	section ("Scheduled Events") {
        	if (state.filterNotif != null) {
            paragraph "${state.filterNotif}"
            }
        }
        section ("Upcoming Reminders") {
			if (remMsg != null) {
			paragraph "Reminder Scheduled: $remMsg on $remDate at $remTime"
            }
    	}
    }    
}
page name: "mDashConfig"
	def mDashConfig(){
        dynamicPage(name: "mDashConfig", uninstall: false) {
        section ("Local Weather") {
        	input "mLocalWeather", "bool", title: "Display local weather conditions on Dashboard", required: false, default: false, submitOnChange: true
            }
        if (mLocalWeather) {
		section ("Local Weather Information") {
            href "mWeatherConfig", title: "Tap here to configure Weather information on Dashboard", description: "", state: complete
			}
        }            
		section ("Thermoststats") {
        	input "ThermoStat1", "capability.thermostat", title: "First ThermoStat", required: false, default: false, submitOnChange: true 
        	input "ThermoStat2", "capability.thermostat", title: "Second ThermoStat", required: false, default: false, submitOnChange: true 
            }
        section ("Temperature Sensors") {
        	input "tempSens1", "capability.temperatureMeasurement", title: "First Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens2", "capability.temperatureMeasurement", title: "Second Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens3", "capability.temperatureMeasurement", title: "Third Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens4", "capability.temperatureMeasurement", title: "Fourth Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens5", "capability.temperatureMeasurement", title: "Fifth Temperature Sensor", required: false, default: false, submitOnChange: true 
        }
    }
}
def mWeatherConfig() {
	dynamicPage(name: "mWeatherConfig", title: "Weather Settings") {
		section {
    		input "wMetric", "bool", title: "Report Weather In Metric Units\n(°C / km/h)", defaultValue: false, required: false, submitOnChange: true 
            input "wZipCode", "text", title: "Zip Code (If Location Not Set)", required: "false"
		}
	}
}                   
/*************************************************************************************************************
   CREATE INITIAL TOKEN
************************************************************************************************************/
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}
/*************************************************************************************************************
   LAMBDA DATA MAPPING
************************************************************************************************************/
mappings {
    path("/b") { action: [GET: "processBegin"] }
	path("/r") { action: [GET: "remindersHandler"] }
	path("/t") { action: [GET: "processTts"] }
}
/************************************************************************************************************
		Base Process
************************************************************************************************************/
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
    state.ParentRelease = release()
    runEvery1Hour(mGetWeatherUpdates)
    //Reminders
    state.esEvent = [:]
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    unsubscribe()
    state.esEvent = [:]
    initialize()
}
def initialize() {
        //WEATHER UPDATES
        state.todayWeather = state.todayWeather ?: mGetWeather()
        state.activeAlert = state.activeAlert ?: mGetWeatherAlerts()
        //CoRE and other 3rd party apps
        webCoRE_init() 
        sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
        def children = getChildApps()
    	if (debug) log.debug "Refreshing Profiles for 3rd party apps, ${getChildApps()*.label}"
        if (!state.accessToken) {
        	if (debug) log.error "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
			}
        //SHM status change and keypad initialize
    		subscribe(location, locationHandler)
        	subscribe(location, "remindR", runReport) //used for running ES Profiles from RemindR app
        	state.esProfiles = state.esProfiles ? state.esProfiles : []
            state.lambdaReleaseTxt = "Not Set"
            state.lambdaReleaseDt = "Not Set" 
            state.lambdatextVersion = "Not Set"
        //Alexa Responses
			state.pTryAgain = false
        	state.pContCmds = settings.pDisableContCmds == false ? true : settings.pDisableContCmds == true ? false : true
            state.pMuteAlexa = settings.pEnableMuteAlexa
			state.pShort = settings.pUseShort
            state.pContCmdsR = "init"       
        //Other Settings
			state.pendingConfirmation = false     
	}  
/*************************************************************************/
/* webCoRE Connector v0.2                                                */
/*************************************************************************/
/*  Copyright 2016 Adrian Caramaliu <ady624(at)gmail.com>                */
/*                                                                       */
/*  This program is free software: you can redistribute it and/or modify */
/*  it under the terms of the GNU General Public License as published by */
/*  the Free Software Foundation, either version 3 of the License, or    */
/*  (at your option) any later version.                                  */
/*                                                                       */
/*  This program is distributed in the hope that it will be useful,      */
/*  but WITHOUT ANY WARRANTY; without even the implied warranty of       */
/*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the         */
/*  GNU General Public License for more details.                         */
/*                                                                       */
/*  You should have received a copy of the GNU General Public License    */
/*  along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
/*************************************************************************/
/*  Initialize the connector in your initialize() method using           */
/*     webCoRE_init()                                                    */
/*  Optionally, pass the string name of a method to call when a piston   */
/*  is executed:                                                         */
/*     webCoRE_init('pistonExecutedMethod')                              */
/*************************************************************************/
/*  List all available pistons by using one of the following:            */
/*     webCoRE_list() - returns the list of id/name pairs                */
/*     webCoRE_list('id') - returns the list of piston IDs               */
/*     webCoRE_list('name') - returns the list of piston names           */
/*************************************************************************/
/*  Execute a piston by using the following:                             */
/*     webCoRE_execute(pistonIdOrName)                                   */
/*  The execute method accepts either an id or the name of a             */
/*  piston, previously retrieved by webCoRE_list()                       */
/*************************************************************************/
private webCoRE_handle(){return'webCoRE'}
private webCoRE_init(pistonExecutedCbk){state.webCoRE=(state.webCoRE instanceof Map?state.webCoRE:[:])+(pistonExecutedCbk?[cbk:pistonExecutedCbk]:[:]);subscribe(location,"${webCoRE_handle()}.pistonList",webCoRE_handler);if(pistonExecutedCbk)subscribe(location,"${webCoRE_handle()}.pistonExecuted",webCoRE_handler);webCoRE_poll();}
private webCoRE_poll(){sendLocationEvent([name: webCoRE_handle(),value:'poll',isStateChange:true,displayed:false])}
public  webCoRE_execute(pistonIdOrName,Map data=[:]){def i=(state.webCoRE?.pistons?:[]).find{(it.name==pistonIdOrName)||(it.id==pistonIdOrName)}?.id;if(i){sendLocationEvent([name:i,value:app.label,isStateChange:true,displayed:false,data:data])}}
public  webCoRE_list(mode){def p=state.webCoRE?.pistons;if(p)p.collect{mode=='id'?it.id:(mode=='name'?it.name:[id:it.id,name:it.name])}}
public  webCoRE_handler(evt){switch(evt.value){case 'pistonList':List p=state.webCoRE?.pistons?:[];Map d=evt.jsonData?:[:];if(d.id&&d.pistons&&(d.pistons instanceof List)){p.removeAll{it.iid==d.id};p+=d.pistons.collect{[iid:d.id]+it}.sort{it.name};state.webCoRE = [updated:now(),pistons:p];};break;case 'pistonExecuted':def cbk=state.webCoRE?.cbk;if(cbk&&evt.jsonData)"$cbk"(evt.jsonData);break;}}                       
/************************************************************************************************************
		CoRE Integration
************************************************************************************************************/
def listEchoSistantProfiles() {
log.warn "child requesting esProfiles"
	return state.esProfiles = state.esProfiles ? state.esProfiles : []
}
def getProfileList(){
		return getChildApps()*.label
		if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
}
def childUninstalled() {
	if (debug) log.debug "Profile has been deleted, refreshing Profiles for CoRE, ${getChildApps()*.label}"
    sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
} 
def getChildSize(child) {
	def childList = []
	def childMasterApp
    childApps.each {ch ->
        	childMasterApp = ch.app.name
		if (childMasterApp == child) {
        	String children  = (String) ch.label
            childList += children
      	}
 	}
    return childList.size()
}
def remindrHandler(evt) {
	if (!evt) return
    log.warn "received event from RemindR with data: $evt.data"
	switch (evt.value) {
		case "refresh":
		state.esProfiles = evt.jsonData && evt.jsonData?.profiles ? evt.jsonData.profiles : []
			break
	}
}
/************************************************************************************************************
		Begining Process - Lambda via page b
************************************************************************************************************/
def processBegin(){ 
    def versionTxt  = params.versionTxt 		
    def versionDate = params.versionDate
    def releaseTxt = params.releaseTxt
    def event = params.intentResp
        state.lambdaReleaseTxt = releaseTxt
        state.lambdaReleaseDt = versionDate
        state.lambdatextVersion = versionTxt
    def versionSTtxt = textVersion()
    def releaseSTtxt = release()
    def pPendingAns = false 
    def pContinue = state.pMuteAlexa
    def pShort = state.pShort
    def String outputTxt = (String) null 
    	state.pTryAgain = false
    if (debug) log.debug "^^^^____LAUNCH REQUEST___^^^^" 
    if (debug) log.debug "Launch Data: (event) = '${event}', (Lambda version) = '${versionTxt}', (Lambda release) = '${releaseTxt}', (ST Main App release) = '${releaseSTtxt}'"
//try {
    if (event == "noAction") {//event == "AMAZON.NoIntent" removed 1/20/17
    	state.pinTry = null
        state.savedPINdata = null
        state.pContCmdsR = null // added 1/20/2017
        state.pTryAgain = false
    }
// >>> NO Intent <<<<    
    if (event == "AMAZON.NoIntent"){
    	if(state.pContCmdsR == "level" || state.pContCmdsR == "repeat"){
            if (state.lastAction != null) {
            	if (state.pContCmdsR == "level") {state.pContCmdsR = "repeat"}
                def savedData = state.lastAction
                outputTxt = controlHandler(savedData) 
                pPendingAns = "level"
            }
            else {
                state.pContCmdsR = null
                pPendingAns = null
            }
        }
        if( state.pContCmdsR == "door"){
            if (state.lastAction != null) {
                state.lastAction = null
                state.pContCmdsR = null 
                pPendingAns = null 
            }
        }
        if( state.pContCmdsR == "feedback" ||  state.pContCmdsR == "bat" || state.pContCmdsR == "act" ){
            if (state.lastAction != null) {
                state.lastAction = null
                state.pContCmdsR = null 
                pPendingAns = null 
            }
        }
        if( state.pContCmdsR == "init" || state.pContCmdsR == "undefined"){
        	state.pTryAgain = false
        }
        if( state.pContCmdsR == null){
        	state.pTryAgain = false
        }
    }
// >>> YES Intent <<<<     
    if (event == "AMAZON.YesIntent") {
        if (state.pContCmdsR == "level" || state.pContCmdsR == "repeat") {
            state.pContCmdsR = null
            state.lastAction = null
            pPendingAns = "level"
        }
        else {
        	state.pTryAgain = false
        }
        if(state.pContCmdsR == "door"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
 				//NEW PIN VALIDATION!!!!! ///// ADD THE THE usePIN variable below to run the PIN VALIDATION
 				if(state.usePIN_D == true) {
     				//RUN PIN VALIDATION PROCESS
                	def pin = "undefined"
               		def command = "validation"
                	def num = 0
                	def unit = "doors"
                	outputTxt = pinHandler(pin, command, num, unit)
                    pPendingAns = "pin"
                    if (state.pinTry == 3) {pPendingAns = "undefined"}
                    log.warn "try# ='${state.pinTry}'"
					return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            	}
                else {
                outputTxt = controlHandler(savedData) 
                pPendingAns = "door"
            	}
        	}
        }
        if(state.pContCmdsR == "feedback"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
                outputTxt = getMoreFeedback(savedData) 
                pPendingAns = "feedback"
				return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
         }
         if(state.pContCmdsR == "bat" || state.pContCmdsR == "act"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
                outputTxt = savedData
                pPendingAns = "feedback"
                state.pContCmdsR = null
				return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
       }
       if(state.pContCmdsR == "caps"){
            if (state.lastAction!= null) {
                outputTxt = state.lastAction
                pPendingAns = "caps"
				state.pContCmdsR = null 
				state.lastAction = null
                return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
        }        
     }
// >>> Handling a Profile Intent <<<<      
     if (!event.startsWith("AMAZON") && event != "main" && event != "security" && event != "feedback" && event != "profile" && event != "noAction"){
		childApps?.each {child ->
			if (child?.label.toLowerCase() == event?.toLowerCase()) { 
                pContinue = child?.checkState()  
            }
       	}
        //if Alexa is muted from the child, then mute the parent too / MOVED HERE ON 2/9/17
        pContinue = pContinue == true ? true : state.pMuteAlexa == true ? true : pContinue
		return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	     
	}
	if (debug){
    	log.debug "Begining Process data: (event) = '${event}', (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
      	"; data sent: pContinue = '${pContinue}', pShort = '${pShort}',  pPendingAns = '${pPendingAns}', versionSTtxt = '${versionSTtxt}', releaseSTtxt = '${releaseSTtxt}' outputTxt = '${outputTxt}' ; "+
        "other data: pContCmdsR = '${state.pContCmdsR}', pinTry'=${state.pinTry}' "
	}
    return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	 

} 
/*catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
	}
}   */
/************************************************************************************************************
   TEXT TO SPEECH PROCESS - Lambda via page t
************************************************************************************************************/
def processTts() {
//LAMBDA VARIABLES
def ptts = params.ttstext 
def pintentName = params.intentName
//OTHER VARIABLES
def String outputTxt = (String) null 
def String pContCmdsR = (String) null
def pContCmds = false
def pTryAgain = false
def pPIN = false
def dataSet = [:]
if (debug) log.debug "Messaging Profile Data: (ptts) = '${ptts}', (pintentName) = '${pintentName}'"

    pContCmdsR = "profile"
	def tProcess = true
//try {

if (ptts == "this is a test"){
	outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
}

    if(ptts.contains("no ") || ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it" || ptts == "yes" && state.pContCmdsR != "wrongIntent"){
    	if(ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it" || ptts.contains("thank")){
            outputTxt = "ok, I am here if you need me"
            pContCmds = false
            return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    	}
		else {
            outputTxt = "ok, please continue, "
            pContCmds = false
            return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    	}        
    }
    else{
         childApps.each {child ->
            if (child.label.toLowerCase() == pintentName.toLowerCase()) { 
                if (debug) log.debug "Found a profile: '${pintentName}'"
                pintentName = child.label
                // recording last message
                state.lastMessage = ptts
                state.lastIntent = pintentName
                state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
                dataSet = [ptts:ptts, pintentName:pintentName] 
				def childRelease = child.checkRelease()
				log.warn "childRelease = $childRelease"
                if (ptts.startsWith("get") || ptts.endsWith("tonight") || ptts.contains("weather") || ptts.contains("temperature") || ptts.contains("forecast") || ptts.contains("humidity") || ptts.contains("rain") || ptts.contains("wind") || ptts.contains("humidity")) {
                	def pResponse = child.profileFeedbackEvaluate(dataSet)
                    log.info "child.profileWeatherEvaluate executed from the main at line 3680"
                	outputTxt = pResponse.outputTxt
                	pContCmds = pResponse.pContCmds
                	pContCmdsR = pResponse.pContCmdsR
                	pTryAgain = pResponse.pTryAgain
                	}
				if (ptts.startsWith("give") || ptts.startsWith("for") || ptts.startsWith("tell") || ptts.startsWith("what") || ptts.startsWith("how") || ptts.startsWith("is") || ptts.startsWith("when") || ptts.startsWith("which") || ptts.startsWith("are") || ptts.startsWith("how many") || ptts.startsWith("check") || ptts.startsWith("who")) {
                    def pResponse = child.profileFeedbackEvaluate(dataSet)
                    log.info "child.profileFeedbackEvaluate executed from the main at line 3688"
                	outputTxt = pResponse.outputTxt
                	pContCmds = pResponse.pContCmds
                	pContCmdsR = pResponse.pContCmdsR
                	pTryAgain = pResponse.pTryAgain
                	}
				else {
                    def pResponse = child.profileEvaluate(dataSet)
                	log.info "child.profileMessagingEvaluate executed from the main at line 3704"
                    outputTxt = pResponse.outputTxt
                	pContCmds = pResponse.pContCmds
                	pContCmdsR = pResponse.pContCmdsR
                	pTryAgain = pResponse.pTryAgain
                	}
            	}
        	}
        if (outputTxt?.size()>0){
            return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }
        else {
            if (state.pShort != true){
            	outputTxt = "I wish I could help, but EchoSistant couldn't find a Profile named " + pintentName + " or the command may not be supported"
            }
            else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "} 
            pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain": pTryAgain, "pPIN":pPIN]
        }

        def hText = "run a messaging and control profile"
		if (state.pShort != true){ 
			outputTxt = "Sorry, I heard that you were looking to " + hText + " but Echosistant wasn't able to take any actions "
			return outputTxt
        }
		else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "
        	return outputTxt
            }         
		pTryAgain = true
		return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]              
	}
} 
/*catch (Throwable t) {
log.error t
outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
state.pTryAgain = true
return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
} 
}	*/
/************************************************************************************************************
   REMINDERS AND EVENTS PROCESS - Lambda via page r
************************************************************************************************************/
def remindersHandler() {
		//LAMBDA VARIABLES
	def rCalendarName = params.rCalendarName 
	def rProfile = params.rProfile   
	def rType = params.rType //type of event
	def rFrequency = params.rFrequency //units/frequency 
	def rStartingDate = params.rStartingDate 
	def rStartingTime = params.rStartingTime 
	def rDuration = params.rDuration // number
	def rMessage = params.rMessage  
        //OTHER VARIABLES
        def String outputTxt = (String) null 
 		def String pContCmdsR = (String) null
        def pContCmds = false
        def pTryAgain = false
        def pPIN = false
        def String messageType  = state.esEvent.eType //(String) null
		def multiCalendar = false
		def String calendar = (String) null
        String newTime
        String newDate
        def data = [:]
//try {
        if (debug) log.debug 	"Reminders & Events Profile Data: (rCalendarName) = $rCalendarName,(rType) = $rType, (rFrequency) = $rFrequency, (rStartingDate) = $rStartingDate," +
        						" (rStartingTime) = $rStartingTime,(rDuration) = $rDuration,(rMessage) = $rMessage"
	
    if(!state.esEvent.eStartingDate && rStartingDate != "undefined" &&  rStartingDate != null) {
		state.esEvent.eStartingDate = rStartingDate
	}

//WHEN TYPE COMES IN    
    if (rType != "undefined" &&  rType != null){
    def String missingField = (String) null 
    	rType = rType.contains("event") ? "event" : rType.contains("recurring") ? "recurring" : rType.contains("a reminder") ? "reminder" : rType
        state.esEvent.eType = rType
        if(rStartingDate && !state.esEvent.eStartingDate && rStartingDate != "undefined" ) state.esEvent.eStartingDate = rStartingDate
        if(rStartingTime && !state.esEvent.eStartingTime && rStartingTime != "undefined") state.esEvent.eStartingTime = rStartingTime
			if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                newTime = date.format( "h:mm aa" )
                newDate = date.format( 'MM/dd/yyyy' )
        	}
		if(state.esEvent.eType == "event"){
        	if (state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eCalendar && state.esEvent.eDuration){
            	outputTxt = "Ok, scheduling event to $state.esEvent.eText on $newDate at $newTime, is that correct?"
        	}
        	else missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : !state.esEvent.eCalendar ? "Which calendar?" : !state.esEvent.eDuration ? "For fow long?" : null
		}
        if(state.esEvent.eType == "reminder") {
			if (state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime){
					outputTxt = "Ok, scheduling reminder to $state.esEvent.eText on $newDate at $newTime, is that correct?"
            }
            else missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : null 
		}
        if(state.esEvent.eType == "recurring") {
			if (state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eFrequency && state.esEvent.eDuration){	            
				def repeatUnit = rFrequency == "hourly" ? "hours" : rFrequency == "daily" ? "days" : rFrequency == "weekly" ? "days" : rFrequency == "monthly" ? "months" : rFrequency == "yearly" ? "months" : null                    
                outputTxt = "Ok, scheduling $state.esEvent.eFrequency reminder to $state.esEvent.eText every $state.esEvent.eDuration"+
                    			" $repeatUnit, starting on $newDate at $newTime, is that correct?"            
			}
			else missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : !state.esEvent.eDuration ? "For fow long?" : null
    	}
		if(missingField) {
        	log.warn "missingField = $missingField"
			outputTxt = missingField
		}
        pContCmdsR = "feedback" 
		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
    }
    if(state.esEvent.eText && state.esEvent.eType == "quickReminder" && !state.esEvent.eDuration){
//WHEN DURATION COMES IN
        if(rDuration != null && rDuration != "undefined" && rFrequency != "undefined" && rFrequency != null){
			state.esEvent.eDuration = rDuration
			state.esEvent.eFrequency = rFrequency
            outputTxt = "Ok, scheduling quick reminder to $state.esEvent.eText$state.esEvent.eDuration $state.esEvent.eFrequency is that correct?"
			pContCmdsR = "feedback"            
    	}
        else {
            outputTxt = "Sorry, I still didn't get the number, "
            pTryAgain = true
        }
		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
	}
//WHEN MESSAGE COMES IN
    if (rMessage != "undefined" &&  rMessage != null){
    	def tts = rMessage
        def quickMessage
        int iLength
        def test = tts.contains("this is a test") ? true : tts.contains("a test") ? true : false 
        if (test){
			outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
			return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]       
   		}
        def reminder = tts.startsWith("set a reminder to") ? "set a reminder to " : tts.startsWith("set reminder to") ? "set reminder to " : null
        if (reminder == null) reminder = tts.startsWith("remind me to") ? "remind me to " : tts.startsWith("set the reminder to") ? "set the reminder to " : null 
        if (reminder == null) reminder = tts.startsWith("i need to") ? "i need to " : tts.startsWith("need to") ? "need to " : tts.startsWith("I need to") ? "I need to " : null 
		if (reminder == null) reminder = tts.startsWith("add a reminder to") ? "add a reminder to " : tts.startsWith("add reminder to") ? "add reminder to " : null         
       	if (reminder == null) reminder = tts.startsWith("schedule reminder to") ? "schedule reminder to " : tts.startsWith("add the reminder to") ? "add the reminder to " : null 	
        if (reminder == null) reminder = tts.startsWith("schedule a reminder to") ? "schedule a reminder to " : tts.startsWith("schedule the reminder to") ? "schedule the reminder to " : null
        if (reminder == null) reminder = tts.startsWith("set a reminder") ? "set a reminder " : tts.startsWith("set reminder") ? "set reminder " : null
        if (reminder == null) reminder = tts.startsWith("remind me") ? "remind me " : tts.startsWith("set the reminder") ? "set the reminder " : null 
		if (reminder == null) reminder = tts.startsWith("add a reminder") ? "add a reminder " : tts.startsWith("add reminder") ? "add reminder " : null         
       	if (reminder == null) reminder = tts.startsWith("schedule reminder") ? "schedule reminder " : tts.startsWith("add the reminder") ? "add the reminder " : null 	
        if (reminder == null) reminder = tts.startsWith("schedule a reminder") ? "schedule a reminder " : tts.startsWith("schedule the reminder") ? "schedule the reminder " : null           
		//QUICK REMINDERS
        def quickReminder = tts.endsWith("minute") ? "minutes" : tts.endsWith("minutes") ? "minutes" : tts.endsWith("hours") ? "hours" : tts.endsWith("hour") ? "hours" : tts.endsWith("day") ? "days" : tts.endsWith("days") ? "days" : "undefined"
        def quickReplace = tts.endsWith("minute") ? "minute" : tts.endsWith("minutes") ? "minutes" : tts.endsWith("hours") ? "hours" : tts.endsWith("hour") ? "hour" : tts.endsWith("day") ? "day" : tts.endsWith("days") ? "days" : "undefined"
        tts = tts.replace("one", "1").replace("two", "2").replace("three", "3").replace("four", "4").replace("five", "5").replace("six", "6").replace("seven", "7").replace("eight", "8").replace("nine", "9")
        def length = tts.findAll( /\d+/ )*.toInteger()
			if(length[0] !=null && quickReminder !="undefined") {
            	iLength = (int)length.get(0)                    
                if(reminder){
                	quickMessage = tts.replace("${reminder}", "").replace("in ${iLength}", "").replace("${quickReplace}", "")
                }
                else{
               		quickMessage = tts ? tts.replace("in ${iLength}", "").replace("${quickReplace}", "") : null
            	}
                if(quickMessage) {
                    state.esEvent.eText = quickMessage
                    state.esEvent.eDuration = length[0]
                    state.esEvent.eFrequency = quickReminder
                    state.esEvent.eType = "quickReminder"
                }
                else {
                    outputTxt = "sorry, I was unable to get the number,  "
                    pTryAgain = true
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
                }
            }
            if(quickReminder !="undefined" && iLength != null){ 
                outputTxt = "Ok, scheduling quick reminder to $state.esEvent.eText in $state.esEvent.eDuration $state.esEvent.eFrequency, is that correct?"
				pContCmdsR = "feedback"            
				return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
            }   
		//recurring reminders - fields required: rStartingDate & rStartingTime & rFrequency & rMessage & rDuration 
            /*
            What is the reminder? (rMessage)
            How often? - rFrequency
            What is the number of X to repeat the reminder? rDuration + rFrequency
            Starting on what day and time? (rStartingDate & rStartingTime) 
            */
        def recurring = tts.startsWith("set a recurring reminder to") ? "set a recurring reminder to " : tts.startsWith("set recurring reminder to") ? "set recurring reminder to " : null
        if (recurring == null) recurring = tts.startsWith("set the recurring reminder to") ? "set the recurring reminder to " : null 
		if (recurring == null) recurring = tts.startsWith("add a recurring reminder to") ? "add a recurring reminder to " : tts.startsWith("add recurring reminder to") ? "add recurring reminder to " : null         
       	if (recurring == null) recurring = tts.startsWith("schedule recurring reminder to") ? "schedule recurring reminder to " : tts.startsWith("add the recurring reminder to") ? "add the recurring reminder to " : null 	
        if (recurring == null) recurring = tts.startsWith("schedule a recurring reminder to") ? "schedule a recurring reminder to " : tts.startsWith("schedule the recurring reminder to") ? "schedule the recurring reminder to " : null
        if (recurring == null) recurring = tts.startsWith("set a recurring reminder") ? "set a recurring reminder " : tts.startsWith("set recurring reminder") ? "set recurring reminder " : null
        if (recurring == null) recurring = tts.startsWith("set the recurring reminder") ? "set the recurring reminder " : null 
		if (recurring == null) recurring = tts.startsWith("add a recurring reminder") ? "add a recurring reminder " : tts.startsWith("add recurring reminder") ? "add recurring reminder " : null         
       	if (recurring == null) recurring = tts.startsWith("schedule recurring reminder") ? "schedule recurring reminder " : tts.startsWith("add the recurring reminder") ? "add the recurring reminder " : null 	
        if (recurring == null) recurring = tts.startsWith("schedule a recurring reminder") ? "schedule a recurring reminder " : tts.startsWith("schedule the recurring reminder") ? "schedule the recurring reminder " : null       
		//event - fields required: rStartingDate & rStartingTime & rFrequency & rMessage & rDuration (rCalendarName) 
            /*
            What is the reminder? (rMessage)
            Starting on what day and time? (rStartingDate & rStartingTime)
            For how long? - rDuration + rFrequency
            Which Calendar? (rStartingDate & rStartingTime) 
            */        
        def event = tts.startsWith("add an event to my calendar") ? "add an event to my calendar" : null
        if (event == null) event = tts.startsWith("add event to my calendar") ? "add event to my calendar" : null
        if (event == null) event = tts.startsWith("set an event to") ? "set an event to " : tts.startsWith("set event to") ? "set event to " : null
        if (event == null) event = tts.startsWith("set the event to") ? "set the event to " : null 
		if (event == null) event = tts.startsWith("add an event to") ? "add an event to " : tts.startsWith("add event to") ? "add event to " : null         
       	if (event == null) event = tts.startsWith("schedule event to") ? "schedule event to " : tts.startsWith("add the event to") ? "add the event to " : null 	
        if (event == null) event = tts.startsWith("schedule an event to") ? "schedule an event to " : tts.startsWith("schedule the event to") ? "schedule the event to " : null 
        if (event == null) event = tts.startsWith("set an event ") ? "set an event" : tts.startsWith("set event ") ? "set event" : null
        if (event == null) event = tts.startsWith("set the event") ? "set the event " : null 
		if (event == null) event = tts.startsWith("add an event") ? "add an event " : tts.startsWith("add event") ? "add event " : null         
       	if (event == null) event = tts.startsWith("schedule event") ? "schedule event " : tts.startsWith("add the event") ? "add the event " : null 	
        if (event == null) event = tts.startsWith("schedule an event") ? "schedule an event " : tts.startsWith("schedule the event") ? "schedule the event " : null         
        def message = reminder ? tts.replace("${reminder}", "") : recurring ? tts.replace("${recurring}", "") : event ? tts.replace("${event}", "") : null
        messageType = messageType ?: reminder ? "reminder" : recurring ? "recurring" : event ? "event" : null
		log.warn "message type from state: $messageType"
        if(messageType == "event"){
            childApps.each { child ->
            	log.warn " label = $child.label"
                if(child.label == "Reminders") {
                def calendars = child.listGCalendars()
                    multiCalendar = calendars.size() > 1 ? true : false
                    if(multiCalendar == false){
                    	state.esEvent.eCalendar = calendars
                    } 
                }
            }
        }
        log.warn "multiCalendar = $multiCalendar"
        log.warn "messageType = $messageType, rMessage = $rMessage, reminder = $reminder, recurring = $recurring,  event = $event, message = $message"
		state.esEvent.eType = state.esEvent.eType ?: messageType
		if (message == null) message = rMessage
            if(!state.esEvent.eStartingDate && !state.esEvent.eStartingTime) {
                state.esEvent.eText = message
                outputTxt = "Starting on what day and time?"
                pContCmdsR = "feedback"
            }
            else {
                if(!state.esEvent.eStartingDate && state.esEvent.eStartingTime ) {
                    state.esEvent.eText = message
                    outputTxt = "Starting on what date?"
                    pContCmdsR = "feedback" 
                }
                if (state.esEvent.eStartingDate && !state.esEvent.eStartingTime ) {
                state.esEvent.eText = message
                outputTxt = "At what time?"
                pContCmdsR = "feedback" 
                }
                if(state.esEvent.eStartingDate && state.esEvent.eStartingTime && !state.esEvent.eDuration && messageType != "reminder") {
                    state.esEvent.eText = message
                    pContCmdsR = "feedback"
                    if(messageType == "event"){
                    	outputTxt = "For fow long?"
                    }
                    else if (messageType == "recurring"){
                    	outputTxt = "How often?"
                    }
                }
                if(state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eDuration && !state.esEvent.eCalendar && multiCalendar && messageType == "event") {
                    state.esEvent.eText = message
                    outputTxt = "Which Calendar?"
                    pContCmdsR = "feedback"
                }
                if(state.esEvent.eStartingDate && state.esEvent.eStartingTime && messageType == "reminder") {
                    state.esEvent.eText = message
                    def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                    Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                    newTime = date.format( "h:mm aa" )
                    newDate = date.format( 'MM/dd/yyyy' )
            		outputTxt = "Ok, scheduling reminder to $state.esEvent.eText on $newDate at $newTime, is that correct?"
                    pContCmdsR = "feedback"
                }                
                if(!state.esEvent.eType) {
                    state.esEvent.eText = message
                	outputTxt = "Sorry, I didn't catch the type of event, is this a reminder, a recurring reminder or, an event?"
                	pContCmdsR = "feedback"
                }                   
                
            }
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
    }
//WHEN STARTING DATE & STARTING TIME COMES IN
	if (rStartingDate != "undefined" && rStartingTime != "undefined" && rStartingDate != null && rStartingTime != null){
		state.esEvent.eStartingDate = rStartingDate
        state.esEvent.eStartingTime = rStartingTime
        if (messageType == "reminder" && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eText){
			if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                newTime = date.format( "h:mm aa" )
                newDate = date.format( 'MM/dd/yyyy' )
        	}
            outputTxt = "Ok, scheduling reminder to $state.esEvent.eText on $newDate at $newTime, is that correct?"
			pContCmdsR = "feedback"
    	} 
        if(!state.esEvent.eText) {
        	outputTxt = "What is the event?"
        	pContCmdsR = "feedback"
		}
        else {
            if (!state.esEvent.eDuration && messageType != "reminder" && state.esEvent.eType){
                    pContCmdsR = "feedback"
                    if(messageType == "event"){
                    	outputTxt = "For fow long?"
                    }
                    else if (messageType == "recurring"){
                    	outputTxt = "How often?"
                    }
            }
            else{
            log.warn "eType = $eType"
            	if(multiCalendar && !state.esEvent.eCalendar && messageType == "event"){
                	outputTxt = "Which calendar?"
        			pContCmdsR = "feedback"
            	}
                
                else if (!state.esEvent.eType || state.esEvent.eType == null ){
                	outputTxt = "Sorry, I didn't catch the type of event, is this a reminder, a recurring reminder or an event?"
                	pContCmdsR = "feedback"
               }                 
           	}
      	}
    	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]     
 	}
    if(rFrequency != "undefined" && rFrequency != null && messageType == "recurring"){
    	if(rFrequency == "hourly" || rFrequency == "daily" || rFrequency == "monthly" || rFrequency == "weekly"){
            def repeatUnit = rFrequency == "hourly" ? "hours" : rFrequency == "daily" ? "days" : rFrequency == "weekly" ? "days" : rFrequency == "monthly" ? "months" : rFrequency == "yearly" ? "months" : null
            log.warn "repeatUnit = $repeatUnit, rFrequency =  $rFrequency, state eDuration = state.esEvent.eDuration"
            if(!state.esEvent.eDuration){
                state.esEvent.eFrequency = rFrequency
            	outputTxt = "What is the number of $repeatUnit to repeat the reminder"
        		pContCmdsR = "feedback"	
  				return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]     
            }
    	}
  	}
	if (rDuration != "undefined" && rDuration != null) {
    	state.esEvent.eDuration = rDuration
        	if(state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime && !state.esEvent.eCalendar && multiCalendar && messageType == "event"){
                	outputTxt = "Which calendar?"
        			pContCmdsR = "feedback"           
            }
            else {
            	if(messageType == "recurring"){
                	def frequency = state.esEvent.eFrequency
                    def repeatUnit = frequency == "hourly" ? "hours" : frequency == "daily" ? "days" : frequency == "weekly" ? "days" : frequency == "monthly" ? "months" : frequency == "yearly" ? "months" : null
                    if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                        def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                        Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                        newTime = date.format( "h:mm aa" )
                        newDate = date.format( 'MM/dd/yyyy' )
                    }
                    outputTxt = "Ok, scheduling $state.esEvent.eFrequency reminder to $state.esEvent.eText every $state.esEvent.eDuration"+
                    			" $repeatUnit, starting on $newDate at $newTime, is that correct?"
            		pContCmdsR = "feedback"
                }
                else {
                	if(messageType == "event"){
                        if (state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eCalendar && messageType == "event"){
                            if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                                def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                                Date date = Date.parse("yyyy-MM-dd HH:mm", olddate)
                                newTime = date.format( "h:mm aa" )
                                newDate = date.format( 'MM/dd/yyyy' )
                            }
                            outputTxt = "Ok, scheduling event to $state.esEvent.eText on $newDate at $newTime, is that correct?"
                            pContCmdsR = "feedback"                
                        }
                        else {
							def missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : !state.esEvent.eCalendar ? "Which calendar?" : !state.esEvent.eDuration ? "For fow long?" : null
							log.warn "missingField = $missingField"
            				outputTxt = missingField
							pContCmdsR = "feedback" 			
            			}
            		}
                }
            }
  	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]     
    }
    if(rCalendarName != "undefined" && rCalendarName != null && messageType == "event"){
    	state.esEvent.eCalendar = rCalendarName
        	if(state.esEvent.eText && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eDuration){
                if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                    def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                    Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                    newTime = date.format( "h:mm aa" )
                    newDate = date.format( 'MM/dd/yyyy' )
                }
            	outputTxt = "Ok, scheduling event to $state.esEvent.eText on $newDate at $newTime, is that correct?"
            	pContCmdsR = "feedback"         
            }
            else {
				def missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : !state.esEvent.eCalendar ? "Which calendar?" : !state.esEvent.eDuration ? "For fow long?" : null
				log.warn "missingField = $missingField"
            	outputTxt = missingField
				pContCmdsR = "feedback" 
 			}
  	return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]     
    }
    if (rFrequency == "yes" || rFrequency == "yup" || rFrequency == "yeah" || rFrequency == "you got it" || rFrequency == "no" || rFrequency == "cancel" || rFrequency == "neh" || rFrequency == "nope"){
		if (rFrequency == "yes" || rFrequency == "yup" || rFrequency == "yeah" || rFrequency == "you got it" ){
        	def event = messageType == "recurring" ? "${state.esEvent.eFrequency} reminder" : messageType
            log.warn "event = $event"
			if(event){
                if(state.esEvent.eStartingDate && state.esEvent.eStartingTime){
                    def olddate = state.esEvent.eStartingDate + " " + state.esEvent.eStartingTime
                    Date date = Date.parse("yyyy-MM-dd HH:mm",olddate)
                    newTime = date.format( "h:mm aa" )
                    newDate = date.format( 'MM/dd/yyyy' )
                }
                if(event == "event"){
                	data = ["eCalendar": state.esEvent.eCalendar, "eStartingDate": state.esEvent.eStartingDate , "eStartingTime": state.esEvent.eStartingTime, "eDuration": state.esEvent.eDuration, "eText": state.esEvent.eText]
                    sendLocationEvent(name: "echoSistant", value: "addEvent", data: ${}, displayed: true, isStateChange: true, descriptionText: "echoSistant add event request")
                    outputTxt = "Great! I sent the event to G Cal to be added on your calendar"
                }
                else {
					data = ["eStartingDate": state.esEvent.eStartingDate , "eStartingTime": state.esEvent.eStartingTime, "eDuration": state.esEvent.eDuration, "eFrequency": state.esEvent.eFrequency, "eText": state.esEvent.eText, "eType": state.esEvent.eType]
            		def sendingTo
                    childApps.each { child ->
            			sendingTo = child.label
                		if(child.label == "Reminders") {
                			outputTxt = child.profileEvaluate(data)
						}
                     }
					state.pendingConfirmation = true
				}
                pContCmds = state.pContCmds
                state.esEvent = [:]
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
            }
            else {
				def missingField = !state.esEvent.eText ? "What is the event?" : !state.esEvent.eStartingDate ? "Starting on what date?" : !state.esEvent.eStartingTime ? "Starting at what time?" : !state.esEvent.eCalendar ? "Which calendar?" : !state.esEvent.eDuration ? "For fow long?" : null
				log.warn "missingField = $missingField"
            	outputTxt = missingField
				pContCmdsR = "feedback" 
        	}
        }
		else {
        	if(state.pendingConfirmation == true){
            	outputTxt = "Ok, I am here when you need me"
				state.pendingConfirmation = false
            }
            else outputTxt = "Ok, canceling"
            	state.esEvent = [:]
				pContCmds = false
            	return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 
    	}
    } 
    else {
		if (messageType == "reminder" && state.esEvent.eStartingDate && state.esEvent.eStartingTime && state.esEvent.eText){
			outputTxt = "Ok, scheduling reminder to $state.esEvent.eText on $state.esEvent.eStartingDate at $state.esEvent.eStartingTime, is that correct?"
			pContCmdsR = "feedback"
    	}
        else {
    		outputTxt = "Sorry, I didn't get that"
    		pTryAgain = true 
   		}
    }
	if(state.pendingConfirmation == true){
    	if(rProfile != "undefined" && rProfile != null) {
    		outputTxt = "Ok, forwarding reminder to $rProfile"
		}
    	else state.pendingConfirmation = false
	}
    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN] 


} 
/*catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    } 
}	*/
/***********************************************************************************************************************
    MISC. - RUN REPORT FROM PROFILE
***********************************************************************************************************************/
def runReport(profile) {
def result
           		childApps.each {child ->
                        def ch = child.label
                		if (ch == profile) { 
                    		if (debug) log.debug "Found a profile, $profile"
                            result = child.runProfile(ch)
						}
            	}
                return result
}
/***********************************************************************************************************************
    MISC. - LAST MESSAGE HANDLER
***********************************************************************************************************************/
private getLastMessageMain() {
	def outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime
    return  outputTxt 
}
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
/*****************************************************************
  Facebook Messenger
 *****************************************************************/
def fbMessageEvent(fbResponseTxt){
    def fbMessage = "${fbResponseTxt}"
	log.info "This is what was sent to this method: '${fbMessage}'"
     if (fbMessage.startsWith("what")){
   /*  	def fbMessageChildName
           		childApps.each {child ->
                        def ch = child.label
                        	ch = ch.replaceAll("[^a-zA-Z0-9 ]", "")
                		if (ch.toLowerCase() == fbMessage?.toLowerCase()) { 
                    		if (debug) log.debug "Found a profile"
                            fbMessageChildName = child.label
                            def dataSet = [ptts:ptts, pintentName:pintentName] 
                            def childRelease = child?.checkRelease()
                            log.warn "childRelease = $childRelease"
                            outputTxt = child.runProfile(fbMessageChildName)
						}
            	}*/
                def data = [fbResponseTxt:fbResponseTxt] 
         //       return data
                feedbackHandler(fbResponseTxt)
          //      }
 //          	childApps?.each {child ->
 //           if (child?.label.toLowerCase() == fbMessage?.toLowerCase()) 
 //          		fbMessageChild = child?.checkState()  
    //        }
   //        log.info "The child app is " + "${outputTxt}"
    //   	}
        log.info "This worked"
        }
    else {
        log.info "This was a failure"
        }
    }  
        //if Alexa is muted from the child, then mute the parent too / MOVED HERE ON 2/9/17
//        pContinue = pContinue == true ? true : state.pMuteAlexa == true ? true : pContinue
//		return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	     
//	}
    
def messengerGetHandler(){
    if (params.hub.mode == 'subscribe' && params.hub.verify_token == settings.verifyToken) {
        log.debug "Validating webhook"
        render contentType: "text/html", data: params.hub.challenge, status: 200
    } else {
        log.debug "Failed validation. Make sure the Verify Tokens match."
        render contentType: "text/html", data: params.hub.challenge, status: 403        
    }
}

def messengerPostHandler(){
    def fbJSON = request.JSON
    def fbResponseTxt = "${fbJSON.entry.messaging.message.text[0][0]}" //"Sorry I don't know you."
    def fbMessage = "${fbJSON.entry.messaging.message.text[0][0]}"
    log.info "${fbMessage}"
    if (debug) log.debug "FB MESSENGER POST EVENT - MESSAGE:  ${fbJSON.entry.messaging.message.text[0][0]}"
    if (debug) log.debug "FB MESSENGER POST EVENT - SENDER_ID:  ${fbJSON.entry.messaging.sender.id[0][0]}"
	if(fbAllowedUsers != null && fbAllowedUsers.indexOf("${fbJSON.entry.messaging.sender.id[0][0]}") >= 0){ 
    fbResponseTxt = "${fbJSON.entry.messaging.message.text[0][0]}" as String
    // The above is my code so wont work for you you will need to set response Txt whatever value you want to return in the FB chat in response to a message
    }

fbMessageEvent(fbResponseTxt)
//    fbSendMessage(fbJSON.entry.messaging.sender.id[0][0], responseTxt)
}

def fbSendMessage(userid, message){
	// Send a message to FB
    def params = [
        uri: "https://graph.facebook.com/v2.6/me/messages?access_token=$fbAccessToken",
        body: [recipient: [id: userid], message: [text: message] ]
    ]
    try { httpPostJson(params) { resp ->
    	resp.headers.each { log.debug "${it.name} : ${it.value}" }
        if (debug) log.debug "response contentType: ${resp.contentType}"
    } } catch (e) { if (debug) log.debug "something went wrong: $e" }
}

/*
		// This is used for linking to FB messenger and setting valid users
		section ("Facebook Messenger Settings") { 
            input "verifyToken", "password", title: "Verify Token", description:"", required: false
            input "fbAccessToken", "password", title: "FB Page Access Token", description:"", required: false
            input "fbAllowedUsers", "text", title: "Allowed User IDs", description:"", required: false
            paragraph "Verify Token: Used to setup link to FB bot (You make up this value)\nAccess Token: To allow ST to send messages via FB Messenger (From developers.facebook.com)\nAllowed Users: These are the Facebook user IDs, seperated by commas (You can get this in the debug logging)"
        }
        
        // This you should put in the same method you use for returning the app ID and OAuth 
        log.trace "URL FOR USE AT DEVELOPERS.FACEBOOK.COM:\n${getApiServerUrl()}/api/smartapps/installations/${app.id}/m?access_token=${state.accessToken}"
*/



/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
X 																											X
X                       					UI FUNCTIONS													X
X                        																					X
/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
/************************************************************************************************************
   		UI - Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = app.label // Parent Name
}
private def textLicense() {
	def text =
	"Licensed under the Apache License, Version 2.0 (the 'License'); "+
	"you may not use this file except in compliance with the License. "+
	"You may obtain a copy of the License at"+
	" \n"+
	" http://www.apache.org/licenses/LICENSE-2.0"+
	" \n"+
	"Unless required by applicable law or agreed to in writing, software "+
	"distributed under the License is distributed on an 'AS IS' BASIS, "+
	"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
	"See the License for the specific language governing permissions and "+
	"limitations under the License."
}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
//	Naming Conventions: 
// 	description = pageName + D (E.G: description: mIntentD())
// 	state = pageName + S (E.G: state: mIntentS(),
/************************************************************************************************************/       
/** General Settings Page **/
def mSettingsS() {
    def result = ""
    if (ShowLicense || debug) {
    	result = "complete"	
    }
    result
}
def mSettingsD() {
    def text = "Tap here to Configure"
    if (ShowLicense || debug) { 
            text = "Configured"
    }
    text
}
/** Configure Profiles Pages **/
def mRoomsS(){
    def result = ""
    if (childApps?.size()) {
    	result = "complete"	
    }
    result
}
def mRoomsD() {
    def text = "No Profiles have been configured. Tap here to begin"
    def ch = childApps?.size()     
    if (ch == 1) {
        text = "One profile has been configured. Tap here to view and change"
    }
    else {
    	if (ch > 1) {
        text = "${ch} Profiles have been configured. Tap here to view and change"
     	}
    }
    text
}