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
    String javaClassTemplate = '''<% import com.arcmind.codegen.RelationshipType; %>package ${bean.packageName};
<% imports.each { imp-> %>import ${imp};
<% } %>
@Entity <% if (!bean.namesMatch) { %> @Table(name="${bean.table.name}") <% } 
%>${queries}
public class ${bean.name} implements Serializable {
    /** ID */
    @Id <% if (!bean.id.namesMatch) { %>@Column(name="${bean.id.column.name.toUpperCase()}")<% } %> 
    @GeneratedValue( strategy = GenerationType.AUTO )
    private ${bean.id.javaClass.name} id;

    /* ------- Relationships ------ */
   <%
   for (r in bean.relationships) {
	    if (r.ignore) continue
   		if (r.type == RelationshipType.ONE_TO_MANY && r.bidirectional==false) {
   %>
    @OneToMany(cascade = CascadeType.ALL) @JoinColumn(name="${r.key.foriegnKey.name}")
    private Set<${r.relatedClass.name}> ${r.name} = new HashSet<${r.relatedClass.name}>();
   <% } else if (r.type == RelationshipType.ONE_TO_MANY && r.bidirectional==true) { 
    %>
    @OneToMany(mappedBy="${r.otherSide.name}", cascade = CascadeType.ALL)
    private Set<${r.relatedClass.name}> ${r.name} = new HashSet<${r.relatedClass.name}>();
   <% 
    } else if (r.type == RelationshipType.MANY_TO_ONE) { 
   %>
    @ManyToOne (cascade = {CascadeType.REFRESH, CascadeType.MERGE}) @JoinColumn(name="${r.key.foriegnKey.name}")
    private ${r.relatedClass.name} ${r.name};
   <%
    } else if (r.type == RelationshipType.MANY_TO_MANY) {
   %>
    
    @ManyToMany 
    @JoinTable(name="${r.key.foriegnKey.table.name}",
    		joinColumns=@JoinColumn(name="${r.otherSide.key.foriegnKey.name}"),
    		inverseJoinColumns=@JoinColumn(name="${r.key.foriegnKey.name}"))	
    private Set <${r.relatedClass.name}> ${r.name} = new HashSet<${r.relatedClass.name}>();
   <% }//else if
    }//end iterate through beans %>

    /** Properties's fields */
   <% bean.properties.each { property-> %>
    <% if (!property.namesMatch) {%>@Column(name="${property.column.name.toUpperCase()}")<% } %>
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

   <%
   /* Iterate through the relationships */
   bean.relationships.each { r->
   		/* If the relationship is a OneToMany; generate  the getter, setter and add/remove methods. */
        if (r.type == RelationshipType.ONE_TO_MANY || r.type == RelationshipType.MANY_TO_MANY) { %>

    public Set<${r.relatedClass.name}> get${r.name.cap()}() {
        return this.${r.name.unCap()};
    }

    public void set${r.name.cap()}(Set<${r.relatedClass.name}> ${r.name.unCap()} ) {
        this.${r.name.unCap()} = ${r.name.unCap()};
    }
    <% 

    String relatedInstance =  r.singularName.unCap(); 
    String propertyName = r.otherSide?.name?.cap();

    %>
    public void add${r.singularName.cap()}(${r.relatedClass.name} ${relatedInstance}) {
    	<% if (r.bidirectional==true && r.type!=RelationshipType.MANY_TO_MANY) {    %>${relatedInstance}.set${propertyName}(this);<%     } %>
    	${r.name}.add(${relatedInstance});
    }

    public void remove${r.singularName.cap()}(${r.relatedClass.name} ${relatedInstance}) {
    	<% if (r.bidirectional==true && r.type!=RelationshipType.MANY_TO_MANY) {    %>${relatedInstance}.set${propertyName}(null);<%     } %>
    	${r.name}.remove(${relatedInstance});
    }
 

    <% } else if (r.type == RelationshipType.MANY_TO_ONE) { %>

    public ${r.relatedClass.name} get${r.name.cap()}() {
        return this.${r.name.unCap()};
    }

    public void set${r.name.cap()}(${r.relatedClass.name} ${r.name.unCap()}) {
        this.${r.name.unCap()} = ${r.name.unCap()};
    }

    <% }} %>


   <% bean.properties.each { property-> %>
    public ${property.javaClass.name} get${property.name.cap()}() {
        return this.${property.name.unCap()};
    }

    public void set${property.name.cap()}(${property.javaClass.name} ${property.name.unCap()}) {
        this.${property.name.unCap()} = ${property.name.unCap()};
    }
   <% } %>

    public boolean equals(Object other) {
    	if (other==null) {
    		return false;
    	}
    	${bean.name} other${bean.name} = (${bean.name}) other;
    	if (other${bean.name}.id==null && this.id==null) {
    		return other${bean.name}.hashCode() == this.hashCode();
    	} else if (this.id == null) {
    		return false;
    	} else {
    		return this.id.equals(other${bean.name}.id);
    	}
    }
    
    public int hashCode() {
    	return id == null ? super.hashCode() : id.hashCode();
    }

}
'''
    boolean needsColumnImport(JavaClass bean) {
        for (JavaProperty property : bean.properties) {
            //If they don't match, then you need a column
            if (!property.namesMatch || !property.column.nullable) {
                return true
            }
        }
        if (!bean.id.namesMatch || !bean.id.column.nullable) {
            return true
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
        
        imports << "java.io.Serializable"
        imports << "javax.persistence.Entity"
        imports << "javax.persistence.GeneratedValue"
        imports << "javax.persistence.GenerationType"
        imports << "javax.persistence.Id"
         
        bean.relationships.each {Relationship relationship ->
         	if (relationship.type == RelationshipType.ONE_TO_MANY) {
         		imports << "java.util.Set"
         		imports << "java.util.HashSet"
         		imports << "javax.persistence.CascadeType"
         		imports << "javax.persistence.OneToMany"
             	if (relationship.bidirectional == false) {
             			imports << "javax.persistence.JoinColumn"
             	}
         	} else if (relationship.type == RelationshipType.MANY_TO_ONE) {
         		imports << "javax.persistence.ManyToOne"
         		imports << "javax.persistence.CascadeType"
         	} else if (relationship.type == RelationshipType.MANY_TO_MANY) {
         		imports << "java.util.Set"
         		imports << "java.util.HashSet"
     			imports << "javax.persistence.JoinTable"
     			imports << "javax.persistence.ManyToMany"
     			imports << "javax.persistence.JoinColumn"
         	}
        }
        
        if (bean.relationships.size()>0) {
        	imports << "javax.persistence.NamedQueries" 
        	imports << "javax.persistence.NamedQuery"
        }
        
        if (needsColumnImport(bean)) {
            imports << "javax.persistence.Column"
        }
        
        if (!bean.namesMatch) {
        	imports << "javax.persistence.Table"
        }
         
        Set<String> impSet = new HashSet<String> (imports)
        impSet.remove (null)
        impSet = new TreeSet(impSet)
        if (debug) println "Calculated imports for ${bean.name}, ${imports}"
        impSet
    }

    String readPopulatedTemplate = '''
@NamedQueries( {
		@NamedQuery(name = "${bean.name}.readPopulated", query = "SELECT DISTINCT ${bean.name.unCap()} FROM ${bean.name} ${bean.name.unCap()} "
				${relationships}
				+ " WHERE ${bean.name.unCap()}.id=?1")
		})'''

    String readPopulatedRelationshipTemplate = 
'''				+ " LEFT JOIN FETCH ${bean.name.unCap()}.${relationship.name.unCap()}"'''

    String createQueriesString(JavaClass bean) {

    	
    	List<String> relationships = []
    	bean.relationships.each {Relationship relationship -> 
	    	relationships << engine.createTemplate(readPopulatedRelationshipTemplate).make(["bean":bean, "relationship":relationship]).toString()		
    	}
    	
    	String relationshipsStr = relationships.join("\n")
    	
    	String query = engine.createTemplate(readPopulatedTemplate).make(["bean":bean, "relationships":relationshipsStr]).toString()
    	return query
    }
    
    def writeClassFiles() {
        for (JavaClass bean in classes) {
        	
        	if (debug) println "Writing ${bean.name} class file"
            def binding = ["bean":bean, "imports":calculateImportsFromBean(bean), "queries": createQueriesString(bean)]        
            String templateOutput = engine.createTemplate(javaClassTemplate).make(binding).toString()
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