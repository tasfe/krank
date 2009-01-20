package org.crank
import junit.framework.TestCase
/**
 * Created by IntelliJ IDEA.
 * User: richardhightower
 * Date: Jan 19, 2009
 * Time: 1:56:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class CodegenMojoTest extends TestCase {

    CodegenMojo mojo

    public void setUp () {

            mojo = new CodegenMojo()
            mojo.packageName="com.somecompany.employeetask"
            mojo.url="jdbc:mysql://localhost:3306/presto2"
            mojo.debug=true
            mojo.tableNames="DEPARTMENT,EMPLOYEE,ROLE"
            mojo.xmlFileName=
            mojo.xmlDataSourceFileName=
            mojo.propertiesFile=
            mojo.userName="presto2"
            mojo.password="presto2"
            mojo.appConfigDir="./codegen"
            mojo.driver="com.mysql.jdbc.Driver"
            mojo.codeGenPackage="com.arcmind.codegen"
            mojo.generators="FacesConfigCodeGen,JPACodeGenerator,SpringJavaConfigCodeGen,XHTMLCodeGenerator"
            mojo.rootDir="."
            mojo.actions = "all"
            mojo.appConfigDir = "."
            mojo.useGUI = true
    }

    public void testMojo() {
        mojo.execute()
    }

}