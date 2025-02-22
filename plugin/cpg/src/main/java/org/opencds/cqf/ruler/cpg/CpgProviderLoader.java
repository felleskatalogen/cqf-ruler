package org.opencds.cqf.ruler.cpg;

import org.opencds.cqf.external.cr.PostInitProviderRegisterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;

public class CpgProviderLoader {
	private static final Logger myLogger = LoggerFactory.getLogger(CpgProviderLoader.class);
	private final FhirContext myFhirContext;
	private final ResourceProviderFactory myResourceProviderFactory;
	private final CpgProviderFactory myCpgProviderFactory;

	// This is just here to force the observer to register
	private final PostInitProviderRegisterer myPostInitProviderRegisterer;

	public CpgProviderLoader(FhirContext theFhirContext, ResourceProviderFactory theResourceProviderFactory,
			CpgProviderFactory theCrProviderFactory, PostInitProviderRegisterer thePostInitProviderRegisterer) {
		myFhirContext = theFhirContext;
		myResourceProviderFactory = theResourceProviderFactory;
		myCpgProviderFactory = theCrProviderFactory;
		this.myPostInitProviderRegisterer = thePostInitProviderRegisterer;
		loadProvider();
	}

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myLogger.info("Registering DSTU3 Ruler Cpg Providers");
				myResourceProviderFactory.addSupplier(myCpgProviderFactory::getLibraryEvalProvider);
				myResourceProviderFactory.addSupplier(myCpgProviderFactory::getCqlExecutionProvider);
				break;
			case R4:
				myLogger.info("Registering R4 Ruler Cpg Providers");
				myResourceProviderFactory.addSupplier(myCpgProviderFactory::getLibraryEvalProvider);
				myResourceProviderFactory.addSupplier(myCpgProviderFactory::getCqlExecutionProvider);
				break;
			default:
				throw new ConfigurationException("Clinical Reasoning not supported for FHIR version "
						+ myFhirContext.getVersion().getVersion());
		}
	}
}
