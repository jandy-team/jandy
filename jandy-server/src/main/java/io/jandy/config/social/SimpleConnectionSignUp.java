/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jandy.config.social;

import java.util.concurrent.atomic.AtomicLong;

import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Component;

/**
 * Simple little {@link ConnectionSignUp} command that allocates new userIds in memory.
 * Doesn't bother storing a user record in any local database, since this quickstart just stores the user id in a cookie.
 * @author Keith Donald
 */
@Component
public final class SimpleConnectionSignUp implements ConnectionSignUp {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	
	public String execute(Connection<?> connection) {
		User user = userRepository.findByGitHubId(Long.parseLong(connection.getKey().getProviderUserId()));
		if (user == null) {
			user = userService.signUp((Connection<? extends GitHub>)connection);
		}
		return Long.toString(user.getId());
	}

}