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
	SettingsTreeModel settingsTreeModel = new SettingsTreeModel()
	JavaClass currentClass
	JavaProperty currentProperty
	Relationship currentRelationship
	JDBCSettings currentSettings
	JPanel classPane
	JPanel propertyPane
	JPanel relationshipPane
	JPanel codeGenMainPane
	JPanel settingsPane
	ClassEditSupport classEditSupport
	JavaPropertyEditSupport propertyEditSupport
	RelationshipEditSupport relationshipEditSupport
	CodeGenMainEditSupport codeGenMainEditSupport
	SettingsEditSupport settingsEditSupport;

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

	public void readXML() {
		main.reader.tables=[]
		main.modelGen.classes=[]
		use(StringCategory) {
			main.readXML()
		}
		tableTreeModel.setTables(main.reader.tables)
		classTreeModel.setClasses(main.modelGen.classes)
	}

	def modifyProperties () {
		mainTabPane.addTab("Modify Properties", codeGenMainPane)
		mainTabPane.setSelectedComponent(codeGenMainPane)
		codeGenMainEditSupport.populateForm(main)
		main.configureCollaborators()
	}
	def selecteProperty(JavaProperty property) {
		mainTabPane.addTab("Property", propertyPane)
		mainTabPane.setSelectedComponent(propertyPane)
		currentProperty = property
		propertyEditSupport.populateForm(property)

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
		relationshipEditSupport.populateForm(relationship)
	}
	
	def addDataSource() {
		//todo
		JLabel labelUrl = new JLabel("URL");
		JLabel labelUserName = new JLabel("Username:");
		JLabel labelPassword = new JLabel("Password:");
		JLabel labelDriver = new JLabel("Driver:");
		JTextField url = new JTextField();
		JTextField userName = new JTextField();
		JTextField password = new JTextField();
		
		Object[] ob=[new JLabel("You MUST fill all fields!"),labelUrl,url,labelUserName,userName,
		             labelPassword, password, labelDriver]
		String drv = JOptionPane.showInputDialog(ob);
		
		if (debug) {
			printlnClosure """Data source added: url:${url.text},username:${userName.text}, 
password: ${password.text}, driver: ${drv}"""
		}
		
		if (drv) {
		
			main.dataSourceReader.settings << 
			new JDBCSettings(
					url: url.text,
					userName: userName.text,
					password: password.text,
					driver: drv)
			
			main.writeDataSourceXML()
			updateJDBCTree()
		}
	}
	
	def selectDataSource(JDBCSettings settings) {
		mainTabPane.addTab("JDBC Settings", settingsPane)
		mainTabPane.setSelectedComponent(settingsPane)
		currentSettings = settings
		settingsEditSupport.populateForm(settings)		
	}
	
	public void treeTableSelected(TreeSelectionEvent event) {
		setStatus ""
		mainTabPane.addTab("Modify Properties", codeGenMainPane)
		mainTabPane.selectedComponent = codeGenMainPane
		codeGenMainEditSupport.populateForm(main)
		main.configureCollaborators()
		
		String tableName = event.path.lastPathComponent.table.name
		List list = main.tableNames.split(',')
		list = list - tableName
		list = list - ""
		list << tableName
		main.tableNames=list.join(',')
		codeGenMainEditSupport.tableNames.text = main.tableNames
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
	
	//todo
	def treeSettingsSelected(TreeSelectionEvent event) {
		setStatus ""
		
		Object selectedItem = event.path.lastPathComponent
		
		if (selectedItem instanceof SettingsHolder) {
			selectDataSource(selectedItem.settings)
		}
	}	

	def setStatus(String msg){
		status.setText("  " + msg)
	}

	public GeneratorSwingApp() {

		/* Initialize edit support for class, property and relationships. */
		classEditSupport = new ClassEditSupport(classTreeModel:classTreeModel)
		propertyEditSupport = new JavaPropertyEditSupport(classTreeModel:classTreeModel)
		relationshipEditSupport = new RelationshipEditSupport(classTreeModel:classTreeModel)
		codeGenMainEditSupport = new CodeGenMainEditSupport()
		settingsEditSupport = new SettingsEditSupport()

		buildGUI ()

		/* Initialize CodeGenMain. */
		CodeGenMain.metaClass.println = printlnClosure
		main = new CodeGenMain()
		main.readProperties()
		main.configureCollaborators()
		main.initDataSource()
		
		// Update DataSources Tree
		updateJDBCTree()

		/* Initilize Console and mainTabPane. */
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
		mainTabPane.remove(relationshipPane)
		mainTabPane.remove(classPane)
		mainTabPane.remove(propertyPane)
		mainTabPane.remove(codeGenMainPane)

	}
	private updateJDBCTree() {
		settingsTreeModel.setSettings(main.dataSourceReader.settings)
	}

	public static void main (String [] args) {
		GeneratorSwingApp app = new GeneratorSwingApp()
	}

	/* This method is very long, but it uses a builder to layout the complete GUI. It is layed out in a hierarchy so it is easy to read.
	 * This method contains no logic. It cust contains layout and event wiring.
	 */
	def buildGUI() {
		mainFrame=
        swing.frame(title:'CodeGen Code Generator', size:[1200,1000], defaultCloseOperation:JFrame.EXIT_ON_CLOSE,  show:true) {
            fileActions = actions() {
                action(name: "Exit", mnemonic: 'X', closure: { exit() })
            }

            Closure handleReverseDB = {
                doOutside { //Runs in a seperate thread
                    edt {setStatus "Reverse engineering database please standby..."}
                    reverseDB()
                    edt {setStatus "Done reverse engineering database." }
                }
            }

            Closure handleWriteXML = {
            	doOutside {
            		edt {setStatus "Writing XML file out ${main.persister.fileName}"}
            		main.writeXML()
            		edt {setStatus "Done writing XML file out ${main.persister.fileName}"}
            	}
            }

            Closure handleReadXML = {
                doOutside {
                    edt {setStatus "Reading XML file in ${main.persister.fileName}"}
                    readXML()
                    edt {setStatus "Done reading XML file in ${main.persister.fileName}"}
                }
            }

            Closure handleGenerateJavaClasses = {
                    doOutside {
                        edt {setStatus "Writing Java class files to ${main.codeGen.outputDir}"}
                        
                        use(StringCategory) {
                        	main.generateJavaClasses()
                        }
                        edt {setStatus "Done writing Java class files to ${main.codeGen.outputDir}"}
                    }
            }

            mainActions = actions() {
                action(name: "Reverse DB", mnemonic: 'R', closure: handleReverseDB)
                action(name: "Generate Java", mnemonic: 'G', closure: handleGenerateJavaClasses)
                action(name: "Write XML", mnemonic: 'W', closure: handleWriteXML)
                action(name: "Read XML", mnemonic: 'e', closure: handleReadXML)
                action(name: "Save Properties", mnemonic: 'S', closure: {use(StringCategory){main.writeProperties()}})
                action(name: "Modify Properties", mnemonic: 'o', closure: { modifyProperties() })
                action(name: "Add New Data Source", mnemonic: 'D', closure: { addDataSource() })
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
                    scrollPane(title:"Tables", tabMnemonic: "T") { 
                    	tree(model: tableTreeModel, valueChanged: {treeTableSelected(it)}) 
                    }
                    scrollPane(title:"Classes", tabMnemonic: "C") {
                        tree(model: classTreeModel, valueChanged: {treeClassSelected(it)})
                    }                    
                    scrollPane(title:"JDBC Settings", tabMnemonic: "J") {
                        tree(model: settingsTreeModel, valueChanged: {treeSettingsSelected(it)})
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
                        flowLayout(alignment:FlowLayout.LEFT)
                        panel{
                            boxLayout(axis:BoxLayout.Y_AXIS)
                            label("Edit Property")
                            label("", preferredSize:[20,20])
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Name", preferredSize:[100,20])
                                propertyEditSupport.propertyName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Size", preferredSize:[100,20])
                                propertyEditSupport.size = textField(preferredSize:[25,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                button(text:"Apply", actionPerformed: {propertyEditSupport.updateObject(this.currentProperty)})
                            }
                        }//panel
                    }//propertyPane
                    relationshipPane = panel (title:"Relationship", tabMnemonic: "R") {
                        flowLayout(alignment:FlowLayout.LEFT)
                        panel{
                            boxLayout(axis:BoxLayout.Y_AXIS)
                            label("Edit Relationship")
                            label("", preferredSize:[20,20])
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Name", preferredSize:[100,20])
                                relationshipEditSupport.relationshipName = textField(preferredSize:[200,20])
                                label(preferredSize:[300,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Type", preferredSize:[100,20])
                                DefaultComboBoxModel model = new DefaultComboBoxModel([RelationshipType.ONE_TO_ONE, RelationshipType.ONE_TO_MANY, RelationshipType.MANY_TO_MANY, RelationshipType.MANY_TO_ONE] as Object[])
                                relationshipEditSupport.type = comboBox(model:model)
                                label(preferredSize:[300,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Singular Name", preferredSize:[100,20])
                                relationshipEditSupport.singularName = textField(preferredSize:[200,20])
                                label("Only needed for XToMany", preferredSize:[300,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Ignore", preferredSize:[100,20])
                                panel (preferredSize:[200,26]) {
                                	relationshipEditSupport.ignore = checkBox()
                                }
                                label("If true not processesd",preferredSize:[300,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Bidirectional", preferredSize:[100,20])
                                panel (preferredSize:[200,26]) {
                                	relationshipEditSupport.bidirectional = checkBox()
                                }
                                label("Do both sides reference each other",preferredSize:[300,20])
                            }
                            panel {
                                button(text:"Apply", actionPerformed: {relationshipEditSupport.updateObject(this.currentRelationship)})
                            }
                        }//panel
                    }//relationshipPane
                    codeGenMainPane = panel (title:"Modify Properties", tabMnemonic: "M") {
                        flowLayout(alignment:FlowLayout.LEFT)
                        panel{
                            boxLayout(axis:BoxLayout.Y_AXIS)
                            label("Modify Properties")
                            label("", preferredSize:[20,20])
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("JDBC URL", preferredSize:[100,20])
                                codeGenMainEditSupport.url = textField(preferredSize:[500,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("JDBC Driver", preferredSize:[100,20])
                                codeGenMainEditSupport.driver = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("DB User Name", preferredSize:[100,20])
                                codeGenMainEditSupport.userName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("DB Password", preferredSize:[100,20])
                                codeGenMainEditSupport.password = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Table Names", preferredSize:[100,20])
                                codeGenMainEditSupport.tableNames = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Package Name", preferredSize:[100,20])
                                codeGenMainEditSupport.packageName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Output dir", preferredSize:[100,20])
                                codeGenMainEditSupport.outputDir = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Config dir", preferredSize:[100,20])
                                codeGenMainEditSupport.appConfigDir = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("XML File", preferredSize:[100,20])
                                codeGenMainEditSupport.xmlFileName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Properties File", preferredSize:[100,20])
                                codeGenMainEditSupport.propertiesFile = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Debug mode", preferredSize:[100,20])
                                codeGenMainEditSupport.debug = checkBox(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                button(text:"Apply", actionPerformed: {codeGenMainEditSupport.updateObject(this.main)})
                                button(text:"Save", actionPerformed: {codeGenMainEditSupport.updateObject(this.main); main.writeProperties()})
                                button(text:"Clear Tables", actionPerformed: {main.tableNames="";codeGenMainEditSupport.tableNames.text=""})
                                button(text:"Close", actionPerformed: {mainTabPane.remove(codeGenMainPane)})
                            }
                        }//panel
                    }//codeGenMainPane
                    
                    settingsPane = panel (title:"JDBC Settings", tabMnemonic: "J") {
                        flowLayout(alignment:FlowLayout.LEFT)
                        panel{
                            boxLayout(axis:BoxLayout.Y_AXIS)
                            label("Edit JDBC Settings")
                            label("", preferredSize:[20,20])
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("JDBC URL", preferredSize:[100,20])
                                settingsEditSupport.url = textField(preferredSize:[500,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("DB User Name", preferredSize:[100,20])
                                settingsEditSupport.userName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("DB Password", preferredSize:[100,20])
                                settingsEditSupport.password = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("JDBC Driver", preferredSize:[100,20])
                                settingsEditSupport.driver = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                button(text:"Save", actionPerformed: {settingsEditSupport.updateObject(this.currentSettings); main.writeDataSourceXML(); updateJDBCTree()})
                                button(text:"Close", actionPerformed: {mainTabPane.remove(settingsPane)})
                            }
                        }//panel
                    }//settingsPane
                    
                }//mainTabPane
            }//splitPane
        }//frame
	}//buildGUI()
}

class ClassEditSupport {
	JTextField packageName
	JTextField className
	JavaClassTreeModel classTreeModel
	GeneratorSwingApp app
	def updateObject (JavaClass cls) {
		app.println "update object was ${cls}"
		cls.packageName = packageName.text
		cls.name = className.text
		app.println "update object now ${cls}"
		classTreeModel.nodeChanged(cls)
	}
	def populateForm (JavaClass cls) {
		packageName.text = cls.packageName
		className.text = cls.name
	}
}

class JavaPropertyEditSupport {
	JTextField propertyName
	JTextField size
	JavaClassTreeModel classTreeModel
	GeneratorSwingApp app
	def updateObject (JavaProperty prp) {
		app.println "update object was ${prp}"
		prp.name = propertyName.text
		try {
			prp.size = Integer.parseInt(size.text)
		} catch (Exception ex) {
			prp.size = null
		}
		app.println "update object now ${prp}"
		classTreeModel.nodeChanged(prp)
	}
	def populateForm (JavaProperty prp) {
		propertyName.text = prp.name
		size.text = prp.size.toString()
	}
}


class RelationshipEditSupport {
	JTextField relationshipName
	JTextField singularName
	JComboBox type
	JavaClassTreeModel classTreeModel
	JCheckBox ignore
	JCheckBox bidirectional
	
	
	GeneratorSwingApp app
	
	def updateObject (Relationship rel) {
		app.println "update object was ${rel}"
		rel.name = relationshipName.text
		rel.type = (RelationshipType) type.selectedItem
		rel.singularName = singularName.text
		rel.ignore = ignore.selected
		rel.bidirectional = bidirectional.selected
		app.println "update object now ${rel}"
		classTreeModel.nodeChanged(rel)
	}
	def populateForm (Relationship rel) {
		relationshipName.text = rel.name
		type.selectedItem = rel.type
		singularName.text = rel.singularName
		bidirectional.selected = rel.bidirectional
		ignore.selected = rel.ignore
	}
}

class CodeGenMainEditSupport {
	JTextField url
	JTextField userName
	JTextField password
	JTextField driver
	JTextField tableNames
	JTextField packageName
	JTextField outputDir
	JTextField appConfigDir
	JTextField xmlFileName
	JTextField propertiesFile
	JCheckBox debug
	def updateObject (CodeGenMain main) {
		main.url = url.text
		main.userName = userName.text
		main.password = password.text
		main.driver = driver.text
		main.tableNames = tableNames.text
		main.packageName = packageName.text
		main.outputDir = outputDir.text
		main.appConfigDir = appConfigDir.text
		main.xmlFileName = xmlFileName.text
		main.propertiesFile = propertiesFile.text
		if (debug.selected) {
			main.debug = "true"
		} else {
			main.debug = null
		}
		main.configureCollaborators()
	}
	def populateForm (CodeGenMain main) {
		url.text = main.url
		userName.text = main.userName
		password.text = main.password
		driver.text = main.driver
		tableNames.text = main.tableNames
		packageName.text = main.packageName
		outputDir.text = main.outputDir
		appConfigDir.text = main.appConfigDir
		xmlFileName.text =  main.xmlFileName
		propertiesFile.text = main.propertiesFile
		if (debug == null) {
			debug.selected=false
		} else {
			debug.selected=true
		}
	}

}

class SettingsEditSupport {
	JTextField url
	JTextField userName
	JTextField password
	JTextField driver
	
	def updateObject (JDBCSettings settings) {
		settings.url = url.text
		settings.userName = userName.text
		settings.password = password.text
		settings.driver = driver.text
	}
	
	def populateForm (JDBCSettings settings) {
		url.text = settings.url
		userName.text = settings.userName
		password.text = settings.password
		driver.text = settings.driver		
	}
}