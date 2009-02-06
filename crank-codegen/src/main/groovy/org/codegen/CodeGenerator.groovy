/**
 * 
 */
package org.codegen

import org.codegen.model.JavaClass



/**
 * @author richardhightower
 *
 */
public interface CodeGenerator{
	boolean isUse()
	void setUse(boolean use)
	void process()
	void setDebug(boolean debug)
    void setTrace(boolean trace)
	void setClasses(List<JavaClass> classes)
	void setRootDir(File file)
	void setPackageName(String packageName)
}
