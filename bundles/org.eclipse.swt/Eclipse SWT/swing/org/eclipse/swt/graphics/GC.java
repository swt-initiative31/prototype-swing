/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are
 * supported by SWT are located. Instances are used to draw on either an
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * </dl>
 *
 * <p>
 * The SWT drawing coordinate system is the two-dimensional space with the origin
 * (0,0) at the top left corner of the drawing area and with (x,y) values increasing
 * to the right and downward respectively.
 * </p>
 *
 * <p>
 * The result of drawing on an image that was created with an indexed
 * palette using a color that is not in the palette is platform specific.
 * Some platforms will match to the nearest color while other will draw
 * the color itself. This happens because the allocated image might use
 * a direct palette on platforms that do not support indexed palette.
 * </p>
 *
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 *
 * <p>
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#gc">GC snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class GC extends Resource {
	public Object handle; //TODO set the right type

	GC() {
	}

	/**
	 * Constructs a new instance of this class which has been
	 * configured to draw on the specified drawable. Sets the
	 * foreground color, background color and font in the GC
	 * to match those in the drawable.
	 * <p>
	 * You must dispose the graphics context when it is no longer required.
	 * </p>
	 * @param drawable the drawable to draw on
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
	 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
	 *    <li>ERROR_INVALID_ARGUMENT
	 *          - if the drawable is an image that is not a bitmap or an icon
	 *          - if the drawable is an image or printer that is already selected
	 *            into another graphics context</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
	 * </ul>
	 * @see #dispose()
	 */
	public GC(Drawable drawable) {
		this(drawable, 0);
	}

	/**
	 * Constructs a new instance of this class which has been
	 * configured to draw on the specified drawable. Sets the
	 * foreground color, background color and font in the GC
	 * to match those in the drawable.
	 * <p>
	 * You must dispose the graphics context when it is no longer required.
	 * </p>
	 *
	 * @param drawable the drawable to draw on
	 * @param style the style of GC to construct
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
	 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
	 *    <li>ERROR_INVALID_ARGUMENT
	 *          - if the drawable is an image that is not a bitmap or an icon
	 *          - if the drawable is an image or printer that is already selected
	 *            into another graphics context</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
	 * </ul>
	 *
	 * @see #dispose()
	 *
	 * @since 2.1.2
	 */
	public GC(Drawable drawable, int style) {
		if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			GCData data = new GCData();
//			data.style = checkStyle(style);
//			long contextId = drawable.internal_new_GC(data);
//			Device device = data.device;
//			if (device == null) device = Device.getDevice();
//			if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//			this.device = data.device = device;
//			init(drawable, data, contextId);
//			init();
//		} finally {
//			if (pool != null) pool.release();
//		}
	}

	/**
	 * Sets the receiver's anti-aliasing value to the parameter,
	 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
	 * or <code>SWT.ON</code>. Note that this controls anti-aliasing for all
	 * <em>non-text drawing</em> operations.
	 * <p>
	 * This operation requires the operating system's advanced
	 * graphics subsystem which may not be available on some
	 * platforms.
	 * </p>
	 *
	 * @param antialias the anti-aliasing setting
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
	 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
	 * </ul>
	 *
//	 * @see #getAdvanced
//	 * @see #setAdvanced
//	 * @see #setTextAntialias
	 *
	 * @since 3.1
	 */
	public void setAntialias(int antialias) {
		if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//		boolean mode = true;
//		switch (antialias) {
//			case SWT.DEFAULT:
//				/* Printer is off by default */
//				if (!handle.isDrawingToScreen()) mode = false;
//				break;
//			case SWT.OFF: mode = false; break;
//			case SWT.ON: mode = true; break;
//			default:
//				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//		}
//		data.antialias = antialias;
//		handle.setShouldAntialias(mode);
	}

	/**
	 * Returns <code>true</code> if the GC has been disposed, and <code>false</code>
	 * otherwise.
	 * <p>
	 * This method gets the dispose state for the GC. When a GC has been disposed,
	 * it is an error to invoke any other method (except {@link #dispose()}) using
	 * the GC.
	 *
	 * @return <code>true</code> when the GC is disposed and <code>false</code>
	 *         otherwise
	 */
	@Override
	public boolean isDisposed() {
		return handle == null;
	}

	/**
	 * Draws the given image in the receiver at the specified
	 * coordinates.
	 *
	 * @param image the image to draw
	 * @param x the x coordinate of where to draw
	 * @param y the y coordinate of where to draw
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li></ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
	 * </ul>
	 */
	public void drawImage(Image image, int x, int y) {
		if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
	}

	/**
	 * Copies a rectangular area from the source image into a (potentially
	 * different sized) rectangular area in the receiver. If the source
	 * and destination areas are of differing sizes, then the source
	 * area will be stretched or shrunk to fit the destination area
	 * as it is copied. The copy fails if any part of the source rectangle
	 * lies outside the bounds of the source image, or if any of the width
	 * or height arguments are negative.
	 *
	 * @param image the source image
	 * @param srcX the x coordinate in the source image to copy from
	 * @param srcY the y coordinate in the source image to copy from
	 * @param srcWidth the width in points to copy from the source
	 * @param srcHeight the height in points to copy from the source
	 * @param destX the x coordinate in the destination to copy to
	 * @param destY the y coordinate in the destination to copy to
	 * @param destWidth the width in points of the destination rectangle
	 * @param destHeight the height in points of the destination rectangle
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if any of the width or height arguments are negative.
	 *    <li>ERROR_INVALID_ARGUMENT - if the source rectangle is not contained within the bounds of the source image</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
	 * </ul>
	 */
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
		if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
		if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);
	}

	void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
//		NSImage imageHandle = srcImage.handle;
//		NSSize size = imageHandle.size();
//		int imgWidth = (int)size.width;
//		int imgHeight = (int)size.height;
//		if (simple) {
//			srcWidth = destWidth = imgWidth;
//			srcHeight = destHeight = imgHeight;
//		} else {
//			simple = srcX == 0 && srcY == 0 &&
//				srcWidth == destWidth && destWidth == imgWidth &&
//				srcHeight == destHeight && destHeight == imgHeight;
//			if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
//				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//			}
//		}
//		NSAutoreleasePool pool = checkGC(CLIPPING | TRANSFORM);
//		try {
//			if (srcImage.memGC != null) {
//				srcImage.createAlpha();
//			}
//			handle.saveGraphicsState();
//			NSAffineTransform transform = NSAffineTransform.transform();
//			transform.scaleXBy(1, -1);
//			transform.translateXBy(0, -(destHeight + 2 * destY));
//			transform.concat();
//			NSRect srcRect = new NSRect();
//			srcRect.x = srcX;
//			srcRect.y = imgHeight - (srcY + srcHeight);
//			srcRect.width = srcWidth;
//			srcRect.height = srcHeight;
//			NSRect destRect = new NSRect();
//			destRect.x = destX;
//			destRect.y = destY;
//			destRect.width = destWidth;
//			destRect.height = destHeight;
//			imageHandle.drawInRect(destRect, srcRect, OS.NSCompositeSourceOver, data.alpha / 255f);
//			handle.restoreGraphicsState();
//		} finally {
//			uncheckGC(pool);
//		}
	}
}
