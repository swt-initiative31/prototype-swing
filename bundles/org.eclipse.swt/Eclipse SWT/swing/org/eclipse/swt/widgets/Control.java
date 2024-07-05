package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class Control extends Widget {

	public java.awt.Container view;

	Composite parent;
	int drawCount, backgroundAlpha = 255;
	int[] foreground, background;

	Object layoutData;

	Font font;

	Control() {
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
	 * @see SWT#BORDER
	 * @see SWT#LEFT_TO_RIGHT
	 * @see SWT#RIGHT_TO_LEFT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Control(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		createWidget();
	}

	/**
	 * Returns the receiver's shell. For all controls other than shells, this simply
	 * returns the control's nearest ancestor shell. Shells return themselves, even
	 * if they are children of other shells.
	 *
	 * @return the receiver's shell
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getParent
	 */
	public Shell getShell() {
		checkWidget();
		return parent.getShell();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Composite</code> or null
	 * when the receiver is a shell that was created with null or a display for a
	 * parent.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Composite getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Sets the font that the receiver will use to paint textual information to the
	 * font specified by the argument, or to the default font for that kind of
	 * control if the argument is null.
	 *
	 * @param font the new font (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setFont(Font font) {
		checkWidget();
		if (font != null) {
			if (font.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.font = font;
		setFont(font != null ? font.handle : defaultFont().handle);
	}

	void setFont(java.awt.Font font) {
		if (view instanceof java.awt.Component) {
			((java.awt.Component) view).setFont(font);
		}
	}

	@Override
	void createWidget() {
		state |= DRAG_DETECT;
		checkOrientation(parent);
		super.createWidget();
		checkBackground();
		checkBuffered();
		setDefaultFont();
		setZOrder();
		setRelations();
		if ((state & PARENT_BACKGROUND) != 0) {
			setBackground();
		}
	}

	void checkBackground() {
		Shell shell = getShell();
		if (this == shell)
			return;
		state &= ~PARENT_BACKGROUND;
		Composite composite = parent;
		do {
			int mode = composite.backgroundMode;
			if (mode != 0 || backgroundAlpha == 0) {
				if (mode == SWT.INHERIT_DEFAULT || backgroundAlpha == 0) {
					Control control = this;
					do {
						if ((control.state & THEME_BACKGROUND) == 0) {
							return;
						}
						control = control.parent;
					} while (control != composite);
				}
				state |= PARENT_BACKGROUND;
				return;
			}
			if (composite == shell)
				break;
			composite = composite.parent;
		} while (true);
	}

	void checkBuffered() {
		style |= SWT.DOUBLE_BUFFERED;
	}

	void setDefaultFont() {
		if (display.smallFonts) {
			setFont(defaultFont().handle);
			setSmallSize();
		}
	}

	Font defaultFont() {
		if (display.smallFonts)
			return display.getSystemFont();
		return Font.swing_new(display, defaultNSFont());
	}

	void setSmallSize() {
		// TODO
//		if (view instanceof java.awt.Component) {
//			NSCell cell = ((java.awt.Component)view).cell();
//			if (cell != null) cell.setControlSize (OS.NSSmallControlSize);
//		}
	}

	java.awt.Font defaultNSFont() {
		return display.getSystemFont().handle;
	}

	void setZOrder() {
		java.awt.Container topView = topView();
		// TODO
//		parent.contentView().add(topView, OS.NSWindowBelow, null);
	}

	java.awt.Container topView() {
		return view;
	}

	java.awt.Container contentView() {
		return view;
	}

	void setRelations() {
		if (parent == null)
			return;
		Control[] children = parent._getChildren();
		int count = children.length;
		if (count > 1) {
			/*
			 * the receiver is the last item in the list, so its predecessor will be the
			 * second-last item in the list
			 */
			Control child = children[count - 2];
			if (child != this) {
				child.addRelation(this);
			}
		}
	}

	void addRelation(Control control) {
	}

	void setBackground() {
		if (!drawsBackground())
			return;
		Control control = findBackgroundControl();
		if (control == null)
			control = this;
//		if (control.backgroundImage != null) {
//			setBackgroundImage (control.backgroundImage.handle);
//		} else {
		int[] color = control.background != null ? control.background : control.defaultBackground().handle;
		java.awt.Color awtColor = new java.awt.Color(color[0], color[1], color[2], color[3]);
		setBackgroundColor(awtColor);
//		}
	}

	boolean drawsBackground() {
		return true;
	}

	Control findBackgroundControl() {
		if ((// backgroundImage != null ||
		background != null) && backgroundAlpha > 0)
			return this;
		return (parent != null && !isTransparent() && (state & PARENT_BACKGROUND) != 0) ? parent.findBackgroundControl()
				: null;
	}

	Color defaultBackground() {
		return display.getWidgetColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	void setBackgroundColor(java.awt.Color nsColor) {
	}

	boolean isTransparent() {
		if (background != null)
			return false;
		return parent.isTransparent();
	}

	void updateLayout(boolean all) {
		/* Do nothing */
	}

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * control need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 *
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the control
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * control need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the receiver's
	 * <em>contents</em> have changed, therefore any caches that a layout manager
	 * containing the control may have been keeping need to be flushed. When the
	 * control is resized, the changed flag will be <code>false</code>, so layout
	 * manager caches can be retained.
	 * </p>
	 *
	 * @param wHint   the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint   the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed <code>true</code> if the control's contents have changed, and
	 *                <code>false</code> otherwise
	 * @return the preferred size of the control.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		int border = getBorderWidth();
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
	}

	/**
	 * Returns the receiver's border width in points.
	 *
	 * @return the border width
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getBorderWidth() {
		checkWidget();
		return 0;
	}

	/**
	 * Sets the receiver's size and location in points to the rectangular area
	 * specified by the arguments. The <code>x</code> and <code>y</code> arguments
	 * are relative to the receiver's parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the <code>x</code> and
	 * <code>y</code> arguments are relative to the display.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param x      the new x coordinate for the receiver
	 * @param y      the new y coordinate for the receiver
	 * @param width  the new width for the receiver
	 * @param height the new height for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		setBounds(x, y, Math.max(0, width), Math.max(0, height), true, true);
	}

	void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		/*
		 * Bug in Cocoa. On Mac 10.8, a text control loses and gains focus when its
		 * bounds changes. The fix is to ignore these events.
		 */
		Display display = this.display;
		Control oldIgnoreFocusControl = display.ignoreFocusControl;
		display.ignoreFocusControl = this;
		java.awt.Container topView = topView();
		if (move && resize) {
			topView.setBounds(x, y, width, height);
		} else if (move) {
			topView.setLocation(x, y);
		} else if (resize) {
			topView.setSize(width, height);
		}
		display.ignoreFocusControl = oldIgnoreFocusControl;
	}

	/**
	 * Sets the receiver's size and location in points to the rectangular area
	 * specified by the argument. The <code>x</code> and <code>y</code> fields of
	 * the rectangle are relative to the receiver's parent (or its display if its
	 * parent is null).
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param rect the new bounds for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setBounds(Rectangle rect) {
		checkWidget();
		if (rect == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setBounds(rect.x, rect.y, Math.max(0, rect.width), Math.max(0, rect.height), true, true);
	}

	/**
	 * Returns layout data which is associated with the receiver.
	 *
	 * @return the receiver's layout data
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Object getLayoutData() {
		checkWidget();
		return layoutData;
	}

	/**
	 * Sets the layout data associated with the receiver to the argument.
	 *
	 * @param layoutData the new layout data for the receiver.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setLayoutData(Object layoutData) {
		checkWidget();
		this.layoutData = layoutData;
	}

	boolean hasBorder () {
		return (style & SWT.BORDER) != 0;
	}


}
