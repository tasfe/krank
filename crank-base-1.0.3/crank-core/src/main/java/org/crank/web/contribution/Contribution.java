package org.crank.web.contribution;

import java.io.IOException;
import java.io.Writer;

/**
 * A contribution is something that writes to a web page more or less.
 * 
 * We created this construct so we could store snippets (templates and
 * plain text) and then resue these from Custom Tags, JSF components, 
 * Tapestry widgets, whatever.
 * 
 * This enables us to store our snippets in our IOC container config 
 * (think Spring application context), in a file, in a classpath resource
 * or as a web application resource and then reuse it.
 *  
 * @author Rick Hightower
 *
 */
public interface Contribution {
    
    /** 
     * Writes the contents of a contribution out to the 
     * Browser.
     * 
     * @param writer
     * @throws IOException
     */
	public void addToWriter(Writer writer) throws IOException;
}
