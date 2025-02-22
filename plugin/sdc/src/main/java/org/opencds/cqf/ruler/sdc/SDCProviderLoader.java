package org.opencds.cqf.ruler.sdc;

import org.opencds.cqf.external.cr.PostInitProviderRegisterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;

public class SDCProviderLoader {
	private static final Logger myLogger = LoggerFactory.getLogger(SDCProviderLoader.class);
	private final FhirContext myFhirContext;
	private final ResourceProviderFactory myResourceProviderFactory;
	private final SDCProviderFactory mySDCProviderFactory;

	// This is just here to force the observer to register
	private final PostInitProviderRegisterer myPostInitProviderRegisterer;
	public SDCProviderLoader(FhirContext theFhirContext, ResourceProviderFactory theResourceProviderFactory,
			SDCProviderFactory theCrProviderFactory, PostInitProviderRegisterer thePostInitProviderRegisterer) {
		myFhirContext = theFhirContext;
		myResourceProviderFactory = theResourceProviderFactory;
		mySDCProviderFactory = theCrProviderFactory;
		this.myPostInitProviderRegisterer = thePostInitProviderRegisterer;
		loadProvider();
	}

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myLogger.info("Registering DSTU3 Ruler SDC Providers");
				myResourceProviderFactory.addSupplier(mySDCProviderFactory::getTransformProvider);
				myResourceProviderFactory.addSupplier(mySDCProviderFactory::getExtractProvider);
				break;
			case R4:
				myLogger.info("Registering R4 Ruler SDC Providers");
				myResourceProviderFactory.addSupplier(mySDCProviderFactory::getTransformProvider);
				myResourceProviderFactory.addSupplier(mySDCProviderFactory::getExtractProvider);
				break;
			default:
				throw new ConfigurationException("SDC not supported for FHIR version "
						+ myFhirContext.getVersion().getVersion());
		}
	}
}
