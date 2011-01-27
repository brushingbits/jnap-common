/*
 * HibernateTypeResolver.java created on 2010-08-15
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.brushingbits.jnap.common.exception.ApiException;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentIdentifierBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.PersistentSortedMap;
import org.hibernate.collection.PersistentSortedSet;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.util.Assert;

/**
 * @author Daniel Rochetti
 * @since 1.0
 */
public class HibernateTypeResolver extends DefaultTypeResolver {

	protected static final Map<Class<?>, Class<?>> COLLECTIONS_MAPPING = new LinkedHashMap<Class<?>, Class<?>>();
	static {
		// Map
		COLLECTIONS_MAPPING.put(PersistentSortedMap.class, TreeMap.class);
		COLLECTIONS_MAPPING.put(PersistentMap.class, HashMap.class);
		COLLECTIONS_MAPPING.put(LinkedHashMap.class, LinkedHashMap.class);
		COLLECTIONS_MAPPING.put(SortedMap.class, TreeMap.class);
		COLLECTIONS_MAPPING.put(Map.class, HashMap.class);

		// Collection
		COLLECTIONS_MAPPING.put(PersistentList.class, ArrayList.class);
		COLLECTIONS_MAPPING.put(PersistentBag.class, ArrayList.class);
		COLLECTIONS_MAPPING.put(PersistentIdentifierBag.class, ArrayList.class);
		COLLECTIONS_MAPPING.put(LinkedList.class, LinkedList.class);
		COLLECTIONS_MAPPING.put(Queue.class, LinkedList.class);
		COLLECTIONS_MAPPING.put(List.class, ArrayList.class);
		COLLECTIONS_MAPPING.put(PersistentSortedSet.class, TreeSet.class);
		COLLECTIONS_MAPPING.put(PersistentSet.class, HashSet.class);
		COLLECTIONS_MAPPING.put(SortedSet.class, TreeSet.class);
		COLLECTIONS_MAPPING.put(Set.class, HashSet.class);
		COLLECTIONS_MAPPING.put(Collection.class, ArrayList.class);
	}

	@Override
	public void init() {
		try {
			Class.forName("org.hibernate.Hibernate");
		} catch (ClassNotFoundException e) {
			throw new ApiException("Error initializing " + HibernateTypeResolver.class.getSimpleName() +
					". Hibernate has not been found on the classpath.", e);
		}
	}

	@Override
	public Class<?> resolve(Object source, BeanPropertyFilter filter) {
		Class<?> type = null;
		if (source instanceof HibernateProxy) {
			HibernateProxy proxy = (HibernateProxy) source;
			LazyInitializer lazyInitializer = proxy.getHibernateLazyInitializer();
			if (!lazyInitializer.isUninitialized() || !filter.isIgnoreUninitializedProxies()) {
				type = lazyInitializer.getImplementation().getClass();
			}
		} else if (source instanceof PersistentCollection) {
			PersistentCollection collection = (PersistentCollection) source;
			if (collection.wasInitialized() || !filter.isIgnoreUninitializedProxies()) {
				type = getUnderlyingCollectionType(collection.getClass());
			}
		} else {
			type = super.resolve(source, filter);
		}
		return type;
	}

	/**
	 * 
	 * @param collectionType
	 * @return
	 */
	protected Class<?> getUnderlyingCollectionType(Class<?> collectionType) {
		Class<?> underlyingType = null;
		for (Class<?> key : COLLECTIONS_MAPPING.keySet()) {
			if (key.isAssignableFrom(collectionType)) {
				underlyingType = COLLECTIONS_MAPPING.get(key);
				break;
			}
		}
		Assert.notNull(underlyingType);
		return underlyingType;
	}

}
