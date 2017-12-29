package com.projects.core.serviceInte;

import org.apache.sling.api.resource.PersistenceException;

public interface ServicesInte {
	
	public boolean updateNode()
			throws org.apache.sling.api.resource.LoginException, PersistenceException ;

}
