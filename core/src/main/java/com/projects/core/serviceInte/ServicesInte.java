package com.projects.core.serviceInte;

import org.apache.sling.api.resource.PersistenceException;

public interface ServicesInte {
	
	public boolean getUserResourceResolver()
			throws org.apache.sling.api.resource.LoginException, PersistenceException ;

}
