/**
 *
 */
package com.arcmind.codegen

import javax.swing.*
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Cursor
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import com.arcmind.codegen.ui.components.ListDialog

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
	boolean isViewConsole
	boolean isHideConsole	
	JTextArea console
	List<Action> viewActions
	List<Action> mainActions
	List<Action> fileActions
	JTabbedPane treeTabPane
	JTabbedPane mainTabPane
	JScrollPane consolePane
	JScrollPane settingsTreePane
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
	SettingsEditSupport settingsEditSupport
	String backupPropertiesFile
	String backupXmlFileName
	XMLPersister backupXMLPersister
	Font myFont = new Font("Tahoma", Font.PLAIN, 12)
    Font menuFont = myFont.deriveFont(11F)
	boolean debug = true
    boolean trace = true

	def println(String message) {
		console.append(message + "\n")
	}

	Closure printlnClosure = { String message ->
		console?.append(message + "\n")
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
		use(StringCategory, ExceptionCategory){
			main.reverseDB()
		}
		tableTreeModel.setTables(main.reader.tables)
		classTreeModel.setClasses(main.modelGen.classes)
	}

	public void readModel() {
        use(StringCategory,ExceptionCategory) {
            main.reader.tables=[]
            main.modelGen.classes=[]
            try {
                main.readXML()
                tableTreeModel.setTables(main.reader.tables)
                classTreeModel.setClasses(main.modelGen.classes)
            } catch (Exception ex) {
                setStatus "${ex.class.simpleName} ${ex.message}"
                ex.printMe("Unable to read model", this.&println)
            }
        }
	}

	def modifyProperties () {
		mainTabPane.addTab("Modify Properties", codeGenMainPane)
		mainTabPane.setSelectedComponent(codeGenMainPane)
		codeGenMainEditSupport.populateForm(main)
		main.configureCollaborators()
	}
	def refreshProperties () {
		mainTabPane.remove(codeGenMainPane)
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
	
	def handleAddDataSource() {
		JLabel labelUrl = new JLabel("URL");
		JLabel labelUserName = new JLabel("Username:");
		JLabel labelPassword = new JLabel("Password:");
		JLabel labelDriver = new JLabel("Driver:");
		JTextField url = new JTextField();
		JTextField userName = new JTextField();
		JTextField password = new JTextField();
		
		Object[] ob=[new JLabel("You MUST fill all fields!"),
		             labelUrl,url,labelUserName,userName,
		             labelPassword, password, labelDriver]
		String drv = JOptionPane.showInputDialog(ob);
		
		if (debug) {
			printlnClosure """Data source added: url:${url.text}, username:${userName.text}, password: ${password.text}, driver: ${drv}"""
		}
		
		if (drv) {
			JDBCSettings newSettings = 
				new JDBCSettings(
						url: url.text,
						userName: userName.text,
						password: password.text,
						driver: drv)
			main.dataSourceReader.settings << newSettings 
			main.writeDataSourceXML()
			updateJDBCTree(main.dataSourceReader.settings)
			showDataSourcePane(newSettings)
			setStatus "Data source added: url:${url.text},username:${userName.text},password: ${password.text}, driver: ${drv}"
		}		
		
	}
	
	def showDataSourcePane(settings) {
		treeTabPane.setSelectedComponent(settingsTreePane)
		selectDataSource(settings)
	}	
	
	def selectDataSource(JDBCSettings settings) {
		mainTabPane.addTab("JDBC Settings", settingsPane)
		mainTabPane.setSelectedComponent(settingsPane)
		currentSettings = settings
		settingsEditSupport.populateForm(settings)		
	}
	
	def useTheseSettings() {
		settingsEditSupport.updateObject(this.currentSettings); 
		main.writeDataSourceXML(); 
		updateJDBCTree(main.dataSourceReader.settings)
		
		mainTabPane.remove(settingsPane)
		
		main.url = currentSettings.url
		main.userName = currentSettings.userName
		main.password = currentSettings.password
		main.driver = currentSettings.driver
		
		modifyProperties()
		
		updateJDBCTree(main.dataSourceReader.settings)
	}
	
	def saveTheseSettings() {
		settingsEditSupport.updateObject(currentSettings);
		main.writeDataSourceXML();
		updateJDBCTree(main.dataSourceReader.settings)
	}
	
	public boolean isEnabledRootDir() {
		!main.rootDir
	}
	
	public void treeTableSelected(TreeSelectionEvent event) {
		setStatus ""
		mainTabPane.addTab("Modify Properties", codeGenMainPane)
		mainTabPane.selectedComponent = codeGenMainPane
		codeGenMainEditSupport.populateForm(main)
		main.configureCollaborators()
		
		String tableName = event.path.lastPathComponent.table.name

		List list = main.tableNames!= null ? main.tableNames.split(',') : []
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


    private void initEditors() {
        /* Initialize edit support for class, property and relationships. */
        classEditSupport = new ClassEditSupport(classTreeModel:classTreeModel)
        propertyEditSupport = new JavaPropertyEditSupport(classTreeModel:classTreeModel)
        relationshipEditSupport = new RelationshipEditSupport(classTreeModel:classTreeModel)
        codeGenMainEditSupport = new CodeGenMainEditSupport()
        codeGenMainEditSupport.app = this;
        settingsEditSupport = new SettingsEditSupport()
    }
    
    private void initListeners() {
		codeGenMainEditSupport.rootDir.getDocument().addDocumentListener(
				new MyDocumentListener(app:this));
    }    	

    public void show () {
        initEditors()
		buildGUI ()
		initListeners()
		
		// Update DataSources Tree
		updateJDBCTree(main.dataSourceReader.settings)

		/* Initilize Console and mainTabPane. */
		hideConsole.enabled=false
		mainTabPane.remove(consolePane)
		mainTabPane.remove(relationshipPane)
		mainTabPane.remove(classPane)
		mainTabPane.remove(propertyPane)
		mainTabPane.remove(codeGenMainPane)
		mainTabPane.remove(settingsPane)
    }
	public GeneratorSwingApp() {
		/* Initialize CodeGenMain. */
		CodeGenMain.metaClass.println = printlnClosure
		main = new CodeGenMain()
		main.readProperties()
		main.configureCollaborators()
	}
	private updateJDBCTree(param) {
		settingsTreeModel.setSettings(param)
	}

	public static void main (String [] args) {
		GeneratorSwingApp app = new GeneratorSwingApp()
        app.show()
	}

	/* This method is very long, but it uses a builder to layout the complete GUI. It is layed out in a hierarchy so it is easy to read.
	 * This method contains no logic. It cust contains layout and event wiring.
	 */
	def buildGUI() {
		mainFrame=
        swing.frame(title:'CodeGen Code Generator', size:[1500,1000], defaultCloseOperation:JFrame.EXIT_ON_CLOSE,  show:true) {
			
            Closure handleWriteModel = {
           		if (main.wasNotSetXmlFile) {
           			setXmlFromFileDialog(false, true)
           		}
            	doOutside {
            		edt {setStatus "Writing XML file out ${main.persister.fileName}"}
            		main.writeXML()
            		edt {setStatus "Done writing XML file out ${main.persister.fileName}"}
            	}
            }
            
            Closure handleWriteAsModel = {
            		backupXMLCredentials()
           			if (setXmlFromFileDialog(false, false)) {
                    	doOutside {
                    		edt {setStatus "Writing XML file out ${main.persister.fileName}"}
                    		main.writeXML()
                    		edt {setStatus "Done writing XML file out ${main.persister.fileName}"}
                    		//restoreXMLCredentials()
                    	}
           			}
            }     
            
            Closure handleOpenProperties = {
            		main.backupPropFile(backupPropertiesFile)
           			if (setPropFromFileDialog(true, false)) {
	            		use(StringCategory,ExceptionCategory){
	            			main.readProperties()
	            		}
	            		refreshProperties()
	            		edt {setStatus "Done reading properties file in ${main.propertiesFile}"}
           			}
            		//main.restorePropFile(backupPropertiesFile)            		
            }            
            
            Closure handleWriteProperties = {
            		if (!validateRootDir(main.rootDir)){
            			return
            		}
            		if (main.wasNotSetPropFile) {
            			setPropFromFileDialog(false, true)
            		}
            		use(StringCategory,ExceptionCategory){
            			main.writeProperties()
            		}
            		refreshProperties()
            		edt {setStatus "Done writing properties file out ${main.propertiesFile}"}
            }
            
            Closure handleWriteAsProperties = {
            		if (!validateRootDir(main.rootDir)){
            			return
            		}
            		main.backupPropFile(backupPropertiesFile)
           			if (setPropFromFileDialog(false, false)) {
	            		use(StringCategory,ExceptionCategory){
	            			main.writeProperties()
	            		}
           			}
            		refreshProperties()
            		edt {setStatus "Done writing properties file out ${main.propertiesFile}"}
            		//main.restorePropFile(backupPropertiesFile)
            }            

            Closure handleReadModel = {
                doOutside {
                    edt {setStatus "Reading XML file in ${main.persister.fileName}"}
                    readModel()
                    edt {setStatus "Done reading XML file in ${main.persister.fileName}"}
                }
            }
            
            Closure handleOpenXML = {
            		backupXMLCredentials()
            		if (setXmlFromFileDialog(true, false)) {
	                    doOutside {
	                        edt {setStatus "Reading XML file in ${main.persister.fileName}"}
	                        readModel()
	                        edt {setStatus "Done reading XML file in ${main.persister.fileName}"}
	                        //restoreXMLCredentials()
	                    }
            		}
                }
            
            Closure handlePickRootDir =  {
            		setRootDirFromDialog(true)            		
            }
            
            Closure handleCodeGenCheckBoxClick = {
            		codeGenMainEditSupport.updateObject(this.main)
            }
            
            fileActions = actions() {
                action(name: "Exit", mnemonic: 'X', closure: { exit() })
                action(name: "Write Model", mnemonic: 'W', closure: handleWriteModel)
                action(name: "Read Model", mnemonic: 'e', closure: handleReadModel)
                action(name: "Open Properties...", mnemonic: 'O', closure: handleOpenProperties)
                action(name: "Open Model...", mnemonic: 'L', closure: handleOpenXML)
                action(name: "Save Properties", mnemonic: 'S', closure: handleWriteProperties)
                action(name: "Save Properties As...", mnemonic: 'P', closure: handleWriteAsProperties)
                action(name: "Save Model As...", mnemonic: 'M', closure: handleWriteAsModel)
            }

            Closure handleReverseDB = {
                doOutside { //Runs in a seperate thread                	
                    edt {setStatus "Reverse engineering database please standby..."}
                    enableMenuBar(false)
                    modifyCursor(false)
                    try {
                        reverseDB()
                        edt {setStatus "Done reverse engineering database." }
                    } catch (Exception ex) {
                        setStatus "${ex.class.simpleName} ${ex.message}"
                        ExceptionCategory.printMe(ex, "Problem reverse engineering db", this.&println)
                    } finally {
                        enableMenuBar(true)
                        modifyCursor(true)
                    }
                }
            }

            Closure handleGenerateCode = {
                    doOutside {
                        edt {setStatus "Generating Artifacts to ${main.rootDir}"}


	                    use(StringCategory,ExceptionCategory) {

	                        }
                        enableMenuBar(false)
                        modifyCursor(false)
                        try {
                            main.generateArtifacts()
	                        edt {setStatus "Done generating Artifacts to ${main.rootDir}"}
                        } catch(Exception ex) {
                        	edt {
                                setStatus "${ex.class.simpleName} ${ex.message}"
                                ExceptionCategory.printMe (ex, "Unable to generate artifacts", this.&println)
                            }
                        }
                        finally {
                        	enableMenuBar(true)
                        	modifyCursor(true)
                        }
                    }
            }

            mainActions = actions() {
                action(name: "Reverse DB", mnemonic: 'R', closure: handleReverseDB)
                action(name: "Generate Code", mnemonic: 'G', closure: handleGenerateCode)
                action(name: "Modify Properties", mnemonic: 'o', closure: { modifyProperties() })
                action(name: "Add New Data Source...", mnemonic: 'D', closure: { handleAddDataSource() })
            }
            viewActions = actions () {
                viewConsole = action(name: "View Console", mnemonic: 'V', closure: { showConsole() })
                hideConsole = action(name: "Hide Console", mnemonic: 'H', closure: { closeConsole() })
                action(name: "Clear Console", mnemonic: 'l', closure: { clearConsole() })
            }
            menuBar() {
                menu(text: "File", mnemonic: 'F', font: menuFont) {
                    fileActions.each {menuItem(it, font: menuFont)}
                }
                menu (text: "Main", mnemonic: 'M', font: menuFont) {
                    mainActions.each {menuItem(it, font: menuFont)}
                }
                menu (text: "Window", font: menuFont) {
                    viewActions.each {menuItem(it, font: menuFont)}
                }                
            }

            toolBar (constraints: BorderLayout.PAGE_START) {
                fileActions.each {button(it, font: menuFont, toolTipText: it.getValue("Name"))}
                mainActions.each {button(it, font: menuFont, toolTipText: it.getValue("Name"))}
                viewActions.each {button(it, font: menuFont, toolTipText: it.getValue("Name"))}                
            }

            splitPane(constraints: BorderLayout.CENTER) {
                treeTabPane = tabbedPane(constraints: BorderLayout.WEST, preferredSize:[300,300]) {
                    scrollPane(title:"Tables", tabMnemonic: "T") { 
                    	tree(model: tableTreeModel, valueChanged: {treeTableSelected(it)}) 
                    }
                    scrollPane(title:"Classes", tabMnemonic: "C") {
                        tree(model: classTreeModel, valueChanged: {treeClassSelected(it)})
                    }                    
                    settingsTreePane = scrollPane(title:"JDBC Settings", tabMnemonic: "J") {
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
                                label("Root dir", preferredSize:[100,20])
                                codeGenMainEditSupport.rootDir = 
                                	textField(preferredSize:[200,20]) 
                                codeGenMainEditSupport.rootDirButton = button(text:"...", actionPerformed: {handlePickRootDir()}, preferredSize:[30,20], enabled: enabledRootDir)
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("XML File", preferredSize:[100,20])
                                codeGenMainEditSupport.xmlFileName = textField(preferredSize:[200,20])
                                button(text:"...", actionPerformed: {handleOpenXML()}, preferredSize:[30,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("XML DataSource File", preferredSize:[100,20])
                                codeGenMainEditSupport.xmlDataSourceFileName = textField(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }                            
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Properties File", preferredSize:[100,20])
                                codeGenMainEditSupport.propertiesFile = textField(preferredSize:[200,20])
                                button(text:"...", actionPerformed: {handleOpenProperties()}, preferredSize:[30,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Debug mode", preferredSize:[100,20])
                                codeGenMainEditSupport.debug = checkBox(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Trace mode", preferredSize:[100,20])
                                codeGenMainEditSupport.trace = checkBox(preferredSize:[200,20])
                                label(preferredSize:[100,20])
                            }
                            panel {
                                boxLayout(axis:BoxLayout.X_AXIS)
                                label("Code generators", preferredSize:[100,20])
                                main.codeGenerators.each {CodeGenerator cg ->
                                	JCheckBox cgCheck = checkBox(preferredSize:[100,20],text:cg.class.simpleName, actionPerformed: handleCodeGenCheckBoxClick)
                                	codeGenMainEditSupport.codeGeneratorsUsed.add(cgCheck)
                                }
                                codeGenMainEditSupport.populateForm(main)
                            }
                            panel {
                                button(text:"Apply", actionPerformed: {handleApplyProperties()})
                                button(text:"Save", actionPerformed: {handleSaveProperties()})
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
                                button(text:"Save", actionPerformed: {saveTheseSettings()})
                                button(text:"Close", actionPerformed: {mainTabPane.remove(settingsPane)})
                                label(preferredSize:[100,20])
                                button(text:"Use", actionPerformed: {useTheseSettings()})
                            }
                        }//panel
                    }//settingsPane                    
                }//mainTabPane
            }//splitPane
            panel (constraints: BorderLayout.PAGE_END) {
            	status = label(text:"  Welcome to CodeGen!", font: menuFont.deriveFont(Font.BOLD,12F))
            }
        }//frame
	}//buildGUI()
	private handleSaveProperties() {
		if (!validateRootDir(codeGenMainEditSupport.rootDir.text)){
			return
		}
		codeGenMainEditSupport.updateObject(this.main); 
		main.writeProperties()
	}
	private handleApplyProperties() {
		if (!validateRootDir(codeGenMainEditSupport.rootDir.text)){
			return
		}
		codeGenMainEditSupport.updateObject(this.main)
	}
	private restoreXMLCredentials() {
		main.persister = backupXMLPersister.cloneThis()
		main.xmlFileName = backupXmlFileName
	}
	
	private backupXMLCredentials() {
		backupXMLPersister = main.persister.cloneThis()
		backupXmlFileName = main.xmlFileName
	}
	
	private void enableMenuBar(trueOrFalse) {
		if (trueOrFalse == false) { //Save state
			isViewConsole = viewConsole.enabled
			isHideConsole = hideConsole.enabled
		}
		// Enable/disable ALL items
	 	viewActions.each({it.enabled = trueOrFalse})
		mainActions.each({it.enabled = trueOrFalse})
		fileActions.each({it.enabled = trueOrFalse})
		if (trueOrFalse == true) { // Restore state
			viewConsole.enabled = isViewConsole 
			hideConsole.enabled = isHideConsole 
		}
	}	
	private modifyCursor(trueOrFalse) {
		trueOrFalse ? mainFrame.getContentPane().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) :
				mainFrame.getContentPane().setCursor(
						Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))					
	}
	
	private setPropFromFileDialog(isOpenOrSave, isUpdateFlag) {
		JFileChooser fc = new JFileChooser(main.calculatePropFile())
		CodeGenFileFilter filter = new CodeGenFileFilter(description:"Properties File")
	    filter.addExtension("properties");
	    fc.setFileFilter(filter)
		
        int returnVal = isOpenOrSave? fc.showOpenDialog(mainFrame) : fc.showSaveDialog(mainFrame)

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            main.propertiesFile = fc.getSelectedFile().getAbsolutePath()
            if (isUpdateFlag)
            main.wasNotSetPropFile = false
        }
        
        return (returnVal == JFileChooser.APPROVE_OPTION) ? true : false
	}
	
	private setXmlFromFileDialog(isOpenOrSave, isUpdateFlag) {
		JFileChooser fc = new JFileChooser(main.calculatePropFile())
		CodeGenFileFilter filter = new CodeGenFileFilter(description:"XML File")
	    filter.addExtension("xml")
	    fc.setFileFilter(filter)
		
        int returnVal = isOpenOrSave? fc.showOpenDialog(mainFrame) : fc.showSaveDialog(mainFrame)

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            updateXMLCredentials(file)
            if (isUpdateFlag)
            main.wasNotSetXmlFile = false
        }
        
        return (returnVal == JFileChooser.APPROVE_OPTION) ? true : false
	}	
	private updateXMLCredentials(File file) {
		main.xmlFileName = file.getName()
		main.persister.fileName = main.xmlFileName
		main.persister.outputDir = file.getParentFile()
	}
	
	private void setRootDirFromDialog(isOpenOrSave) {
		JFileChooser fc = new JFileChooser()
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = isOpenOrSave ? fc.showOpenDialog(mainFrame) : fc.showSaveDialog(mainFrame)
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			RootDirValidator validator = new RootDirValidator(rootDir : fc.getSelectedFile().getPath());
			if (validator.validate()){
				ListDialog.showDialog(
                        mainFrame,
                        null,
                        false,//Success
                        "Select",
                        "Success:",
                        "Root Dir Success",
                        validator.data.toArray(new String[0]),
                        null,
                        null);				
				
				codeGenMainEditSupport.rootDir.text = fc.getSelectedFile().getPath()
				main.rootDir = codeGenMainEditSupport.rootDir.text
			} else {
				ListDialog.showDialog(
                        mainFrame,
                        null,
                        true,//Error
                        "Close",
                        "Failures:",
                        "Root Dir Failure",
                        validator.data.toArray(new String[0]),
                        null,
                        null);				
			}
		}
	}
	
	private boolean validateRootDir(rootDir) {
		RootDirValidator validator = new RootDirValidator(rootDir : rootDir);
		if (!validator.validate()){
			ListDialog.showDialog(
                    mainFrame,
                    null,
                    true,//Error
                    "Close",
                    "Failures:",
                    "Root Dir Failure",
                    validator.data.toArray(new String[0]),
                    null,
                    null);
			return false;
		}
		return true
	}
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
	JTextField rootDir
	JTextField xmlFileName
	JTextField xmlDataSourceFileName
	JTextField propertiesFile
	JCheckBox debug
    JCheckBox trace
	JButton rootDirButton
	List<JCheckBox> codeGeneratorsUsed = new ArrayList<JCheckBox>()
	
	GeneratorSwingApp app
	
	def updateObject (CodeGenMain main) {
		main.url = url.text
		main.userName = userName.text
		main.password = password.text
		main.driver = driver.text
		main.tableNames = tableNames.text
		main.packageName = packageName.text
		main.outputDir = outputDir.text
		main.appConfigDir = appConfigDir.text
		main.rootDir = rootDir.text
		main.xmlFileName = xmlFileName.text
		main.xmlDataSourceFileName = xmlDataSourceFileName.text
		main.propertiesFile = propertiesFile.text
		if (debug.selected) {
			main.debug = "true"
		} else {
			main.debug = null
		}
        if (trace.selected) {
            main.trace = "true"
        } else {
            main.trace = null
        }

		codeGeneratorsUsed.each { JCheckBox chk ->
			main.codeGenerators.each {
				if (it.class.simpleName == chk.text){
					it.use = chk.selected
				}
			}
		}
		main.updateUsedCodeGenerators()
		
		main.configureCollaborators()
		app.updateJDBCTree(main.dataSourceReader.settings)
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
		rootDir.text = main.rootDir
		xmlFileName.text =  main.xmlFileName
		xmlDataSourceFileName.text = main.xmlDataSourceFileName
		propertiesFile.text = main.propertiesFile
		if (debug == null) {
			debug.selected=false
		} else {
			debug.selected=true
		}
        if (trace == null) {
            trace.selected=false
        } else {
            trace.selected=true
        }
		main.codeGenerators.each { CodeGenerator cg ->
			codeGeneratorsUsed.each {
				if (it.text == cg.class.simpleName) {
					it.selected = cg.use
				}
			}
		}
	}

}

class SettingsEditSupport {
	JTextField url
	JTextField userName
	JTextField password
	JTextField driver
	
	GeneratorSwingApp app
	
	def updateObject (JDBCSettings settings) {
		app.println "update object was ${settings}"
		settings.url = url.text
		settings.userName = userName.text
		settings.password = password.text
		settings.driver = driver.text
		app.println "update object now ${settings}"
	}
	
	def populateForm (JDBCSettings settings) {
		url.text = settings.url
		userName.text = settings.userName
		password.text = settings.password
		driver.text = settings.driver		
	}
}

class MyDocumentListener implements DocumentListener {
	GeneratorSwingApp app
	
    public void insertUpdate(DocumentEvent e) {
		refreshButton()        
    }
    public void removeUpdate(DocumentEvent e) {
    	refreshButton()
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }
    
    def refreshButton() {
        app.codeGenMainEditSupport.rootDirButton.enabled = 
        	app.codeGenMainEditSupport.rootDir.text.equals('')
    }
}