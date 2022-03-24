
public class SequenceObject {

	private String id;	
	private String sourceRef;
	private String targetRef;
	private String name;
	
	public SequenceObject() {
		super();
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSourceRef() {
		return sourceRef;
	}
	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}
	public String getTargetRef() {
		return targetRef;
	}
	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SequenceObject [id=" + id + ", name=" + name + ", sourceRef=" + sourceRef + ", targetRef=" + targetRef
				+ "]";
	}

	
	
}
