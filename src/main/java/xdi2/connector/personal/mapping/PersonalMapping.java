package xdi2.connector.personal.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xdi2.core.ContextNode;
import xdi2.core.Graph;
import xdi2.core.features.dictionary.Dictionary;
import xdi2.core.features.multiplicity.Multiplicity;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.core.xri3.impl.XRI3SubSegment;

public class PersonalMapping {

	public static final XRI3Segment XRI_S_PERSONAL_CONTEXT = new XRI3Segment("(https://personal.com)");

	private static final Logger log = LoggerFactory.getLogger(PersonalMapping.class);

	private Graph mappingGraph;

	/**
	 * Converts a Personal data XRI to a native Personal gem identifier.
	 * Example: (0000)$!(preferred_first_name) --> 0000
	 */
	public String personalDataXriToPersonalGemIdentifier(XRI3Segment personalDataXri) {

		// convert

		String personalGemIdentifier = subSegmentXRefValue(personalDataXri, 0);

		// done

		if (log.isDebugEnabled()) log.debug("Converted " + personalDataXri + " to " + personalGemIdentifier);

		return personalGemIdentifier;
	}

	/**
	 * Converts a Personal data XRI to a native Personal field identifier.
	 * Example: (0000)$!(preferred_first_name) --> preferred_first_name
	 */
	public String personalDataXriToPersonalFieldIdentifier(XRI3Segment personalDataXri) {

		// convert

		String personalFieldIdentifier = subSegmentXRefValue(personalDataXri, 1);

		// done

		if (log.isDebugEnabled()) log.debug("Converted " + personalDataXri + " to " + personalFieldIdentifier);

		return personalFieldIdentifier;
	}

	/**
	 * Maps and converts a Personal data XRI to an XDI data XRI.
	 * Example: (0000)$!(preferred_first_name) --> +first$!(+name)
	 */
	public XRI3Segment personalDataXriToXdiDataXri(XRI3Segment personalGemFieldXri) {

		// map

		XRI3Segment personalDataDictionaryXri = new XRI3Segment("" + XRI_S_PERSONAL_CONTEXT + "+(+(" + subSegmentXRefValue(personalGemFieldXri, 0) + "))" + "+(+(" + subSegmentXRefValue(personalGemFieldXri, 1) + "))");
		ContextNode personalDataContextNode = this.mappingGraph.findContextNode(personalDataDictionaryXri, false);

		ContextNode xdiDataDictionaryContextNode = Dictionary.getCanonicalContextNode(personalDataContextNode);
		XRI3Segment xdiDataDictionaryXri = xdiDataDictionaryContextNode.getXri();

		// convert

		StringBuilder buffer = new StringBuilder();

		for (int i=0; i<xdiDataDictionaryXri.getNumSubSegments(); i++) {

			if (i + 1 < xdiDataDictionaryXri.getNumSubSegments()) {

				buffer.append(Multiplicity.entitySingletonArcXri((XRI3SubSegment) xdiDataDictionaryXri.getSubSegment(i)).toString());
			} else {

				buffer.append(Multiplicity.attributeSingletonArcXri((XRI3SubSegment) xdiDataDictionaryXri.getSubSegment(i)).toString());
			}
		}

		XRI3Segment xdiDataXri = new XRI3Segment(buffer.toString());

		// done

		if (log.isDebugEnabled()) log.debug("Mapped and converted " + personalGemFieldXri + " to " + xdiDataXri);

		return xdiDataXri;
	}

	/*
	 * Getters and setters
	 */

	public Graph getMappingGraph() {

		return this.mappingGraph;
	}

	public void setMappingGraph(Graph mappingGraph) {

		this.mappingGraph = mappingGraph;
	}

	/*
	 * Helper methods
	 */

	private static String subSegmentXRefValue(XRI3Segment xri, int i) {

		if (! xri.getSubSegment(i).hasXRef()) return null;
		if (! xri.getSubSegment(i).getXRef().hasXRIReference()) return null;

		return xri.getSubSegment(i).getXRef().getXRIReference().toString();
	}
}
