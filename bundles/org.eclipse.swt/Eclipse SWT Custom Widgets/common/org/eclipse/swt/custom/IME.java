package org.eclipse.swt.custom;

import org.eclipse.swt.graphics.*;

//TODO implement
public class IME {

	public IME(StyledText styledText, int none) {
		// TODO Auto-generated constructor stub
	}

	public int getCommitCount() {
		System.out.println("WARN: Not implemented yet: "+ new Throwable().getStackTrace()[0]);
		return -1;
	}

	public int getCompositionOffset() {
		System.out.println("WARN: Not implemented yet: "+ new Throwable().getStackTrace()[0]);
		return 42;
	}

	public boolean getWideCaret() {
		System.out.println("WARN: Not implemented yet: "+ new Throwable().getStackTrace()[0]);
		return false;
	}

	public int getCaretOffset() {
		System.out.println("WARN: Not implemented yet: "+ new Throwable().getStackTrace()[0]);
		return -1;
	}

	public String getText() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public int[] getRanges() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public TextStyle[] getStyles() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
