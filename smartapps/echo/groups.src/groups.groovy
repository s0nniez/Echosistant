/**
 *  Groups
 *
 *  Copyright 2017 Bobby Dobrescu
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
 
definition(
    name: "Groups",
    namespace: "Echo",
    author: "Bobby Dobrescu",
    description: "EchoSistant Add-on",
    category: "My Apps",
	parent: "Echo:EchoSistantLabs",
    iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")

preferences {
    page name:"groupSetup"
}

page name: "groupSetup"
def groupSetup() {
		dynamicPage(name: "groupSetup", title: "",install: true, uninstall: true,submitOnChange: true) {
        
		section([title:"Options", mobileOnly:true]) {
			label title:"Name your Group", required:true
		}
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
    
    def installed() {
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
	app.updateLabel(sceneName)
}

def getGroupData() {
	return new groovy.json.JsonBuilder(settings)
}