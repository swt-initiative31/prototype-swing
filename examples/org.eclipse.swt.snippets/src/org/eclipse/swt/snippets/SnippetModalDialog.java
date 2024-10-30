package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Open up a 2nd display in a non-UI thread and run its main loop too.
 */
public class SnippetModalDialog {

	public static void main(String[] args) {
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
				SelectionListener.widgetSelectedAdapter(e -> addText(label, "CLICK!")));

		display.timerExec(1_000, () -> {
			marquise(display, button);

		});

		Button openMsgBox = new Button(shell, SWT.PUSH);
		openMsgBox.setText("Open Msg box");
		openMsgBox.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.APPLICATION_MODAL| SWT.PRIMARY_MODAL| SWT.SYSTEM_MODAL);
			if (mb.open() == SWT.OK)
				addText(label, "OK");
			else
				addText(label, "NOT OK");
		}));
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

	private static void marquise(Display display, Button button) {
		String t = button.getText();
		t = t.substring(1) + t.substring(0, 1);

		button.setText(t);

		display.timerExec(1_000, () -> marquise(display, button));
	}

	private static void addText(Label label, String text) {
		label.setText(label.getText() + " " + text);
	}

}
