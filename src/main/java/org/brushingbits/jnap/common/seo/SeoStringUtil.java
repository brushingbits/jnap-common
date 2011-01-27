/*
 * SeoStringUtil.java created on 2011-01-26
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
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 'slugs'
 * @author Daniel Rochetti
 * @since 1.0
 */
public class SeoStringUtil {

	private static Log logger = LogFactory.getLog(SeoStringUtil.class);

	private static List<SeoStopWordCleaner> seoStopWordCleaners;

	static {
		seoStopWordCleaners = new ArrayList<SeoStopWordCleaner>();
		seoStopWordCleaners.add(new EnglishSeoStopWordCleaner());
		seoStopWordCleaners.add(new FrenchSeoStopWordCleaner());
		seoStopWordCleaners.add(new GermanSeoStopWordCleaner());
		seoStopWordCleaners.add(new ItalianSeoStopWordCleaner());
		seoStopWordCleaners.add(new PortugueseSeoStopWordCleaner());
		seoStopWordCleaners.add(new SpanishSeoStopWordCleaner());
	}

	public static void registerSeoWordRemover(SeoStopWordCleaner seoWordCleaner) {
		seoStopWordCleaners.add(seoWordCleaner);
	}

	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String makeSeoFriendly(String src) {
		return makeSeoFriendly(src, true);
	}

	/**
	 * 
	 * @param src
	 * @param cleanUnusefulWords
	 * @return
	 */
	public static String makeSeoFriendly(String src, boolean cleanUnusefulWords) {
		return makeSeoFriendly(src, cleanUnusefulWords ? Locale.getDefault() : null);
	}

	/**
	 * 
	 * @param src
	 * @param locale
	 * @return
	 */
	public static String makeSeoFriendly(String src, Locale locale) {
		String seoFriendlyText = src.trim();

		// normalize
		seoFriendlyText = Normalizer.normalize(src, Form.NFD);

		// try to remove stop words if locale is specified
		if (locale != null) {
			SeoStopWordCleaner wordCleaner = null;
			for (SeoStopWordCleaner cleaner : seoStopWordCleaners) {
				if (ArrayUtils.contains(cleaner.getSupportedLocales(), locale)) {
					wordCleaner = cleaner;
					break;
				}
			}
			if (wordCleaner == null) {
				logger.warn(MessageFormat.format("A locale was specified ({0}) but no "
						+ "SeoStopWordCleaner was found for it", locale.toString()));
			} else {
				seoFriendlyText = wordCleaner.clean(seoFriendlyText);
			}
		}

		// replace duplicated spaces with a single one
		seoFriendlyText = seoFriendlyText.replaceAll("[\\s]{2,}", " ");

		// replace spaces with '-'
		seoFriendlyText = seoFriendlyText.replaceAll("[\\s]", "-");

		// remove remaining non-latin characters
		seoFriendlyText = seoFriendlyText.replaceAll("[^\\w-]", StringUtils.EMPTY);

		// convert to lowercase (using english locale rules) and return
		return seoFriendlyText.toLowerCase(Locale.ENGLISH);
	}

}
