package org.neodatis.knowledger.core.interfaces.knowledgebase;

import java.util.List;


/** The interface for data repositories
 * 
 * @author olivier
 *
 */
public interface ExternalRepository {

	void create(Object object) throws Exception;

	void update(Object object) throws Exception;

	void delete(Object object) throws Exception;

	void delete(List objects) throws Exception;

	void close();

}