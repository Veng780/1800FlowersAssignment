package eng.victor.flowers.api;

public class PatchOperation {
	
		
	private String op;
	
	private String path;
	
	private String value;

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	public static PatchOperation AddOp(String path, String value) {
		PatchOperation op = new PatchOperation();
		op.op = "add";
		op.path = path;
		op.value = value;
		return op;
	}
}
