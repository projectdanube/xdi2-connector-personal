package xdi2.connector.personal.personalcomapi;

import java.io.File;
import java.io.IOException;

public class ContactInfoGem extends AbstractGem {

	public ContactInfoGem() {

		super(new File("contactinfogem.txt"));
	}

	public String getEmail() throws IOException {

		return this.loadLine(0);
	}

	public void setEmail(String email) throws IOException {

		this.saveLine(0, email);
	}

	public String getPhone() throws IOException {

		return this.loadLine(1);
	}

	public void setPhone(String phone) throws IOException {

		this.saveLine(1, phone);
	}

	public String getWebsite() throws IOException {

		return this.loadLine(2);
	}

	public void setWebsite(String website) throws IOException {

		this.saveLine(2, website);
	}

	public String getFacebook() throws IOException {

		return this.loadLine(3);
	}

	public void setFacebook(String facebook) throws IOException {

		this.saveLine(3, facebook);
	}

	public String getTwitter() throws IOException {

		return this.loadLine(4);
	}

	public void setTwitter(String twitter) throws IOException {

		this.saveLine(4, twitter);
	}
}
