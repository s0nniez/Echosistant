/**
 *  Shortcuts
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
    name: "Shortcuts",
    namespace: "Echo",
    author: "Bobby Dobrescu",
    description: "EchoSistant Add-on",
    category: "Convenience",
	parent: "Echo:EchoSistantLabs",
    iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")

preferences {
    page name:"shortcutSetup"
}

page name: "shortcutSetup"
def shortcutSetup() {
	dynamicPage(name: "shortcutSetup", title: "",install: true, uninstall: true,submitOnChange: true) {
        
		section() {
			label title:"Name your Shortcut", required:true
		}
        section("Phrase") { //, hideWhenEmpty: true
            input "shortcut", "text", title: "When I say: Alexa...", description: "text", required: "false"
			paragraph "Example text: I am reading in the bedroom"
        }
        section("Action") { //, hideWhenEmpty: true
            input "action", "text", title: "...I want EchoSistant to perform this action", required: "false"
			paragraph "Example action: Set Bedroom Lamp to 80%"
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
	//app.updateLabel(sceneName)
}