/* jaDTi package - v0.6.1 */

package be.ac.ulg.montefiore.run.jadti.test;

import be.ac.ulg.montefiore.run.jadti.*;
import be.ac.ulg.montefiore.run.jadti.io.*;
import java.io.*;
import java.util.*;


/*
 * A short example program of the jaDTi library.
 */
public class Classify {

    static private ItemSet learningSet;
    static private ItemSet testSet;

    
    static public void main(String[] args) {
    		
	if (!readArgs(args))
	   System.exit(-1);
	
	System.out.println(testSet.attributeSet().size()+" | "+learningSet.attributeSet().size());
	
	AttributeSet attributes = learningSet.attributeSet();
	SymbolicAttribute goalAttribute =
	    (SymbolicAttribute) attributes.attribute(attributes.size()-1);
	
//      removeKnown(attributes.indexOf(attributes.findByName("CellId_1")),
//  		    learningSet);
//  	removeKnown(attributes.indexOf(attributes.findByName("CTT_3")),
//  		    testSet);
	
	
	System.out.println("Learning set size:" + learningSet.size());
	System.out.println("Test set size:" + testSet.size());
	
	Vector<Attribute> testAttributesVector = new Vector<Attribute>();

	for(int i =1; i<attributes.size()-1;i++){
		testAttributesVector.add(attributes.attribute(i));
	}
	//testAttributesVector.add(attributes.findByName("weather"));
	//testAttributesVector.add(attributes.findByName("temperature"));
	//testAttributesVector.add(attributes.findByName("humidity"));
	//testAttributesVector.add(attributes.findByName("wind"));
	
	AttributeSet testAttributes = new AttributeSet(testAttributesVector);
	
	System.out.println("Building tree");
	System.out.println("Goal Attribute : "+goalAttribute.toString());
	DecisionTree tree =
	    buildTree(learningSet, testAttributes, goalAttribute);
	
	//printDot(tree);

	System.out.println("Testing");
	System.out.println("Correct classification ratio: " +
			   test(tree, testSet));
    }
    
    
    /*
     * Build the decision tree.
     */
    static private DecisionTree buildTree(ItemSet learningSet, 
					  AttributeSet testAttributes, 
					  SymbolicAttribute goalAttribute) {
	SimpleDecisionTreeBuilder builder = 
	    new DecisionTreeBuilder(learningSet, testAttributes,
				    goalAttribute);
	builder.setTestScoreThreshold(0.0001 * learningSet.size());
	
	return builder.build().decisionTree();
    }
    
    
    /*
     * Remove items with a known value for the specified attribute.
     **/
    static private void removeKnown(int attributeIndex, ItemSet set) {
	for (int i = 0; i < set.size(); i++)
	    if (!set.item(i).valueOf(attributeIndex).isUnknown())
		set.remove(i--);
    }
    
    
    /*
     * Remove items with an unknown value for the specified attribute.
     **/
    static private void removeUnknown(int attributeIndex, ItemSet set) {
	for (int i = 0; i < set.size(); i++)
	    if (set.item(i).valueOf(attributeIndex).isUnknown())
		set.remove(i--);
    }
    
    
    /*
     * Prints a dot file content depicting a tree.
     */
    static private void printDot(DecisionTree tree) {
	System.out.println((new DecisionTreeToDot(tree)).produce());
    }
    
    
    /*
     * Test the tree using a test set.
     */
    static private double test(DecisionTree tree, ItemSet testSet) {
	double ratio = 0.;
	int attributeIndex = 1, nbTests = 0;
	
	for (int i = 0; i < testSet.size(); i++) {
	    Item testItem = testSet.item(i);
	    if (tree.guessGoalAttribute(testItem).
		equals(testItem.valueOf(0))){
	    	ratio++;	
	    	System.out.println("Trouvé ! Topic réel : "+testItem.valueOf(0)+" Topic deviné : "+tree.guessGoalAttribute(testItem));
	    }
	    else {
	    	System.out.println("Non trouvé ! Topic réel : "+testItem.valueOf(0)+" Topic deviné : "+tree.guessGoalAttribute(testItem));
	    }
	    	    
	    nbTests++;
	}
	
	return ratio / (double) nbTests;
    }
    
    
    /*
     * Reads command-line arguments.
     **/
    static private boolean readArgs(String[] args) {
	if (args.length != 2) {
	    System.err.println("Usage: Classify <learningDB> <testDB>");
	    return false;
	}
	
	try {
	    learningSet = ItemSetReader.read(new FileReader(args[0]));
	    //testSet = ItemSetReader.read(new FileReader(args[1]), learningSet.attributeSet());
	    testSet = ItemSetReader.read(new FileReader(args[1]));
	}
	catch(FileNotFoundException e) {
	    System.err.println("File not found.");
	    return false;
	}
	catch(IOException e) {
	    System.err.println("IO error");
	    return false;
	}
	
	return true;    
    }
}
