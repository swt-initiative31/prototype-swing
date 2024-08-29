package org.eclipse.swt.snippets;

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

	public static void main(String[] args) {
		new Thread(() -> createDisplay()).start();
		createDisplay();
	}

	private static void createDisplay() {
		Display display = new Display();
		Shell shell = new Shell(display);
		String shellText = "Thread: " + Thread.currentThread().getName();
		shell.setText(shellText);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
