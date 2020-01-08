package org.neodatis.knowledger.gui.tool.populator;

import java.util.List;

public interface IPluggableAutomaticPopulator {
	
	/**
	 *  Function that will be executed by the populator wizard for each element in the unknown entities list
	 *  
	 *  <pre>
	 *  The populator receives 3 lists
	 *  - The unknown entities list
	 *  - The available connectors
	 *  - The available right parts
	 *  
	 *  The idea is to define, for each element of the unknown list, a 3-adic relation like element 'connector' 'right part'
	 *  The 'connector' must be part of the available connectors list 
	 *  the 'right part' must be part of the available right part list
	 *  
	 *   example :
	 *   index =1
	 *   unknownEntities = (olivier,car,house)
	 *   availableconnectors = (is-instance-of, is-subclass-of)
	 *   availableEntities = (Human,Transport)
	 *   definedConnectors = (is-instance-of,?,?)
	 *   definedEntities = (Human,?,?
	 *   
	 *   The result should/could be (showing only that changes)
	 *   
	 *   definedConnectors = (is-instance-of,is-subclass-of,?)
	 *   definedEntities = (Human,Transport,?)
	 *   
	 *  
	 *  </pre>
	 *  
	 * @param index The index of the element to be defined - index of the unknownEntities list
	 * @param unknownEntities list of all element that must be defined
	 * @param availableConnectors All available connector taht can be used to define the relation 
	 * @param availableEntities Available right parts - to define the right part of the relation
	 * @param definedConnectors The already defined connectors
	 * @param definedEntities The already defined right parts 
	 */
	void execute(int index, List unknownEntities, List availableConnectors, List availableEntities, List definedConnectors, List definedEntities);

}
