package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.internal.win32.DROPFILES;

/**
 * The class <code>FileTransfer</code> provides a platform specific mechanism 
 * for converting a list of files represented as a java <code>String[]</code> to a 
 * platform specific representation of the data and vice versa.  
 * Each <code>String</code> in the array contains the absolute path for a single 
 * file or directory.
 * See <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String[]</code> containing a list of files is shown 
 * below:</p>
 * 
 * <code><pre>
 *     File file1 = new File("C:\temp\file1");
 *     File file2 = new File("C:\temp\file2");
 *     String[] fileData = new String[2];
 *     fileData[0] = file1.getAbsolutePath();
 *     fileData[1] = file2.getAbsolutePath();
 * </code></pre>
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	
private FileTransfer() {}

/**
 * Returns the singleton instance of the FileTransfer class.
 *
 * @return the singleton instance of the FileTransfer class
 */
public static FileTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts a list of file names
 * represented by a java <code>String[]</code> to a platform specific representation.
 * Each <code>String</code> in the array contains the absolute path for a single 
 * file or directory.  For additional information see 
 * <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String[]</code> containing the file names to be 
 * converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {

	if (object == null || !(object instanceof String[])) {
		transferData.result = COM.E_FAIL;
		return;
	}
	
	if (isSupportedType(transferData)) {

		String[] fileNames = (String[]) object;
		StringBuffer allFiles = new StringBuffer();
		for (int i = 0; i < fileNames.length; i++) {
			allFiles.append(fileNames[i]); 
			allFiles.append('\0'); // each name is null terminated
		}
		TCHAR buffer = new TCHAR(0, allFiles.toString(), true); // there is an extra null terminator at the very end
		
		DROPFILES dropfiles = new DROPFILES();
		dropfiles.pFiles = DROPFILES.sizeof;
		dropfiles.pt_x = dropfiles.pt_y = 0;
		dropfiles.fNC = 0;
		dropfiles.fWide = COM.IsUnicode ? 1 : 0;
		
		// Allocate the memory because the caller (DropTarget) has not handed it in
		// The caller of this method must release the data when it is done with it.
		int byteCount = buffer.length() * TCHAR.sizeof;
		int newPtr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, DROPFILES.sizeof + byteCount);
		COM.MoveMemory(newPtr, dropfiles, DROPFILES.sizeof);
		COM.MoveMemory(newPtr + DROPFILES.sizeof, buffer, byteCount);
		
		transferData.stgmedium = new STGMEDIUM();
		transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
		transferData.stgmedium.unionField = newPtr;
		transferData.stgmedium.pUnkForRelease = 0;
		transferData.result = COM.S_OK;
		return;
	}
	
	// did not match the TYMED
	transferData.stgmedium = new STGMEDIUM();
	transferData.result = COM.DV_E_TYMED;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a list of file names to a java <code>String[]</code>.  
 * Each String in the array contains the absolute path for a single file or directory. 
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a list of file names if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) {
		transferData.result = COM.E_FAIL;
		return null;
	}
	
	// get file names from IDataObject
	IDataObject dataObject = new IDataObject(transferData.pIDataObject);
	dataObject.AddRef();
	
	FORMATETC formatetc = new FORMATETC();
	formatetc.cfFormat = COM.CF_HDROP;
	formatetc.ptd = 0;
	formatetc.dwAspect = COM.DVASPECT_CONTENT;
	formatetc.lindex = -1;
	formatetc.tymed = COM.TYMED_HGLOBAL;

	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;
			
	transferData.result = dataObject.GetData(formatetc, stgmedium);
	dataObject.Release();
	if (transferData.result != COM.S_OK) {
		return null;
	}
	
	// How many files are there?
	int count = COM.DragQueryFile(stgmedium.unionField, 0xFFFFFFFF, null, 0);
	String[] fileNames = new String[count];
	for (int i = 0; i < count; i++){
		// How long is the name ?
		int size = COM.DragQueryFile(stgmedium.unionField, i, null, 0) + 1;
		TCHAR lpszFile = new TCHAR(0, size);	
		// Get file name and append it to string
		COM.DragQueryFile(stgmedium.unionField, i, lpszFile, size);
		fileNames[i] = lpszFile.toString(0, lpszFile.strlen());
	}
	COM.DragFinish(stgmedium.unionField); // frees data associated with HDROP data
	return fileNames;
}
protected int[] getTypeIds(){
	return new int[] {COM.CF_HDROP};
}
protected String[] getTypeNames(){
	return new String[] {"CF_HDROP"}; //$NON-NLS-1$
}
}
