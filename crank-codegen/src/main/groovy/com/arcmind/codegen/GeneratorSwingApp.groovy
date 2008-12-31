/**
 * 
 */
package com.arcmind.codegen

import javax.swing.*
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.FlowLayout
import javax.swing.event.TreeSelectionEvent



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
	JavaClass currentClass
	JavaProperty currentProperty
	Relationship currentRelationship
	JPanel classPane
	JPanel propertyPane
	JPanel relationshipPane
	ClassEditSupport classEditSupport
	
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
		mainTabPane.setSelectedComponent(consolePane)
	}
	
	public void closeConsole() {
		println "Close console"
		viewConsole.enabled=true
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
	}
	
	public void clearConsole() {
		console.text = ""
	}
	
	public void reverseDB() {
		main.reader.tables=[]
		main.modelGen.classes=[]
		
		use(StringCategory){
			main.reverseDB()
		}
		
		tableTreeModel.setTables(main.reader.tables)
		classTreeModel.setClasses(main.modelGen.classes)

	}
	
	def selecteProperty(JavaProperty property) {
		mainTabPane.addTab("Property", propertyPane)
		mainTabPane.setSelectedComponent(propertyPane)
		currentProperty = property
		
	}
	def selecteClass(JavaClass javaClass) {
		mainTabPane.addTab("Class", classPane)
		mainTabPane.setSelectedComponent(classPane)
		currentClass = javaClass
		classEditSupport.populateForm(currentClass)
		
	}
	def selecteRelationship(Relationship relationship) {
		mainTabPane.addTab("Relationship", relationshipPane)
		mainTabPane.setSelectedComponent(relationshipPane)
		currentRelationship = relationship
		
	}

	public void treeClassSelected(TreeSelectionEvent event) {
		setStatus ""
		
		/* close the tab that was open. */
		if (currentClass!=null) {
			mainTabPane.remove(classPane)
		} else if (currentProperty!=null) {
			mainTabPane.remove(propertyPane)
		} else if (currentRelationship!=null) {
			mainTabPane.remove(relationshipPane)
		}
		/* Reset last selected item. */
		currentClass = null
		currentProperty = null
		currentRelationship = null
		
		/* Select the new item. */
		Object selectedItem = event.path.lastPathComponent
		if (selectedItem instanceof JavaProperty) {
			selecteProperty(selectedItem)
		} else if (selectedItem instanceof JavaClassHolder) {
			selecteClass(selectedItem.javaClass)
		} else if (selectedItem instanceof Relationship) {
			selecteRelationship(selectedItem)
		} else {
			println "You selected ${selectedItem} of type ${selectedItem.class.name}"
		}
	}
		
	def setStatus(String msg){  
		status.setText("  " + msg)
	}
	
	public GeneratorSwingApp() {
		
		/* Initialize edit support for class, property and relationships. */
		classEditSupport = new ClassEditSupport(classTreeModel:classTreeModel)

		mainFrame=
        swing.frame(title:'CodeGen Code Generator', size:[1200,1000], defaultCloseOperation:JFrame.EXIT_ON_CLOSE,  show:true) {
            fileActions = actions() {
                action(name: "Exit", mnemonic: 'X', closure: { exit() })
            }
            
            Closure handleGenerateJavaAction = {
                	doOutside { //Runs in a seperate thread
                		edt {setStatus "Reverse engineering database please standby..."}
                		reverseDB()
                		edt {setStatus "Done reverse engineering database." }
                	}                		
            }
            
            mainActions = actions() {
                action(name: "Reverse DB", mnemonic: 'R', closure: handleGenerateJavaAction)
                action(name: "Generate Java", mnemonic: 'G', closure: {use(StringCategory){main.generateJavaClasses() }})
                action(name: "Write XML", mnemonic: 'W', closure: {use(StringCategory) {main.writeXML() }})
                action(name: "Read XML", mnemonic: 'e', closure: {use(StringCategory) {main.readXML() }})
                action(name: "Save Properties", mnemonic: 'S', closure: {use(StringCategory){main.writeProperties()}})
                action(name: "Modify Properties", mnemonic: 'o', closure: {  })
            }
            viewActions = actions () {
                viewConsole = action(name: "View Console", mnemonic: 'V', closure: { showConsole() })
                hideConsole = action(name: "Hide Console", mnemonic: 'H', closure: { closeConsole() })
                action(name: "Clear Console", mnemonic: 'l', closure: { clearConsole() })
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
                status = label(text:"  Welcome to CodeGen")
            }
			  

			  
            splitPane(constraints: BorderLayout.CENTER) {
                treeTabPane = tabbedPane(constraints: BorderLayout.WEST, preferredSize:[300,300]) {
                    scrollPane(title:"Tables", tabMnemonic: "T") { tree(model: tableTreeModel) }
                    scrollPane(title:"Classes", tabMnemonic: "C") {
                    	tree(model: classTreeModel, valueChanged: {treeClassSelected(it)}) 
                    }
                }
                mainTabPane = tabbedPane(constraints: BorderLayout.CENTER) {
                    consolePane = scrollPane (title:"Console", tabMnemonic: "C") {
                        console = textArea()
                    }
                    classPane = panel (title:"Class", tabMnemonic: "l") {
                    		flowLayout(alignment:FlowLayout.LEFT)
	                    	panel{
                    			boxLayout(axis:BoxLayout.Y_AXIS)
                    			label("Edit Class")
                    			label("", preferredSize:[20,20])
                    			panel {
                    				boxLayout(axis:BoxLayout.X_AXIS)
                    				label("Package", preferredSize:[100,20])
                    				classEditSupport.packageName = textField(preferredSize:[200,20])
                    				label(preferredSize:[100,20])
                    			}
                    			panel {
                    				boxLayout(axis:BoxLayout.X_AXIS)
                    				label("Class Name", preferredSize:[100,20])
                    				classEditSupport.className = textField(preferredSize:[125,20])
                    				label(preferredSize:[100,20])
                    			}
                    			panel {
                    				button(text:"Apply", actionPerformed: {classEditSupport.updateObject(this.currentClass)})
                    			}
	                    	}//panel
                    }//classPane
                    propertyPane = panel (title:"Property", tabMnemonic: "P") {
                    	label(constraints: BorderLayout.NORTH, text:"Edit Property details")
                    }
                    relationshipPane = panel (title:"Relationship", tabMnemonic: "R") {
                    	label(constraints: BorderLayout.NORTH, text:"Edit Relationship details")
                    }
                    
                }
            }
		      
			  


		}
		
		/* Initialize CodeGenMain. */
		CodeGenMain.metaClass.println = printlnClosure
		main = new CodeGenMain()
		main.readProperties()
		main.configureCollaborators()
		
		/* Initilize Console and mainTabPane. */
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
		mainTabPane.remove(relationshipPane)
		mainTabPane.remove(classPane)
		mainTabPane.remove(propertyPane)
		
		
	}
	
	public static void main (String [] args) {
		GeneratorSwingApp app = new GeneratorSwingApp()
	}
	
	
}

class ClassEditSupport {
	JTextField packageName
	JTextField className
	JavaClassTreeModel classTreeModel
	def updateObject (JavaClass cls) {
		println "update object was ${cls}"
		cls.packageName = packageName.text
		cls.name = className.text
		println "update object now ${cls}"
	}
	def populateForm (JavaClass cls) {
		packageName.text = cls.packageName
		className.text = cls.name
	}
}

