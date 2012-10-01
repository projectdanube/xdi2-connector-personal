package xdi2.connector.personal.contributor;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xdi2.connector.personal.api.PersonalApi;
import xdi2.connector.personal.mapping.PersonalMapping;
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

	private static final Logger log = LoggerFactory.getLogger(PersonalContributor.class);

	private Graph graph;
	private PersonalApi personalApi;
	private PersonalMapping personalMapping;

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

			this.getContributors().addContributor(new PersonalGemFieldContributor());
		}
	}

	@ContributorCall(addresses={"($)($)"})
	private class PersonalGemFieldContributor extends AbstractContributor {

		private PersonalGemFieldContributor() {

			super();
		}

		@Override
		public boolean getContext(XRI3Segment[] contributorXris, XRI3Segment relativeContextNodeXri, XRI3Segment contextNodeXri, GetOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

			XRI3Segment personalContextXri = contributorXris[contributorXris.length - 3];
			XRI3Segment userXri = contributorXris[contributorXris.length - 2];
			XRI3Segment personalDataXri = contributorXris[contributorXris.length - 1];

			log.debug("personalContextXri: " + personalContextXri + ", userXri: " + userXri + ", personalDataXri: " + personalDataXri);

			// retrieve the Personal value

			String personalValue = null;

			try {

				String personalGemIdentifier = PersonalContributor.this.personalMapping.personalDataXriToPersonalGemIdentifier(personalDataXri);
				String personalFieldIdentifier = PersonalContributor.this.personalMapping.personalDataXriToPersonalFieldIdentifier(personalDataXri);
				if (personalGemIdentifier == null) return false;
				if (personalFieldIdentifier == null) return false;

				String accessToken = GraphUtil.retrieveAccessToken(PersonalContributor.this.getGraph(), userXri);
				if (accessToken == null) throw new Exception("No access token.");

				JSONObject user = PersonalContributor.this.retrieveUser(executionContext, accessToken);
				if (user == null) throw new Exception("No user.");
				if (! user.has(personalGemIdentifier)) return false;

				JSONObject gem = user.getJSONObject(personalGemIdentifier);
				if (! gem.has(personalFieldIdentifier)) return false;

				personalValue = gem.getString(personalFieldIdentifier);
			} catch (Exception ex) {

				throw new Xdi2MessagingException("Cannot load user data: " + ex.getMessage(), ex, null);
			}

			// add the Personal value to the response

			if (personalValue != null) {

				ContextNode contextNode = messageResult.getGraph().findContextNode(contextNodeXri, true);
				contextNode.createLiteral(personalValue);
			}

			// done

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

	public PersonalMapping getPersonalMapping() {

		return this.personalMapping;
	}

	public void setPersonalMapping(PersonalMapping personalMapping) {

		this.personalMapping = personalMapping;
	}
}
