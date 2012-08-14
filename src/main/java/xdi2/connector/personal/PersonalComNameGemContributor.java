package xdi2.connector.personal;

import xdi2.connector.personal.personalcomapi.NameGem;
import xdi2.core.ContextNode;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.GetOperation;
import xdi2.messaging.MessageResult;
import xdi2.messaging.ModOperation;
import xdi2.messaging.exceptions.Xdi2MessagingException;
import xdi2.messaging.target.ExecutionContext;

public class PersonalComNameGemContributor extends PersonalComAbstractContributor {

	private NameGem nameGem;

	public PersonalComNameGemContributor() {

	}

	public void init() {

		this.nameGem = this.getPersonalComApi().getNameGem();
	}

	@Override
	public boolean get(XRI3Segment contextNodeXri, GetOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		String contextNodeXriString = contextNodeXri.toString();
		String literalData = null;

		try {

			if (contextNodeXriString.endsWith("$!(+firstname)")) literalData = this.nameGem.getFirstname();
			else if (contextNodeXriString.endsWith("$!(+lastname)")) literalData = this.nameGem.getLastname();
			else return false;
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot load name gem data: " + ex.getMessage(), ex, null);
		}

		ContextNode contextNode = messageResult.getGraph().findContextNode(contextNodeXri, true);
		contextNode.createLiteral(literalData);

		return true;
	}

	@Override
	public boolean modLiteral(XRI3Segment contextNodeXri, String literalData, ModOperation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		String contextNodeXriString = contextNodeXri.toString();

		try {

			if (contextNodeXriString.endsWith("$!(+firstname)")) this.nameGem.setFirstname(literalData);
			else if (contextNodeXriString.endsWith("$!(+lastname)")) this.nameGem.setLastname(literalData);
			else return false;
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot save name gem data: " + ex.getMessage(), ex, null);
		}

		return true;
	}
}
