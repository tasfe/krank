/**
 * 
 */
package com.arcmind.codegen



/**
 * @author richardhightower
 *
 */
public class CodeGenMain{
	String url
	String userName
	String password
	String driver
	String tableNames
	String packageName
	String outputDir
	String appConfigDir
	String xmlFileName
	String propertiesFile
	String debug
	File appConfigDirFile
	List <String> actions = []
	Set <String> availableActions = ["reverse", "write", "read", "generate", "all", "help"]
	Properties configProperties
	JdbcUtils jdbcUtils
	DataBaseMetaDataReader reader
    JavaModelGenerator modelGen
	CodeGenerator codeGen
	XMLPersister persister
	List collaborators 

	Closure logClosure = {String methodName, methodArgs->
		def validMethod = delegate.metaClass.getMetaMethod(methodName, methodArgs)
		if (validMethod==null) {
			return delegate.metaClass.invokeMissingMethod(delegate, methodName, methodArgs)
		}  else if (validMethod.name=="println") {
			return  validMethod.invoke(delegate, methodArgs)
		}
		System.out.println "Running ${methodName}(${methodArgs})"
		def result = validMethod.invoke(delegate, methodArgs)
		System.out.println "Completed ${methodName}(${methodArgs})"
		result
	}
	

	
	public void run() {

		if (actions.contains("all")) {
			actions << "reverse" 
			actions << "generate"
			actions << "write"
			actions << "saveProps"
		}
		
		if (actions.contains("help") || actions.empty) {
			help()
		}

		if (actions.contains("saveProps")) {
			writeProperties()
		}
		
		/* Read the model from the database or the XML file. */
		if (actions.contains("reverse")) {
			if (debug) println "Reverse engineering the database tables"
			/* Process the database tables */
	        reader.jdbcUtils = jdbcUtils        
	        reader.processDB()			
 	        /* Convert the tables into JavaClasses. */
	        modelGen.tables = reader.tables
	        modelGen.convertTablesToJavaClasses()
		} else if (actions.contains("read")) {
			if (debug) println "Reading XML file"
			persister.read()
			reader.tables = persister.tables
			modelGen.classes = persister.classes
		}
		
		if (actions.contains("generate")) {
			if (debug) println "Generating Java classes"
	        /* Output the generated classes. */
	        codeGen.classes = modelGen.classes
	        codeGen.writeClassFiles()			
		}
		
		if (actions.contains("write")) {
			if (debug) println "Writing XML file containing table and classes model"
	        /* Write out xml file. */
	        persister.classes = modelGen.classes
	        persister.tables = reader.tables
	        persister.persist()			
		}
		
		if (debug) println "Success!"
	}

    /** This is the main entry point for this program. */
    public static void main (String [] args) {
    	use(StringCategory) { 
        	CodeGenMain codeGenMain = new CodeGenMain()
        	if (codeGenMain.processArgs(args)) {
        		codeGenMain.run()
        	}
    	}
    }
    
    /* Parse the command line arguments. */
	public boolean processArgs (String [] args) {
		boolean invalidArgument = false

        String propertyName = null
        String value = null
		/* First pass make sure they passed valid arguments. */
		for (arg in args) {
				if (arg.contains("=")) {
					def command = arg.split("=")
					propertyName = command[0]
					value = command[1]
					if (!this.metaClass.hasProperty(this, propertyName)) {
						invalidArgument = true
						println "Invalid command line argument ${arg}"
					}

				}
		}
		
		/* If there are errors do not proceed. */
		if (invalidArgument) {
            println "Problems with command line arguments"
            return false;
        }
		
		/* Read properties from the command line and the properties file. */
		copyPropsFromArgs(args)
		appConfigDirFile = appConfigDir == null ? new File("./codegen") : new File(appConfigDir)
		readProperties()
		copyPropsFromArgs(args) //let the command line args overide the properties file

		invalidArgument = configureCollaborators()
		
		if (invalidArgument) {
			println "Unable to configure collaborators"
			return false
		}
		
		return true

	}
    
    private boolean configureCollaborators() {
    	boolean invalidArgument = false
    	
		if (debug) {
			DataBaseMetaDataReader.metaClass.invokeMethod = logClosure
			JavaModelGenerator.metaClass.invokeMethod = logClosure
			CodeGenerator.metaClass.invokeMethod = logClosure			
		}
		
		jdbcUtils = new JdbcUtils()
		codeGen = new CodeGenerator()
		reader = new DataBaseMetaDataReader()
		persister = new XMLPersister()
		modelGen = new JavaModelGenerator()

		collaborators = [jdbcUtils, reader, modelGen, codeGen, persister]
		
		/* Configure related classes. */
		jdbcUtils.url = url
		jdbcUtils.userName = userName
		jdbcUtils.driver = driver
		jdbcUtils.password = password == null ? "" : password

		/* Collaborator debug configuration. */		
		for (collaborator in collaborators) {
			collaborator.debug = debug == null ? false : Boolean.valueOf(debug)			
		}
		
		
		/* configure tablesNames to process. */ 
		try {
			modelGen.tableNames = tableNames != null ? new HashSet(Arrays.asList(tableNames.split(","))) : [] as HashSet;
		} catch (e) {
			e.printStackTrace()
			invalidArgument = true
			println "Unable to parse tableNames argument ${tableNames}."
		}
		
		
		modelGen.packageName = packageName		
		
		/* Configure output dirs by creating dirs if they do not exist. */
		codeGen.outputDir = outputDir == null ? new File("./target") : new File(outputDir)				
		if (!codeGen.outputDir.isDirectory()) {
			codeGen.outputDir.mkdirs()
		}
		persister.outputDir = appConfigDirFile
		if (!persister.outputDir.isDirectory()) {
			persister.outputDir.mkdirs()
		}
        persister.fileName = xmlFileName == null ? "codegen.xml" : xmlFileName
        		
        return invalidArgument

    }
    
	private void readProperties() {
			configProperties = new Properties()
			File propFile = calculatePropFile()
			if (propFile.exists()) {
				if (debug) println "Found properties file ${propFile}, reading it into application arguments" 
				configProperties.load(new StringReader(propFile.text))
				for (key in configProperties.keySet()) {
					if (configProperties[key]!=null) {
						if (debug) println "overiding values not set: ${key}=${configProperties[key]}"
						this[key] = configProperties[key]
					}
				}
			} else {
				if (debug) println "Properties file not found, so writng new properties file based on arguments passed"
				writeProperties()
			}
	}

	private File calculatePropFile () {
		propertiesFile==null ? new File(appConfigDirFile,"config.properties") : new File(propertiesFile) 
	}
	
	private void writeProperties() {
			configProperties = new Properties()
			File propFile = calculatePropFile()
			for (key in this.properties.keySet()) {
				
				Object value = this[key]
				if (value!=null) {
					if (value instanceof String) {
						if (debug ) println "Setting values into properties file ${key} = ${value}"
						configProperties[key] = value
					}
				}
			}
			propFile.parentFile.mkdirs()
			
			propFile.newOutputStream().withStream{stream ->
				configProperties.store(stream, "prop file")
			}
	}
	
	public void copyPropsFromArgs(String[] args) {
		actions = []
		for (String arg : args) {
				if (arg.contains("=")) { 
				def command = arg.split("=")
				String propertyName = command[0]
				String value = command[1]
				this[propertyName]=value
				if (debug) println "Processing property ${propertyName}=${value}"
			} else {
				if (availableActions.contains(arg)) {
					if (debug) println "Found action ${arg}"
					actions << arg
				} else {
					println "Action not understood " + arg
				}
			}
		}
	}
   
	public void help () {
		
		
	println """
	codeGen is used to generate a JPA Java model from a relational database.
	Here are the command line arguments that codeGen takes.

	These are the one that can set parameters, i.e., password=foo.
	They take the form <parameter name>=<value>
	
	Parameters
	appConfigDir	Where to find the config.properties file (defaults to ./codegen)
	propertiesFile  The location of properties where parameters can be stored 
					(defaults to ./codegen/config.properties)

	JDBC/Database Parameters
	url				JDBC URL of database
	userName		User name for JDBC connection
	password		Password for connection
	driver			Driver name for JDBC connection
	tableNames		List of tables names to process
	
	Code Generataion Parameters
	packageName		Package name of clases that will be generated
	outputDir		The output directory of the classes
	xmlFileName		XML file that contains the reversed model
	debug			Puts the app in debug mode

	Command line parameters take precedence over what is stored in the config.properties file.
	All command line arguments can be stored in the properties file.

	Actions denote what codeGen should do. They do not get passed a parameter.

	reverse			Processes the database into table and classes model
	write			Write the database and classes model into an xml file
	read			Read the XML table and classes model back into memory
	generate		Generate Java classes based on the table and classes model
	all 			Does all of the above
	help			Prints this message
	saveProps		Saves the properties passed to command line to the properties file

	Example 
	codegen.sh all url=jdbc:hsqldb:file:/db-codeGen/testdb userName=sa driver=org.hsqldb.jdbcDriver packageName=org.crank outputDir=./target appConfigDir=./codegen tableNames=Department,Employee
	"""		
	}
	
}
