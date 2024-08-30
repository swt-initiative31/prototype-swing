package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Open up a 2nd display from a non-UI thread. <br/>
 * <br/>
 * <strong>IMPORTANT NOTE</strong><br/>
 * This snippet has a bug: if the shell created in the non-UI thread is closed
 * first then the other shell (the one created in the UI thread) exposes buggy
 * behavior:
 * <ul>
 * <li>It will not close when the close button (the "X" button in Windows) is
 * pressed.</li>
 * <li>It will fail to repaint the background when enlarging the window.</li>
 * </ul>
 */
public class SnippetDoubleDisplay {

public static void main (String [] args) {
	new Thread(() -> doIt()).start();
	doIt();
}

private static void doIt() {
	final Display display = new Display ();
	final Color red = display.getSystemColor (SWT.COLOR_RED);
	final Color blue = display.getSystemColor (SWT.COLOR_BLUE);
	Shell shell = new Shell (display);
	shell.setText("Snippet 68");
	shell.setLayout (new RowLayout ());
	Button button = new Button (shell, SWT.PUSH);
	button.setText ("Stop Timer (Thread '" + Thread.currentThread().getName() + "')");
	final Label label = new Label (shell, SWT.BORDER);
	label.setBackground (red);
	final int time = 500;
	final Runnable timer = new Runnable () {
		@Override
		public void run () {
			if (label.isDisposed ()) return;
			Color color = label.getBackground ().equals (red) ? blue : red;
			label.setBackground (color);
			display.timerExec (time, this);
		}
	};
	display.timerExec (time, timer);
	button.addListener (SWT.Selection, event -> display.timerExec (-1, timer));
	button.pack ();
	label.setLayoutData (new RowData (button.getSize ()));
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
