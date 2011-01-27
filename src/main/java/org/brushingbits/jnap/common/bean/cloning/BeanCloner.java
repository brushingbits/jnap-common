/*
 * BeanCloner.java created on 2010-10-18
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
package org.brushingbits.jnap.common.bean.cloning;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.brushingbits.jnap.common.bean.visitor.BeanPropertyFilter;
import org.brushingbits.jnap.common.bean.visitor.DefaultTypeResolver;
import org.brushingbits.jnap.common.bean.visitor.HibernateTypeResolver;
import org.brushingbits.jnap.common.bean.visitor.PropertyTypeResolver;
import org.brushingbits.jnap.common.util.StringMatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

/**
 * TODO make "org.brushingbits.jnap.common.bean.visitor.BeanPropertyVisitor"
 * generic enough so cloning, property extractor, etc can be subclassed from the
 * visitor.
 * 
 * @author Daniel Rochetti
 * @since 1.0
 */
public class BeanCloner {

	protected BeanPropertyFilter propertyFilter;
	protected PropertyTypeResolver propertyTypeResolver;

	private VisitorContext context;
	private boolean preventCircularVisiting;
	private List<Class<?>> standardTypes;
	private Map alreadyCloned;

	/**
	 * 
	 * @param propertyFilter
	 */
	public BeanCloner(BeanPropertyFilter propertyFilter, PropertyTypeResolver propertyTypeResolver) {
		// validating arguments
		Assert.notNull(propertyFilter);
		Assert.notNull(propertyTypeResolver);

		this.propertyFilter = propertyFilter;
		this.context = new VisitorContext();
		this.alreadyCloned = new HashMap<Object, Object>();
		this.preventCircularVisiting = true;

		// init type resolver (fallback to default if necessary)
		try {
			propertyTypeResolver.init();
			this.propertyTypeResolver = propertyTypeResolver;
		} catch (Exception e) {
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
	public BeanCloner(BeanPropertyFilter propertyFilter) {
		this(propertyFilter, new HibernateTypeResolver());
	}

	/**
	 * 
	 */
	public BeanCloner() {
		this(BeanPropertyFilter.getDefault());
	}

	public Object clone(Object source) {
		Object copy = null;
		if (source != null) {
			final String currentPath = getCurrentPath();
			final Class<?> type = this.propertyTypeResolver.resolve(source, this.propertyFilter);
			
			final boolean excluded = type == null
					|| shouldExcludeProperty(currentPath)
					|| shouldExcludeType(type)
					|| shouldExcludeAssignableType(type);
			
			final int currentLevel = context.getCurrentLevel();
			final boolean included = currentLevel == -1 || shouldIncludeProperty(currentPath);
			final boolean validDepth = isValidDepth();

			if (included || (!excluded && validDepth)) {
				if (isStandardType(type)) {
					copy = source;
				} else if (type.isArray()) {
					copy = cloneArray((Object[]) source, type);
				} else if (Collection.class.isAssignableFrom(type)) {
					copy = cloneCollection((Collection<?>) source, type);
				} else if (Map.class.isAssignableFrom(type)) {
					copy = cloneMap((Map<?, ?>) source, type);
				} else {
					// so, we assume it's a nested bean (let's go 'down' another level)
					if (context.wasAlreadyVisited(source) && this.preventCircularVisiting) {
						copy = null;
					} else if (context.wasAlreadyVisited(source) && !this.preventCircularVisiting) {
						copy = this.alreadyCloned.get(source);
					} else {
						context.nextLevel();
						if (isValidDepth()) {
							copy = cloneBean(source, type);
						}
						context.prevLevel();
					}
				}
			}
		}
		return copy;
	}

	/**
	 * @param currentLevel
	 * @return
	 */
	private boolean isValidDepth() {
		return propertyFilter.getDepth() == -1 || context.getCurrentLevel() <= propertyFilter.getDepth();
	}

	private Object cloneBean(Object bean, Class<?> type) {
		BeanWrapper source = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		BeanWrapper copy = PropertyAccessorFactory.forBeanPropertyAccess(BeanUtils.instantiate(type));

		// keep instance for circular and multiple references
		context.setAsVisited(bean);
		alreadyCloned.put(bean, copy.getWrappedInstance());

		PropertyDescriptor[] beanProperties = copy.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : beanProperties) {
			String name = propertyDescriptor.getName();
			context.pushPath(name);
			if (copy.isReadableProperty(name) && copy.isWritableProperty(name)) {
				Object value = source.getPropertyValue(name);
				copy.setPropertyValue(name, clone(value));
			}
			context.popPath();
		}
		Object beanCopy = copy.getWrappedInstance();
		source = null;
		copy = null;
		return beanCopy;
	}

	private Object[] cloneArray(Object[] array, Class<?> type) {
		Object[] arrayCopy = (Object[]) Array.newInstance(type, array.length);
		for (int i = 0; i < array.length; i++) {
			arrayCopy[i] = clone(array[i]);
		}
		return arrayCopy;
	}

	private Collection<?> cloneCollection(Collection<?> collection,	Class<?> type) {
		Collection<Object> collectionCopy = (Collection<Object>) BeanUtils.instantiate(type);
		for (Object item : collection) {
			collectionCopy.add(clone(item));
		}
		CollectionUtils.filter(collectionCopy, PredicateUtils.notNullPredicate());
		if (collectionCopy.isEmpty()) {
			collectionCopy = null;
		}
		return collectionCopy;
	}

	private Map<Object, Object> cloneMap(Map<?, ?> map, Class<?> type) {
		Map<Object, Object> mapCopy = (Map<Object, Object>) BeanUtils.instantiate(type);
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			mapCopy.put(clone(key), clone(value));
		}
		return mapCopy;
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
		return StringMatcher.match(path,
				propertyFilter.getExcludeProperties().toArray(new String[] {}));
	}

	protected boolean shouldExcludeType(Class<?> type) {
		return StringMatcher.match(type.getName(),
				propertyFilter.getExcludeTypes().toArray(new String[] {}));
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
		return StringMatcher.match(path,
				propertyFilter.getIncludeProperties().toArray(new String[] {}));
	}

	/**
	 * 
	 * @author Daniel Rochetti
	 * @since 1.0
	 */
	protected class VisitorContext {

		private int currentLevel = -1;
		private Deque<String> currentPath;
		private List<Object> alreadyVisited;

		public VisitorContext() {
			this.currentPath = new LinkedList<String>();
			this.alreadyVisited = new ArrayList<Object>();
		}

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

	}

}
