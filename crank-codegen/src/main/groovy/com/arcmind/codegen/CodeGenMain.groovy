package com.arcmind.codegen

import com.arcmind.codegen.oracle.OracleJDBCUtils;
import com.arcmind.codegen.oracle.OracleDataBaseMetaDataReader;


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
	String rootDir
	String appConfigDir
	String xmlFileName
	String xmlDataSourceFileName
	String propertiesFile
	String codeGenPackage
	String generators
	String generatorsUsed
	boolean wasNotSetPropFile
	boolean wasNotSetXmlFile
	boolean wasNotSetXmlDataSourceFile //todo
	String debug
    String trace
	File appConfigDirFile = new File("./codegen")
	List <String> actions = []
	Set <String> availableActions = ["reverse", "write", "read", "generate", "all", "help", "datasource"]
	Properties configProperties
	JdbcUtils jdbcUtils
	OracleJDBCUtils oracleJDBCUtils
	DataBaseMetaDataReader reader
	OracleDataBaseMetaDataReader oracleReader
	DataSourceReader dataSourceReader
	JavaModelGenerator modelGen
	List<CodeGenerator> codeGenerators = []
	XMLPersister persister
	XMLDataSourcePersister dataSourcePersister
	List collaborators
	String oracleNLS = 'en' //default

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
			readModel()
		}
		
		if (actions.contains("datasource")) {
			readDataSourceXML()
		}

		if (actions.contains("generate")) {
			generateArtifacts()
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
		
		/* Oracle injection */
		if (jdbcUtils.oracle) {
			oracleJDBCUtils = new OracleJDBCUtils(jdbcUtils, oracleNLS)
			reader.jdbcUtils = oracleJDBCUtils
			
		}
		
		/* Oracle injection */
		if (jdbcUtils.oracle) {
			oracleReader = new OracleDataBaseMetaDataReader(reader)
			oracleReader.processDB()
			
			// replace tables with Oracle reader tables
			reader.tables =  oracleReader.tables 
			
		} else {
			reader.processDB() //default old way
		}
		
		/* Convert the tables into JavaClasses. */
		modelGen.tables = reader.tables
		modelGen.process()
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

	def generateArtifacts() {
		if (debug) println "Generating artifacts"
		this.codeGenerators.each {CodeGenerator codeGen ->
			/* Output the generated classes. */
			codeGen.classes = modelGen.classes
			if (debug) println "Generating artifacts for ${codeGen.classes} with ${codeGen.class.name}"

            try {
			    codeGen.process()
            } catch (Exception ex) {
                ex.printMe("Unable to generate artifacts", this.&println)
            }
		}
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
		use(StringCategory,ExceptionCategory) {
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

	public List<Class> loadClassesForGenerators () {
		if (this.generators==null || "".equals(this.generators.trim())) {
			codeGenPackage = "com.arcmind.codegen"
			generators="FacesConfigCodeGen,JPACodeGenerator,SpringJavaConfigCodeGen,XHTMLCodeGenerator"
		}
		List<String> classNames = this.generators.split(",").findAll{String className -> 
			className != ""
		}
		if (this.generatorsUsed==null || "".equals(this.generatorsUsed.trim())) {
			generatorsUsed="true,true,true,true"
		}
		
		if (!validateGeneratorsUsedString(generatorsUsed)) {
			printlnClosure "'generatorsUsed' string is not valid: '${generatorsUsed}', using defaults!"
			generatorsUsed="true,true,true,true"
		}

		List<Class> classes = classNames.collect{String className-> 
		className.contains(".") ? Class.forName(className) : Class.forName("${codeGenPackage}.${className}")}
		return classes 
	}
	
	public boolean configureCollaborators() {
		boolean invalidArgument = false
		
		List<Class> codeGenClasses = loadClassesForGenerators()

		if (debug) {
			DataBaseMetaDataReader.metaClass.invokeMethod = logClosure
			JavaModelGenerator.metaClass.invokeMethod = logClosure
			JPACodeGenerator.metaClass.invokeMethod = logClosure
			JdbcUtils.metaClass.println = printlnClosure
			OracleJDBCUtils.metaClass.println = printlnClosure
			DataBaseMetaDataReader.metaClass.println = printlnClosure
			DataSourceReader.metaClass.println = printlnClosure
			JavaModelGenerator.metaClass.println = printlnClosure
			XMLPersister.metaClass.println = printlnClosure
			XMLDataSourcePersister.metaClass.println = printlnClosure
			codeGenClasses.each{Class cls-> cls.metaClass.println = printlnClosure}
		}

		jdbcUtils = new JdbcUtils()
		reader = new DataBaseMetaDataReader()
		dataSourceReader = new DataSourceReader()
		persister = new XMLPersister()
		dataSourcePersister = new XMLDataSourcePersister()
		modelGen = new JavaModelGenerator()

		collaborators = [jdbcUtils, reader, modelGen, persister, dataSourcePersister]
		codeGenerators = codeGenClasses.collect{Class cls -> cls.newInstance()}

		// Define used Code Generators
		setupUsedCodeGenerators()
		
		collaborators.addAll(codeGenerators)

		/* Configure related classes. */
		jdbcUtils.url = url
		jdbcUtils.userName = userName
		jdbcUtils.driver = driver
		jdbcUtils.password = password == null ? "" : password

		/* Collaborator debug configuration. */
		for (collaborator in collaborators) {
			collaborator.debug = debug == null ? false : Boolean.valueOf(debug)
            collaborator.trace = trace == null ? false : Boolean.valueOf(trace)
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

		codeGenerators.each{CodeGenerator codeGen ->
			if (this.packageName != null) {
				codeGen.setPackageName(this.packageName)
			}
			if (this.rootDir != null) {
				codeGen.setRootDir(new File(this.rootDir))
			}
		}
		
		persister.outputDir = appConfigDirFile
		if (!persister.outputDir.isDirectory()) {
			persister.outputDir.mkdirs()
		}
		wasNotSetXmlFile = (xmlFileName == null || "".equals(xmlFileName))
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
	
	public void readProperties() {
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
		wasNotSetPropFile = (propertiesFile==null || "".equals(propertiesFile))
		propertiesFile==null || "".equals(propertiesFile) ? new File(this.appConfigDirFile,"config.properties") : new File(propertiesFile)
	}
	

	private File calculateDataSourceFile () {
		(xmlDataSourceFileName == null || "".equals(xmlDataSourceFileName)) ? 
				(new File((File) this.appConfigDirFile,"dataSource.xml")) :
				(new File((File) this.appConfigDirFile,xmlDataSourceFileName))
	}
	
	private void writeProperties() {
		updateUsedCodeGenerators()
		configProperties = new Properties()
		File propFile = calculatePropFile()
        /* This is a hack. I don't see how propFile can be null at this piont, but for some reason it is. */
        if (propFile==null) {
            propFile = new File("./codegen/config.properties")
        }
		for (key in this.properties.keySet()) {

			Object value = this[key]
			if (value!=null) {
				if (value instanceof String) {
					if (debug ) println "Setting values into properties file ${key} = ${value}"
					configProperties[key] = value
				}
			}
		}
		propFile.getCanonicalFile().getParentFile().mkdirs()

		propFile.newOutputStream().withStream{stream -> configProperties.store(stream, "prop file") }
	}
	
	public backupPropFile(backupPropertiesFile) {
		backupPropertiesFile = StringHelper.clone(propertiesFile)
	}

	public restorePropFile(backupPropertiesFile) {
		propertiesFile = StringHelper.clone(backupPropertiesFile)
	}
	
	private boolean validateGeneratorsUsedString(str) {
		def theRegularExpression = /(true|false),(true|false),(true|false),(true|false)/
		str ==~ theRegularExpression
	}
	
	// Updates Code Generators objects' properties 'use'
	// with values from properties file string 'generatorsUsed'
	private void setupUsedCodeGenerators() {
		assert generatorsUsed
		assert codeGenerators
		assert generatorsUsed.split(",").length == codeGenerators.size()
		List<Boolean> codeGeneratorsUsed = []
		codeGeneratorsUsed = this.generatorsUsed.split(",").collect{
			String flag -> Boolean.valueOf(flag)			
		}

		codeGenerators.eachWithIndex {CodeGenerator cg, int index ->
            cg.use = codeGeneratorsUsed[index]
        }

	}
	
	// Updates string 'generatorsUsed' with values
	// from Code Generators objects' properties 'use'
	public void updateUsedCodeGenerators() {
		if (codeGenerators) {
			List<String> codeGeneratorsUsed = []
			codeGeneratorsUsed = codeGenerators.collect{
				CodeGenerator cg -> cg.use.toString()
			}
			this.generatorsUsed = codeGeneratorsUsed.join(',')
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

	Code Generation Parameters
	packageName		Package name of clases that will be generated
	outputDir		The output directory of the classes
	xmlFileName		XML file that contains the reversed model
	xmlDataSourceFileName		XML file that contains datasources
	debug			Puts the app in debug mode
    trace           Puts the app in trace mode like debug but more

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
