package org.eclipse.swt.events;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>MenuListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>MenuEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see MenuListener
 * @see MenuEvent
 */
public abstract class MenuAdapter implements MenuListener {

/**
 * Sent when a menu is hidden.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the menu operation
 */
public void menuHidden(MenuEvent e) {
}

/**
 * Sent when a menu is shown.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the menu operation
 */
public void menuShown(MenuEvent e) {
}
}
