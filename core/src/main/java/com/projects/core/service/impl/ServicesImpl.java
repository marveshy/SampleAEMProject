package com.projects.core.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projects.core.serviceInte.ServicesInte;
import com.projects.core.util.inte.ResourceNode;

@Component
@Service
public class ServicesImpl implements ServicesInte {

	@Reference
	private ResourceNode resourceNode;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String RESOURCE_NODE = "/content/content/en/jcr:content";

	@Override
	public boolean updateNode() throws org.apache.sling.api.resource.LoginException, PersistenceException {
		Resource res = resourceNode.getResource(RESOURCE_NODE);
		ResourceResolver resolver = res.getResourceResolver();
		try {

			if (res != null) {
				// Create a node that represents the root node
				ModifiableValueMap modMap = res.adaptTo(ModifiableValueMap.class);
				if (modMap != null) {
					if (modMap.get("myKey") != null) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						String myKey = "getUserResourceResolver " + String.valueOf(dateFormat.format(date));
						modMap.replace("myKey", myKey);
						resolver.commit();
						log.info("Successfully saved");
						return true;
					} else {
						modMap.put("myKey", "AnothermyValue");
						resolver.commit();
						log.info("Successfully Add saved");
					}

				}
			}
		} finally {
			if (resolver != null && resolver.isLive())
				resolver.close();
		}

		return false;
	}

}
