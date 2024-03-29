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

package com.excilys.sugadroid.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.excilys.sugadroid.R;
import com.excilys.sugadroid.activities.interfaces.ICallingGetItemDetailsActivity;
import com.excilys.sugadroid.beans.ContactBean;
import com.excilys.sugadroid.beans.interfaces.IAppointmentBean;
import com.excilys.sugadroid.services.interfaces.IContactServices;
import com.excilys.sugadroid.tasks.GetAppointmentContactsTask;
import com.excilys.sugadroid.tasks.GetContactDetailsTask;

public class AppointmentDetailsActivity extends CommonListActivity implements ICallingGetItemDetailsActivity<ContactBean> {

	private static final String			TAG	= AppointmentDetailsActivity.class.getSimpleName();

	private IAppointmentBean			appointment;

	private TextView					nameText;
	private TextView					dateStartText;
	private TextView					durationText;
	private TextView					descriptionText;

	private List<ContactBean>			appointmentContacts;

	private ArrayAdapter<ContactBean>	itemAdapter;

	private Runnable					getItemDetailsTask;

	private Runnable					getAppointmentContactsTask;

	private ContactBean					selectedItem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.appointment_details);

		appointment = (IAppointmentBean) getIntent().getSerializableExtra(CommonActivity.ITEM_IDENTIFIER);

		findViews();
		setTasks();
		setAdapters();
		setListeners();

		executeOnGuiThreadAuthenticatedTask(getAppointmentContactsTask);

	}

	@Override
	protected void findViews() {
		super.findViews();
		nameText = (TextView) findViewById(R.id.appointment_name_text);
		dateStartText = (TextView) findViewById(R.id.appointment_date_start);
		durationText = (TextView) findViewById(R.id.appointment_duration);
		descriptionText = (TextView) findViewById(R.id.appointment_description_text);

		String appointmentName = getString(R.string.appointment_name);
		nameText.setText(appointmentName + " " + appointment.getName());

		hideLoadingText();

		StringBuilder sb = new StringBuilder(getString(R.string.appointment_date_start));
		sb.append(" ").append(appointment.getDayStart().toString(getString(R.string.day_date_format))).append(" ").append(
				getString(R.string.appointment_time_start)).append(" ").append(
				appointment.getTimeStart().plusHours(GeneralSettings.getGMT(this)).toString(getString(R.string.time_format)));

		dateStartText.setText(sb.toString());

		durationText.setText(getString(R.string.appointment_duration) + " " + appointment.getDurationHours()
				+ getString(R.string.appointment_hours) + appointment.getDurationMinutes().toString());

		descriptionText.setText(appointment.getDescription());

	}

	private void setAdapters() {

		appointmentContacts = new ArrayList<ContactBean>();

		itemAdapter = new ArrayAdapter<ContactBean>(this, android.R.layout.simple_list_item_1, appointmentContacts);

		setListAdapter(itemAdapter);
	}

	protected void setListeners() {
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectedItem = itemAdapter.getItem(position);
				executeDelayedOnGuiThreadAuthenticatedTask(500, getItemDetailsTask);
			}

		});
	}

	protected void setTasks() {

		final IContactServices contactServices = (IContactServices) container.getBean("contactServices");

		getItemDetailsTask = new Runnable() {
			public void run() {
				GetContactDetailsTask task = new GetContactDetailsTask(AppointmentDetailsActivity.this, contactServices, selectedItem
						.getId());

				submitRejectableTask(task);
			}

		};

		getAppointmentContactsTask = new Runnable() {
			public void run() {

				GetAppointmentContactsTask task = new GetAppointmentContactsTask(AppointmentDetailsActivity.this, contactServices,
						appointment.getId(), 0, GeneralSettings.getAccountMaxResults(AppointmentDetailsActivity.this));

				hideEmpty();

				submitRejectableTask(task);
			}
		};
	}

	public void updateContactList(final List<ContactBean> contacts) {
		runOnUiThread(new Runnable() {
			public void run() {

				List<ContactBean> allContacts = appointmentContacts;

				allContacts.addAll(contacts);

				showEmpty();
				itemAdapter.notifyDataSetChanged();

			}
		});
	}

	@Override
	public void onItemDetailsLoaded(final ContactBean contact) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "forwarding to item details activity");
				Intent intent = new Intent(AppointmentDetailsActivity.this, ContactDetailsActivity.class);
				intent.putExtra(CommonActivity.ITEM_IDENTIFIER, contact);
				startActivity(intent);
			}
		});
	}

}
