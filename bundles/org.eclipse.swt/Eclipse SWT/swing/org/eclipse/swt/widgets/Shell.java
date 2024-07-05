package org.eclipse.swt.widgets;

import java.awt.*;

import javax.swing.*;

import org.eclipse.swt.*;


public class Shell extends Decorations {

	JFrame window = null;
	boolean opened, moved, resized, fullScreen, center, deferFlushing, scrolling, isPopup;


	/**
	 * Constructs a new instance of this class. This is equivalent
	 * to calling <code>Shell((Display) null)</code>.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 */
	public Shell () {
		this ((Display) null);
	}

	/**
	 * Constructs a new instance of this class given only the style
	 * value describing its behavior and appearance. This is equivalent
	 * to calling <code>Shell((Display) null, style)</code>.
	 * <p>
	 * The style value is either one of the style constants defined in
	 * class <code>SWT</code> which is applicable to instances of this
	 * class, or must be built by <em>bitwise OR</em>'ing together
	 * (that is, using the <code>int</code> "|" operator) two or more
	 * of those <code>SWT</code> style constants. The class description
	 * lists the style constants that are applicable to the class.
	 * Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param style the style of control to construct
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#NO_MOVE
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 * @see SWT#SHEET
	 */
	public Shell (int style) {
		this ((Display) null, style);
	}

	/**
	 * Constructs a new instance of this class given only the display
	 * to create it on. It is created with style <code>SWT.SHELL_TRIM</code>.
	 * <p>
	 * Note: Currently, null can be passed in for the display argument.
	 * This has the effect of creating the shell on the currently active
	 * display if there is one. If there is no current display, the
	 * shell is created on a "default" display. <b>Passing in null as
	 * the display argument is not considered to be good coding style,
	 * and may not be supported in a future release of SWT.</b>
	 * </p>
	 *
	 * @param display the display to create the shell on
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 */
	public Shell (Display display) {
		this (display, SWT.SHELL_TRIM);
	}

	/**
	 * Constructs a new instance of this class given the display
	 * to create it on and a style value describing its behavior
	 * and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in
	 * class <code>SWT</code> which is applicable to instances of this
	 * class, or must be built by <em>bitwise OR</em>'ing together
	 * (that is, using the <code>int</code> "|" operator) two or more
	 * of those <code>SWT</code> style constants. The class description
	 * lists the style constants that are applicable to the class.
	 * Style bits are also inherited from superclasses.
	 * </p><p>
	 * Note: Currently, null can be passed in for the display argument.
	 * This has the effect of creating the shell on the currently active
	 * display if there is one. If there is no current display, the
	 * shell is created on a "default" display. <b>Passing in null as
	 * the display argument is not considered to be good coding style,
	 * and may not be supported in a future release of SWT.</b>
	 * </p>
	 *
	 * @param display the display to create the shell on
	 * @param style the style of control to construct
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#NO_MOVE
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 * @see SWT#SHEET
	 */
	public Shell (Display display, int style) {
		this (display, null, style, 0, false);
	}

	Shell (Display display, Shell parent, int style, long handle, boolean embedded) {

		super ();
		checkSubclass ();
		if (display == null) display = Display.getCurrent ();
		if (display == null) display = Display.getDefault ();
		if (!display.isValidThread ()) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if (parent != null && parent.isDisposed ()) {
			error (SWT.ERROR_INVALID_ARGUMENT);
		}
		if (!Display.getSheetEnabled ()) {
			this.center = parent != null && (style & SWT.SHEET) != 0;
		}
		this.style = checkStyle (style);
		this.parent = parent;
		this.display = display;
		if (handle != 0) {
			if (embedded) {
//				view = new NSView(handle);
			} else {
				state |= FOREIGN_HANDLE;
			}
		}
		reskinWidget();
		createWidget ();
	}

	static int checkStyle (Shell parent, int style) {
		style = Decorations.checkStyle (style);
		style &= ~SWT.TRANSPARENT;
		int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
		if ((style & SWT.SHEET) != 0) {
			if (Display.getSheetEnabled ()) {
				style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX);
				if (parent == null) {
					style &= ~SWT.SHEET;
					style |= SWT.SHELL_TRIM;
				}
			} else {
				style &= ~SWT.SHEET;
				style |= parent == null ? SWT.SHELL_TRIM : SWT.DIALOG_TRIM;
			}
			if ((style & mask) == 0) {
				style |= parent == null ? SWT.APPLICATION_MODAL : SWT.PRIMARY_MODAL;
			}
		}
		int bits = style & ~mask;
		if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
		if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
		if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
		return bits;
	}



	public void setText(String text) {
		window.setTitle("Swing PoC: " + text);

	}

	public void open() {
		window.setVisible(true);
	}

	@Override
	public boolean isDisposed() {
		return window == null;
	}

	@Override
	public Shell getShell () {
		checkWidget();
		return this;
	}

	@Override
	void createHandle () {
		state |= HIDDEN;
		if (window == null && view == null) {
			window = new JFrame();
//			int styleMask = OS.NSBorderlessWindowMask;
//			if ((style & (SWT.TOOL | SWT.SHEET)) != 0) {
//				window = (NSWindow) new SWTWindow().alloc ();
//				if ((style & SWT.SHEET) != 0) {
//					styleMask |= OS.NSDocModalWindowMask;
//				} else {
//					styleMask |= OS.NSUtilityWindowMask | OS.NSNonactivatingPanelMask;
//				}
//			} else {
//				window = (NSWindow) new SWTWindow().alloc ();
//			}
//			if ((style & SWT.NO_TRIM) == 0) {
//				if ((style & SWT.TITLE) != 0) styleMask |= OS.NSTitledWindowMask;
//				if ((style & SWT.CLOSE) != 0) styleMask |= OS.NSClosableWindowMask;
//				if ((style & SWT.MIN) != 0) styleMask |= OS.NSMiniaturizableWindowMask;
//				if ((style & SWT.MAX) != 0) styleMask |= OS.NSResizableWindowMask;
//				if ((style & SWT.RESIZE) != 0) styleMask |= OS.NSResizableWindowMask;
//			}
//			NSScreen screen = null;
//			NSScreen primaryScreen = new NSScreen(NSScreen.screens().objectAtIndex(0));
//			if (parent != null) screen = parentWindow ().screen();
//			if (screen == null) screen = primaryScreen;
//			window = window.initWithContentRect(new NSRect(), styleMask, OS.NSBackingStoreBuffered, (style & SWT.ON_TOP) != 0, screen);			frame.in
//			if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || (style & (SWT.TOOL | SWT.SHEET)) != 0) {
//				window.setHasShadow (true);  // NOT SUPPORTED BY SWING
//			}
//			if ((style & SWT.NO_MOVE) != 0) {
//				window.setMovable(false);    // NOT SUPPORTED BY SWING
//			}
//			if ((style & SWT.TOOL) != 0) {
//				// Feature in Cocoa: NSPanels that use NSUtilityWindowMask are always promoted to the floating window layer.
//				// Fix is to call setFloatingPanel:NO, which turns off this behavior.
//				((NSPanel)window).setFloatingPanel(false);
//				// By default, panels hide on deactivation.
//				((NSPanel)window).setHidesOnDeactivate(false);
//				// Normally a panel doesn't become key unless something inside it needs to be first responder.
//				// TOOL shells always become key, so disable that behavior.
//				((NSPanel)window).setBecomesKeyOnlyIfNeeded(false);
//			}
			window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //window.setReleasedWhenClosed(true);
//			if ((style & SWT.NO_TRIM) == 0) {
//				NSSize size = window.minSize();
//				size.width = NSWindow.minFrameWidthWithTitle(NSString.string(), styleMask);
//				window.setMinSize(size);
//			}
//			if (fixResize ()) {
//				if (window.respondsToSelector(OS.sel_setMovable_)) {
//					OS.objc_msgSend(window.id, OS.sel_setMovable_, 0);
//				}
//			}
//			display.cascadeWindow(window, screen);
			java.awt.Dimension screenFrame = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screenFrame.width * 5 / 8;
			int height = screenFrame.height * 5 / 8;
			java.awt.Dimension frameSize = window.getSize();
			java.awt.Point frameLocation = window.getLocation();
			frameLocation.y = screenFrame.height - ((screenFrame.height - (frameLocation.y + frameSize.height)) + height);
			frameSize.width = width;
			frameSize.height = height;
			window.setSize(frameSize);
			window.setLocation(frameLocation);
//			if ((style & SWT.ON_TOP) != 0) {
//				window.setLevel(OS.NSStatusWindowLevel);
//			}
			super.createHandle ();
			topView().setVisible(false);
		} else {
			state &= ~HIDDEN;

			if (window != null) {
				// In the FOREIGN_HANDLE case, 'window' is an NSWindow created on our behalf.
				// It may already have a content view, so if it does, grab and retain, since we release()
				// the view at disposal time.  Otherwise, create a new 'view' that will be used as the window's
				// content view in setZOrder.
				view = window.getContentPane();

				if (view == null) {
					super.createHandle();
//				} else {
//					TODO ? view.retain();
				}
			} else {
				// In the embedded case, 'view' is already set to the NSView we should add the window's content view to as a subview.
				// In that case we will hold on to the foreign view, create our own SWTCanvasView (which overwrites 'view') and then
				// add it to the foreign view.
				super.createHandle();
				view.add(topView());
			}

			style |= SWT.NO_BACKGROUND;
		}

//		windowDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();
//
//		if (window == null) {
//			NSWindow hostWindow = view.window();
//			attachObserversToWindow(hostWindow);
//		} else {
//			int behavior;
//			if (parent != null) {
//				behavior = OS.NSWindowCollectionBehaviorMoveToActiveSpace;
//			} else if ((style & SWT.TOOL) != 0) {
//				behavior = OS.NSWindowCollectionBehaviorFullScreenAuxiliary;
//			} else {
//				behavior = OS.NSWindowCollectionBehaviorFullScreenPrimary;
//			}
//			window.setCollectionBehavior(behavior);
//			window.setAcceptsMouseMovedEvents(true);
//			window.setDelegate(windowDelegate);
//		}
//
//		NSWindow fieldEditorWindow = window;
//		if (fieldEditorWindow == null) fieldEditorWindow = view.window();
//		id id = fieldEditorWindow.fieldEditor (true, null);
//		if (id != null) {
//			OS.object_setClass (id.id, OS.objc_getClass ("SWTEditorView"));
//			new NSTextView(id).setAllowsUndo(true);
//		}

	}


}
