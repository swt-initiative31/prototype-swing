package org.eclipse.swt.widgets;

import java.awt.*;

import javax.swing.*;

import org.eclipse.swt.*;

public class Composite extends Scrollable {
	Layout layout;
	int layoutCount, backgroundMode;


	/**
	 * Returns a (possibly empty) array containing the receiver's children.
	 * Children are returned in the order that they are drawn.  The topmost
	 * control appears at the beginning of the array.  Subsequent controls
	 * draw beneath this control and appear later in the array.
	 * <p>
	 * Note: This is not the actual structure used by the receiver
	 * to maintain its list of children, so modifying the array will
	 * not affect the receiver.
	 * </p>
	 *
	 * @return an array of children
	 *
//	 * @see Control#moveAbove
//	 * @see Control#moveBelow
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public Control [] getChildren () {
		checkWidget();
		return _getChildren ();
	}

	Control [] _getChildren () {
		java.awt.Container containerView = contentView();
		if (containerView == null) return new Control [0];
		Component[] component = containerView.getComponents();
		int count = (int)component.length;
		Control [] children = new Control [count];
		if (count == 0) return children;
		int j = 0;
		for (int i=0; i<count; i++){
			Widget widget = display.getWidget (component[count - i - 1]);
			if (widget != null && widget != this && widget instanceof Control) {
				children [j++] = (Control) widget;
			}
		}
		if (j == count) return children;
		Control [] newChildren = new Control [j];
		System.arraycopy (children, 0, newChildren, 0, j);
		return newChildren;
	}

	/**
	 * If the argument is <code>true</code>, causes subsequent layout
	 * operations in the receiver or any of its children to be ignored.
	 * No layout of any kind can occur in the receiver or any of its
	 * children until the flag is set to false.
	 * Layout operations that occurred while the flag was
	 * <code>true</code> are remembered and when the flag is set to
	 * <code>false</code>, the layout operations are performed in an
	 * optimized manner.  Nested calls to this method are stacked.
	 *
	 * @param defer the new defer state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #layout(boolean)
	 * @see #layout(Control[])
	 *
	 * @since 3.1
	 */
	public void setLayoutDeferred (boolean defer) {
		checkWidget();
		if (!defer) {
			if (--layoutCount == 0) {
				if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
					updateLayout (true);
				}
			}
		} else {
			layoutCount++;
		}
	}

	@Override
	void updateLayout (boolean all) {
		Composite parent = findDeferredControl ();
		if (parent != null) {
			parent.state |= LAYOUT_CHILD;
			return;
		}
		if ((state & LAYOUT_NEEDED) != 0) {
			boolean changed = (state & LAYOUT_CHANGED) != 0;
			state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
			display.runSkin ();
			layout.layout (this, changed);
		}
		if (all) {
			state &= ~LAYOUT_CHILD;
			Control [] children = _getChildren ();
			for (int i=0; i<children.length; i++) {
				children [i].updateLayout (all);
			}
		}
	}

	Composite findDeferredControl () {
		return layoutCount > 0 ? this : parent.findDeferredControl ();
	}

	@Override
	void createHandle () {
		state |= CANVAS;
		boolean scrolled = (style & (SWT.V_SCROLL | SWT.H_SCROLL)) != 0;
		if (!scrolled)  state |= THEME_BACKGROUND;
		java.awt.Rectangle rect = new java.awt.Rectangle();
//		TODO
//		if (scrolled || hasBorder ()) {
//			NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
//			scrollWidget.initWithFrame (rect);
//			scrollWidget.setDrawsBackground(false);
//			if ((style & SWT.H_SCROLL) != 0) scrollWidget.setHasHorizontalScroller(true);
//			if ((style & SWT.V_SCROLL) != 0) scrollWidget.setHasVerticalScroller(true);
//			scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
//			scrollView = scrollWidget;
//		}
		JPanel widget = new JPanel();
		widget.setBounds(rect);
//		widget.setFocusRingType(OS.NSFocusRingTypeExterior);
		view = widget;
//		if (scrollView != null) {
//			NSClipView contentView = scrollView.contentView();
//			contentView.setAutoresizesSubviews(true);
//			view.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
//		}
	}

}
