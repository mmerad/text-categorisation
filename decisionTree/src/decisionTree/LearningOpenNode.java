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
 * A open node implementing the {@link LearningNode LearningNode}
 * interface.
 **/
public class LearningOpenNode 
    extends OpenNode implements LearningNode {
    
    private final ItemSet learningSet;

    
    /**
     * Creates a new learning open node.
     *
     * @param weight The node weight.
     **/
    public LearningOpenNode(double weight, ItemSet learningSet) {
	super(weight);
	
	this.learningSet = learningSet;
    }
    
    public void replace(Node node) {
	if (!(node instanceof LearningNode))
	    throw new IllegalArgumentException("A learning node can only " +
					       "be replaced by another "+
					       "learning node");
	super.replace(node);
    }
    
    
    public ItemSet learningSet() {
	return learningSet;
    }
}
