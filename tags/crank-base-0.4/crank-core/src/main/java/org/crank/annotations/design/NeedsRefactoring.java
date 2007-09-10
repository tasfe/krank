package org.crank.annotations.design;

public @interface NeedsRefactoring {
	String value();
	String item1() default "";
	String item2() default "";
	String item3() default "";
	String item4() default "";
	String item5() default "";
	String item6() default "";
}
