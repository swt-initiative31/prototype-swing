package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.SerializableCompatibility;

/**
 * Instances of this class represent places on the (x, y)
 * coordinate plane.
 * <p>
 * The coordinate space for rectangles and points is considered
 * to have increasing values downward and to the right from its
 * origin making this the normal, computer graphics oriented notion
 * of (x, y) coordinates rather than the strict mathematical one.
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 *
 * @see Rectangle
 */

public final class Point implements SerializableCompatibility {
	
	/**
	 * the x coordinate of the point
	 */
	public int x;
	
	/**
	 * the y coordinate of the point
	 */
	public int y;
	
/**
 * Constructs a new point with the given x and y coordinates.
 *
 * @param x the x coordinate of the new point
 * @param y the y coordinate of the new point
 */
public Point (int x, int y) {
	this.x = x;
	this.y = y;
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
	if (!(object instanceof Point)) return false;
	Point p = (Point)object;
	return (p.x == this.x) && (p.y == this.y);
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
	return x ^ y;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the point
 */
public String toString () {
	return "Point {" + x + ", " + y + "}";
}

}

