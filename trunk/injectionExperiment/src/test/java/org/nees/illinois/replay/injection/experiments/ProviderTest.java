package org.nees.illinois.replay.injection.experiments;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.guice.ComplexCompositeModule;
import org.nees.illinois.replay.injection.guice.DataTypeBlockModule;
import org.nees.illinois.replay.injection.guice.MixedCompositeModule;
import org.nees.illinois.replay.injection.guice.SimpleBlockModule;
import org.nees.illinois.replay.injection.guice.SimpleCompositeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ProviderTest {

	private final Logger log = LoggerFactory.getLogger(ProviderTest.class);

	@Test
	public void simpleProviderTest() {
		Injector inject = Guice.createInjector(new SimpleBlockModule("Test1"));
		Provider<BlockI> pblock = inject.getProvider(BlockI.class);

		inject = Guice.createInjector(new DataTypeBlockModule());
		Provider<BlockI> pblock1 = inject.getProvider(BlockI.class);

		BlockI block = pblock.get();
		BlockI block1 = pblock1.get();

		log.debug("provided " + block);
		Assert.assertEquals(block.publish(), "SimpleBlock \"Test1\"");

		log.debug("provided " + block1);
		Assert.assertEquals(block1.publish(),
				"DataTypeBlock \"DataType String\" 1 2.3");
	}

	@Test
	public void nuttyProviderTest() {
		List<String> results = new ArrayList<String>();
		List<Provider<CompI>> providers = new ArrayList<Provider<CompI>>();
		List<AbstractModule> modules = new ArrayList<AbstractModule>();

		modules.add(new SimpleCompositeModule("Nutty"));
		results.add("SimpleComposite [block=SimpleBlock \"Nutty\"]");

		modules.add(new MixedCompositeModule("MixedNuts"));
		results.add("MixedComposite [identity=Mixed String, "
				+ "block=SimpleBlock \"MixedNuts\", " 
				+ "datatype=DataTypeBlock \"DataType String\" 1 2.3]");

		modules.add(new ComplexCompositeModule("ExoticNuts"));
		results.add("ComplexComposite [name=2nd Order, "
				+ "dup=DuplicateTypeBlock [id=Dup String 1, id2=Dup String 2, ratio=1.2, ratio3=3.4, count=5, count1=6], "
				+ "block=SimpleBlock \"ExoticNuts\", "
				+ "composite=MixedComposite [identity=2nd Order, "
				+ "block=SimpleBlock \"ExoticNuts\", "
				+ "datatype=DataTypeBlock \"DataType String\" 1 2.3]]");

		for (AbstractModule m : modules) {
			Injector inject = Guice.createInjector(m);
			Provider<CompI> prov = inject.getProvider(CompI.class);
			providers.add(prov);
		}
		
		for(int i = 0; i < results.size(); i++) {
			CompI comp = providers.get(i).get();
			log.debug("provided " + comp);
			Assert.assertEquals(comp.cpublish(), results.get(i));
		}
	}
}
