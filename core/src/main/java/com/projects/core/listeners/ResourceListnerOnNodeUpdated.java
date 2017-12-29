package com.projects.core.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projects.core.util.inte.ResourceNode;

@Component(immediate = true)
@Service
public class ResourceListnerOnNodeUpdated implements EventListener {

	private final String RESOURCE_NODE = "/content/content/en/jcr:content";
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	ResourceResolverFactory resolverFactory;

	@Reference
	private ResourceNode resource;

	@Reference
	private SlingRepository repository;

	private BundleContext bundleContext;
	private Session session;

	private ObservationManager observationManager;

	public void run() {
		log.info("Running...");
	}

	protected void activate(ComponentContext ctx) throws LoginException {
		this.bundleContext = ctx.getBundleContext();
		ResourceResolver resolver = resource.getResource(RESOURCE_NODE).getResourceResolver();
		try {
			session = resolver.adaptTo(Session.class);
			observationManager = session.getWorkspace().getObservationManager();
			observationManager.addEventListener(this, Event.PROPERTY_CHANGED, RESOURCE_NODE, false, null, null, false);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("activate catch" + e.getMessage());
		}
	}

	@Override
	public void onEvent(EventIterator events) {
		try {
			ResourceResolver resolver = resource.getResource(RESOURCE_NODE).getResourceResolver();
			Resource res = resource.getResource(RESOURCE_NODE);
			if (res != null) {
				// Create a node that represents the root node
				ModifiableValueMap modMap = res.adaptTo(ModifiableValueMap.class);
				if (modMap != null) {
					if (modMap.get("myKey") != null) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						String myKey = "ResourceListnerOnNodeUpdated " + String.valueOf(dateFormat.format(date));
						modMap.replace("myKey", myKey);
						resolver.commit();
						log.info("Successfully saved");
					}

				}
				resolver.close();
			}
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
