package com.projects.core.util.inte;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;

public interface ResourceNode {
	
	public Resource getResource(String path) throws LoginException;
}
