/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * ToolBar example snippet: create tool bar (normal, hot and disabled images)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet47 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText("Snippet 47");

	Image image = extracted(display, SWT.COLOR_BLUE);

	Image disabledImage = extracted(display, SWT.COLOR_GREEN);

	Image hotImage = extracted(display, SWT.COLOR_RED);


	image = extracted2(display);
	disabledImage = extracted2(display);
	hotImage = extracted2(display);

	ToolBar bar = new ToolBar (shell, SWT.BORDER | SWT.FLAT);
	Rectangle clientArea = shell.getClientArea ();
	bar.setBounds (clientArea.x, clientArea.y, 200, 32);
	for (int i=0; i<12; i++) {
		ToolItem item = new ToolItem (bar, 0);
		item.setImage (image);
		item.setDisabledImage (disabledImage);
		item.setHotImage (hotImage);
		if (i % 3 == 0) item.setEnabled (false);
	}

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image.dispose ();
	disabledImage.dispose ();
	hotImage.dispose ();
	display.dispose ();
}

private static Image extracted(Display display, int color2) {
	Image disabledImage = new Image (display, 20, 20);
	GC gc = new GC (disabledImage);
	gc.setBackground (display.getSystemColor (color2));
	gc.fillRectangle (disabledImage.getBounds ());
	gc.dispose ();
	return disabledImage;
}

private static Image extracted2(Display display) {
	return new Image(display, "C:\\Users\\visjee\\Pictures\\u.png");
}
}
