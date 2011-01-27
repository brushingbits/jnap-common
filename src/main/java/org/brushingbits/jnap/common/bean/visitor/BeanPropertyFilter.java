/*
 * BeanPropertyFilter.java created on 2010-07-01
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Rochetti
 * @since 1.0
 */
public class BeanPropertyFilter {

	public static final int FULL_RECURSION = -1;

	private int depth;
	private boolean excludeTransient;
	private boolean ignoreUninitializedProxies;
	private List<String> excludeProperties;
	private List<String> excludeTypes;
	private List<Class<?>> excludeAssignableTypes;
	private List<String> includeProperties;


	protected BeanPropertyFilter() {
		// defaults
		this.depth = FULL_RECURSION;
		this.excludeTransient = true;
		this.ignoreUninitializedProxies = true;
		this.excludeProperties = new ArrayList<String>();
		this.excludeTypes = new ArrayList<String>();
		this.excludeAssignableTypes = new ArrayList<Class<?>>();
		this.includeProperties = new ArrayList<String>();

		// default excludes
		excludeTypes("org.hibernate.**", "org.springframework.**");
		excludeProperties("class", "**.class");
	}

	public static BeanPropertyFilter getDefault() {
		return new BeanPropertyFilter();
	}

	public static BeanPropertyFilter getShallow() {
		return new BeanPropertyFilter().depth(0);
	}

	public static BeanPropertyFilter getDeep() {
		return new BeanPropertyFilter().initializeProxies();
	}

	public BeanPropertyFilter depth(int depth) {
		this.depth = depth;
		return this;
	}

	public BeanPropertyFilter includeProperties(String... rules) {
		this.includeProperties.addAll(Arrays.asList(rules));
		return this;
	}

	public BeanPropertyFilter excludeProperties(String... rules) {
		this.excludeProperties.addAll(Arrays.asList(rules));
		return this;
	}

	public BeanPropertyFilter excludeTypes(String... rules) {
		this.excludeTypes.addAll(Arrays.asList(rules));
		return this;
	}

	public BeanPropertyFilter excludeAssignableTypes(Class<?>... types) {
		this.excludeAssignableTypes.addAll(Arrays.asList(types));
		return this;
	}

	public BeanPropertyFilter excludeCollections() {
		this.excludeAssignableTypes(Collection.class);
		return this;
	}

	public BeanPropertyFilter excludeMaps() {
		this.excludeAssignableTypes(Map.class);
		return this;
	}

	public BeanPropertyFilter ignoreUninitializedProxies() {
		this.ignoreUninitializedProxies = true;
		return this;
	}

	public BeanPropertyFilter initializeProxies() {
		this.ignoreUninitializedProxies = false;
		return this;
	}


	//----- getters -----//

	public int getDepth() {
		return depth;
	}

	public boolean isExcludeTransient() {
		return excludeTransient;
	}

	public boolean isIgnoreUninitializedProxies() {
		return ignoreUninitializedProxies;
	}

	public List<String> getExcludeProperties() {
		return excludeProperties;
	}

	public List<String> getExcludeTypes() {
		return excludeTypes;
	}

	public List<Class<?>> getExcludeAssignableTypes() {
		return excludeAssignableTypes;
	}

	public List<String> getIncludeProperties() {
		return includeProperties;
	}

}
