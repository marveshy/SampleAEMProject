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
package com.projects.core.schedulers;

import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projects.core.util.inte.ResourceNode;

//import java.util.Properties;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Component(metatype = true, label = "A scheduled task", immediate=true,
    description = "Simple demo for cron-job like task with properties")
@Service(value = Runnable.class)

@Properties({
	@Property(name = "scheduler.period", longValue = 10),
	@Property(name="scheduler.concurrent", propertyPrivate=true, boolValue=false)
//   @Property(name = "scheduler.expression", value = "30 * * * * ?",
//        description = "Cron-job expression"),
//    @Property(name = "scheduler.concurrent", boolValue=false,
//        description = "Whether or not to schedule this task concurrently")
})
public class SimpleScheduledTask implements Runnable {
	
	@Reference
	private ResourceNode resourceNode;
//	   
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
     
    private BundleContext bundleContext;      
     
    public void run() {
        log.info("Executing a perodic job");
    }
     
    protected void activate(ComponentContext ctx) throws RepositoryException {    	
    	 log.info("ACTIVATED");        
//    	 Node node = resourceNode.getNodeForResource();
//    	 log.info("after" ,node.getPath());
    }
     
    protected void deactivate(ComponentContext ctx) {
    	 log.info("DEACTIVATED");
        this.bundleContext = null;
    }
     

}
