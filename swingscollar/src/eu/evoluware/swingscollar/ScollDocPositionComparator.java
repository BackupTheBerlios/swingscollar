/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */
package eu.evoluware.swingscollar;

/*
 * This file is part of the programmer editor demo
 * Copyright (C) 2001-2005 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Syntax+Highlighting
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * See COPYING.TXT for details.
 */

import java.util.Comparator;

/**
 * A comparator appropriate for use with Collections of
 * ScollDocPositions.
 */
class ScollDocPositionComparator implements Comparator<Object> {
	public static final ScollDocPositionComparator instance = new ScollDocPositionComparator();
	
	private ScollDocPositionComparator() { }
	
    /**
     * Does this Comparator equal another?
     * Since all ScollDocPositionComparators are the same, they
     * are all equal.
     *
     * @return true for ScollDocPositionComparators, false otherwise.
     */
    public boolean equals(Object obj){
    	return this == obj;
    }

    /**
     * Compare two ScollDocPositions
     *
     * @param o1 first ScollDocPosition
     * @param o2 second ScollDocPosition
     * @return negative if first < second, 0 if equal, positive if first > second
     */
    public int compare(Object o1, Object o2){
        if (o1 instanceof ScollDocPosition && o2 instanceof ScollDocPosition){
            ScollDocPosition d1 = (ScollDocPosition)(o1);
            ScollDocPosition d2 = (ScollDocPosition)(o2);
            return (d1.getPosition() - d2.getPosition());
        } else if (o1 instanceof ScollDocPosition){
            return -1;
        } else if (o2 instanceof ScollDocPosition){
            return 1;
        } else if (o1.hashCode() < o2.hashCode()){
            return -1;
        } else if (o2.hashCode() > o1.hashCode()){
            return 1;
        } else {
            return 0;
        }
    }
}

