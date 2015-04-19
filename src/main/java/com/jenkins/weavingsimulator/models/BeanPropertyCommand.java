package com.jenkins.weavingsimulator.models;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.beans.*;

public class BeanPropertyCommand<T> implements Command {
	
	Object theBean;
	T newValue;
	T oldValue;
	PropertyDescriptor theProperty;

	public BeanPropertyCommand (Object bean, String property, T value) {
		theBean = bean;
		newValue = value;
		try {
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] pd = info.getPropertyDescriptors();
			for (PropertyDescriptor p : pd) {
				String x = p.getName();
				String y = property;
				if (p.getName().equals(property)) {
					theProperty = p;
				}
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oldValue = (T) theProperty.getReadMethod().invoke(theBean);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void execute() {
		try {
			theProperty.getWriteMethod().invoke(theBean, newValue);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void undo() {
		try {
			theProperty.getWriteMethod().invoke(theBean, oldValue);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
