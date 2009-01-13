package com.arcmind.codegen



/**
 * The main entry point into the command line version of the codegen application.
 * Processes command line arguments and reads properties file settings.
 * Configures the other classes and performs injection of collaborators.
 * @author richardhightower
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
	String xmlDataSourceFileName
	String propertiesFile
	String debug
	File appConfigDirFile = new File("./codegen")
	List <String> actions = []
	Set <String> availableActions = ["reverse", "write", "read", "generate", "all", "help", "datasource"]
	Properties configProperties
	JdbcUtils jdbcUtils
	DataBaseMetaDataReader reader
	DataSourceReader dataSourceReader
	JavaModelGenerator modelGen
	JPACodeGenerator codeGen
	XMLPersister persister
	XMLDataSourcePersister dataSourcePersister
	List collaborators

	Closure printlnClosure = {String message ->
        println (message)
	}
	Closure logClosure = {String methodName, methodArgs->
		def validMethod = delegate.metaClass.getMetaMethod(methodName, methodArgs)
		if (validMethod==null) {
			return delegate.metaClass.invokeMissingMethod(delegate, methodName, methodArgs)
		}  else if (validMethod.name=="println") {
			return  validMethod.invoke(delegate, methodArgs)
		}
		println "Running ${methodName}(${methodArgs})"
		def result = validMethod.invoke(delegate, methodArgs)
		println "Completed ${methodName}(${methodArgs})"
		result
	}



	public void run() {

		if (actions.contains("all")) {
			all()
		}

		if (actions.contains("help") || actions.empty) {
			help()
		}

		if (actions.contains("saveProps")) {
			writeProperties()
		}

		/* Read the model from the database or the XML file. */
		if (actions.contains("reverse")) {
			reverseDB()
		} else if (actions.contains("read")) {
			readXML()
		}
		
		if (actions.contains("datasource")) {
			readDataSourceXML()
		}

		if (actions.contains("generate")) {
			generateJavaClasses()
		}

		if (actions.contains("write")) {
			writeXML()
		}

		if (debug) println "Success!"
	}

	def all () {
		actions << "reverse"
		actions << "generate"
		actions << "write"
		actions << "saveProps"
		actions << "datasource"
	}

	public void reverseDB() {
		if (debug) println "Reverse engineering the database tables"
		/* Process the database tables */
		reader.jdbcUtils = jdbcUtils
		reader.processDB()
		/* Convert the tables into JavaClasses. */
		modelGen.tables = reader.tables
		modelGen.convertTablesToJavaClasses()
	}

	def readXML() {
		if (debug) println "Reading XML file"
		persister.read()
		reader.tables = persister.tables
		modelGen.classes = persister.classes
	}
	
	def readDataSourceXML() {
		if (debug) println "Reading XML file with datasources from ${dataSourcePersister.fileName}"
		dataSourcePersister.read()
		//todo!
		dataSourceReader.settings = dataSourcePersister.jdbcSettings
	}

	def generateJavaClasses() {
		if (debug) println "Generating Java classes"
		/* Output the generated classes. */
		codeGen.classes = modelGen.classes
		if (debug) println "Generating Java classes for ${codeGen.classes}"
		codeGen.writeClassFiles()
	}

	def writeXML() {
		if (debug) println "Writing XML file containing table and classes model"
		/* Write out xml file. */
		persister.classes = modelGen.classes
		persister.tables = reader.tables
		persister.persist()
	}
	
	def writeDataSourceXML() {
		dataSourcePersister.jdbcSettings = dataSourceReader.settings
		if (debug) println "Writing XML file containing data sources: ${dataSourcePersister.jdbcSettings}"
		dataSourcePersister.persist()
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
		copyPropsFromArgs(args) //let the command line args override the properties file

		invalidArgument = configureCollaborators()

		if (invalidArgument) {
			println "Unable to configure collaborators"
			return false
		}

		return true
	}

	public boolean configureCollaborators() {
		boolean invalidArgument = false

		if (debug) {
			DataBaseMetaDataReader.metaClass.invokeMethod = logClosure
			JavaModelGenerator.metaClass.invokeMethod = logClosure
			JPACodeGenerator.metaClass.invokeMethod = logClosure

			JdbcUtils.metaClass.println = printlnClosure
			DataBaseMetaDataReader.metaClass.println = printlnClosure
			DataSourceReader.metaClass.println = printlnClosure
			JavaModelGenerator.metaClass.println = printlnClosure
			JPACodeGenerator.metaClass.println = printlnClosure
			XMLPersister.metaClass.println = printlnClosure
			XMLDataSourcePersister.metaClass.println = printlnClosure			
		}

		jdbcUtils = new JdbcUtils()
		codeGen = new JPACodeGenerator()
		reader = new DataBaseMetaDataReader()
		dataSourceReader = new DataSourceReader()
		persister = new XMLPersister()
		dataSourcePersister = new XMLDataSourcePersister()
		modelGen = new JavaModelGenerator()

		collaborators = [jdbcUtils, reader, modelGen, codeGen, persister, dataSourcePersister]

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
		persister.fileName = xmlFileName == null || "".equals(xmlFileName) ? "codegen.xml" : xmlFileName

		dataSourcePersister.outputDir = appConfigDirFile
		if (!dataSourcePersister.outputDir.isDirectory()) {
			dataSourcePersister.outputDir.mkdirs()
		}
		dataSourcePersister.fileName = xmlDataSourceFileName == null || "".equals(xmlDataSourceFileName) ? "dataSource.xml" : xmlDataSourceFileName
		initDataSource()
		
		return invalidArgument
	}
	
	public void initDataSource() {
		File dataSourceFile = calculateDataSourceFile()
		if (dataSourceFile.exists()) {
			readDataSourceXML();
		} else {
			writeDataSourceXML();
		}
	}
	
	private void readProperties() {
		configProperties = new Properties()
		File propFile = calculatePropFile()
		if (propFile.exists()) {
			if (debug) println "Found properties file ${propFile}, reading it into application arguments"
			
			//configProperties.load(new StringReader(propFile.text))
			FileInputStream fis = new java.io.FileInputStream(propFile)
			configProperties.load(fis);
			
			for (key in configProperties.keySet()) {
				if (configProperties[key]!=null) {
					if (debug) println "overriding values not set: ${key}=${configProperties[key]}"
					this[key] = configProperties[key]
				}
			}
		} else {
			if (debug) println "Properties file not found, so writing new properties file based on arguments passed"
			writeProperties()
		}
	}

	private File calculatePropFile () {
		propertiesFile==null || "".equals(propertiesFile) ? new File((File) this.appConfigDirFile,"config.properties") : new File(propertiesFile)
	}

	private File calculateDataSourceFile () {
		(xmlDataSourceFileName == null || "".equals(xmlDataSourceFileName)) ? 
				(new File((File) this.appConfigDirFile,"dataSource.xml")) :
				(new File((File) this.appConfigDirFile,xmlDataSourceFileName))
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

		propFile.newOutputStream().withStream{stream -> configProperties.store(stream, "prop file") }
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

	Code Generation Parameters
	packageName		Package name of clases that will be generated
	outputDir		The output directory of the classes
	xmlFileName		XML file that contains the reversed model
	xmlDataSourceFileName		XML file that contains datasources
	debug			Puts the app in debug mode

	Command line parameters take precedence over what is stored in the config.properties file.
	All command line arguments can be stored in the properties file using the saveProps action.

	Actions denote what codeGen should do. They do not get passed a parameter.

	reverse			Processes the database into table and classes model
	write			Write the database and classes model into an xml file
	read			Read the XML table and classes model back into memory
	datasource		Read the XML datasources back into memory
	generate		Generate Java classes based on the table and classes model
	all 			Does all of the above
	help			Prints this message
	saveProps		Saves the properties passed to command line to the properties file

	Example
	codegen.sh all url=jdbc:hsqldb:file:/db-codeGen/testdb userName=sa driver=org.hsqldb.jdbcDriver packageName=org.crank outputDir=./target appConfigDir=./codegen tableNames=Department,Employee
	"""
	}

}
