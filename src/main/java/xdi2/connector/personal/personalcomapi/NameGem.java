package xdi2.connector.personal.personalcomapi;

import java.io.File;
import java.io.IOException;

public class NameGem extends AbstractGem {

	public NameGem() {

		super(new File("namegem.txt"));
	}

	public String getFirstname() throws IOException {

		return this.loadLine(0);
	}

	public void setFirstname(String firstname) throws IOException {

		this.saveLine(0, firstname);
	}

	public String getLastname() throws IOException {

		return this.loadLine(1);
	}

	public void setLastname(String lastname) throws IOException {

		this.saveLine(1, lastname);
	}
}
