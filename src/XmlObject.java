import java.util.ArrayList;
import java.util.List;

public class XmlObject {
	
	private String id;
	private String nodeName;
	private List<SequenceObject> incomingSequence;
	private List<SequenceObject> outgoingSequence;
	private Process processLevel;
	
	public XmlObject() {
		super();
		incomingSequence = new ArrayList<SequenceObject>();
		outgoingSequence = new ArrayList<SequenceObject>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public List<SequenceObject> getIncomingSequence() {
		return incomingSequence;
	}
	
	public SequenceObject getIncomingSequenceByIndex(int num) {
		return incomingSequence.get(num);
	}

	public void addIncomingSequence(SequenceObject incomingSequence) {
		this.incomingSequence.add(incomingSequence);
	}

	public List<SequenceObject> getOutgoingSequence() {
		return outgoingSequence;
	}
	
	public SequenceObject getoutgoingSequenceByIndex(int num) {
		return outgoingSequence.get(num);
	}

	public void addOutgoingSequence(SequenceObject outgoingSequence) {
		this.outgoingSequence.add(outgoingSequence);
	}

	public Process getProcessLevel() {
		return processLevel;
	}

	public void setProcessLevel(Process processLevel) {
		this.processLevel = processLevel;
	}

	@Override
	public String toString() {
		return "XmlObject [id=" + id + ", nodeName=" + nodeName + ", incomingSequence=" + incomingSequence
				+ ", outgoingSequence=" + outgoingSequence +", processLevel=" + processLevel + "]";
	}

}
