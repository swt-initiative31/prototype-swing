package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XmTextVerifyCallbackStruct extends XmAnyCallbackStruct {
	public byte doit;		// 8
	public int currInsert;	// 12
	public int newInsert;	// 16
	public int startPos;	// 20
	public int endPos;		// 24
	public int text;		// 28
	public static final int sizeof = 32;
}
