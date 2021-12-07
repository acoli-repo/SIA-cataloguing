package org.acoli.sc.start;

import org.acoli.sc.config.Config;
import org.acoli.sc.util.AffiliationUtils;
import org.acoli.sc.util.OpenNlpTools;

/**
 * Initialize external tools like OpenNlpTools, AffiliationUtils, etc.
 * @author demo
 *
 */
public class Init {
	
	Init (Config config) {
		
    	OpenNlpTools tools = new OpenNlpTools();
    	AffiliationUtils affiliationUtils = new AffiliationUtils(config);
	}

}
