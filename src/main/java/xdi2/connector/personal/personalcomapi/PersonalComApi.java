package xdi2.connector.personal.personalcomapi;

public class PersonalComApi {

//	private static final Logger log = LoggerFactory.getLogger(PersonalComApi.class);

	private String apiKey;

	public PersonalComApi() {

	}

	public void init() {

		// init connection to the API here
	}

	public NameGem getNameGem() {

		return new NameGem();
	}

	public ContactInfoGem getContactInfoGem() {

		return new ContactInfoGem();
	}

	public String getApiKey() {

		return this.apiKey;
	}

	public void setApiKey(String apiKey) {

		this.apiKey = apiKey;
	}
}
