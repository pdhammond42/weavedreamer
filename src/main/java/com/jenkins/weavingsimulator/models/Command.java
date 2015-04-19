package com.jenkins.weavingsimulator.models;

/** Interface to a Command object that will operate on the draft.
 *  
 * @author pete
 *
 */
public interface Command {
	/** Perform the command. The object is expected to be bound to 
	 * whatever context it needs.
	 */
	void execute();
	/** Reverse the action of the command. The object is expected to 
	 * be bound to whatever context, in particular prior state, 
	 * it needs.
	 */
	void undo();
}
