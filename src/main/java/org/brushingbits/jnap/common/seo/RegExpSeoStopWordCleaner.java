/*
 * RegExpSeoStopWordCleaner.java created on 2011-01-26
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
package org.brushingbits.jnap.common.seo;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * @author Daniel Rochetti
 * @since 1.0
 */
public abstract class RegExpSeoStopWordCleaner implements SeoStopWordCleaner {

	private static Pattern WORD_CLEANER_REGEXP;

	public abstract String[] getSeoUnusefulWords();

	/* (non-Javadoc)
	 * @see org.brushingbits.jnap.common.seo.SeoStopWordCleaner#removeWords(java.lang.String)
	 */
	public String clean(String str) {
		if (WORD_CLEANER_REGEXP == null) {
			WORD_CLEANER_REGEXP = Pattern.compile(MessageFormat.format("\\b({0})\\b",
					StringUtils.join(getSeoUnusefulWords(), '|')), Pattern.CASE_INSENSITIVE);
		}
		Matcher unusefulWordsMatcher = WORD_CLEANER_REGEXP.matcher(str);
		return unusefulWordsMatcher.replaceAll(StringUtils.EMPTY);
	}

}
