/**
 * 
 */
package org.codegen.util



/**
 * @author Alec Kotovich
 *
 */
public class StringHelper{
	
	static String clone(String src) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.flush();
			oos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object dest = ois.readObject();
			ois.close();
			return (String)dest;
		} catch (Throwable th) { 
			return src;
		}		
	}	
	
}
