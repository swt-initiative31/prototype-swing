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
 * that deals with the events that are generated when text
 * is modified.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a text widget using the
 * <code>addModifyListener</code> method and removed using
 * the <code>removeModifyListener</code> method. When the
 * text is modified, the modifyText method will be invoked.
 * </p>
 *
 * @see ModifyEvent
 */
public interface ModifyListener extends SWTEventListener {

/**
 * Sent when the text is modified.
 *
 * @param e an event containing information about the modify
 */
public void modifyText(ModifyEvent e);
}
