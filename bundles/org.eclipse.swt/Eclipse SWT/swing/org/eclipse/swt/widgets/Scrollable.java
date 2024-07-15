package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Scrollable extends Control {
	ScrollBar horizontalBar, verticalBar;

	Scrollable() {
		/* Do nothing */
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#H_SCROLL
	 * @see SWT#V_SCROLL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Scrollable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	void createWidget() {
		super.createWidget();
		if ((style & SWT.H_SCROLL) != 0)
			horizontalBar = createScrollBar(SWT.H_SCROLL);
		if ((style & SWT.V_SCROLL) != 0)
			verticalBar = createScrollBar(SWT.V_SCROLL);
	}

	ScrollBar createScrollBar(int style) {
		// TODO

//	if (scrollView == null) return null;
//	ScrollBar bar = new ScrollBar ();
//	bar.parent = this;
//	bar.style = style;
//	bar.display = display;
//	NSScroller scroller;
//	long actionSelector;
//	NSRect rect = new NSRect();
//	if ((style & SWT.H_SCROLL) != 0) {
//		rect.width = 1;
//	} else {
//		rect.height = 1;
//	}
//	scroller = (NSScroller)new SWTScroller().alloc();
//	scroller.initWithFrame(rect);
//	if ((style & SWT.H_SCROLL) != 0) {
//		scrollView.setHorizontalScroller(scroller);
//		actionSelector = OS.sel_sendHorizontalSelection;
//	} else {
//		scrollView.setVerticalScroller(scroller);
//		actionSelector = OS.sel_sendVerticalSelection;
//	}
//	bar.view = scroller;
//	bar.createJNIRef();
//	bar.register();
//	if ((state & CANVAS) == 0) {
//		bar.target = scroller.target();
//		bar.actionSelector = scroller.action();
//	}
//	scroller.setTarget(scrollView);
//	scroller.setAction(actionSelector);
//	if ((state & CANVAS) != 0) {
//		bar.updateBar(0, 0, 100, 10);
//	}
		return null;
	}


	/**
	 * Returns a rectangle which describes the area of the
	 * receiver which is capable of displaying data (that is,
	 * not covered by the "trimmings").
	 *
	 * @return the client area
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #computeTrim
	 */
	public Rectangle getClientArea () {
		checkWidget();
// 		TODO
//		if (scrollView != null) {
//			NSSize size = scrollView.contentSize();
//			NSClipView contentView = scrollView.contentView();
//			NSRect bounds = contentView.bounds();
//			return new Rectangle((int)bounds.x, (int)bounds.y, (int)size.width, (int)size.height);
//		} else {
			java.awt.Rectangle rect = view.getBounds();
			return new Rectangle(0, 0, (int)rect.width, (int)rect.height);
//		}
	}


	/**
	 * Given a desired <em>client area</em> for the receiver
	 * (as described by the arguments), returns the bounding
	 * rectangle which would be required to produce that client
	 * area.
	 * <p>
	 * In other words, it returns a rectangle such that, if the
	 * receiver's bounds were set to that rectangle, the area
	 * of the receiver which is capable of displaying data
	 * (that is, not covered by the "trimmings") would be the
	 * rectangle described by the arguments (relative to the
	 * receiver's parent).
	 * </p>
	 *
	 * @param x the desired x coordinate of the client area
	 * @param y the desired y coordinate of the client area
	 * @param width the desired width of the client area
	 * @param height the desired height of the client area
	 * @return the required bounds to produce the given client area
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getClientArea
	 */
	public Rectangle computeTrim (int x, int y, int width, int height) {
		checkWidget();
//		if (scrollView != null) {
//			java.awt.Dimension size = new java.awt.Dimension();
//			size.width = width;
//			size.height = height;
//			int border = hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder;
//			// Always include the scroll bar in the trim even when the scroll style is overlay
//			// TODO
////			size = NSScrollView.frameSizeForContentSize(size,
////				(style & SWT.H_SCROLL) != 0 ? OS.class_NSScroller : 0,
////				(style & SWT.V_SCROLL) != 0 ? OS.class_NSScroller : 0,
////				border, OS.NSRegularControlSize, OS.NSScrollerStyleLegacy);
//			width = (int)size.width;
//			height = (int)size.height;
////			TODO
////			NSRect frame = scrollView.contentView().frame();
//			x -= frame.x;
//			y -= frame.y;
//		}
		return new Rectangle (x, y, width, height);
	}

}
