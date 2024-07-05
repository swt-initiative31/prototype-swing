package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

public abstract class Widget {
	int style, state;
	Display display;
	EventTable eventTable;

	Object data;

	/* Global state flags */
	static final int DISPOSED = 1 << 0;
	static final int CANVAS = 1 << 1;
	static final int KEYED_DATA = 1 << 2;
	static final int DISABLED = 1 << 3;
	static final int HIDDEN = 1 << 4;
	static final int HOT = 1 << 5;
	static final int MOVED = 1 << 6;
	static final int RESIZED = 1 << 7;
	static final int EXPANDING = 1 << 8;
	static final int IGNORE_WHEEL = 1 << 9;
	static final int PARENT_BACKGROUND = 1 << 10;
	static final int THEME_BACKGROUND = 1 << 11;

	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED = 1 << 12;

	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1 << 13;

	/* A layout was requested in this widget hierachy */
	static final int LAYOUT_CHILD = 1 << 14;

	/* More global state flags */
	static final int RELEASED = 1 << 15;
	static final int DISPOSE_SENT = 1 << 16;
	static final int FOREIGN_HANDLE = 1 << 17;
	static final int DRAG_DETECT = 1 << 18;
	static final int RESIZING = 1 << 19;

	/* WebKit fixes */
	static final int WEBKIT_EVENTS_FIX = 1 << 20;
	static final String WEBKIT_EVENTS_FIX_KEY = "org.eclipse.swt.internal.webKitEventsFix"; //$NON-NLS-1$
	static final String GLCONTEXT_KEY = "org.eclipse.swt.internal.cocoa.glcontext"; //$NON-NLS-1$
	static final String STYLEDTEXT_KEY = "org.eclipse.swt.internal.cocoa.styledtext"; //$NON-NLS-1$

	static final String IS_ACTIVE = "org.eclipse.swt.internal.isActive"; //$NON-NLS-1$

	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1 << 21;

	/* Bidi "auto" text direction */
	static final int HAS_AUTO_DIRECTION = 0;

	/* Bidi flag and for auto text direction */
	static final int AUTO_TEXT_DIRECTION = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;

	/* Default size for widgets */
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;

	Widget() {
		notifyCreationTracker();
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
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     parent is disposed</li>
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
	 * @see SWT
	 * @see #checkSubclass
	 * @see #getStyle
	 */
	public Widget(Widget parent, int style) {
		checkSubclass();
		checkParent(parent);
		this.style = style;
		display = parent.display;
		reskinWidget();
		notifyCreationTracker();
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * The SWT class library is intended to be subclassed only at specific,
	 * controlled points (most notably, <code>Composite</code> and
	 * <code>Canvas</code> when implementing new widgets). This method enforces this
	 * rule unless it is overridden.
	 * </p>
	 * <p>
	 * <em>IMPORTANT:</em> By providing an implementation of this method that allows
	 * a subclass of a class which does not normally allow subclassing to be
	 * created, the implementer agrees to be fully responsible for the fact that any
	 * such subclass will likely fail between SWT releases and will be strongly
	 * platform specific. No support is provided for user-written classes which are
	 * implemented in this fashion.
	 * </p>
	 * <p>
	 * The ability to subclass outside of the allowed SWT classes is intended purely
	 * to enable those not on the SWT development team to implement patches in order
	 * to get around specific limitations in advance of when those limitations can
	 * be addressed by the team. Subclassing should not be attempted without an
	 * intimate and detailed understanding of the hierarchy.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                         allowed subclass</li>
	 *                         </ul>
	 */
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	/**
	 * Returns the receiver's style information.
	 * <p>
	 * Note that the value which is returned by this method <em>may not match</em>
	 * the value which was provided to the constructor when the receiver was
	 * created. This can occur when the underlying operating system does not support
	 * a particular combination of requested styles. For example, if the platform
	 * widget used to implement a particular SWT widget always has scroll bars, the
	 * result of calling this method would always have the <code>SWT.H_SCROLL</code>
	 * and <code>SWT.V_SCROLL</code> bits set.
	 * </p>
	 *
	 * @return the style bits
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getStyle() {
		checkWidget();
		return style;
	}

	/**
	 * Throws an <code>SWTException</code> if the receiver can not be accessed by
	 * the caller. This may include both checks on the state of the receiver and
	 * more generally on the entire execution context. This method <em>should</em>
	 * be called by widget implementors to enforce the standard SWT invariants.
	 * <p>
	 * Currently, it is an error to invoke any method (other than
	 * <code>isDisposed()</code>) on a widget that has had its
	 * <code>dispose()</code> method called. It is also an error to call widget
	 * methods from any thread that is different from the thread that created the
	 * widget.
	 * </p>
	 * <p>
	 * In future releases of SWT, there may be more or fewer error checks and
	 * exceptions may be thrown for different reasons.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	protected void checkWidget() {
		Display display = this.display;
		if (display == null)
			error(SWT.ERROR_WIDGET_DISPOSED);
		if (display.thread != Thread.currentThread())
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		if ((state & DISPOSED) != 0)
			error(SWT.ERROR_WIDGET_DISPOSED);
	}

	void reskinWidget() {
		if ((state & SKIN_NEEDED) != SKIN_NEEDED) {
			this.state |= SKIN_NEEDED;
			display.addSkinnableWidget(this);
		}
	}

	void checkParent(Widget parent) {
		if (parent == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (parent.isDisposed())
			error(SWT.ERROR_INVALID_ARGUMENT);
		parent.checkWidget();
		parent.checkOpen();
	}

	void checkOpen() {
		/* Do nothing */
	}

	void error(int code) {
		SWT.error(code);
	}

	boolean isValidSubclass() {
		return Display.isValidClass(getClass());
	}

	void createWidget() {
		// createJNIRef ();
		createHandle();
		setOrientation();
		register();
	}

	void createHandle() {
	}

	void setOrientation() {
	}

	void register() {
	}

	public boolean isDisposed() {
		return (state & DISPOSED) != 0;
	}

	public void dispose() {
		/*
		 * Note: It is valid to attempt to dispose a widget more than once. If this
		 * happens, fail silently.
		 */
		if (isDisposed())
			return;
//		if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
		release(true);
	}

	void release(boolean destroy) {
		try (ExceptionStash exceptions = new ExceptionStash()) {
			if ((state & DISPOSE_SENT) == 0) {
				state |= DISPOSE_SENT;
				try {
					sendEvent(SWT.Dispose);
				} catch (Error | RuntimeException ex) {
					exceptions.stash(ex);
				}
			}
			if ((state & DISPOSED) == 0) {
				try {
					releaseChildren(destroy);
				} catch (Error | RuntimeException ex) {
					exceptions.stash(ex);
				}
			}
			if ((state & RELEASED) == 0) {
				state |= RELEASED;
				if (destroy) {
					releaseParent();
					releaseWidget();
					destroyWidget();
				} else {
					releaseWidget();
					releaseHandle();
				}
			}
		}
		notifyDisposalTracker();
	}

	void releaseChildren(boolean destroy) {
	}

	void releaseHandle() {
		state |= DISPOSED;
		display = null;
	}

	void releaseParent() {
		/* Do nothing */
	}

	void releaseWidget() {
		deregister();
		if (display.tooltipTarget == this)
			display.tooltipTarget = null;
		eventTable = null;
		data = null;
	}

	void deregister() {
	}

	void destroyWidget() {
		releaseHandle();
	}

	void sendEvent(int eventType) {
		sendEvent(eventType, null, true);
	}

	void sendEvent(Event event) {
		display.sendEvent(eventTable, event);
	}

	void sendEvent(int eventType, Event event) {
		sendEvent(eventType, event, true);
	}

	void sendEvent(int eventType, Event event, boolean send) {
		if (eventTable == null && !display.filters(eventType)) {
			return;
		}
		if (event == null)
			event = new Event();
		event.type = eventType;
		event.display = display;
		event.widget = this;
		if (event.time == 0) {
			event.time = display.getLastEventTime();
		}
		if (send) {
			sendEvent(event);
		} else {
			display.postEvent(event);
		}
	}

	void notifyCreationTracker() {
		if (WidgetSpy.isEnabled) {
			WidgetSpy.getInstance().widgetCreated(this);
		}
	}

	void notifyDisposalTracker() {
		if (WidgetSpy.isEnabled) {
			WidgetSpy.getInstance().widgetDisposed(this);
		}
	}

	/**
	 * Notifies all of the receiver's listeners for events of the given type that
	 * one such event has occurred by invoking their <code>handleEvent()</code>
	 * method. The event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 *
	 * @param eventType the type of event which has occurred
	 * @param event     the event data
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see SWT // * @see #addListener // * @see #getListeners(int) // * @see
	 *      #removeListener(int, Listener)
	 */
	public void notifyListeners(int eventType, Event event) {
		checkWidget();
		if (event == null)
			event = new Event();
		sendEvent(eventType, event);
	}

	void checkOrientation(Widget parent) {
		style &= ~SWT.MIRRORED;
		if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
			if (parent != null) {
				if ((parent.style & SWT.LEFT_TO_RIGHT) != 0)
					style |= SWT.LEFT_TO_RIGHT;
				if ((parent.style & SWT.RIGHT_TO_LEFT) != 0)
					style |= SWT.RIGHT_TO_LEFT;
			}
		}
		style = checkBits(style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
	}

	static int checkBits(int style, int int0, int int1, int int2, int int3, int int4, int int5) {
		int mask = int0 | int1 | int2 | int3 | int4 | int5;
		if ((style & mask) == 0)
			style |= int0;
		if ((style & int0) != 0)
			style = (style & ~mask) | int0;
		if ((style & int1) != 0)
			style = (style & ~mask) | int1;
		if ((style & int2) != 0)
			style = (style & ~mask) | int2;
		if ((style & int3) != 0)
			style = (style & ~mask) | int3;
		if ((style & int4) != 0)
			style = (style & ~mask) | int4;
		if ((style & int5) != 0)
			style = (style & ~mask) | int5;
		return style;
	}

	String getNameText() {
		return "";
	}

	void reskinChildren(int flags) {
	}

	/**
	 * Marks the widget to be skinned.
	 * <p>
	 * The skin event is sent to the receiver's display when appropriate (usually
	 * before the next event is handled). Widgets are automatically marked for
	 * skinning upon creation as well as when its skin id or class changes. The skin
	 * id and/or class can be changed by calling
	 * {@link Display#setData(String, Object)} with the keys {@link SWT#SKIN_ID}
	 * and/or {@link SWT#SKIN_CLASS}. Once the skin event is sent to a widget, it
	 * will not be sent again unless <code>reskin(int)</code> is called on the
	 * widget or on an ancestor while specifying the <code>SWT.ALL</code> flag.
	 * </p>
	 * <p>
	 * The parameter <code>flags</code> may be either:
	 * </p>
	 * <dl>
	 * <dt><b>{@link SWT#ALL}</b></dt>
	 * <dd>all children in the receiver's widget tree should be skinned</dd>
	 * <dt><b>{@link SWT#NONE}</b></dt>
	 * <dd>only the receiver should be skinned</dd>
	 * </dl>
	 *
	 * @param flags the flags specifying how to reskin
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * @since 3.6
	 */
	public void reskin(int flags) {
		checkWidget();
		reskinWidget();
		if ((flags & SWT.ALL) != 0)
			reskinChildren(flags);
	}

	/**
	 * Returns the application defined widget data associated with the receiver, or
	 * null if it has not been set. The <em>widget data</em> is a single, unnamed
	 * field that is stored with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If the object stored in
	 * the widget data needs to be notified when the widget is disposed of, it is
	 * the application's responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @return the widget data
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - when the receiver has
	 *                         been disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - when called from
	 *                         the wrong thread</li>
	 *                         </ul>
	 *
	 * @see #setData(Object)
	 */
	public Object getData() {
		checkWidget();
		return (state & KEYED_DATA) != 0 ? ((Object[]) data)[0] : data;
	}

	/**
	 * Returns the application defined property of the receiver with the specified
	 * name, or null if it has not been set.
	 * <p>
	 * Applications may have associated arbitrary objects with the receiver in this
	 * fashion. If the objects stored in the properties need to be notified when the
	 * widget is disposed of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 *
	 * @param key the name of the property
	 * @return the value of the property or null if it has not been set
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the key is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see #setData(String, Object)
	 */
	public Object getData(String key) {
		checkWidget();
		if (key == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (key.equals(IS_ACTIVE))
			return Boolean.valueOf(isActive());
		if ((state & KEYED_DATA) != 0) {
			Object[] table = (Object[]) data;
			for (int i = 1; i < table.length; i += 2) {
				if (key.equals(table[i]))
					return table[i + 1];
			}
		}
		return null;
	}

	boolean isActive() {
		return true;
	}

	/**
	 * Sets the application defined widget data associated
	 * with the receiver to be the argument. The <em>widget
	 * data</em> is a single, unnamed field that is stored
	 * with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If
	 * the object stored in the widget data needs to be notified
	 * when the widget is disposed of, it is the application's
	 * responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @param data the widget data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
	 * </ul>
	 *
	 * @see #getData()
	 */
	public void setData (Object data) {
		checkWidget();
		if (WEBKIT_EVENTS_FIX_KEY.equals (data)) {
			state |= WEBKIT_EVENTS_FIX;
			return;
		}
		if (STYLEDTEXT_KEY.equals(data)) {
			setIsStyledText();
			return;
		}
		if ((state & KEYED_DATA) != 0) {
			((Object []) this.data) [0] = data;
		} else {
			this.data = data;
		}
	}

	void setIsStyledText() {
	}

	/**
	 * Sets the application defined property of the receiver
	 * with the specified name to the given value.
	 * <p>
	 * Applications may associate arbitrary objects with the
	 * receiver in this fashion. If the objects stored in the
	 * properties need to be notified when the widget is disposed
	 * of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 *
	 * @param key the name of the property
	 * @param value the new value for the property
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getData(String)
	 */
	public void setData (String key, Object value) {
		checkWidget();
		if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (GLCONTEXT_KEY.equals (key)) {
			setOpenGLContext(value);
			return;
		}
		int index = 1;
		Object [] table = null;
		if ((state & KEYED_DATA) != 0) {
			table = (Object []) data;
			while (index < table.length) {
				if (key.equals (table [index])) break;
				index += 2;
			}
		}
		if (value != null) {
			if ((state & KEYED_DATA) != 0) {
				if (index == table.length) {
					Object [] newTable = new Object [table.length + 2];
					System.arraycopy (table, 0, newTable, 0, table.length);
					data = table = newTable;
				}
			} else {
				table = new Object [3];
				table [0] = data;
				data = table;
				state |= KEYED_DATA;
			}
			table [index] = key;
			table [index + 1] = value;
		} else {
			if ((state & KEYED_DATA) != 0) {
				if (index != table.length) {
					int length = table.length - 2;
					if (length == 1) {
						data = table [0];
						state &= ~KEYED_DATA;
					} else {
						Object [] newTable = new Object [length];
						System.arraycopy (table, 0, newTable, 0, index);
						System.arraycopy (table, index + 2, newTable, index, length - index);
						data = newTable;
					}
				}
			}
		}
		if (key.equals(SWT.SKIN_CLASS) || key.equals(SWT.SKIN_ID)) this.reskin(SWT.ALL);
	}

	void setOpenGLContext(Object value) {
	}

}
