/* jaDTi package - v0.6.1 */

/*
 *  Copyright (c) 2004, Jean-Marc Francois.
 *
 *  This file is part of jaDTi.
 *  jaDTi is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  jaDTi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jahmm; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package decisionTree;


/**
 * This exception reports a all to a method that cannot be called
 * at that moment.
 */
public class CannotCallMethodException
    extends RuntimeException {
    
    /**
     * Creates a new object reporting the reading of an invalid file.
     */
    public CannotCallMethodException() {
    }
    
    /**
     * Creates a new object reporting the reading of an invalid file.
     *
     * @param s A string describing the problem.
     */
    public CannotCallMethodException(String s) {
	super(s);
    }
}
