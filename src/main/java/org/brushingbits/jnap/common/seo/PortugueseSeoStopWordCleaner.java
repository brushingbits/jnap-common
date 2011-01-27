/*
 * PortugueseSeoStopWordCleaner.java created on 2011-01-26
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

import java.util.Locale;

/**
 * @author Daniel Rochetti
 * @since 1.0
 */
public class PortugueseSeoStopWordCleaner extends RegExpSeoStopWordCleaner {

	private static final Locale PORTUGUESE = new Locale("pt");
	private static final Locale PORTUGUESE_BRAZIL = new Locale("pt", "BR");
	private static final Locale[] SUPPORTED_LOCALES = new Locale[] { PORTUGUESE, PORTUGUESE_BRAZIL };

	private static final String[] STOP_WORDS = new String[] { "a", "as", "com",
			"da", "das", "de", "do", "dos", "e", "mas", "o", "os", "um" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoUnusefulWords() {
		return STOP_WORDS;
	}

}
