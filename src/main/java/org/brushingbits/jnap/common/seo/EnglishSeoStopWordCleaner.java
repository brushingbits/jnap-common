/*
 * EnglishSeoStopWordCleaner.java created on 2011-01-26
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
public class EnglishSeoStopWordCleaner extends RegExpSeoStopWordCleaner {

	private static final Locale[] SUPPORTED_LOCALES = new Locale[] {
			Locale.ENGLISH, Locale.UK, Locale.US };

	private static final String[] STOP_WORDS = new String[] { "about", "after",
			"again", "all", "almost", "also", "although", "always", "among",
			"an", "and", "another", "any", "approximately", "are", "aren't",
			"as", "at", "be", "because", "been", "before", "being", "between",
			"both", "but", "by", "can", "could", "did", "do", "does", "done",
			"due", "during", "each", "either", "enough", "especially", "etc",
			"followed", "following", "for", "found", "from", "further", "give",
			"given", "giving", "had", "hardly", "has", "have", "haven't",
			"having", "here", "how", "however", "if", "in", "into", "is", "it",
			"its", "itself", "just", "kg", "km", "largely", "like", "made",
			"mainly", "make", "may", "might", "min", "ml", "mm", "more",
			"most", "mostly", "must", "nearly", "neither", "no", "nor", "not",
			"now", "obtain", "obtained", "of", "often", "on", "only", "or",
			"other", "our", "out", "over", "overall", "per", "perhaps",
			"possible", "previously", "quite", "rather", "really", "regarding",
			"resulted", "resulting", "same", "seem", "seen", "several",
			"should", "show", "showed", "shown", "shows", "significant",
			"significantly", "since", "so", "some", "such", "suggest", "than",
			"that", "the", "their", "theirs", "them", "then", "there", "these",
			"they", "this", "those", "through", "thus", "to", "under", "up",
			"upon", "use", "used", "using", "various", "very", "was", "wasn't",
			"we", "were", "weren't", "what", "when", "whereas", "which",
			"while", "will", "with", "within", "without", "would", "wouldn't" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoStopWords() {
		return STOP_WORDS;
	}

}
