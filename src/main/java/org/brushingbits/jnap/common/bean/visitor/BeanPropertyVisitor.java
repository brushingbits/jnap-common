/*
 * BeanPropertyVisitor.java created on 2010-07-01
 *
 * Created by Brushing Bits Labs
 * http://www.brushingbits.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brushingbits.jnap.common.bean.visitor;

import java.beans.PropertyDescriptor;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brushingbits.jnap.common.util.StringMatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;


/**
 * @author Daniel Rochetti
 * @since 1.0
 */
public abstract class BeanPropertyVisitor {
	
	private static Log logger = LogFactory.getLog(BeanPropertyVisitor.class);

	protected static enum State {
		STILL,
		VISITING_ARRAY,
		VISITING_ARRAY_ITEM,
		VISITING_COLLECTION,
		VISITING_COLLECTION_ITEM,
		VISITING_MAP,
		VISITING_MAP_KEY,
		VISITING_MAP_VALUE,
		VISITING_BEAN_PROPERTY,
		VISITING_BEAN
	};

	protected BeanPropertyFilter propertyFilter;
	protected PropertyTypeResolver propertyTypeResolver;

	protected VisitorContext context;
	protected boolean preventCircularVisiting;
	protected List<Class<?>> standardTypes;

	/**
	 * 
	 * @param propertyFilter
	 */
	public BeanPropertyVisitor(BeanPropertyFilter propertyFilter, PropertyTypeResolver propertyTypeResolver) {
		// validating arguments
		Assert.notNull(propertyFilter);
		Assert.notNull(propertyTypeResolver);

		this.propertyFilter = propertyFilter;
		this.context = new VisitorContext();
		this.preventCircularVisiting = true;

		// init type resolver (fallback to default if necessary)
		try {
			propertyTypeResolver.init();
			this.propertyTypeResolver = propertyTypeResolver;
		} catch (Exception e) {
			logger.warn("", e);
			this.propertyTypeResolver = new DefaultTypeResolver();
		}

		// standard types (default)
		this.standardTypes = new ArrayList<Class<?>>();
		this.standardTypes.add(String.class);
		this.standardTypes.add(Number.class);
		this.standardTypes.add(Boolean.class);
		this.standardTypes.add(Date.class);
		this.standardTypes.add(Calendar.class);
		this.standardTypes.add(URI.class);
		this.standardTypes.add(URL.class);
	}

	/**
	 * 
	 * @param propertyFilter
	 */
	public BeanPropertyVisitor(BeanPropertyFilter propertyFilter) {
		this(propertyFilter, new HibernateTypeResolver());
	}

	/**
	 * 
	 */
	public BeanPropertyVisitor() {
		this(BeanPropertyFilter.getDefault());
	}


	protected abstract void visitBean(Object bean, Class<?> type);

	protected abstract void visitStandardProperty(Object property, Class<?> type);

	protected abstract void visitArray(Object[] array, Class<?> type);

	protected abstract void visitCollection(Collection<?> collection, Class<?> type);

	protected abstract void visitMap(Map<?, ?> map, Class<?> type);

	/**
	 * 
	 * @param source
	 */
	public void visit(Object source) {
		logger.warn("Source object is null, there is nothing to visit!");
		if (source != null) {
			debug("Visiting object...");
			final String currentPath = getCurrentPath();
			debug("...current path is: " + currentPath);
			final Class<?> type = this.propertyTypeResolver.resolve(source, this.propertyFilter);
			debug("...current type is: " + type.getName());
			
			final boolean excluded = type == null
					|| shouldExcludeProperty(currentPath)
					|| shouldExcludeType(type)
					|| shouldExcludeAssignableType(type);
			debug("");
			final int currentLevel = context.getCurrentLevel();
			final boolean included = shouldIncludeProperty(currentPath) || currentLevel == -1;
			debug("");
			final boolean validDepth = propertyFilter.getDepth() == -1 || currentLevel <= propertyFilter.getDepth();
			debug("the current depth is " + currentLevel + ", max depth is " + propertyFilter.getDepth());
			if (included || (!excluded && validDepth)) {
				if (isStandardType(type)) {
					handleStandardProperty(source, type);
				} else if (type.isArray()) {
					handleArray((Object[]) source, type);
				} else if (Collection.class.isAssignableFrom(type)) {
					handleCollection((Collection<?>) source, type);
				} else if (Map.class.isAssignableFrom(type)) {
					handleMap((Map<?, ?>) source, type);
				} else {
					// so, we assume it's a nested bean (let's go 'down' another level)
					if (!context.wasAlreadyVisited(source) || !this.preventCircularVisiting) {
						handleBean(source, type);
					}
				}
			}
		}
	}

	protected void handleBean(Object source, Class<?> type) {
		context.nextLevel();
		visitBean(source, type);
		BeanWrapper sourceBean = PropertyAccessorFactory.forBeanPropertyAccess(source);
		PropertyDescriptor[] beanProperties = BeanUtils.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : beanProperties) {
			String name = propertyDescriptor.getName();
			context.pushPath(name);
			if (sourceBean.isReadableProperty(name)) {
				Object value = sourceBean.getPropertyValue(name);
				visit(value);
			}
			context.popPath();
		}
		context.prevLevel();
	}
	
	protected void handleStandardProperty(Object property, Class<?> type) {
		visitStandardProperty(property, type);
	}
	
	protected void handleArray(Object[] array, Class<?> type) {
		context.setCurrentState(State.VISITING_ARRAY);
		visitArray(array, type);
		for (Object item : array) {
			context.setCurrentState(State.VISITING_ARRAY_ITEM);
			visit(item);
		}
	}
	
	protected void handleCollection(Collection<?> collection, Class<?> type) {
		context.setCurrentState(State.VISITING_COLLECTION);
		visitCollection(collection, type);
		for (Object item : collection) {
			context.setCurrentState(State.VISITING_COLLECTION_ITEM);
			visit(item);
		}
	}
	
	protected void handleMap(Map<?, ?> map, Class<?> type) {
		context.setCurrentState(State.VISITING_MAP);
		visitMap(map, type);
		for (Object key : map.keySet()) {
			context.setCurrentState(State.VISITING_MAP_KEY);
			visit(key);
			context.setCurrentState(State.VISITING_MAP_VALUE);
			visit(map.get(key));
		}
	}

	
	protected boolean isStandardType(Class<?> type) {
		boolean standard = type.isPrimitive() || type.isEnum();
		if (!standard) {
			for (Class<?> standardType : this.standardTypes) {
				standard = standardType.isAssignableFrom(type);
				if (standard) {
					break;
				}
			}
		}
		return standard;
	}

	/**
	 * 
	 * @param type
	 */
	public void registerStandardTypes(Class<?>... type) {
		this.standardTypes.addAll(Arrays.asList(type));
	}

	/**
	 * <code>Mutator</code> ("setter") method for property <code>standardTypes</code>.
	 */
	public void setStandardTypes(List<Class<?>> standardTypes) {
		this.standardTypes = standardTypes;
	}

	protected String getCurrentPath() {
		StringBuilder path = new StringBuilder();
		for (Iterator<String> iter = context.getCurrentPath().descendingIterator(); iter.hasNext();) {
			String pathElement = iter.next();
			path.append(pathElement);
			if (iter.hasNext()) {
				path.append(".");
			}
		}
		return path.toString();
	}


	protected boolean shouldExcludeProperty(String path) {
		return StringMatcher.match(path, propertyFilter.getExcludeProperties().toArray(new String[] {}));
	}
	
	protected boolean shouldExcludeType(Class<?> type) {
		return StringMatcher.match(type.getName(), propertyFilter.getExcludeTypes().toArray(new String[] {}));
	}

	protected boolean shouldExcludeAssignableType(Class<?> type) {
		boolean exclude = false;
		List<Class<?>> excludeAssignableTypes = propertyFilter.getExcludeAssignableTypes();
		for (Class<?> excludeType : excludeAssignableTypes) {
			if (excludeType.isAssignableFrom(type)) {
				exclude = true;
				break;
			}
		}
		return exclude;
	}

	protected boolean shouldIncludeProperty(String path) {
		return StringMatcher.match(path, propertyFilter.getIncludeProperties().toArray(new String[] {}));
	}

	/**
	 * Local logger debug message.
	 * @param msg Debug message.
	 */
	protected void debug(String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * 
	 * @author Daniel Rochetti
	 * @since 1.0
	 */
	protected class VisitorContext {
		
		private int currentLevel = -1;
		private State currentState = State.STILL;
		private Deque<String> currentPath;
		private List<Object> alreadyVisited;

		public int getCurrentLevel() {
			return currentLevel;
		}
		
		public void nextLevel() {
			this.currentLevel += 1;
		}

		public void prevLevel() {
			this.currentLevel -= 1;
		}

		public Deque<String> getCurrentPath() {
			return currentPath;
		}

		public String getCurrentPathRepresentation() {
			StringBuilder path = new StringBuilder();
			for (Iterator<String> iter = currentPath.descendingIterator(); iter.hasNext();) {
				path.append(iter.next());
				if (iter.hasNext()) {
					path.append(".");
				}
			}
			return path.toString();
		}

		public String getCurrentPropertyName() {
			return currentPath.getFirst();
		}

		public void pushPath(String path) {
			this.currentPath.addFirst(path);
		}

		public void popPath() {
			this.currentPath.removeFirst();
		}

		public void setAsVisited(Object bean) {
			this.alreadyVisited.add(bean);
		}

		public boolean wasAlreadyVisited(Object bean) {
			return this.alreadyVisited.contains(bean);
		}

		public BeanPropertyVisitor.State getCurrentState() {
			return currentState;
		}

		public void setCurrentState(BeanPropertyVisitor.State currentState) {
			this.currentState = currentState;
		}

	}

}
