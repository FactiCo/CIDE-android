package mx.com.factico.cide.beans;

public class Vote {
	private String proposalId;
	private String fcbookid;
	private String value;
	
	public Vote(String proposalId, String fcbookid, String value) {
		super();
		this.proposalId = proposalId;
		this.fcbookid = fcbookid;
		this.value = value;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getFcbookid() {
		return fcbookid;
	}

	public void setFcbookid(String fcbookid) {
		this.fcbookid = fcbookid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
