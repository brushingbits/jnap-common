/*
 * DefaultTypeResolver.java created on 15/08/2010
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

/**
 * @author Daniel Rochetti
 */
public class DefaultTypeResolver implements PropertyTypeResolver {

	public void init() {
		// do nothing, default type resolver can always be used
	}

	/* (non-Javadoc)
	 * @see org.brushingbits.jnap.common.bean.PropertyTypeResolver#resolve(java.lang.Object)
	 */
	public Class<?> resolve(Object source, BeanPropertyFilter filter) {
		return source.getClass();
	}


}
