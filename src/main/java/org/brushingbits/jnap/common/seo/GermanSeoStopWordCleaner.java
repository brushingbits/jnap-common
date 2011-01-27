/*
 * GermanSeoStopWordCleaner.java created on 2011-01-26
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
public class GermanSeoStopWordCleaner extends RegExpSeoStopWordCleaner {

	private static final Locale[] SUPPORTED_LOCALES = new Locale[] {
			Locale.GERMAN, Locale.GERMANY };

	private static final String[] STOP_WORDS = new String[] { "aber", "als",
			"am", "an", "auch", "auf", "aus", "bei", "bin", "bis", "bist",
			"da", "dadurch", "daher", "darum", "das", "daﬂ", "dass", "dein",
			"deine", "dem", "den", "der", "des", "dessen", "deshalb", "die",
			"dies", "dieser", "dieses", "doch", "dort", "du", "durch", "ein",
			"eine", "einem", "einen", "einer", "eines", "er", "es", "euer",
			"eure", "fur", "hatte", "hatten", "hattest", "hattet", "hier 	",
			"hinter", "ich", "ihr", "ihre", "im", "in", "ist", "ja", "jede",
			"jedem", "jeden", "jeder", "jedes", "jener", "jenes", "jetzt",
			"kann", "kannst", "konnen", "konnt", "machen", "mein", "meine",
			"mit", "muﬂ", "muﬂt", "musst", "mussen", "nach", "nachdem", "nein",
			"nicht", "nun", "oder", "seid", "sein", "seine", "sich", "sie",
			"sind", "soll", "sollen", "sollst", "sollt", "sonst", "soweit",
			"sowie", "und", "unser 	", "unsere", "unter", "vom", "von", "vor",
			"wann", "warum", "was", "weiter", "weitere", "wenn", "wer",
			"werde", "werden", "werdet", "weshalb", "wie", "wieder", "wieso",
			"wir", "wird", "wirst", "wo", "woher", "wohin", "zu", "zum", "zur",
			"uber" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoUnusefulWords() {
		return STOP_WORDS;
	}

}
