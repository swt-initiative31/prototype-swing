package org.eclipse.swt.widgets;

import java.awt.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.*;

public class Display extends Device {

	/* Windows and Events */
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	boolean disposing;
	int sendEventCount;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer;
	Consumer<RuntimeException> runtimeExceptionHandler = DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER;
	Consumer<Error> errorHandler = DefaultExceptionHandler.RUNTIME_ERROR_HANDLER;
	Thread thread;
	boolean allowTimers = true, runAsyncMessages = true;

	/* Skinning support */
	static final int GROW_SIZE = 1024;
	Widget [] skinList = new Widget [GROW_SIZE];
	int skinCount;

	/* System Tray */
	Tray tray;
	TrayItem currentTrayItem;
	Menu trayItemMenu;

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [1];

	/* Fonts */
	boolean smallFonts;

	/* System Colors */
	int [][] colors;
	int [] alternateSelectedControlTextColor, selectedControlTextColor;
	private int [] alternateSelectedControlColor, secondarySelectedControlColor;

	/* Timer */
	Runnable timerList [];
//	NSTimer nsTimers [];

	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	Control currentControl, trackingControl, tooltipControl, ignoreFocusControl;
	Widget tooltipTarget;

	static Map<java.awt.Component, Widget> widgetMap;
	int loopCount;

	GCData[] contexts;

	/**
	 * Constructs a new instance of this class.
	 * <p>
	 * Note: The resulting display is marked as the <em>current</em>
	 * display. If this is the first display which has been
	 * constructed since the application started, it is also
	 * marked as the <em>default</em> display.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if called from a thread that already created an existing display</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see #getCurrent
	 * @see #getDefault
	 * @see Widget#checkSubclass
	 * @see Shell
	 */
	public Display () {
		this (null);
	}

	/**
	 * Constructs a new instance of this class using the parameter.
	 *
	 * @param data the device data
	 */
	public Display (DeviceData data) {
		super (data);
	}

	/**
	 * Creates the device in the operating system.  If the device
	 * does not have a handle, this method may do nothing depending
	 * on the device.
	 * <p>
	 * This method is called before <code>init</code>.
	 * </p>
	 *
	 * @param data the DeviceData which describes the receiver
	 *
	 * @see #init
	 */
	@Override
	protected void create (DeviceData data) {
		checkSubclass ();
		checkDisplay (thread = Thread.currentThread (), false);
		createDisplay (data);
		register (this);
		synchronizer = new Synchronizer (this);
		if (Default == null) Default = this;
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see Widget#checkSubclass
	 */
	protected void checkSubclass () {
		if (!Display.isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
	}

	static void checkDisplay (Thread thread, boolean multiple) {
		synchronized (Device.class) {
			for (int i=0; i<Displays.length; i++) {
				if (Displays [i] != null) {
					if (!multiple) SWT.error (SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]");
					if (Displays [i].thread == thread) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
				}
			}
		}
	}

	void createDisplay (DeviceData data) {
		// I guess this is not necessary for Swing, but most probably for UNO
	}

	static void register (Display display) {
		synchronized (Device.class) {
			for (int i=0; i<Displays.length; i++) {
				if (Displays [i] == null) {
					Displays [i] = display;
					return;
				}
			}
			Display [] newDisplays = new Display [Displays.length + 4];
			System.arraycopy (Displays, 0, newDisplays, 0, Displays.length);
			newDisplays [Displays.length] = display;
			Displays = newDisplays;
		}
	}

	/**
	 * Reads an event from the operating system's event queue,
	 * dispatches it appropriately, and returns <code>true</code>
	 * if there is potentially more work to do, or <code>false</code>
	 * if the caller can sleep until another event is placed on
	 * the event queue.
	 * <p>
	 * In addition to checking the system event queue, this method also
	 * checks if any inter-thread messages (created by <code>syncExec()</code>
	 * or <code>asyncExec()</code>) are waiting to be processed, and if
	 * so handles them before returning.
	 * </p>
	 *
	 * @return <code>false</code> if the caller can sleep upon return from this method
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
	 * </ul>
	 *
	 * @see #sleep
//	 * @see #wake
	 */
	public boolean readAndDispatch () {
		checkDevice ();
		// Not needed for Swing, maybe for UNO
		//
		// if (sendEventCount == 0 && loopCount == poolCount - 1 && Callback.getEntryCount () == 0) removePool ();
		// addPool ();
		runSkin ();
		runDeferredLayouts ();
		loopCount++;
		boolean events = false;
		try {
			events |= runSettings ();
			events |= runTimers ();
			events |= runContexts ();
			events |= runPopups ();
			// TODO
//			NSEvent event = application.nextEventMatchingMask(OS.NSAnyEventMask, null, OS.NSDefaultRunLoopMode, true);
//			if ((event != null) && (application != null)) {
//				events = true;
//				application.sendEvent(event);
//			}
			events |= runPaint ();
			events |= runDeferredEvents ();
			if (!events) {
				events = isDisposed () || runAsyncMessages (false);
			}
		} finally {
			// Not needed for Swing, maybe for UNO
			//
			// removePool ();
			loopCount--;
			// if (sendEventCount == 0 && loopCount == poolCount && Callback.getEntryCount () == 0) addPool ();
		}
		return events;
	}

	/**
	 * Causes the user-interface thread to <em>sleep</em> (that is,
	 * to be put in a state where it does not consume CPU cycles)
	 * until an event is received or it is otherwise awakened.
	 *
	 * @return <code>true</code> if an event requiring dispatching was placed on the queue.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
//	 * @see #wake
	 */
	public boolean sleep () {
		// TODO; needs to be checked what shall happen here

		checkDevice ();
		if (!synchronizer.isMessagesEmpty()) return true;
		sendPreExternalEventDispatchEvent ();
		try {
			// addPool();
			allowTimers = runAsyncMessages = false;
			//NSRunLoop.currentRunLoop().runMode(OS.NSDefaultRunLoopMode, NSDate.distantFuture());
			allowTimers = runAsyncMessages = true;
		} finally {
			// removePool();
		}
		sendPostExternalEventDispatchEvent ();
		return true;
	}

	/**
	 * If the receiver's user-interface thread was <code>sleep</code>ing,
	 * causes it to be awakened and start running again. Note that this
	 * method may be called from any thread.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @see #sleep
	 */
	public void wake () {
		synchronized (Device.class) {
			if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
			if (thread == Thread.currentThread ()) return;
			wakeThread ();
		}
	}


	boolean runAsyncMessages (boolean all) {
		return synchronizer.runAsyncMessages (all);
	}

	boolean runPaint () {
		return false;
//		if (needsDisplay == null && needsDisplayInRect == null) return false;
//		if (needsDisplay != null) {
//			long count = needsDisplay.count();
//			for (int i = 0; i < count; i++) {
//				OS.objc_msgSend(needsDisplay.objectAtIndex(i).id, OS.sel_setNeedsDisplay_, true);
//			}
//			needsDisplay.release();
//			needsDisplay = null;
//		}
//		if (needsDisplayInRect != null) {
//			long count = needsDisplayInRect.count();
//			for (int i = 0; i < count; i+=2) {
//				NSValue value = new NSValue(needsDisplayInRect.objectAtIndex(i+1));
//				OS.objc_msgSend(needsDisplayInRect.objectAtIndex(i).id, OS.sel_setNeedsDisplayInRect_, value.rectValue());
//			}
//			needsDisplayInRect.release();
//			needsDisplayInRect = null;
//		}
//		return true;
	}



	boolean runContexts () {
//		if (contexts != null) {
//			for (int i = 0; i < contexts.length; i++) {
//				if (contexts[i] != null && contexts[i].flippedContext != null) {
//					contexts[i].flippedContext.flushGraphics();
//				}
//			}
//		}
		return false;
	}

	boolean runPopups () {
		return false;
//		if (popups == null) return false;
//		boolean result = false;
//		while (popups != null) {
//			Menu menu = popups [0];
//			if (menu == null) break;
//			int length = popups.length;
//			System.arraycopy (popups, 1, popups, 0, --length);
//			popups [length] = null;
//			runDeferredEvents ();
//			if (!menu.isDisposed ()) menu._setVisible (true);
//			result = true;
//		}
//		popups = null;
//		return result;
	}

	boolean runSettings () {
		return false;
//		if (!runSettings) return false;
//		runSettings = false;
//
//		boolean ignoreColorChange = false;
//		/*
//		 * Feature in Cocoa: When dark mode is enabled on OSX version >= 10.10 and a SWT TrayItem (NSStatusItem) is present in the menubar,
//		 * changing the OSX appearance or changing the configuration of attached displays causes the textColor and textBackground color to change.
//		 * This sets the text foreground of several widgets as white and hence text is invisible. The workaround is to detect this case and prevent
//		 * the update of LIST_FOREGROUND, LIST_BACKGROUND and COLOR_WIDGET_FOREGROUND colors.
//		 */
//		if (tray != null && tray.itemCount > 0) {
//			/*
//			 * osxMode will be "Dark" when in OSX dark mode. Otherwise, it'll be null.
//			 */
//			NSString osxMode = NSUserDefaults.standardUserDefaults ().stringForKey (NSString.stringWith ("AppleInterfaceStyle"));
//			if (osxMode != null && "Dark".equals (osxMode.getString ())) {
//				ignoreColorChange = true;
//			}
//		}
//		initColors (ignoreColorChange);
//
//		sendEvent (SWT.Settings, null);
//		Shell [] shells = getShells ();
//		for (int i=0; i<shells.length; i++) {
//			Shell shell = shells [i];
//			if (!shell.isDisposed ()) {
//				shell.redraw (true);
//				shell.layout (true, true);
//			}
//		}
//		return true;
	}


	boolean runSkin () {
		if (skinCount > 0) {
			Widget [] oldSkinWidgets = skinList;
			int count = skinCount;
			skinList = new Widget[GROW_SIZE];
			skinCount = 0;
			if (eventTable != null && eventTable.hooks(SWT.Skin)) {
				for (int i = 0; i < count; i++) {
					Widget widget = oldSkinWidgets[i];
					if (widget != null && !widget.isDisposed()) {
						widget.state &= ~Widget.SKIN_NEEDED;
						oldSkinWidgets[i] = null;
						Event event = new Event ();
						event.widget = widget;
						sendEvent (SWT.Skin, event);
					}
				}
			}
			return true;
		}
		return false;
	}

	boolean runTimers () {
		if (timerList == null) return false;
		boolean result = false;
		for (int i=0; i<timerList.length; i++) {
			if (timerList [i] != null) {
				Runnable runnable = timerList [i];
				timerList [i] = null;
				if (runnable != null) {
					result = true;
					runnable.run ();
				}
			}
		}
		return result;
	}

	boolean runDeferredEvents () {
		boolean run = false;
		/*
		* Run deferred events.  This code is always
		* called  in the Display's thread so it must
		* be re-enterant need not be synchronized.
		*/
		while (eventQueue != null) {

			/* Take an event off the queue */
			Event event = eventQueue [0];
			if (event == null) break;
			int length = eventQueue.length;
			System.arraycopy (eventQueue, 1, eventQueue, 0, --length);
			eventQueue [length] = null;

			/* Run the event */
			Widget widget = event.widget;
			if (widget != null && !widget.isDisposed ()) {
				Widget item = event.item;
				if (item == null || !item.isDisposed ()) {
					run = true;
					widget.notifyListeners (event.type, event);
				}
			}

			/*
			* At this point, the event queue could
			* be null due to a recursive invokation
			* when running the event.
			*/
		}

		/* Clear the queue */
		eventQueue = null;
		return run;
	}

	boolean runDeferredLayouts () {
		if (layoutDeferredCount != 0) {
			Composite[] temp = layoutDeferred;
			int count = layoutDeferredCount;
			layoutDeferred = null;
			layoutDeferredCount = 0;
			for (int i = 0; i < count; i++) {
				Composite comp = temp[i];
				if (!comp.isDisposed()) comp.setLayoutDeferred (false);
			}
			return true;
		}
		return false;
	}




	static boolean isValidClass (Class<?> clazz) {
		String name = clazz.getName ();
		int index = name.lastIndexOf ('.');
		return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
	}

	boolean filters (int eventType) {
		if (filterTable == null) return false;
		return filterTable.hooks (eventType);
	}

	void sendEvent (int eventType, Event event) {
		if (eventTable == null && filterTable == null) {
			return;
		}
		if (event == null) event = new Event ();
		event.display = this;
		event.type = eventType;
		if (event.time == 0) event.time = getLastEventTime ();
		sendEvent (eventTable, event);
	}

	void sendEvent (EventTable table, Event event) {
		try {
			sendEventCount++;
			if (!filterEvent (event)) {
				if (table != null) {
					int type = event.type;
					sendPreEvent (type);
					try {
						table.sendEvent (event);
					} finally {
						sendPostEvent (type);
					}
				}
			}
		} finally {
			sendEventCount--;
		}
	}

	void sendPreEvent (int eventType) {
		if (eventType != SWT.PreEvent && eventType != SWT.PostEvent
				&& eventType != SWT.PreExternalEventDispatch
				&& eventType != SWT.PostExternalEventDispatch) {
			sendJDKInternalEvent (SWT.PreEvent, eventType);
		}
	}

	void sendPostEvent (int eventType) {
		if (eventType != SWT.PreEvent && eventType != SWT.PostEvent
				&& eventType != SWT.PreExternalEventDispatch
				&& eventType != SWT.PostExternalEventDispatch) {
			sendJDKInternalEvent (SWT.PostEvent, eventType);
		}
	}

	/**
	 * Sends a SWT.PreExternalEventDispatch event.
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void sendPreExternalEventDispatchEvent () {
		sendJDKInternalEvent (SWT.PreExternalEventDispatch);
	}

	/**
	 * Sends a SWT.PostExternalEventDispatch event.
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void sendPostExternalEventDispatchEvent () {
		sendJDKInternalEvent (SWT.PostExternalEventDispatch);
	}

	private void sendJDKInternalEvent(int eventType) {
		sendJDKInternalEvent(eventType, 0);
	}

	/** does sent event with JDK time**/
	private void sendJDKInternalEvent(int eventType, int detail) {
		if (eventTable == null || !eventTable.hooks (eventType)) {
			return;
		}
		Event event = new Event ();
		event.detail = detail;
		event.display = this;
		event.type = eventType;
		// time is set for debugging purpose only:
		event.time = (int) (System.nanoTime() / 1000_000L);
		if (!filterEvent (event)) {
			sendEvent (eventTable, event);
		}
	}

	boolean isValidThread () {
		return thread == Thread.currentThread ();
	}

	/**
	 * Returns the display which the currently running thread is
	 * the user-interface thread for, or null if the currently
	 * running thread is not a user-interface thread for any display.
	 *
	 * @return the current display
	 */
	public static Display getCurrent () {
		return findDisplay (Thread.currentThread ());
	}

	/**
	 * Returns the default display. One is created (making the
	 * thread that invokes this method its user-interface thread)
	 * if it did not already exist.
	 *
	 * @return the default display
	 */
	public static Display getDefault () {
		synchronized (Device.class) {
			if (Default == null) Default = new Display ();
			return Default;
		}
	}

	/**
	 * Returns the display which the given thread is the
	 * user-interface thread for, or null if the given thread
	 * is not a user-interface thread for any display.  Specifying
	 * <code>null</code> as the thread will return <code>null</code>
	 * for the display.
	 *
	 * @param thread the user-interface thread
	 * @return the display for the given thread
	 */
	public static Display findDisplay (Thread thread) {
		synchronized (Device.class) {
			for (int i=0; i<Displays.length; i++) {
				Display display = Displays [i];
				if (display != null && display.thread == thread) {
					return display;
				}
			}
			return null;
		}
	}


	void postEvent (Event event) {
		/*
		* Place the event at the end of the event queue.
		* This code is always called in the Display's
		* thread so it must be re-enterant but does not
		* need to be synchronized.
		*/
		if (eventQueue == null) eventQueue = new Event [4];
		int index = 0;
		int length = eventQueue.length;
		while (index < length) {
			if (eventQueue [index] == null) break;
			index++;
		}
		if (index == length) {
			Event [] newQueue = new Event [length + 4];
			System.arraycopy (eventQueue, 0, newQueue, 0, length);
			eventQueue = newQueue;
		}
		eventQueue [index] = event;
	}


	boolean filterEvent (Event event) {
		if (filterTable != null) {
			int type = event.type;
			sendPreEvent (type);
			try {
				filterTable.sendEvent (event);
			} finally {
				sendPostEvent (type);
			}
		}
		return false;
	}


	int getLastEventTime () {
		// TODO
		return 0;
	}

	void addSkinnableWidget (Widget widget) {
		if (skinCount >= skinList.length) {
			Widget[] newSkinWidgets = new Widget [(skinList.length + 1) * 3 / 2];
			System.arraycopy (skinList, 0, newSkinWidgets, 0, skinList.length);
			skinList = newSkinWidgets;
		}
		skinList [skinCount++] = widget;
	}

	static boolean getSheetEnabled () {
		return !"false".equals(System.getProperty("org.eclipse.swt.sheet"));
	}

	Color getWidgetColor (int id) {
		if (0 <= id && id < colors.length && colors [id] != null) {
			return Color.swing_new (this, colors [id]);
		}
		return null;
	}

	Widget getWidget (java.awt.Component view) {
		return GetWidget (view);
	}

	static Widget GetWidget (java.awt.Component view) {
		if (view == null) return null;
		return widgetMap.get(view);
	}

	void addWidget (java.awt.Component view, Widget widget) {
		if (view == null) return;

		if (widgetMap.get(view) == null) {
			widgetMap.put(view, widget);
		}
	}

	void error (int code) {
		SWT.error(code);
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Display</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
	 * </ul>
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC (GCData data) {
		return 0;
//		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
//		if (screenWindow == null) {
//			NSWindow window = (NSWindow) new NSWindow ().alloc ();
//			NSRect rect = new NSRect();
//			window = window.initWithContentRect(rect, OS.NSBorderlessWindowMask, OS.NSBackingStoreBuffered, false);
//			window.setReleasedWhenClosed(false);
//			screenWindow = window;
//		}
//		NSGraphicsContext context = screenWindow.graphicsContext();
//		if (context == null) {
//			// create a bitmap based context, which will still work e.g. for text size computations
//			// it is unclear if the bitmap needs to be larger than the text to be measured.
//			// the following values should be big enough in any case.
//			int width = 1920;
//			int height = 256;
//			NSBitmapImageRep rep = (NSBitmapImageRep) new NSBitmapImageRep().alloc();
//			rep = rep.initWithBitmapDataPlanes(0, width, height, 8, 3, false, false, OS.NSDeviceRGBColorSpace,
//					OS.NSAlphaFirstBitmapFormat, width * 4, 32);
//			context = NSGraphicsContext.graphicsContextWithBitmapImageRep(rep);
//			rep.release();
//		}
////		NSAffineTransform transform = NSAffineTransform.transform();
////		NSSize size = handle.size();
////		transform.translateXBy(0, size.height);
////		transform.scaleXBy(1, -1);
////		transform.set();
//		if (data != null) {
//			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//			if ((data.style & mask) == 0) {
//				data.style |= SWT.LEFT_TO_RIGHT;
//			}
//			data.device = this;
//			data.background = getSystemColor(SWT.COLOR_WHITE).handle;
//			data.foreground = getSystemColor(SWT.COLOR_BLACK).handle;
//			data.font = getSystemFont();
//		}
//		return context.id;
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Display</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param hDC the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public void internal_dispose_GC (long hDC, GCData data) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
	}

	/**
	 * Returns the current exception handler. It will receive all exceptions thrown by listeners
	 * and external callbacks in this display. If code wishes to temporarily replace the exception
	 * handler (for example, during a unit test), it is common practice to invoke this method prior
	 * to replacing the exception handler so that the old handler may be restored afterward.
	 *
	 * @return the current exception handler. Never <code>null</code>.
	 * @since 3.106
	 */
	public final Consumer<RuntimeException> getRuntimeExceptionHandler () {
		return runtimeExceptionHandler;
	}

	/**
	 * Returns the current exception handler. It will receive all errors thrown by listeners
	 * and external callbacks in this display. If code wishes to temporarily replace the error
	 * handler (for example, during a unit test), it is common practice to invoke this method prior
	 * to replacing the error handler so that the old handler may be restored afterward.
	 *
	 * @return the current error handler. Never <code>null</code>.
	 * @since 3.106
	 */
	public final Consumer<Error> getErrorHandler () {
		return errorHandler;
	}

	void wakeThread () {
//		TODO
//		//new pool?
//		NSObject object = new NSObject().alloc().init();
//		object.performSelectorOnMainThread(OS.sel_release, null, false);
	}

}
