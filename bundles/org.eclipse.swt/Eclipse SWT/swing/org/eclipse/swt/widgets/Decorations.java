package org.eclipse.swt.widgets;

import org.eclipse.swt.*;

public class Decorations extends Canvas {


	static int checkStyle (int style) {
		if ((style & SWT.NO_TRIM) != 0) {
			style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
		} else if ((style & SWT.NO_MOVE) != 0) {
			style |= SWT.TITLE;
		}
		if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
			style |= SWT.TITLE;
		}
		return style;
	}

}
