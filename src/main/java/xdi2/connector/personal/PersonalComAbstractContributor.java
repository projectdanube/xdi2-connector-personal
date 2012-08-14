package xdi2.connector.personal;

import xdi2.connector.personal.personalcomapi.PersonalComApi;
import xdi2.messaging.target.contributor.AbstractContributor;

public abstract class PersonalComAbstractContributor extends AbstractContributor {

	private PersonalComApi personalComApi;

	public PersonalComAbstractContributor() {

	}

	public PersonalComApi getPersonalComApi() {

		return this.personalComApi;
	}

	public void setPersonalComApi(PersonalComApi personalComApi) {

		this.personalComApi = personalComApi;
	}
}
