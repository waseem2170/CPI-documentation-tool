
public class Process {
	
	private String processId;
	private String processLevel;
	private String processName;
	
	public Process() {
		super();
		
	}
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessLevel() {
		return processLevel;
	}
	public void setProcessLevel(String processLevel) {
		this.processLevel = processLevel;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String toString() {
		return "Process [processId=" + processId + ", processLevel=" + processLevel + ", processName=" + processName
				+ "]";
	}	
}
