package mx.com.factico.cide.beans;

import java.io.Serializable;

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String parent;
	private String proposalId;
	private String message;
	private From from;
	
	public Comment() {}
	
	public Comment(String parent, String proposalId, String message, From from) {
		super();
		this.parent = parent;
		this.proposalId = proposalId;
		this.message = message;
		this.from = from;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}
	
	public class From implements Serializable {
		private static final long serialVersionUID = 1L;

		private String fcbookid;
		private String name;

		public From(String fcbookid, String name) {
			super();
			this.fcbookid = fcbookid;
			this.name = name;
		}

		public String getFcbookid() {
			return fcbookid;
		}

		public void setFcbookid(String fcbookid) {
			this.fcbookid = fcbookid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}