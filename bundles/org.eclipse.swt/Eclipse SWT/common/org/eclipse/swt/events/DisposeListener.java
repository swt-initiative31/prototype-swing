package org.eclipse.swt.events;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated when a widget
 * is disposed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a widget using the
 * <code>addDisposeListener</code> method and removed using
 * the <code>removeDisposeListener</code> method. When a
 * widget is disposed, the widgetDisposed method will
 * be invoked.
 * </p>
 *
 * @see DisposeEvent
 */
public interface DisposeListener extends SWTEventListener {

/**
 * Sent when the widget is disposed.
 *
 * @param e an event containing information about the dispose
 */
public void widgetDisposed(DisposeEvent e);
}
