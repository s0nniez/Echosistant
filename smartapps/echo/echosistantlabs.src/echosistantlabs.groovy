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
    def text = "R.5.0.1d"
}
/**********************************************************************************************************************************************/
preferences {   
    page name: "mainParentPage"
    page name: "profiles"
    page name: "settings"
	page name: "mailbox"
    page name: "tokens"
    page name: "tConfirmation"            
    page name: "tTokenReset"
    page name: "dashboard"
    page name: "weather"

}            
//dynamic page methods
page name: "mainParentPage"
def mainParentPage() {	
    dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
        section ("") { 
            href "profiles", title: "Configure Profiles", description: mRoomsD(), state: mRoomsS(),
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
            href "settings", title: "General Settings", description: mSettingsD(), state: mSettingsS(),
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"                               
        }     
		section ("Echo Dashboard", hideable: true, hidden: false) { 
                def shmLocation = location.currentState("alarmSystemStatus")?.value
                def shmStatus = shmLocation == "off" ? "Disarmed" : shmLocation == "away" ? "Armed (Away)" : shmLocation == "stay" ? "Armed (Stay)" : null
                def cont = sFeedback == "Disable" ?  "Alexa feedback is disabled" : sFeedback == "None" ? "Alexa is set to keep the session open, but does not provide audio feedback" : "Alexa is set to provide ${sFeedback?.toLowerCase()}"
                def conv = sConversation ? "Conversation mode is enabled" : "Conversation mode is disabled"
                if (mSettings) {
                        paragraph "Settings:\n\t${cont}\n\t${conv}"
                }
                if (mLocation) paragraph 	"Location:\n\tCurrent Mode: ${location.currentMode} \n"  +
                    						"\tSmart Home Monitor: ${shmStatus}"
                if (mLocalWeather) paragraph "Weather:\n\t${state.todayWeather}"
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
                href "mailbox", title: "Echo Mailbox", description: "Tap here to check your mailbox", state: complete
                href "dashboard", title: "Tap here to configure Dashboard", description: "", state: complete
            }
        }
    }
page name: "profiles"    
def profiles() {
    dynamicPage (name: "profiles", title: "", install: true, uninstall: false) {
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
page name: "settings"  
def settings(){
    dynamicPage(name: "settings", uninstall: true) {
        section ("Directions, How-to's, and Troubleshooting") { 
            href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "Tap to go to the EchoSistant Wiki", description: none,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/wiki.png"
            input "debug", "bool", title: "Enable Debug Logging", default: true, submitOnChange: true 

        } 
		section ("Alexa Voice Global Settings") {            
			input "sFeedback", "enum", title: "Alexa Feedback Settings", required: false, defaultValue: "Default Answers" , submitOnChange: true, 
           		options:  ["Default Answers","Short Answers","None", "Disable"]
			input "sConversation", "bool", title: "Disable Conversation Mode", required: false, defaultValue: false

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
            href "tokens", title: "Revoke/Reset Security Access Token", description: none
        }
        section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){
        }	
    }             
}
page name: "tokens"
def tokens(){
    dynamicPage(name: "tokens", title: "Security Tokens", uninstall: false){
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
            href "tConfirmation", title: "Reset Access Token and Application ID", description: none
        }
    }
} 
page name: "tConfirmation"
def tConfirmation(){
    dynamicPage(name: "tConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
        section {
            href "tTokenReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
            paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
                "You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
                "Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
        }
        section(" "){
            href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
        }
    }
}
page name: "tTokenReset"
def tTokenReset(){
    dynamicPage(name: "tTokenReset", title: "Access Token Reset", uninstall: false){
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
page name: "mailbox"
def mailbox(){
    dynamicPage(name: "mailbox", uninstall: false) {
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
page name: "dashboard"
def dashboard(){
    dynamicPage(name: "dashboard", uninstall: false) {
        section ("Settings") {
            input "mSettings", "bool", title: "Display EchoSistant Settings", required: false, default: false, submitOnChange: true
        }
		section ("Location") {
            input "mLocation", "bool", title: "Display Location Status", required: false, default: false, submitOnChange: true
        }
		section ("Local Weather") {
            input "mLocalWeather", "bool", title: "Display local weather conditions", required: false, default: false, submitOnChange: true
        }
        if (mLocalWeather) {
            section ("Local Weather Information") {
                href "weather", title: "Tap here to configure Weather information on Dashboard", description: "", state: complete
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
def weather() {
    dynamicPage(name: "weather", title: "Weather Settings") {
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
    path("/t") { action: [GET: "processTts"] }
    path("/update") { action: [GET: "getAppSettings"] }
}
/************************************************************************************************************
Base Process
************************************************************************************************************/
def installed() {
    if (debug) log.debug "Installed with settings: ${settings}"
    state.ParentRelease = release()
}
def updated() { 
    if (debug) log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}
def initialize() {
	//webCoRE
    webCoRE_init()
	sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
	state.esProfiles = state.esProfiles ? state.esProfiles : []
    subscriptions()
    //GENERATE TOKEN
    if (!state.accessToken) {
        if (debug) log.error "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
        OAuthToken()
    }
    state.todayWeather = state.todayWeather ?: mGetWeather() //WEATHER UPDATES for DASHBOARD
    state.activeAlert = state.activeAlert ?: mGetWeatherAlerts() //WEATHER UPDATES for DASHBOARD
    state.lambdaReleaseTxt = "Not Set"
    state.lambdaReleaseDt = "Not Set" 
    state.lambdatextVersion = "Not Set"
    //Alexa Responses
    state.pTryAgain = false
    state.pContCmds = settings.sConversation == false ? true : settings.sConversation == true ? false : true
    state.pFeedback = sFeedback
    state.pContCmdsR = "init"       
    //Other Settings
    state.pendingConfirmation = false
    //log.debug getAllSettings().toString().replaceAll(/\b(\w+\S)\b/, '"$1"').replace('" "',' ')
    //':{deviceType:{' + getSettings().toString().replaceAll(/^\[|\]$/, '') + '},groups:{' + getChildApps().collect{group -> group.label + ':{deviceType:{' + group.getSettings().toString().replaceAll(/^\[|\]$/, '') + '}}'}.toString().replaceAll(/^\[|\]$/, '') + '}}'
    //log.debug getSettings().toString()
    //log.debug getAllSettings()//.replaceAll(', ',',').replaceAll(/\b(\w+\s*)\b/, '"$1"').replace('""','') //.replaceAll(/\b(\w+\S)\b/, '"$1"').replace('" "',' ')
}

def subscriptions(){
	subscribe(app, appHandler)
    subscribe(location, locationHandler)
    subscribe(location, "remindR", remindrHandler) //used for running ES Profiles from RemindR app
}
/*************************************************************************/
/* webCoRE Connector v0.2                                                */
/*************************************************************************/
/*  Initialize using webCoRE_init()                                      */
/*  Optionally, pass the string name of a method to call when a piston   */
/*  is executed: webCoRE_init('pistonExecutedMethod')                    */
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

def getProfileList(){
		def cList = getChildApps()*.label
        log.info "cList = $cList"
        state.esProfiles = null
		state.esProfiles = state.esProfiles ? state.esProfiles + cList :  cList 
        //return getChildApps()*.label
		if (debug) log.debug "Refreshing Profiles for CoRE, $state.esProfiles" //${getChildApps()*.label}"
        return state.esProfiles
}
def childUninstalled() {
	if (debug) log.debug "Profile has been deleted, refreshing Profiles for CoRE, ${getChildApps()*.label}"
    sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
} 
/************************************************************************************************************
	RemindR Events
************************************************************************************************************/
def remindrHandler(evt) {
    if (!evt) return
    log.warn "received event from RemindR with data: $evt.data"
    switch (evt.value) {
        case "refresh":
        state.esProfiles = evt.jsonData && evt.jsonData?.profiles ? state.esProfiles + evt.jsonData.profiles : state.esProfiles ? state.esProfiles : []
        break
    }
}
/************************************************************************************************************
	App Touch and Alexa Responses
************************************************************************************************************/
def appHandler(evt) {
    toggleContCommands()
    log.debug "app event ${evt.name}:${evt.value} received"
}
def toggleContCommands() {
	if(state.pFeedback) state.pFeedback = false
    else state.pFeedback = true
}

def checkToken() {
	def parentToken = [:]
    parentToken.appId = app.id
    parentToken.token = state.accessToken
    return parentToken
}

def getAppSettings() {
	log.debug "Settings Updated"
	def profileSettings = []
    getChildApps().each{profile ->
    	profileSettings << ["${profile.label}":profile.getProfileSettings()]
    }
    def mainESSettings = []
    settings.each{k,d ->
    	mainESSettings << ["${k}":d]
    }
    def allSettings = ["EchoSistant": mainESSettings] + ["profiles" :profileSettings]
    return [contentType: "application/json", allESSettings: allSettings]
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
    def pContinue = state.pFeedback
    def pShort = false //state.pShort
    def String outputTxt = (String) null 
    state.pTryAgain = false
    if (debug) log.debug "^^^^____LAUNCH REQUEST___^^^^" 
    if (debug) log.debug "Launch Data: (event) = '${event}', (Lambda version) = '${versionTxt}', (Lambda release) = '${releaseTxt}', (ST Main App release) = '${releaseSTtxt}'"
    //try {
    // >>> NO RESPONSE <<<< 
    if (event == "noAction") log.debug "No response"
    // >>> NO Intent <<<<    
    if (event == "AMAZON.NoIntent") log.debug " No intent used"
    // >>> YES Intent <<<<     
    if (event == "AMAZON.YesIntent") log.debug " Yes intent used"
    
    if (debug){
        log.debug "Begining Process data: (event) = '${event}', (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
            "; data sent: pContinue = '${pContinue}', pShort = '${pShort}',  pPendingAns = '${pPendingAns}', versionSTtxt = '${versionSTtxt}', releaseSTtxt = '${releaseSTtxt}' outputTxt = '${outputTxt}' ; "+
            "other data: pContCmdsR = '${state.pContCmdsR}', pinTry'=${state.pinTry}' "
    }
    
    //def pProfileData = "{\"Profile\":{\"Dear\":[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"},\"groups\" :{\"Basement\":[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"}}],\"Master\" :[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"}}]}}],\"Home\" :[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"},\"groups\" :{\"Living Room\":[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"}}],\"Office\" :[{\"devices\":{\"light\":\"Desk Lamp\",\"fan\":\"Living Room Fan\"}}]}}]}}"

    return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt, "profileData": URLEncoder.encode(getAppSettings())]	 

} 
/*catch (Throwable t) {
log.error t
outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
state.pTryAgain = true
return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
}
}   */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Could possibly updated these lists from a website
String getWeatherWords() 	{"get,tonight,weather,temperature,forecast,humidity,rain,wind,humidity"}
String getFeedBackWords() 	{"give,for,tell,what,how,is,when,which,are,how many,check,who"}
String getStopWords() 		{"no,stop,cancel,kill it,zip it,thank"}

//New Word Parsing Functions. 
String parseWordReturn(String input, String fromList) {
    return fromList.split(",").find({input.contains(it)})
}
Boolean parseWordFound(String input, String fromList) {
    return fromList.split(",").find({input.contains(it)}) ? true : false
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/************************************************************************************************************
   TEXT TO SPEECH PROCESS PARENT - Lambda via page t
************************************************************************************************************/
def processTts() {
    //LAMBDA VARIABLES
    def pShort = false
    def ptts = params.ttstext 
    def pintentName = params.intentName
    //OTHER VARIABLES
    def String outputTxt = (String) null 
    def String pContCmdsR = (String) null
    def pContCmds = false
    def pTryAgain = false
    def pPIN = false
    def dataSet = [:]
    if (debug) log.debug "Messaging Profile Data: (ptts) = '${ptts}', (pintentName) = '${pintentName}' state.pContCmdsR = ${state.pContCmdsR}"
    pContCmdsR = "profile"
    def tProcess = true
    //try {
    if (ptts == "this is a test"){
        outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This needs to be fixed... 
    if(parseWordFound(ptts, stopWords) && state.pContCmdsR != "wrongIntent"){
            outputTxt = "ok, I am here if you need me"
            pContCmds = false
            return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]      
    } else {
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
				def pResponse = child.processText(dataSet)
				log.info "child.profileMessagingEvaluate executed from the main at line 503"
				outputTxt = pResponse.outputTxt
                pContCmds = pResponse.pContCmds
                pContCmdsR = pResponse.pContCmdsR
                pTryAgain = pResponse.pTryAgain
			}
        }
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
	if (outputTxt){
		return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
  	}
	else {
		if (state.pShort != true){outputTxt = "I wish I could help, but EchoSistant couldn't find a Profile named " + pintentName + " or the command may not be supported"}
        else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "} 
  	pTryAgain = true
    return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":pShort, "pContCmdsR":pContCmdsR, "pTryAgain": pTryAgain, "pPIN":pPIN]
 	}
} 
/*catch (Throwable t) {
log.error t
outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
state.pTryAgain = true
return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
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