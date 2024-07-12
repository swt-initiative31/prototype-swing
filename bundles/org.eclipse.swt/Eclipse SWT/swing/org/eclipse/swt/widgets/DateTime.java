/*******************************************************************************
 * Copyright (c) 2024 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.awt.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

public class DateTime extends Composite {
	private static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	private static final int MAX_YEAR = 9999;
	private static final char SINGLE_QUOTE = '\''; // short date format may include quoted text
	private static final char MINUTES_FORMAT_CONSTANT = 'm'; // 1-2 lowercase 'm's represent minutes
	private static final char SECONDS_FORMAT_CONSTANT = 's'; // 1-2 lowercase 's's represent seconds

	public DateTime(Composite parent, int style) {
		super(parent, checkStyle(style));
// TODO (visjee) Not implemented yet
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		/*
		 * Even though it is legal to create this widget with scroll bars, they serve no
		 * useful purpose because they do not automatically scroll the widget's client
		 * area. The fix is to clear the SWT style.
		 */
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
		style = checkBits(style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
		style = checkBits(style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
		if ((style & SWT.DATE) == 0)
			style &= ~SWT.DROP_DOWN;
		return style;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	@Override
	protected Container createHandle() {
		return handle;
		// TODO (visjee) Not implemented yet
	}

	String getCustomShortDateFormat() {
		return toolTipText;
		// TODO (visjee) Not implemented yet
	}

	String getCustomShortTimeFormat() {
		StringBuilder buffer = new StringBuilder(getTimeFormat());
		int length = buffer.length();
		boolean inQuotes = false;
		int start = 0, end = 0;
		while (start < length) {
			char ch = buffer.charAt(start);
			if (ch == SINGLE_QUOTE)
				inQuotes = !inQuotes;
			else if (ch == SECONDS_FORMAT_CONSTANT && !inQuotes) {
				end = start + 1;
				while (end < length && buffer.charAt(end) == SECONDS_FORMAT_CONSTANT)
					end++;
				// skip the preceding separator
				while (start > 0 && buffer.charAt(start) != MINUTES_FORMAT_CONSTANT)
					start--;
				start++;
				break;
			}
			start++;
		}
		if (start < end)
			buffer.delete(start, end);
		return buffer.toString();
	}

	String getTimeFormat() {
		return toolTipText;
		// TODO (visjee) Not implemented yet
	}

	public int getDay() {
		return -1;
		// TODO (visjee) Not implemented yet
	}

	public int getHours() {
		return -1;
		// TODO (visjee) Not implemented yet
	}

	public int getMinutes() {
		// TODO (visjee) Not implemented yet
		return -1;
	}

	public int getMonth() {
		return -1;
		// TODO (visjee) Not implemented yet
	}

	@Override
	String getNameText() {
		return (style & SWT.TIME) != 0 ? getHours() + ":" + getMinutes() + ":" + getSeconds()
				: (getMonth() + 1) + "/" + getDay() + "/" + getYear();
	}

	public int getSeconds() {
		return -1;
		// TODO (visjee) Not implemented yet
	}

	public int getYear() {
		return -1;
		// TODO (visjee) Not implemented yet
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		// TODO (visjee) Not implemented yet
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	public void setDate(int year, int month, int day) {
		checkWidget();
		// TODO (visjee) Not implemented yet
	}

	public void setDay(int day) {
		// TODO (visjee) Not implemented yet
	}

	public void setHours(int hours) {
		checkWidget();
		// TODO (visjee) Not implemented yet
	}

	public void setMinutes(int minutes) {
		checkWidget();
		// TODO (visjee) Not implemented yet
	}

	public void setMonth(int month) {
		checkWidget();
		// TODO (visjee) Not implemented yet
	}

	@Override
	public void setOrientation(int orientation) {
		/* Currently supported only for CALENDAR style. */
		if ((style & SWT.CALENDAR) != 0)
			super.setOrientation(orientation);
	}

	public void setSeconds(int seconds) {
		checkWidget();
		if (seconds < 0 || seconds > 59)
			return;
		// TODO (visjee) Not implemented yet
	}

	public void setTime(int hours, int minutes, int seconds) {
		checkWidget();
		if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59)
			return;
		// TODO (visjee) Not implemented yet
	}

	public void setYear(int year) {
		checkWidget();
		if (year < MIN_YEAR || year > MAX_YEAR)
			return;
		// TODO (visjee) Not implemented yet
	}

}
