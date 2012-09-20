package xdi2.connector.personal.contributor;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import xdi2.connector.personal.api.PersonalApi;
import xdi2.connector.personal.util.GraphUtil;
import xdi2.core.ContextNode;
import xdi2.core.Graph;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.GetOperation;
import xdi2.messaging.MessageEnvelope;
import xdi2.messaging.MessageResult;
import xdi2.messaging.exceptions.Xdi2MessagingException;
import xdi2.messaging.target.ExecutionContext;
import xdi2.messaging.target.contributor.AbstractContributor;
import xdi2.messaging.target.contributor.ContributorCall;
import xdi2.messaging.target.interceptor.MessageEnvelopeInterceptor;

public class PersonalContributor extends AbstractContributor implements MessageEnvelopeInterceptor {

	private Graph graph;
	private PersonalApi personalApi;

	public PersonalContributor() {

		super();

		this.getContributors().addContributor(new PersonalUserContributor());
	}

	/*
	 * MessageEnvelopeInterceptor
	 */

	@Override
	public boolean before(MessageEnvelope messageEnvelope, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		PersonalContributorExecutionContext.resetUsers(executionContext);

		return false;
	}

	@Override
	public boolean after(MessageEnvelope messageEnvelope, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		return false;
	}

	@Override
	public void exception(MessageEnvelope messageEnvelope, MessageResult messageResult, ExecutionContext executionContext, Exception ex) {

	}

	/*
	 * Sub-Contributors
	 */

	@ContributorCall(addresses={"($)"})
	private class PersonalUserContributor extends AbstractContributor {

		private PersonalUserContributor() {

			super();

			this.getContributors().addContributor(new PersonalUserAttributeContributor());
		}
	}

	@ContributorCall(addresses={"$!(preferred_last_name)","$!(preferred_first_name)","$!(home_mobile_phone)","$!(personal_email)"})
	private class PersonalUserAttributeContributor extends AbstractContributor {

		private PersonalUserAttributeContributor() {

			super();
		}

		@Override
		public boolean getContext(XRI3Segment contributorXri, XRI3Segment relativeContextNodeXri, XRI3Segment contextNodeXri, GetOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {
			
			String contributorXriString = contributorXri.toString();
			String literalData = null;

			try {

				String accessToken = GraphUtil.retrieveAccessToken(PersonalContributor.this.getGraph());
				if (accessToken == null) throw new Exception("No access token.");

				JSONObject user = PersonalContributor.this.retrieveUser(executionContext, accessToken);
				if (user == null) throw new Exception("No user.");

				if (contributorXriString.equals("$!(preferred_first_name)")) literalData = user.getJSONArray("gem0").getJSONObject(0).getString("preferred_first_name");
				else if (contributorXriString.equals("$!(preferred_last_name)")) literalData = user.getJSONArray("gem0").getJSONObject(0).getString("preferred_last_name");
				else if (contributorXriString.equals("$!(home_mobile_phone)")) literalData = user.getJSONArray("gem0").getJSONObject(1).getString("home_mobile_phone");
				else if (contributorXriString.equals("$!(personal_email)")) literalData = user.getJSONArray("gem0").getJSONObject(1).getString("personal_email");
				else return false;
			} catch (Exception ex) {

				throw new Xdi2MessagingException("Cannot load user data: " + ex.getMessage(), ex, null);
			}

			if (literalData != null) {

				ContextNode contextNode = messageResult.getGraph().findContextNode(contextNodeXri, true);
				contextNode.createLiteral(literalData);
			}

			return true;
		}
	}

	/*
	 * Helper methods
	 */

	private JSONObject retrieveUser(ExecutionContext executionContext, String accessToken) throws IOException, JSONException {

		JSONObject user = PersonalContributorExecutionContext.getUser(executionContext, accessToken);

		if (user == null) {

			user = this.personalApi.getUser(accessToken);
			PersonalContributorExecutionContext.putUser(executionContext, accessToken, user);
		}

		return user;
	}
	
	/*
	 * Getters and setters
	 */

	public Graph getGraph() {

		return this.graph;
	}

	public void setGraph(Graph graph) {

		this.graph = graph;
	}

	public PersonalApi getPersonalApi() {

		return this.personalApi;
	}

	public void setPersonalApi(PersonalApi personalApi) {

		this.personalApi = personalApi;
	}
}
