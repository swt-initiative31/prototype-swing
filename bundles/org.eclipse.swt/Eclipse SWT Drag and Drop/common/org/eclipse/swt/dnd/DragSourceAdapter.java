package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>DragSourceListener</code> interface.
 * 
 * <p>Classes that wish to deal with <code>DragSourceEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.</p>
 *
 * @see DragSourceListener
 * @see DragSourceEvent
 */
public class DragSourceAdapter implements DragSourceListener {
	/**
	 * This implementation of <code>dragStart</code> permits the drag operation to start.
	 * For additional information see <code>DragSourceListener.dragStart</code>.
	 */
	public void dragStart(DragSourceEvent event){};
	/**
	 * This implementation of <code>dragFinished</code> does nothing.
	 * For additional information see <code>DragSourceListener.dragFinished</code>.
	 */
	public void dragFinished(DragSourceEvent event){};
	/**
	 * This implementation of <code>dragSetData</code> does nothing.
	 * For additional information see <code>DragSourceListener.dragSetData</code>.
	 */
	public void dragSetData(DragSourceEvent event){};
}
