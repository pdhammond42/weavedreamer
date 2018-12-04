package com.jenkins.weavedreamer.models;

import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BeanPropertyCommandTest extends TestCase {
	
	public class ABean {
		private int foo;
		public void setFoo(int f) {
			foo = f;
		}
		public int getFoo() {
			return foo;
		}
	}
	
	public void testCommandSetsProperty () {
		ABean bean = new ABean();
		BeanPropertyCommand<Integer> command = new BeanPropertyCommand<Integer> (bean, "foo", 42);
		command.execute();
		assertThat(bean.getFoo(), is(42));
	}

	public void testCommandCanUndo () {
		ABean bean = new ABean();
		bean.setFoo(11);
		BeanPropertyCommand<Integer> command = new BeanPropertyCommand<Integer> (bean, "foo", 42);
		command.execute();
		command.undo();
		assertThat(bean.getFoo(), is(11));
	}
}
