package org.crank.web.contribution;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

/** 
 * This represents a simple contribution. It can be configured in an IoC 
 * container. 
 * 
 * @author Rick Hightower
 *
 */
public class SimpleContributionSupport implements Contribution, Serializable {
	private static final long serialVersionUID = 1L;
	private String contributionText;
	
    protected String getContributionText() {
        return this.contributionText;
    }

    public void setContributionText(String contributionText) {
		this.contributionText = contributionText;
	}

	public void addToWriter(Writer writer) throws IOException {
		writer.write(getContributionText());
	}
	
}
