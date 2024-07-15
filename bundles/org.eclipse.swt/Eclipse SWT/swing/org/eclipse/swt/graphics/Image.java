/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;

/**
 * Instances of this class are graphics which have been prepared for display on
 * a specific device. That is, they are ready to paint using methods such as
 * <code>GC.drawImage()</code> and display on widgets with, for example,
 * <code>Button.setImage()</code>.
 * <p>
 * If loaded from a file format that supports it, an <code>Image</code> may have
 * transparency, meaning that certain pixels are specified as being transparent
 * when drawn. Examples of file formats that support transparency are GIF and
 * PNG.
 * </p>
 * <p>
 * There are two primary ways to use <code>Images</code>. The first is to load a
 * graphic file from disk and create an <code>Image</code> from it. This is done
 * using an <code>Image</code> constructor, for example:
 *
 * <pre>
 * Image i = new Image(device, "C:\\graphic.bmp");
 * </pre>
 *
 * A graphic file may contain a color table specifying which colors the image
 * was intended to possess. In the above example, these colors will be mapped to
 * the closest available color in SWT. It is possible to get more control over
 * the mapping of colors as the image is being created, using code of the form:
 *
 * <pre>
 * ImageData data = new ImageData("C:\\graphic.bmp");
 * RGB[] rgbs = data.getRGBs();
 * // At this point, rgbs contains specifications of all
 * // the colors contained within this image. You may
 * // allocate as many of these colors as you wish by
 * // using the Color constructor Color(RGB), then
 * // create the image:
 * Image i = new Image(device, data);
 * </pre>
 * <p>
 * Applications which require even greater control over the image loading
 * process should use the support provided in class <code>ImageLoader</code>.
 * </p>
 * <p>
 * Application code must explicitly invoke the <code>Image.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see Color
 * @see ImageData
 * @see ImageLoader
 * @see <a href="http://www.eclipse.org/swt/snippets/#image">Image snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples:
 *      GraphicsExample, ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class Image extends Resource implements Drawable {

	/**
	 * specifies whether the receiver is a bitmap or an icon (one of
	 * <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API. It
	 * is marked public only so that it can be shared within the packages provided
	 * by SWT. It is not available on all platforms and should never be accessed
	 * from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int type;

	/**
	 * the handle to the OS image resource (Warning: This field is platform
	 * dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API. It
	 * is marked public only so that it can be shared within the packages provided
	 * by SWT. It is not available on all platforms and should never be accessed
	 * from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public Object handle; // Todo

	/**
	 * The GC the image is currently selected in.
	 */
	GC memGC;

	/**
	 * The global alpha value to be used for every pixel.
	 */

	/**
	 * The width of the image.
	 */
	int width = -1;

	/**
	 * The height of the image.
	 */
	int height = -1;

	/**
	 * Specifies the default scanline padding.
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

	/**
	 * ImageFileNameProvider to provide file names at various Zoom levels
	 */
	private ImageFileNameProvider imageFileNameProvider;

	/**
	 * ImageDataProvider to provide ImageData at various Zoom levels
	 */
	private ImageDataProvider imageDataProvider;

	/**
	 * Style flag used to differentiate normal, gray-scale and disabled images based
	 * on image data providers. Without this, a normal and a disabled image of the
	 * same image data provider would be considered equal.
	 */
	private int styleFlag = SWT.IMAGE_COPY;

	Image(Device device) {
		super(device);
	}

	/**
	 * Constructs an instance of this class from the given <code>ImageData</code>.
	 * <p>
	 * You must dispose the image when it is no longer required.
	 * </p>
	 *
	 * @param device the device on which to create the image
	 * @param data   the image data to create the image from (must not be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if device is
	 *                                     null and there is no current device</li>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the image
	 *                                     data is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_UNSUPPORTED_DEPTH - if the
	 *                                     depth of the ImageData is not
	 *                                     supported</li>
	 *                                     </ul>
	 * @exception SWTError
	 *                                     <ul>
	 *                                     <li>ERROR_NO_HANDLES if a handle could
	 *                                     not be obtained for image creation</li>
	 *                                     </ul>
	 *
	 * @see #dispose()
	 */
	public Image(Device device, ImageData data) {
		super(device);
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread())
//			pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			init(data);
			init();
//		} finally {
//			if (pool != null)
//				pool.release();
//		}
	}

	/**
	 * Constructs an empty instance of this class with the specified width and
	 * height. The result may be drawn upon by creating a GC and using any of its
	 * drawing operations, as shown in the following example:
	 *
	 * <pre>
	 * Image i = new Image(device, width, height);
	 * GC gc = new GC(i);
	 * gc.drawRectangle(0, 0, 50, 50);
	 * gc.dispose();
	 * </pre>
	 * <p>
	 * Note: Some platforms may have a limitation on the size of image that can be
	 * created (size depends on width, height, and depth). For example, Windows 95,
	 * 98, and ME do not allow images larger than 16M.
	 * </p>
	 * <p>
	 * You must dispose the image when it is no longer required.
	 * </p>
	 *
	 * @param device the device on which to create the image
	 * @param width  the width of the new image
	 * @param height the height of the new image
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if device is
	 *                                     null and there is no current device</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if either
	 *                                     the width or height is negative or
	 *                                     zero</li>
	 *                                     </ul>
	 * @exception SWTError
	 *                                     <ul>
	 *                                     <li>ERROR_NO_HANDLES if a handle could
	 *                                     not be obtained for image creation</li>
	 *                                     </ul>
	 *
	 * @see #dispose()
	 */
	public Image(Device device, int width, int height) {
		super(device);
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		try {
//			init(width, height);
			init();
		} finally {
//			if (pool != null) pool.release();
		}
	}

	/**
	 * Constructs a new instance of this class based on the provided image, with an
	 * appearance that varies depending on the value of the flag. The possible flag
	 * values are:
	 * <dl>
	 * <dt><b>{@link SWT#IMAGE_COPY}</b></dt>
	 * <dd>the result is an identical copy of srcImage</dd>
	 * <dt><b>{@link SWT#IMAGE_DISABLE}</b></dt>
	 * <dd>the result is a copy of srcImage which has a <em>disabled</em> look</dd>
	 * <dt><b>{@link SWT#IMAGE_GRAY}</b></dt>
	 * <dd>the result is a copy of srcImage which has a <em>gray scale</em>
	 * look</dd>
	 * </dl>
	 * <p>
	 * You must dispose the image when it is no longer required.
	 * </p>
	 *
	 * @param device   the device on which to create the image
	 * @param srcImage the image to use as the source
	 * @param flag     the style, either <code>IMAGE_COPY</code>,
	 *                 <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code>
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if device is
	 *                                     null and there is no current device</li>
	 *                                     <li>ERROR_NULL_ARGUMENT - if srcImage is
	 *                                     null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the flag
	 *                                     is not one of <code>IMAGE_COPY</code>,
	 *                                     <code>IMAGE_DISABLE</code> or
	 *                                     <code>IMAGE_GRAY</code></li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the image
	 *                                     has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_IMAGE - if the image is
	 *                                     not a bitmap or an icon, or is otherwise
	 *                                     in an invalid state</li>
	 *                                     <li>ERROR_UNSUPPORTED_DEPTH - if the
	 *                                     depth of the image is not supported</li>
	 *                                     </ul>
	 * @exception SWTError
	 *                                     <ul>
	 *                                     <li>ERROR_NO_HANDLES if a handle could
	 *                                     not be obtained for image creation</li>
	 *                                     </ul>
	 *
	 * @see #dispose()
	 */
	public Image(Device device, Image srcImage, int flag) {
		super(device);
		if (srcImage == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (srcImage.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		switch (flag) {
		case SWT.IMAGE_COPY:
		case SWT.IMAGE_DISABLE:
		case SWT.IMAGE_GRAY:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		try {
			this.type = srcImage.type;
//			/* Get source image size */
//			NSSize size = srcImage.handle.size();
//			int srcWidth = (int)size.width;
//			int srcHeight = (int)size.height;
//
//			/* Copy alpha information (transparent pixel and alpha data) for 100% & 200% image representations from source image*/
//			alphaInfo_100 = new AlphaInfo();
//			copyAlphaInfo(srcImage.alphaInfo_100, alphaInfo_100);
//			if (srcImage.alphaInfo_200 != null) {
//				alphaInfo_200 = new AlphaInfo();
//				copyAlphaInfo(srcImage.alphaInfo_200, alphaInfo_200);
//			}
//
//			/* Create the image */
//			handle = (NSImage)new NSImage().alloc();
//			handle = handle.initWithSize(size);
//			handle.setCacheMode(OS.NSImageCacheNever);
//
//			/* Create the 100% representation for the new image from source image & apply flag */
//			createRepFromSourceAndApplyFlag(srcImage.getRepresentation (100), srcWidth, srcHeight, flag);
//
//			imageFileNameProvider = srcImage.imageFileNameProvider;
//			imageDataProvider = srcImage.imageDataProvider;
//			this.styleFlag = srcImage.styleFlag | flag;
//			if (imageFileNameProvider != null || imageDataProvider != null) {
//				/* If source image has 200% representation then create the 200% representation for the new image & apply flag */
//				NSBitmapImageRep rep200 = srcImage.getRepresentation (200);
//				if (rep200 != null) createRepFromSourceAndApplyFlag(rep200, srcWidth * 2, srcHeight * 2, flag);
//			}
			init();
		} finally {
//			if (pool != null) pool.release();
		}
	}

	/**
	 * Constructs an instance of this class by loading its representation from the
	 * ImageData retrieved from the ImageDataProvider. Throws an error if an error
	 * occurs while loading the image, or if the result is an image of an
	 * unsupported type.
	 * <p>
	 * This constructor is provided for convenience for loading image as per DPI
	 * level.
	 *
	 * @param device            the device on which to create the image
	 * @param imageDataProvider the ImageDataProvider object that is to be used to
	 *                          get the ImageData
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if device is
	 *                                     null and there is no current device</li>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the
	 *                                     ImageDataProvider is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     ImageData provided by ImageDataProvider
	 *                                     is null at 100% zoom</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_IO - if an IO error occurs
	 *                                     while reading from the file</li>
	 *                                     <li>ERROR_INVALID_IMAGE - if the image
	 *                                     file contains invalid data</li>
	 *                                     <li>ERROR_UNSUPPORTED_DEPTH - if the
	 *                                     image file describes an image with an
	 *                                     unsupported depth</li>
	 *                                     <li>ERROR_UNSUPPORTED_FORMAT - if the
	 *                                     image file contains an unrecognized
	 *                                     format</li>
	 *                                     </ul>
	 * @exception SWTError
	 *                                     <ul>
	 *                                     <li>ERROR_NO_HANDLES if a handle could
	 *                                     not be obtained for image creation</li>
	 *                                     </ul>
	 * @since 3.104
	 */
	public Image(Device device, ImageDataProvider imageDataProvider) {
		super(device);
		if (imageDataProvider == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.imageDataProvider = imageDataProvider;
		ImageData data = imageDataProvider.getImageData(100);
		if (data == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			init (data);
//			init ();
//			ImageData data2x = imageDataProvider.getImageData (200);
//			if (data2x != null) {
//				alphaInfo_200 = new AlphaInfo();
//				NSBitmapImageRep rep = createRepresentation (data2x, alphaInfo_200);
//				handle.addRepresentation(rep);
//				rep.release();
//			}
//		} finally {
//			if (pool != null) pool.release();
//		}
	}

	/**
	 * Returns <code>true</code> if the image has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the image. When an image has been
	 * disposed, it is an error to invoke any other method (except
	 * {@link #dispose()}) using the image.
	 *
	 * @return <code>true</code> when the image is disposed and <code>false</code>
	 *         otherwise
	 */
	@Override
	public boolean isDisposed() {
		return handle == null;
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Image</code>. It is marked public only so that it can be shared within
	 * the packages provided by SWT. It is not available on all platforms, and
	 * should never be called from application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC(GCData data) {
		return 0;

//		if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//		if (type != SWT.BITMAP || memGC != null) {
//			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//		}
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			int scaleFactor = DPIUtil.getDeviceZoom() / 100;
//			NSBitmapImageRep imageRep = getRepresentation();
//
//			NSGraphicsContext context = NSGraphicsContext.graphicsContextWithBitmapImageRep(imageRep);
//			if (context == null) {
//				imageRep.setAlpha(false);
//				context = NSGraphicsContext.graphicsContextWithBitmapImageRep(imageRep);
//			}
//			NSGraphicsContext flippedContext = NSGraphicsContext.graphicsContextWithGraphicsPort(context.graphicsPort(), true);
//			context = flippedContext;
//			context.retain();
//			if (data != null) data.flippedContext = flippedContext;
//			NSGraphicsContext.static_saveGraphicsState();
//			NSGraphicsContext.setCurrentContext(context);
//			NSAffineTransform transform = NSAffineTransform.transform();
//			NSSize size = handle.size();
//			transform.translateXBy(0, size.height * scaleFactor);
//			transform.scaleXBy(scaleFactor, -scaleFactor);
//			transform.set();
//			NSGraphicsContext.static_restoreGraphicsState();
//			if (data != null) {
//				int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//				if ((data.style & mask) == 0) {
//					data.style |= SWT.LEFT_TO_RIGHT;
//				}
//				data.device = device;
//				data.background = device.COLOR_WHITE.handle;
//				data.foreground = device.COLOR_BLACK.handle;
//				data.font = device.systemFont;
//				data.image = this;
//			}
//			return context.id;
//		} finally {
//			if (pool != null) pool.release();
//		}
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Image</code>. It is marked public only so that it can be shared within
	 * the packages provided by SWT. It is not available on all platforms, and
	 * should never be called from application code.
	 * </p>
	 *
	 * @param hDC  the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public void internal_dispose_GC(long hDC, GCData data) {
//		long context = hDC;
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			if (context != 0) {
//				/*
//				* Bug in cocoa.  For some reason, there cases when the image pixels
//				* are not transfered from the underlining CGImage into the bitmap
//				* representation.   This only happens when bitmapData() is called.
//				*/
//				NSBitmapImageRep imageRep = getRepresentation();
//				imageRep.bitmapData();
//
//				NSGraphicsContext contextObj = new NSGraphicsContext(context);
//				contextObj.release();
//			}
////			handle.setCacheMode(OS.NSImageCacheDefault);
//		} finally {
//			if (pool != null) pool.release();
//		}
	}

	/**
	 * Returns an <code>ImageData</code> based on the receiver. Modifications made
	 * to this <code>ImageData</code> will not affect the Image.
	 *
	 * @return an <code>ImageData</code> containing the image's data and attributes
	 *         at 100% zoom level.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_INVALID_IMAGE - if the image is not a
	 *                         bitmap or an icon</li>
	 *                         </ul>
	 *
	 * @see ImageData
	 */
	public ImageData getImageData() {
		return getImageData(100);
	}

	/**
	 * Returns an {@link ImageData} for the given zoom level based on the receiver.
	 * <p>
	 * Note that this method is mainly intended to be used by custom implementations
	 * of {@link ImageDataProvider} that draw a composite image at the requested
	 * zoom level based on other images. For custom zoom levels, the image data may
	 * be an auto-scaled version of the native image and may look more blurred or
	 * mangled than expected.
	 * </p>
	 * <p>
	 * Modifications made to the returned {@code ImageData} will not affect this
	 * {@code Image}.
	 * </p>
	 *
	 * @param zoom The zoom level in % of the standard resolution (which is 1
	 *             physical monitor pixel == 1 SWT logical point). Typically 100,
	 *             150, or 200.
	 * @return an <code>ImageData</code> containing the image's data and attributes
	 *         at the given zoom level
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_INVALID_IMAGE - if the image is not a
	 *                         bitmap or an icon</li>
	 *                         </ul>
	 *
	 * @since 3.106
	 */
	public ImageData getImageData(int zoom) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//		NSAutoreleasePool pool = null;
//		if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
//		try {
//			if (zoom == 100) {
//				NSBitmapImageRep imageRep;
//				imageRep = getRepresentation (100);
//				return _getImageData(imageRep, alphaInfo_100);
//			}
//			if (zoom == 200) {
//				NSBitmapImageRep imageRep200 = getRepresentation (200);
//				if (alphaInfo_100.alphaData != null && alphaInfo_200 != null) {
//					if (alphaInfo_200.alphaData == null) initAlpha_200(imageRep200);
//				}
//				if (alphaInfo_200 == null) {
//					initAlpha_200(imageRep200);
//				}
//				return _getImageData(imageRep200, alphaInfo_200);
//			}
//		} finally {
//			if (pool != null) pool.release();
//		}
		return DPIUtil.autoScaleImageData(device, getImageData(100), zoom, 100);
	}

}
