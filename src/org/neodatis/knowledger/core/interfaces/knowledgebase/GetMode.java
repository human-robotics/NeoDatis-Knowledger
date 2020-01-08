package org.neodatis.knowledger.core.interfaces.knowledgebase;

public class GetMode {
	
	private String name;
	public static final GetMode THROW_EXCEPTION_IF_DOES_NOT_EXIST = new GetMode("THROW_EXCEPTION_IF_DOES_NOT_EXIST");
	public static final GetMode CREATE_IF_DOES_NOT_EXIST = new GetMode("CREATE_IF_DOES_NOT_EXIST");
	public static final GetMode RETURN_NULL_IF_DOES_NOT_EXIST = new GetMode("RETURN_NULL_IF_DOES_NOT_EXIST");
	
	protected GetMode(String type){
		this.name= type;
	}

	public String getName() {
		return name;
	}
	/** for jdk 1.5 enum compatibility*/
	public String name(){
		return name;
	}
}
