package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
 * Instances of this class manage operating system resources that
 * define how text looks when it is displayed. Fonts may be constructed
 * by providing a device and either name, size and style information
 * or a <code>FontData</code> object which encapsulates this data.
 * <p>
 * Application code must explicitly invoke the <code>Font.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see FontData
 */
public final class Font {
	/**
	 * the handle to the OS font resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * the code page of the font
	 * (Warning: This field is platform dependent)
	 * 
	 * @since 2.0
	 */
	public String codePage;

	/**
	 * The device where this image was created.
	 */
	Device device;

Font () {
}

/**	 
 * Constructs a new font given a device and font data
 * which describes the desired font's appearance.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param fd the FontData that describes the desired font (must not be null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the fd argument is null</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given font data</li>
 * </ul>
 */
public Font (Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new FontData[] {fd});
	if (device.tracking) device.new_Object(this);
}

/**	 
 * Constructs a new font given a device and font datas
 * which describes the desired font's appearance.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param fds the array of FontData that describes the desired font (must not be null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the fds argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the length of fds is zero</li>
 *    <li>ERROR_NULL_ARGUMENT - if any fd in the array is null</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given font data</li>
 * </ul>
 */
public Font (Device device, FontData[] fds) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds.length == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<fds.length; i++) {
		if (fds[i] == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	init(device, fds);
	if (device.tracking) device.new_Object(this);
}

/**	 
 * Constructs a new font given a device, a font name,
 * the height of the desired font in points, and a font
 * style.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the name argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given arguments</li>
 * </ul>
 */
public Font (Device device, String name, int height, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new FontData[]{new FontData(name, height, style)});
	if (device.tracking) device.new_Object(this);
}

/**
 * Disposes of the operating system resources associated with
 * the font. Applications must dispose of all fonts which
 * they allocate.
 */
public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	if (handle == device.systemFont.handle) return;
	OS.XmFontListFree (handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	Font font = (Font)object;
	return device == font.device && handle == font.handle;
}

/**
 * Returns the code page for the specified font list.
 *
 * @return the code page for the font list
 */
static String getCodePage (int xDisplay, int fontList) {
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, fontList)) return null;
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	int fontListEntry;
	String codePage = null;
	/* Go through each entry in the font list */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == OS.XmFONT_IS_FONT) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct,fontPtr,20 * 4);
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int length = OS.strlen(ptr);
					byte[] nameBuf = new byte[length];
					OS.memmove(nameBuf, ptr, length);
					/* Use the character encoding for the default locale */
					String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
					int start = xlfd.lastIndexOf ('-');
					if (start != -1 && start > 0) {
						start = xlfd.lastIndexOf ('-', start - 1);
						if (start != -1) {
							codePage = xlfd.substring (start + 1, xlfd.length ());
							if (codePage.indexOf ("iso") == 0) {
								if (OS.IsLinux) {
									codePage = "ISO-" + codePage.substring (3, codePage.length ());
								}
							}
						}
					}
					OS.XtFree(ptr);
					break;
				}
				propPtr += 8;
			}
		}
		else { 
			/* FontList contains a fontSet */
			
			/* Get the font set locale */
			int localePtr = OS.XLocaleOfFontSet(fontPtr);
			int length = OS.strlen (localePtr);
			byte [] locale = new byte [length + 1];
			OS.memmove (locale, localePtr, length);
			
			/* Get code page for the font set locale */
			OS.setlocale (OS.LC_CTYPE,  locale);
			int codesetPtr = OS.nl_langinfo (OS.CODESET);
			length = OS.strlen (codesetPtr);
			byte [] codeset = new byte [length];
			OS.memmove (codeset, codesetPtr, length);
			codePage = new String(Converter.mbcsToWcs(null, codeset));
			
			/* Reset the locale */
			OS.setlocale (OS.LC_CTYPE, new byte[1]);
		}
	}
	OS.XmFontListFreeFontContext(context);
	return codePage;
}

/**
 * Returns an array of <code>FontData</code>s representing the receiver.
 * On Windows, only one FontData will be returned per font. On X however, 
 * a <code>Font</code> object <em>may</em> be composed of multiple X 
 * fonts. To support this case, we return an array of font data objects.
 *
 * @return an array of font data objects describing the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontData[] getFontData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int xDisplay = device.xDisplay;
	/*
	 * Create a font context to iterate over each element in the font list.
	 * If a font context can not be created, return null.
	 */
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, handle)) return null;
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	String[] xlfds = new String[0];
	/* Go through each entry in the font list */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == OS.XmFONT_IS_FONT) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct,fontPtr,20 * 4);
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int length = OS.strlen(ptr);
					byte[] nameBuf = new byte[length];
					OS.memmove(nameBuf, ptr, length);
					/* Use the character encoding for the default locale */
					String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
					/* Add the xlfd to the array */
					String[] newXlfds = new String[xlfds.length + 1];
					System.arraycopy(xlfds, 0, newXlfds, 0, xlfds.length);
					newXlfds[newXlfds.length - 1] = xlfd;
					xlfds = newXlfds;
					OS.XtFree(ptr);
					break;
				}
				propPtr += 8;
			}
		}
		else { 
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr,fontStructPtr,fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs,fontStructPtr[0],nFonts * 4);
			for (int i = 0; i < nFonts; i++) { // Go through each fontStruct in the font set.
				OS.memmove(fontStruct,fontStructs[i],20 * 4);
				int propPtr = fontStruct.properties;
				for (int j = 0; j < fontStruct.n_properties; j++) {
					// Reef through properties looking for XAFONT
					int[] prop = new int[2];
					OS.memmove(prop, propPtr, 8);
					if (prop[0] == OS.XA_FONT) {
						/* Found it, prop[1] points to the string */
						int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
						int length = OS.strlen(ptr);
						byte[] nameBuf = new byte[length];
						OS.memmove(nameBuf, ptr, length);
						String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
						/* Add the xlfd to the array */
						String[] newXlfds = new String[xlfds.length + 1];
						System.arraycopy(xlfds, 0, newXlfds, 0, xlfds.length);
						newXlfds[newXlfds.length - 1] = xlfd;
						xlfds = newXlfds;
						OS.XFree(ptr);
						break;
					}
					propPtr += 8;
				}
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	if (xlfds.length == 0) return null;
	FontData[] fontData = new FontData[xlfds.length];
	/* Construct each fontData out of the xlfd */
	try {
		for (int i = 0; i < xlfds.length; i++) {
			fontData[i] = FontData.motif_new(xlfds[i]);
		}
	} catch (Exception e) {
		/*
		 * Some font servers, for example, xfstt, do not pass
		 * reasonable font properties to the client, so we
		 * cannot construct a FontData for these. Return null.
		 */
		return null;
	}
	return fontData;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return handle;
}

void init (Device device, FontData[] fds) {
	this.device = device;
	
	/* Change current locale if needed. Note: only the first font data is used */
	FontData firstFd = fds[0];
	if (firstFd.lang != null) {
		String lang = firstFd.lang;
		String country = firstFd.country;
		String variant = firstFd.variant;
		String osLocale = lang;
		if (country != null) osLocale += "_" + country;
		if (variant != null) osLocale += "." + variant;
		int length = osLocale.length();
		byte [] buffer = new byte[length + 1];
		for (int i=0; i<length; i++) {
			buffer[i] = (byte)osLocale.charAt(i);
		}
		OS.setlocale (OS.LC_CTYPE, buffer);
	}
	
	/* Generate desire font name */
	Point dpi = null;
	if (device.setDPI) dpi = device.getDPI();
	StringBuffer stringBuffer = new StringBuffer();
	for (int i = 0; i < fds.length; i++) {
		FontData fd = fds[i];
		int hRes = fd.horizontalResolution, vRes = fd.verticalResolution;
		if (dpi != null) {
			fd.horizontalResolution = dpi.x;
			fd.verticalResolution = dpi.y;
		}
		stringBuffer.append(fd.getXlfd());
		stringBuffer.append(',');
		fd.horizontalResolution = hRes;
		fd.verticalResolution = vRes;
	}

	/* Append simplified font name */		
	FontData newFd = new FontData();
	newFd.points = firstFd.points;
	/*
	* Bug in Motif.  In Japanese AIX only, in some cases loading a bold Japanese
	* font takes a very long time (10 minutes) when there are no Japanese bold
	* fonts available.  The fix is to wildcard the field weight.
	*/
	if (OS.IsAIX && OS.IsDBLocale) {
		stringBuffer.append(newFd.getXlfd());
	} else {
		newFd.weight = firstFd.weight;
		newFd.slant = firstFd.slant;
		stringBuffer.append(newFd.getXlfd());
		newFd.weight = null;
		newFd.slant = null;		
		stringBuffer.append(',');
		stringBuffer.append(newFd.getXlfd());
	}
	
	/* Load font list entry */		 
	boolean warnings = device.getWarnings ();
	device.setWarnings (false);
	byte[] buffer = Converter.wcsToMbcs(null, stringBuffer.toString() , true);
	int fontListEntry = OS.XmFontListEntryLoad(device.xDisplay, buffer, OS.XmFONT_IS_FONTSET, OS.XmFONTLIST_DEFAULT_TAG);
	device.setWarnings (warnings);
	if (fontListEntry != 0) {
		handle = OS.XmFontListAppendEntry(0, fontListEntry);
		OS.XmFontListEntryFree(new int[]{fontListEntry});
		int codesetPtr = OS.nl_langinfo(OS.CODESET);
		int length = OS.strlen(codesetPtr);
		byte[] codeset = new byte[length];
		OS.memmove(codeset, codesetPtr, length);
		codePage = new String(Converter.mbcsToWcs(null, codeset));
	} else {
		Font systemFont = device.systemFont;
		handle = systemFont.handle;
		codePage = systemFont.codePage;
	}
	
	/* Reset current locale if needed */
	if (firstFd.lang != null) OS.setlocale(OS.LC_CTYPE, new byte[0]);

	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

/**
 * Returns <code>true</code> if the font has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the font.
 * When a font has been disposed, it is an error to
 * invoke any other method using the font.
 *
 * @return <code>true</code> when the font is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

public static Font motif_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.device = device;
	font.handle = handle;
	font.codePage = getCodePage(device.xDisplay, handle);
	return font;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + handle + "}";
}
}
