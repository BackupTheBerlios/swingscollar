/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */
package eu.evoluware.swingscollar;

/**
 * A wrapper for a position in a document appropriate for storing
 * in a collection.
 */
class ScollDocPosition {

    /**
     * The actual position
     */
    private int position;

    /**
     * Get the position represented by this ScollDocPosition
     *
     * @return the position
     */
    int getPosition(){
        return position;
    }

    /**
     * Construct a ScollDocPosition from the given offset into the document.
     *
     * @param position The position this DocObject will represent
     */
    public ScollDocPosition(int position){
        this.position = position;
    }

    /**
     * Adjust this position.
     * This is useful in cases that an amount of text is inserted
     * or removed before this position.
     *
     * @param adjustment amount (either positive or negative) to adjust this position.
     * @return the ScollDocPosition, adjusted properly.
     */
    public ScollDocPosition adjustPosition(int adjustment){
        position += adjustment;
        return this;
    }

    /**
     * Two ScollDocPositions are equal iff they have the same internal position.
     *
     * @return if this ScollDocPosition represents the same position as another.
     */
    public boolean equals(Object obj){
        if (obj instanceof ScollDocPosition){
            ScollDocPosition d = (ScollDocPosition)(obj);
            if (this.position == d.position){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * A string representation useful for debugging.
     *
     * @return A string representing the position.
     */
    public String toString(){
        return "" + position;
    }
}
