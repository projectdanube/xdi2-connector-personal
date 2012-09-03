package xdi2.connector.personal;

import xdi2.connector.personal.personalcomapi.ContactInfoGem;
import xdi2.core.ContextNode;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.GetOperation;
import xdi2.messaging.MessageResult;
import xdi2.messaging.ModOperation;
import xdi2.messaging.exceptions.Xdi2MessagingException;
import xdi2.messaging.target.ExecutionContext;

public class PersonalComContactInfoGemContributor extends PersonalComAbstractContributor {

	private ContactInfoGem contactInfoGem;

	public PersonalComContactInfoGemContributor() {

	}

	public void init() {

		this.contactInfoGem = this.getPersonalComApi().getContactInfoGem();
	}

	@Override
	public boolean getContext(XRI3Segment contributorXri, XRI3Segment relativeContextNodeXri, XRI3Segment contextNodeXri, GetOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		String contextNodeXriString = contextNodeXri.toString();
		String literalData = null;

		try {

			if (contextNodeXriString.endsWith("$!(+email)")) literalData = this.contactInfoGem.getEmail();
			else if (contextNodeXriString.endsWith("$!(+phone)")) literalData = this.contactInfoGem.getPhone();
			else if (contextNodeXriString.endsWith("$!(+website)")) literalData = this.contactInfoGem.getWebsite();
			else if (contextNodeXriString.endsWith("$!(+facebook)")) literalData = this.contactInfoGem.getFacebook();
			else if (contextNodeXriString.endsWith("$!(+twitter)")) literalData = this.contactInfoGem.getTwitter();
			else return false;
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot load contact info gem data: " + ex.getMessage(), ex, null);
		}

		ContextNode contextNode = messageResult.getGraph().findContextNode(contextNodeXri, true);
		contextNode.createLiteral(literalData);

		return true;
	}

	@Override
	public boolean modLiteral(XRI3Segment contributorXri, XRI3Segment relativeContextNodeXri, XRI3Segment contextNodeXri, String literalData, ModOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		String contextNodeXriString = contextNodeXri.toString();

		try {

			if (contextNodeXriString.endsWith("$!(+email)")) this.contactInfoGem.setEmail(literalData);
			else if (contextNodeXriString.endsWith("$!(+phone)")) this.contactInfoGem.setPhone(literalData);
			else if (contextNodeXriString.endsWith("$!(+website)")) this.contactInfoGem.setWebsite(literalData);
			else if (contextNodeXriString.endsWith("$!(+facebook)")) this.contactInfoGem.setFacebook(literalData);
			else if (contextNodeXriString.endsWith("$!(+twitter)")) this.contactInfoGem.setTwitter(literalData);
			else return false;
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot save contact info gem data: " + ex.getMessage(), ex, null);
		}

		return true;
	}
}
