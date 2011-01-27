/*
 * FrenchSeoStopWordCleaner.java created on 2011-01-26
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
public class FrenchSeoStopWordCleaner extends RegExpSeoStopWordCleaner {

	private static final Locale[] SUPPORTED_LOCALES = new Locale[] { Locale.FRANCE, Locale.FRENCH };

	private static final String[] STOP_WORDS = new String[] { "a", "adieu",
			"afin", "ah", "ai", "aie", "aient", "aies", "aille", "ainsi",
			"ait", "all", "alla", "allais", "allait", "allant", "alle",
			"aller", "allerent", "allez", "allons", "alors", "apres", "aprcs",
			"as", "assez", "au", "au-dela", "au-delr", "au-dessous",
			"au-dessus", "aucun", "aucune", "aucunes", "aucuns", "aupres",
			"auprcs", "auquel", "aura", "aurai", "aurais", "aurez", "auront",
			"aussi", "aussitot", "autant", "autour", "autre", "autres",
			"autrui", "aux", "auxquelles", "auxquels", "av", "avaient",
			"avais", "avait", "aval", "avant", "avec", "avez", "avoir",
			"avons", "ayant", "ayez", "ayons", "bah", "bas", "beaucoup",
			"bien", "bonte", "bout", "but", "c'", "c'est-a-dire",
			"c'est-r-dire", "ca", "car", "ce", "ceci", "cela", "celle",
			"celle-ci", "celle-la", "celle-lr", "celles", "celles-ci",
			"celles-la", "celles-lr", "celui", "celui-ci", "celui-la",
			"celui-lr", "cependant", "ces", "cet", "cette", "ceux", "ceux-ci",
			"ceux-la", "ceux-lr", "chacun", "chacune", "chaque", "chez",
			"chut", "ci", "circa", "combien", "comme", "comment", "commme",
			"compte", "contre", "crac", "crainte", "cotc", "d", "d'", "dans",
			"de", "deca", "dedans", "dehors", "dela", "delr", "depuis", "des",
			"desquelles", "desquels", "dessous", "dessus", "devant", "deçr",
			"dire", "divers", "diverses", "donc", "dont", "du", "duquel",
			"durant", "dcs", "depens", "depit", "e", "elle", "elle-meme",
			"elles", "elles-memes", "en", "entre", "envers", "es", "est", "et",
			"etaient", "etais", "etait", "etant", "ete", "etes", "etiez",
			"etions", "etre", "eu", "eurent", "eut", "eux", "eux-memes", "fai",
			"faire", "fais", "faisais", "faisait", "faisant", "faisons",
			"fait", "faites", "fasse", "faute", "fera", "ferai", "ferais",
			"feras", "ferez", "ferons", "firent", "fit", "font", "furent",
			"fut", "he", "helas", "holr", "hors", "il", "ils", "irai", "irais",
			"iras", "irons", "iront", "j'", "je", "jusqu'", "jusque", "l'",
			"la", "laquelle", "le", "lequel", "les", "lesquelles", "lesquels",
			"leur", "leurs", "lieu", "loin", "lors", "lorsqu'", "lorsque",
			"lui", "lui-meme", "m'", "ma", "mains", "maintes", "maints",
			"mais", "malgre", "me", "merci", "mes", "mien", "mienne",
			"miennes", "miens", "milieu", "moi", "moi-meme", "moins", "mon",
			"moyen", "meme", "memes", "na", "ne", "neanmoins", "ni", "nom.",
			"nombre", "non", "nos", "notre", "notres", "nous", "nous-memes",
			"on", "ont", "or", "ou", "ouais", "oude", "par", "par-dela",
			"par-delr", "par-dessus", "parce", "parmi", "part", "partant",
			"partir", "pas", "passe", "pendant", "personne", "peu",
			"peut-etre", "plein", "plupart", "plus", "plusieurs", "plutot",
			"pour", "pourquoi", "pourvu", "pres", "prises", "proche", "proie",
			"prcs", "puis", "puisqu'", "puisque", "periode", "qu'", "quand",
			"que", "quel", "quelconque", "quelle", "quelles", "quelqu'un",
			"quelque", "quelques", "quelques-unes", "quelques-uns", "quels",
			"qui", "quiconque", "quoi", "quoique", "revoici", "revoila",
			"revoilr", "rien", "s'", "sa", "sais", "sans", "sauf", "se",
			"sein", "selon", "sens", "sera", "serai", "serais", "seras",
			"serez", "serons", "seront", "ses", "si", "sien", "sienne",
			"siennes", "siens", "signe", "sinon", "soi", "soi-meme", "soient",
			"sois", "soit", "sommes", "son", "sont", "souci", "sous", "soyez",
			"soyons", "suis", "sur", "surtout", "sus", "ta", "tandis", "tant",
			"te", "tel", "telle", "telles", "tels", "tes", "toc", "toi", "ton",
			"tous", "tout", "toute", "toutes", "travers", "trop", "trcve",
			"tu", "un", "une", "unes", "uns", "va", "vais", "vas", "vers",
			"voici", "voie", "voila", "voilr", "vont", "vos", "vous",
			"vous-meme", "vous-memes", "vu", "vue", "votre", "votres", "y", "r" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoStopWords() {
		return STOP_WORDS;
	}

}
