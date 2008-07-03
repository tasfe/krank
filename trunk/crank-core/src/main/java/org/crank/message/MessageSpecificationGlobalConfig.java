package org.crank.message;

public class MessageSpecificationGlobalConfig {
	private static boolean useDetail = true;
	private static boolean useSummary = true;
	private static boolean useSubject = true;
	public static boolean isUseDetail() {
		return useDetail;
	}
	public void setUseDetail(boolean useDetail) {
		MessageSpecificationGlobalConfig.useDetail = useDetail;
	}
	public static boolean isUseSummary() {
		return useSummary;
	}
	public void setUseSummary(boolean useSummary) {
		MessageSpecificationGlobalConfig.useSummary = useSummary;
	}
	public static boolean isUseSubject() {
		return useSubject;
	}
	public void setUseSubject(boolean useSubject) {
		MessageSpecificationGlobalConfig.useSubject = useSubject;
	}
	
}
