/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Andrey Loskutov <loskutov@gmx.de> - bug 488172
 *     Stefan Xenos (Google) - bug 487254 - StyledText.getTopIndex() can return negative values
 *     Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 *     Karsten Thoms <thoms@itemis.de> - bug 528746 add getOffsetAtPoint(Point)
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

//TODO (visjee) SWING
public class StyledText extends Canvas {

	public int leftMargin;
	public int rightMargin;
	public int topMargin;
	public int bottomMargin;
	public int lineSpacing;
	public int topIndex;
	public int indent;
	public int alignment;
	public boolean hasStyleWithVariableHeight;
	public int wrapIndent;
	public boolean justify;
	public int[] tabs;
	public int verticalScrollOffset;
	public IME ime;
	public int topIndexY;
	public int caretDirection;

	public StyledText(Composite parent, int style) {
		super(parent, style);
	}

	public String getMarginColor() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public String getTopMargin() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public String getRightMargin() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public String getBottomMargin() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public int getLeftMargin() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public int getIndent() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public boolean getWordWrap() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public int getAlignment() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public boolean getJustify() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public int getCaretWidth() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public int getTopIndex() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public void setScrollBars(boolean b) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public int getVerticalScrollOffset() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public StyledTextEvent getLineBackgroundData(int offset, String text) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public Color getSelectionForeground() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public boolean getBlockSelection() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	public Color getSelectionBackground() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public boolean isFixedLineHeight() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	public int getWrapWidth() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public void paintObject(GC gc, int i, int j, int lineAscent, int descent, StyleRange style, Object object, int k) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void resetCache(int lineIndex, int i) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setCaretLocations() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public int[] getSelectionRanges() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public StyledTextEvent getLineStyleData(int lineOffset, String line) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public boolean isWordWrap() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	public int getOffsetAtLine(int lineIndex) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public String getLine(int lineIndex) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public StyledTextEvent getBidiSegments(int lineOffset, String line) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public void calculateScrollBars() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void scrollVertical(int i, boolean b) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public boolean isBidiCaret() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	public void createCaretBitmaps() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void redrawLinesBullet(int[] redrawLines) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void append(String string) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setText(String string) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public int getOffsetAtPoint(Point point) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public void addSelectionListener(SelectionListener widgetSelectedAdapter) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void addCaretListener(CaretListener listener) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setSelectionRanges(int[] newRanges) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public int getLineAtOffset(int caretOffset) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public int[] getLineTabStops(int line) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public void setLineTabStops(int i, int j, int[] ks) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setTabStops(int[] is) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setWordWrap(boolean b) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setStyleRange(StyleRange linkStyle) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public StyleRange getStyleRangeAtOffset(int offset) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public int getCaretOffset() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	public void setLineBullet(int i, int j, Bullet bullet1) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setSelectionRange(int insertPos, int length) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void insert(String string) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public void setSelection(Point selection) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public Point getSelection() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public void replaceTextRange(int i, int length, String string) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	public Object getText(int x, int i) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

}
