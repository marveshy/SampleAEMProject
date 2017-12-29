package com.projects.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.projects.core.util.inte.ResourceNode;

@Component
@Service
public class ResourceNodeImpl implements ResourceNode {
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private ResourceResolverFactory resourceFact;

	private final String USER_MAPPED = "userAccessService";

	@Override
	public Resource getResource(String path) throws LoginException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, USER_MAPPED);
		ResourceResolver resolver = null;
		resolver = resolverFactory.getServiceResourceResolver(param);
		return resolver.getResource(path);

	}
}