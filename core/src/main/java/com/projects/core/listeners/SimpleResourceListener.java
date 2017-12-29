/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.projects.core.listeners;

import javax.jcr.Node;
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
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projects.core.util.inte.ResourceNode;

/**
 * A service to demonstrate how changes in the resource tree
 * can be listened for. It registers an event handler service.
 * The component is activated immediately after the bundle is
 * started through the immediate flag.
 * Please note, that apart from EventHandler services,
 * the immediate flag should not be set on a service.
 */

/**
 * A service to demonstrate how changes in the resource tree can be listened
 * for. It registers an event handler service. The component is activated
 * immediately after the bundle is started through the immediate flag. Please
 * note, that apart from EventHandler services, the immediate flag should not be
 * set on a service.
 */

@Component(immediate = true)
@Service
// @Component(immediate = true)
// @Service(value = EventHandler.class)
// @Property(name = EventConstants.EVENT_TOPIC, value =
// "org/apache/sling/api/resource/Resource/*")
public class SimpleResourceListener implements EventListener {

	private final String GROUPE_NAME = "chanversegroupe";
	private final String AUTHORIZABLE_ID = "rep:authorizableId";
	private final String PATH = "/home/users/chanverse";
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	ResourceResolverFactory resolverFactory;

	@Reference
	private SlingRepository repository;

	@Reference
	private ResourceNode resource;

	private BundleContext bundleContext;
	private Session session;

	private ObservationManager observationManager;

	public void run() {
		log.info("Running...");
	}

	protected void activate(ComponentContext ctx) {
		this.bundleContext = ctx.getBundleContext();
		try {
			session = repository.loginAdministrative(null);
			observationManager = session.getWorkspace().getObservationManager();
			observationManager.addEventListener(this, Event.NODE_ADDED, PATH, true, null, null, false);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("activate catch" + e.getMessage());
		}
	}

	@Override
	public void onEvent(EventIterator events) {
		Session adminSession = null;
		try {
			ResourceResolver resolver = resource.getResource(PATH).getResourceResolver();
			adminSession = resolver.adaptTo(Session.class);
			final UserManager userManager = resolver.adaptTo(UserManager.class);
			log.info("A new node was added to /home/chanverse ");
			Event event = (Event) events.nextEvent();
			Node node = session.getNode(event.getPath());
			String authorizableId = String.valueOf(node.getProperty(AUTHORIZABLE_ID).getValue());
			Group group = (Group) (userManager.getAuthorizable(GROUPE_NAME));
			if (group != null && userManager.getAuthorizable(authorizableId) != null) {
				group.addMember(userManager.getAuthorizable(authorizableId));
				adminSession.save();
			}
			if (session != null && session.isLive())
				session.logout();

			if (resolver != null)
				resolver.close();

		} catch (PathNotFoundException e) {
			e.printStackTrace();
			log.info("path not found" + e.getMessage());
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.info("repository not found" + e.getMessage());
		} catch (LoginException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			log.info("LoginException " + e.getMessage());
		}

	}
}