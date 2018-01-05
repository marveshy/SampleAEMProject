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
package com.projects.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.projects.core.serviceInte.ServicesInte;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(paths = "/bin/page", methods = "GET")
public class SimpleServlet extends SlingSafeMethodsServlet {

	@Reference
	private ServicesInte service;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String userId = getCurrentUserId(req);
			Map<String, String> map = new HashMap<String, String>();
			map.put("path", "/home/users/chanverse");
			map.put("type", "rep:User");
			map.put("property", "rep:authorizableId");
			map.put("property.operation", "equals");
			map.put("property.value", userId);

			// Quering JCR using com.day.cq.search.QueryBuilder API
			PredicateGroup predicateGroup = PredicateGroup.create(map);
			QueryBuilder builder = req.getResourceResolver().adaptTo(QueryBuilder.class);
			Query query = builder.createQuery(predicateGroup, req.getResourceResolver().adaptTo(Session.class));
			SearchResult result = query.getResult();
			Iterator<Node> nodes = result.getNodes();
			nodes.forEachRemaining(node -> {
				try {
					
					resp.getOutputStream().println("path " +node.getPath());					
					resp.getOutputStream().println("userId " +userId);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// resp.getOutputStream().println(node.)
			});

			

		} catch (Exception e) {
			resp.getOutputStream().println(e.getMessage());
		}
		
	}
	
	public String getCurrentUserId(SlingHttpServletRequest request) {
		  ResourceResolver resolver = request.getResourceResolver();
		  Session session = resolver.adaptTo(Session.class);
		  String userId = session.getUserID();

		  return userId;

		 }
}
