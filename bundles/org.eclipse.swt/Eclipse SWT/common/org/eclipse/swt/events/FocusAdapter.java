package org.eclipse.swt.events;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>FocusListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>FocusEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see FocusListener
 * @see FocusEvent
 */
public abstract class FocusAdapter implements FocusListener {

/**
 * Sent when a control gets focus.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the focus change
 */
public void focusGained(FocusEvent e) {
}

/**
 * Sent when a control loses focus.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the focus change
 */
public void focusLost(FocusEvent e) {
}
}
