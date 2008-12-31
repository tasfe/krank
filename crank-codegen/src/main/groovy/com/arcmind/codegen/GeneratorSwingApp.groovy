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
	CodeGenMain main 
	SwingBuilder swing = new SwingBuilder()
	JLabel status
	Action viewConsole
	Action hideConsole
	JTextArea console
	List<Action> viewActions
	List<Action> mainActions
	List<Action> fileActions
	JTabbedPane treeTabPane
	JTabbedPane mainTabPane
	JScrollPane consolePane
	DBTableTreeModel tableTreeModel = new DBTableTreeModel()
	JavaClassTreeModel classTreeModel = new JavaClassTreeModel()
	
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
		mainTabPane.addTab("Console", consolePane)
	}
	
	public void closeConsole() {
		println "Close console"
		viewConsole.enabled=true
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
	}
	
	public void reverseDB() {
		use(StringCategory){
			main.reverseDB()
		}
		tableTreeModel.setTables(main.reader.tables)
		classTreeModel.setClasses(main.modelGen.classes)
	}
		
	public GeneratorSwingApp() {


		mainFrame=
        swing.frame(title:'CodeGen Code Generator', size:[1000,1000], defaultCloseOperation:JFrame.EXIT_ON_CLOSE,  show:true) {
            fileActions = actions() {
                action(name: "Exit", mnemonic: 'X', closure: { exit() })
            }
            mainActions = actions() {
                action(name: "Reverse DB", mnemonic: 'R', closure: { reverseDB() })
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
			  
            toolBar (constraints: BorderLayout.PAGE_START) {
                fileActions.each {button(it)}
                mainActions.each {button(it)}
                viewActions.each {button(it)}
            }
			  
            status = label(text:"Welcome to CodeGen",
                constraints: BorderLayout.NORTH)

			  
            splitPane() {
                treeTabPane = tabbedPane(constraints: BorderLayout.WEST, preferredSize:[300,300]) {
                    scrollPane(title:"Tables", tabMnemonic: "T") { tree(model: tableTreeModel) }
                    scrollPane(title:"Classes", tabMnemonic: "C") { tree(model: classTreeModel) }
                }
                mainTabPane = tabbedPane(constraints: BorderLayout.CENTER) {
                    consolePane = scrollPane (title:"Console", tabMnemonic: "C") {
                        console = textArea()
                    }
                }
            }
		      
			  


		}
		
		CodeGenMain.metaClass.println = printlnClosure
		main = new CodeGenMain()
		main.readProperties()
		main.configureCollaborators()
		
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
		
	}
	
	public static void main (String [] args) {
		GeneratorSwingApp app = new GeneratorSwingApp()
	}
	
	
}
