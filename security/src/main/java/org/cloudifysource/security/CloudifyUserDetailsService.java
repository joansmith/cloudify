/*******************************************************************************
 * Copyright (c) 2013 GigaSpaces Technologies Ltd. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.cloudifysource.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.Assert;


/**
 * Retrieves user details from an in-memory list defined in the security configuration file specified on bootstrap.
 * This class is the Cloudify parallel to Spring's {@link InMemoryUserDetailsManager}, adapted to support users'
 * authorization groups.
 * It is also intended for testing and demonstration purposes, where a full blown persistent system isn't required.
 * @author noak
 * @since 2.7
 */
public class CloudifyUserDetailsService implements UserDetailsService {

	private Logger logger = java.util.logging.Logger.getLogger(CloudifyUserDetailsService.class.getName());
    private final Map<String, CloudifyUserDetails> users = new HashMap<String, CloudifyUserDetails>();
    
	
    public CloudifyUserDetailsService(final Collection<CloudifyUserDetails> users) {
        for (CloudifyUserDetails user : users) {
        	createUser(user);
        }
    }
    

    private void createUser(final CloudifyUserDetails cloudifyUserDetails) {
        Assert.isTrue(!userExists(cloudifyUserDetails.getUsername()));
        users.put(cloudifyUserDetails.getUsername().toLowerCase(), new CloudifyUser(cloudifyUserDetails));
    }
    

    private boolean userExists(final String username) {
        return users.containsKey(username.toLowerCase());
    }
    
 
    @Override
    public CloudifyUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        CloudifyUserDetails cloudifyUserDetails = users.get(username.toLowerCase());

        if (cloudifyUserDetails == null) {
            throw new UsernameNotFoundException(username);
        }

        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("CloudifyUserDetailsService: loading user: " + username + ", roles: " 
            		+ Arrays.toString(cloudifyUserDetails.getAuthorities().toArray()) 
            		+ ", authgroups: " + Arrays.toString(cloudifyUserDetails.getAuthGroups().toArray()));        	
        }

        return new CloudifyUser(cloudifyUserDetails);
    }

}
