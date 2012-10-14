package xdi2.tests.connector.personal.mapping;

import junit.framework.TestCase;
import xdi2.connector.personal.mapping.PersonalMapping;
import xdi2.core.Graph;
import xdi2.core.impl.memory.MemoryGraphFactory;
import xdi2.core.xri3.impl.XRI3Segment;

public class PersonalMappingTest extends TestCase {

	private Graph mappingGraph;
	private PersonalMapping personalMapping;

	@Override
	protected void setUp() throws Exception {

		this.mappingGraph = MemoryGraphFactory.getInstance().loadGraph(PersonalMapping.class.getResourceAsStream("mapping.xdi"));
		this.personalMapping = new PersonalMapping();
		this.personalMapping.setMappingGraph(this.mappingGraph);
	}

	@Override
	protected void tearDown() throws Exception {

		this.mappingGraph.close();
	}

	public void testMapping() throws Exception {

		XRI3Segment personalDataXri = new XRI3Segment("+(0000)$!(+(preferred_first_name))");
		XRI3Segment xdiDataXri = new XRI3Segment("+first$!(+name)");

		assertEquals("0000", this.personalMapping.personalDataXriToPersonalGemIdentifier(personalDataXri));
		assertEquals("preferred_first_name", this.personalMapping.personalDataXriToPersonalFieldIdentifier(personalDataXri));

		assertEquals(xdiDataXri, this.personalMapping.personalDataXriToXdiDataXri(personalDataXri));
		assertEquals(personalDataXri, this.personalMapping.xdiDataXriToPersonalDataXri(xdiDataXri));
	}
}
