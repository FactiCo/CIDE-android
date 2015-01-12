package mx.com.factico.cide.beans;

import com.google.gson.annotations.Expose;

public class Facebook {
	@Expose private String fcbookid;
	private String name;

	public Facebook(String fcbookid, String name) {
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
