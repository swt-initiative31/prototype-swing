package org.eclipse.swt.snippets;

import org.eclipse.swt.widgets.*;

/**
 * Open up a 2nd display in the UI thread.
 */
public class SnippetDoubleDisplay {

	public static void main(String[] args) {
		new Display();
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 1");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
