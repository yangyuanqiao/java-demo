package test.learn;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

//declare a new annotation
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationTest {

}

@Retention(RetentionPolicy.RUNTIME)
@interface Demo {

	String str();

	int val();
}

class PackageDemo {

	// set values for the annotation
	@Demo(str = "Demo Annotation", val = 100)
	// a method to call in the main
	public static void example() {
		PackageDemo ob = new PackageDemo();

		try {
			Class c = ob.getClass();

			// get the method example
			Method m = c.getMethod("example");

			// get the annotation for class Demo
			Demo annotation = m.getAnnotation(Demo.class);

			// print the annotation
			System.out.println(annotation.str() + " " + annotation.val());
		} catch (NoSuchMethodException exc) {
			exc.printStackTrace();
		}
	}

	public static void main(String args[]) {
		example();
	}
}