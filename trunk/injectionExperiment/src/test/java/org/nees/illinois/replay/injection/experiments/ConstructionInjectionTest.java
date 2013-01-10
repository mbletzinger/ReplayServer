package org.nees.illinois.replay.injection.experiments;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.guice.AnnotatedBlockModule;
import org.nees.illinois.replay.injection.guice.ComplexCompositeModule;
import org.nees.illinois.replay.injection.guice.MixedCompositeModule;
import org.nees.illinois.replay.injection.guice.SimpleBlockModule;
import org.nees.illinois.replay.injection.guice.SimpleCompositeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ConstructionInjectionTest {

	private final Logger log = LoggerFactory
			.getLogger(ConstructionInjectionTest.class);

	@Test
	public void simpleInjectionTest() {
		Injector inject = Guice.createInjector(new SimpleBlockModule("Test1"));
		BlockI block = inject.getInstance(BlockI.class);
		log.debug("created " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Test1\"");

	}

	@Test
	public void simpleCompositeInjectionTest() {
		Injector inject = Guice.createInjector(new SimpleCompositeModule(
				"Test2"));
		BlockI block = inject.getInstance(BlockI.class);
		log.debug("created " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Test2\"");
		CompI comp = inject.getInstance(CompI.class);
		log.debug("created " + comp);
		Assert.assertEquals(comp.cpublish(),
				"SimpleComposite [block=SimpleBlock \"Test2\"]");
	}

	@Test
	public void mixedCompositeInjectionTest() {
		Injector inject = Guice
				.createInjector(new MixedCompositeModule("Test3"));
		BlockI block = inject.getInstance(BlockI.class);
		log.debug("created " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Test3\"");
		CompI comp = inject.getInstance(CompI.class);
		log.debug("created " + comp);
		Assert.assertEquals(comp.cpublish(),
				"MixedComposite [identity=Mixed String, block=SimpleBlock \"Test3\","
						+ " datatype=DataTypeBlock \"DataType String\" 1 2.3]");
	}

	@Test
	public void annotatedInjectionTest() {
		Injector inject = Guice
				.createInjector(new AnnotatedBlockModule("Test4"));
		BlockI block = inject.getInstance(BlockI.class);
		log.debug("created " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Default\"");
		CompI comp = inject.getInstance(CompI.class);
		log.debug("created " + comp);
		Assert.assertEquals(
				comp.cpublish(),
				"AnnotatedComposite [name=, simple=SimpleBlock \"Test4\", "
						+ "datatype=DataTypeBlock \"Test4\" 1 2.3, "
						+ "duplicate=DuplicateTypeBlock [id=Test4, id2=Test42, ratio=1.2, ratio3=3.4, count=5, count1=6]]");
	}

	@Test
	public void complexInjectionTest() {
		Injector inject = Guice.createInjector(new ComplexCompositeModule(
				"Test5"));
		BlockI block = inject.getInstance(BlockI.class);
		log.debug("created " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Test5\"");
		CompI comp = inject.getInstance(CompI.class);
		log.debug("created " + comp);
		Assert.assertEquals(
				comp.cpublish(),
				"ComplexComposite [name=2nd Order, "
						+ "dup=DuplicateTypeBlock [id=Dup String 1, id2=Dup String 2, ratio=1.2, ratio3=3.4, count=5, count1=6], "
						+ "block=SimpleBlock \"Test5\", "
						+ "composite=MixedComposite [identity=2nd Order, "
						+ "block=SimpleBlock \"Test5\", "
						+ "datatype=DataTypeBlock \"DataType String\" 1 2.3]]");
	}
}
