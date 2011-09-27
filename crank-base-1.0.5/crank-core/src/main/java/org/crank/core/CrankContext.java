package org.crank.core;

public class CrankContext {
	private static ObjectRegistry objectRegistry;
	
	private static Object createObjectBasedOnConstant (String className) {
			try {
				return Class.forName(className).newInstance();
			} catch (Exception ex){
				throw new RuntimeException(ex);
			}
	}
	
	public static ObjectRegistry getObjectRegistry() {
		if (objectRegistry==null) {
			objectRegistry = (ObjectRegistry) createObjectBasedOnConstant(CrankConstants.OBJECT_REGISTRY);
		}
		return objectRegistry;
	}
	
	public static ResourceBundleLocator getResourceBundleLocator() {
		return (ResourceBundleLocator) getObjectRegistry().getObjectsByType(ResourceBundleLocator.class)[0];
	}
	

	public static void setObjectRegistry(ObjectRegistry objectRegistry) {
		CrankContext.objectRegistry = objectRegistry;
	}


}
