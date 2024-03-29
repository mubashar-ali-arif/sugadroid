/* ============================================================================
 *
 * Copyright 2009 eBusiness Information - Excilys group
 *
 * Author: Pierre-Yves Ricau (py.ricau+sugadroid@gmail.com)
 *
 * Company contact: ebi@ebusinessinformation.fr
 *
 * This file is part of SugaDroid.
 *
 * SugaDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SugaDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SugaDroid.  If not, see <http://www.gnu.org/licenses/>.
 * ============================================================================
 */

package com.excilys.sugadroid.beans;

import java.util.StringTokenizer;

import com.excilys.sugadroid.beans.interfaces.ISessionBean;

public class SessionBeanImpl implements ISessionBean {

	private String sessionId;
	private String username;
	private String userId;
	private String url;
	private String version;
	private boolean version4_5;
	private SessionState state = SessionState.NOT_LOGGED_IN;

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#getSessionId()
	 */
	public synchronized String getSessionId() {
		return sessionId;
	}

	/**
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#getUsername()
	 */
	public synchronized String getUsername() {
		return username;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#getUserId()
	 */
	public synchronized String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#setLoggedIn(java.lang.String,
	 *      java.lang.String, java.lang.String, int, java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void setLoggedIn(String sessionId, String userId,
			String username, String url, String version) {
		this.sessionId = sessionId;
		this.username = username;
		this.userId = userId;
		this.url = url;
		this.version = version;

		StringTokenizer stk = new StringTokenizer(version, ".");

		if (stk.nextToken().equals("4") && stk.nextToken().equals("5")) {
			version4_5 = true;
		} else {
			version4_5 = false;
		}
		state = SessionState.LOGGED_IN;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#logout()
	 */
	public synchronized void logout() {
		sessionId = null;
		username = null;
		userId = null;
		url = null;
		version = null;
		state = SessionState.NOT_LOGGED_IN;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#getUrl()
	 */
	public synchronized String getUrl() {
		return url;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#getVersion()
	 */
	public synchronized String getVersion() {
		return version;
	}

	/**
	 * 
	 * @see com.excilys.sugadroid.beans.interfaces.ISessionBean#isVersion4_5()
	 */
	public synchronized boolean isVersion4_5() {
		return version4_5;
	}

	public synchronized SessionState getState() {
		return state;
	}

	public synchronized void setLoginIn() {
		logout();
		state = SessionState.LOGIN_IN;
	}
}
