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

package com.excilys.sugadroid.tasks;

import java.util.List;

import com.excilys.sugadroid.activities.AccountDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.services.exceptions.ServiceException;
import com.excilys.sugadroid.services.interfaces.IContactServices;

public class GetAccountContactsTask extends
		AuthenticatedTask<AccountDetailsActivity> {

	private String accountId;
	private int offset;
	private IContactServices contactServices;
	private int maxResults;

	public GetAccountContactsTask(AccountDetailsActivity activity,
			IContactServices contactServices, String accountId, int offset,
			int maxResults) {
		super(activity);
		this.contactServices = contactServices;
		this.accountId = accountId;
		this.offset = offset;
		this.maxResults = maxResults;
	}

	@Override
	public void doRunAuthenticatedTask() throws ServiceException {
		List<ContactBean> contacts = contactServices.getAccountContacts(
				accountId, offset, maxResults);

		activity.updateContactList(contacts);
	}
}
