package com.arcmind.codegen
import groovy.text.SimpleTemplateEngine

/**
 * Generates .java files from JavaClass models objects. 
 */
class CodeGenerator {
    List<JavaClass> classes
    /** The target output dir. Defaults to ./target */
    File outputDir = new File("./target")
    boolean debug
    SimpleTemplateEngine engine = new SimpleTemplateEngine()
    String textTemplate = '''
<% import com.arcmind.codegen.RelationshipType; %>

package ${bean.packageName};

import java.io.Serializable;
import javax.persistence.Entity;

<% imports.each { imp-> %>import ${imp};
<% } %>

@Entity <% if (!bean.namesMatch) { %> @Table('${bean.table.name}') <% } %>
public class ${bean.name} implements Serializable {
    /** ID */
    @Id @Column ('${bean.id.column.name.toUpperCase()}') @GeneratedValue( strategy = GenerationType.AUTO )
    private ${bean.id.javaClass.name} id;

    /* ------- Relationships ------ */
   <% bean.relationships.each {r -> 
   if (r.type == RelationshipType.ONE_TO_MANY) {
   %>
    @OneToMany(cascade = CascadeType.ALL) @JoinColumn('${r.key.foriegnKey.name}')
    private Set <${r.relatedClass.name}> ${r.name};
   <% } else if (r.type == RelationshipType.MANY_TO_ONE) { %>
    @ManyToOne (cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private ${r.relatedClass.name} ${r.name};
   <%} else if (r.type == RelationshipType.MANY_TO_MANY) {%>
    @ManyToMany @JoinColumn('${r.key.foriegnKey.name}') @JoinTable('${r.key.foriegnKey.table.name}')
    private Set <${r.relatedClass.name}> ${r.name};
   <% }} %>

    /** Properties's fields */
   <% bean.properties.each { property-> %>
    <% if (!property.namesMatch) {%>@Column('${property.column.name.toUpperCase()}')<% } %>
    private ${property.javaClass.name} ${property.name.unCap()};
   <% } %>

    public ${bean.name} () {

    }

    public void setId(${bean.id.javaClass.name} id) {
    	this.id = id;
    }
    public ${bean.id.javaClass.name} getId() {
    	return id;
    }

   <% bean.relationships.each { r->
        if (r.type == RelationshipType.ONE_TO_MANY) { %>

    public Set<${r.relatedClass.name}> get${r.name.cap()}() {
        return this.${r.name.unCap()};
    }

    public void set${r.name.cap()}(Set<${r.relatedClass.name}> ${r.name.unCap()}} ) {
        this.${r.name.unCap()} = ${r.name.unCap()};
    }

    <% } else if (r.type == RelationshipType.MANY_TO_ONE || r.type == RelationshipType.MANY_TO_MANY) { %>

    public ${r.relatedClass.name} get${r.name.cap()}() {
        return this.${r.name.unCap()};
    }

    public void set${r.name.cap()}(${r.relatedClass.name} ${r.name.unCap()}} ) {
        this.${r.name.unCap()} = ${r.name.unCap()};
    }

    <% }} %>


   <% bean.properties.each { property-> %>
    public ${property.javaClass.name} get${property.name.cap()}() {
        return this.${property.name.unCap()};
    }

    public void set${property.name.cap()}(${property.javaClass.name} ${property.name.unCap()}} ) {
        this.${property.name.unCap()} = ${property.name.unCap()};
    }
   <% } %>
}
'''

    boolean needsColumnImport(JavaClass bean) {
        for (JavaProperty property : bean.properties) {
            //If they don't match, then you need a column
            if (!property.namesMatch || !property.column.nullable) {
                return true
            }
        }
        return false
    }

    Set<String> calculateImportsFromBean(JavaClass bean) {
    	
    	
    	 
        List<String> imports = bean.properties.collect { JavaProperty property ->
            if (!property.javaClass.primitive && !property.javaClass.packageName.startsWith("java.lang")) {
                return "${property.javaClass.packageName}.${property.javaClass.name}"
            } else {
                return null;
            }
        }
         
        imports << "javax.persistence.GeneratedValue"
        imports << "javax.persistence.GenerationType"
        imports << "javax.persistence.Id"
         
        bean.relationships.each {Relationship relationship ->
         	if (relationship.type == RelationshipType.ONE_TO_MANY) {
         		imports << "javax.persistence.OneToMany"
         		imports << "javax.persistence.JoinColumn"
         		imports << "javax.persistence.CascadeType"
         	} else if (relationship.type == RelationshipType.MANY_TO_ONE) {
         		imports << "javax.persistence.ManyToOne"
         		imports << "javax.persistence.CascadeType"
         	}	
        }
        
        if (needsColumnImport(bean)) {
            imports << "javax.persistence.Column"
        }
         
        Set<String> impSet = new HashSet<String> (imports)
        impSet.remove (null)
        if (debug) println "Calculated imports for ${bean.name}, ${imports}"
        impSet
    }

    
    
    def writeClassFiles() {
        for (JavaClass bean in classes) {
        	if (debug) println "Writing ${bean.name} class file"
            def binding = ["bean":bean, "imports":calculateImportsFromBean(bean)]        
            String templateOutput = engine.createTemplate(textTemplate).make(binding).toString()
            File outputFileDir = new File(outputDir, bean.packageName.replace('.','/'))
        	if (debug) println "Outputting ${bean.name} to ${outputFileDir}"
            outputFileDir.mkdirs()
            File javaFile = new File (outputFileDir, bean.name + ".java")
        	if(debug) println "Java file ${javaFile}"
            javaFile.newWriter().withWriter{BufferedWriter writer->
            	writer.write(templateOutput)
            }
        }
    }

}