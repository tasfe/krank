/**
 * 
 */
package com.arcmind.codegen

import javax.swing.*
import groovy.swing.SwingBuilder
import java.awt.BorderLayout


/**
 * @author richardhightower
 *
 */
public class GeneratorSwingApp{

	JFrame mainFrame
	CodeGenMain main = new CodeGenMain()
	SwingBuilder swing = new SwingBuilder()
	JLabel status
	Action viewConsole
	Action hideConsole
	JTextArea console
	List<Action> viewActions
	List<Action> mainActions
	List<Action> fileActions
	
	boolean debug = true
	
	def println(String message) {
		console.append(message + "\n")
	}
	
	Closure printlnClosure = { String message ->
		console.append(message + "\n")
	}

	public void exit() {
		System.exit(0)
	}
	
	public void showConsole() {
		println "Show console"
		hideConsole.enabled=true
		viewConsole.enabled=false
		console.show()
		console.setSize(500, 500)
		
	}
	
	public void closeConsole() {
		println "Close console"
		viewConsole.enabled=true
		hideConsole.enabled=false
		console.hide()
		mainFrame.invalidate()
		mainFrame.repaint()
	}
		
	public GeneratorSwingApp() {
		main.readProperties()
		main.configureCollaborators()

		mainFrame=
			  swing.frame(title:'CodeGen Code Generator', size:[300,300], defaultCloseOperation:JFrame.EXIT_ON_CLOSE,  show:true) {
			  fileActions = actions() {
				  action(name: "Exit", mnemonic: 'X', closure: { exit() })
			  }
			  mainActions = actions() {
				   action(name: "Reverse DB", mnemonic: 'R', closure: {use(StringCategory){main.reverseDB()}})
				   action(name: "Generate Java", mnemonic: 'G', closure: {use(StringCategory){main.generateJavaClasses() }})
			       action(name: "Write XML", mnemonic: 'W', closure: {use(StringCategory) {main.writeXML() }})
			       action(name: "Read XML", mnemonic: 'C', closure: {use(StringCategory) {main.readXML() }})
			       action(name: "Save Properties", mnemonic: 'S', closure: {use(StringCategory){main.writeProperties()}})
			       action(name: "Modify Properties", mnemonic: 'o', closure: {  })				  
			  }
			  viewActions = actions () {
			       viewConsole = action(name: "View Console", mnemonic: 'V', closure: { showConsole() })
			       hideConsole = action(name: "Hide Console", mnemonic: 'H', closure: { closeConsole() })
			  }
			  menuBar() {
				    menu(text: "File", mnemonic: 'F') {
				    	fileActions.each {menuItem(it)}				    	
				    }
				    menu (text: "Main", mnemonic: 'M') {
				    	mainActions.each {menuItem(it)}				    	
				    }
				    menu (text: "Window") {
				    	viewActions.each {menuItem(it)}
				    }
			  }

			  
			  status = label(text:"Welcome to CodeGen",
		                      constraints: BorderLayout.NORTH)
		      console = textArea(constraints: BorderLayout.CENTER, size:[500, 500])
		}
		
		hideConsole.enabled=false
		console.hide()
		console.append("Welcome to CodeGen... Let's get crack-ah-lackin!")
		
	}
	
	public static void main (String [] args) {
		GeneratorSwingApp app = new GeneratorSwingApp()
	}
	
	
}
