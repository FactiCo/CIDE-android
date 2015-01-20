package mx.com.factico.cide;

public interface Stack {
	boolean isEmpty();

	Object peek();

	void push(Object o);

	Object pop();
}