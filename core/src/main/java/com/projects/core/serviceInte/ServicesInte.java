package com.projects.core.serviceInte;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;

public interface ServicesInte {
	
	public boolean updateNode()
			throws org.apache.sling.api.resource.LoginException, PersistenceException ;

	public void UpdateUserNodeOnFirstLogin(String idCurrentUser) throws LoginException ;
	
	public String idPath(String currentUser) ;
	
	
}
