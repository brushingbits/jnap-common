/*
 * ItalianSeoStopWordCleaner.java created on 2011-01-26
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
public class ItalianSeoStopWordCleaner extends RegExpSeoStopWordCleaner {

	private static final Locale[] SUPPORTED_LOCALES = new Locale[] {
			Locale.ITALY, Locale.ITALIAN };

	private static final String[] STOP_WORDS = new String[] { "a", "adesso",
			"ai", "al", "alla", "allo", "allora", "altre", "altri", "altro",
			"anche", "ancora", "avere", "aveva", "avevano", "ben", "buono",
			"che", "chi", "cinque", "comprare", "con", "consecutivi",
			"consecutivo", "cosa", "cui", "da", "del", "della", "dello",
			"dentro", "deve", "devo", "di", "doppio", "due", "e", "ecco",
			"fare", "fine", "fino", "fra", "gente", "giu", "ha", "hai",
			"hanno", "ho", "il", "indietro 	", "invece", "io", "la", "lavoro",
			"le", "lei", "lo", "loro", "lui", "lungo", "ma", "me", "meglio",
			"molta", "molti", "molto", "nei", "nella", "no", "noi", "nome",
			"nostro", "nove", "nuovi", "nuovo", "o", "oltre", "ora", "otto",
			"peggio", "pero", "persone", "piu", "poco", "primo", "promesso",
			"qua", "quarto", "quasi", "quattro", "quello", "questo", "qui",
			"quindi", "quinto", "rispetto", "sara", "secondo", "sei",
			"sembra 	", "sembrava", "senza", "sette", "sia", "siamo", "siete",
			"solo", "sono", "sopra", "soprattutto", "sotto", "stati", "stato",
			"stesso", "su", "subito", "sul", "sulla", "tanto", "te", "tempo",
			"terzo", "tra", "tre", "triplo", "ultimo", "un", "una", "uno",
			"va", "vai", "voi", "volte", "vostro" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoUnusefulWords() {
		return STOP_WORDS;
	}

}
