package org.neodatis.knowledger.core;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class PropertyDoesNotExistException extends RuntimeException {

	private String id;

	private String propertyName;

	/**
	 * 
	 */
	public PropertyDoesNotExistException(String id, String propertyName) {
		super();
		this.id = id;
		this.propertyName = propertyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "entity with id " + id + " does not have property '" + propertyName+"'";
	}

}
