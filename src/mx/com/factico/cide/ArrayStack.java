package mx.com.factico.cide;

import java.util.ArrayList;
import java.util.List;

public class ArrayStack implements Stack {
	private int top = -1;
	private List<Object> stack;

	public ArrayStack() {
		stack = new ArrayList<Object>();
	}
	
	public ArrayStack(int maxElements) {
		stack = new ArrayList<Object>(maxElements);
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public Object peek() {
		if (top < 0)
			throw new java.util.EmptyStackException();
		return stack.get(top);
	}

	public void push(Object o) {
		stack.add(++top, o);
	}

	public Object pop() {
		if (top < 0)
			throw new java.util.EmptyStackException();
		return stack.get(top--);
	}
}