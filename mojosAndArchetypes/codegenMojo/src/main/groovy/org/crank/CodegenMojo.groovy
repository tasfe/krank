
package org.crank

import org.codehaus.groovy.maven.mojo.GroovyMojo
import com.arcmind.codegen.*
import org.apache.maven.project.MavenProject
import org.apache.maven.model.Resource

/**
 * Example Maven2 Groovy Mojo.
 *
 * @goal codegen
 */
class CodegenMojo extends GroovyMojo {

    /**
     * JDBC Url
     *
     * @required
     * @parameter expression="${codegen.jdbc.url}"
     */
    String url
    
    
    /**
     * JDBC User Name
     *
     * @required
     * @parameter expression="${codegen.jdbc.userName}"
     */
    String userName

    /**
     * JDBC Password
     *
     * @required
     * @parameter expression="${codegen.jdbc.password}"
     */
    String password

    /**
     * JDBC Driver
     *
     * @required
     * @parameter expression="${codegen.jdbc.driver}"
     */
    String driver

    /**
     * Table names
     *
     * 
     * @parameter expression="${codegen.tableNames}"
     */
    String tableNames
    
    /**
     * Default package name
     *
     * 
     * @parameter expression="${codegen.packageName}"  default-value="com.mycompany"
     */
    String packageName

    /**
     * Default package name
     *
     * 
     * @parameter expression="${codegen.packageName}" default-value="."
     */
    String rootDir

    
    /**
     * App configuration directory.
     *
     * 
     * @parameter expression="${codegen.appConfigDir}" default-value="./codegen"
     */
    String appConfigDir
    
    /**
     * xmlFileName
     *
     * 
     * @parameter expression="${codegen.xmlFileName}" default-value="codegen.xml"
     */
    String xmlFileName


    /**
     * xmlDataSourceFileName
     *
     * 
     * @parameter expression="${codegen.xmlDataSourceFileName}" default-value="datasource.xml"
     */
    String xmlDataSourceFileName

    /**
     * Configuration file propertiesFile
     *
     *
     * @parameter expression="${codegen.propertiesFile}" default-value="config.properties"
     */
    String propertiesFile

    /**
     *
     *
     * @parameter expression="${codegen.codeGenPackage}" default-value="com.arcmind.codegen"
     */
    String codeGenPackage

    /**
     *
     *
     * @parameter expression="${codegen.generators}" default-value="FacesConfigCodeGen,JPACodeGenerator,SpringJavaConfigCodeGen,XHTMLCodeGenerator"
     */
    String generators

    /**
     *  @parameter expression="${codegen.actions}" default-value="all"
     */
    String actions


    /*
     * @parameter expression="${codegen.debug}" 
     */
    String debug

    /*
     *  @parameter expression="${codegen.usePom}" default-value="true"
     */
    boolean usePom

//    /**
//     * @parameter expression="${project}"
//     * @required
//     * @readonly
//     */
//    MavenProject project

    /**
     * @parameter expression="${codegen.debug}" default-value="true"
     */
    boolean useGUI

    /**
     * @parameter expression="${project.build.sourceDirectory}" default-value="true"
     */
    String sourceDirectory


    private static String PROPS_TO_COPY = "url,userName,password,driver,tableNames,packageName,rootDir,appConfigDir,xmlFileName,xmlDataSourceFileName,propertiesFile,codeGenPackage,generators,debug"

    void execute() {
        use(StringCategory) {



            CodeGenMain main
            if (useGUI) {
                log.info "EXECUTING GUI!!!!! ------------------------------------"
                GeneratorSwingApp generatorSwingApp = new GeneratorSwingApp()
                main = generatorSwingApp.main
                configure(main)
                generatorSwingApp.show()

                while (true) {
                    Thread.sleep(100000000) 
                }

            } else {
                main = new CodeGenMain()
                main.actions.addAll(actions.split(",") as List<String>)
                configure(main)
                main.run()
            }

        }
    }

    def configure(CodeGenMain main) {
        List<String> props = PROPS_TO_COPY.split(",")
        for (String prop : props) {
            main[prop]=this[prop]
            log.info "property set as follows: ${prop}=${main[prop]}"
        }
        main.configureCollaborators()
    }
}
