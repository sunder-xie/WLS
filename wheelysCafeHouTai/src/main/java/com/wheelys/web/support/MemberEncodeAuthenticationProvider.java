/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wheelys.web.support;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class MemberEncodeAuthenticationProvider implements AuthenticationProvider {
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(MemberEncodeAuthenticationToken.class, authentication,
				"MemberEncodeAuthenticationProvider only supports MemberEncodeAuthenticationToken");
		MemberEncodeAuthenticationToken auth = (MemberEncodeAuthenticationToken) authentication;
		auth.setAuthenticated(true);
		return auth;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (MemberEncodeAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
