package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Open up a 2nd display in a non-UI thread and run its main loop too.
 */
public class SnippetDoubleDisplay {

	public static void main(String[] args) {
		new Thread(() -> doIt()).start();
		doIt();
	}

	private static void doIt() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Thread: '" + Thread.currentThread().getName() + "'");
		shell.setLayout(new GridLayout());

		Label label = new Label(shell, SWT.WRAP | SWT.BORDER);
		GridData labelData = new GridData();
		labelData.widthHint = 10;
		labelData.horizontalAlignment = SWT.FILL;
		label.setLayoutData(labelData);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Click this button and you'll see the label above change its content");
		button.addSelectionListener(
				SelectionListener.widgetSelectedAdapter(e -> label.setText(label.getText() + " CLICK!")));

		shell.pack();

		labelData.widthHint = label.getBounds().width;

		shell.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
