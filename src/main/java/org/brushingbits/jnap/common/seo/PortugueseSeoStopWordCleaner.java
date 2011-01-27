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
	private static final Locale[] SUPPORTED_LOCALES = new Locale[] {
			PORTUGUESE, PORTUGUESE_BRAZIL };

	private static final String[] STOP_WORDS = new String[] { "a", "agora",
			"ainda", "alguem", "algum", "alguma", "algumas", "alguns", "ampla",
			"amplas", "amplo", "amplos", "ante", "antes", "ao", "aos", "apos",
			"aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "ate",
			"atraves", "cada", "coisa", "coisas", "com", "como", "contra",
			"contudo", "da", "daquele", "daqueles", "das", "de", "dela",
			"delas", "dele", "deles", "depois", "dessa", "dessas", "desse",
			"desses", "desta", "destas", "deste", "destes", "deve", "devem",
			"devendo", "dever", "devera", "deverao", "deveria", "deveriam",
			"devia", "deviam", "disse", "disso", "disto", "dito", "diz",
			"dizem", "do", "dos", "e", "ela", "elas", "ele", "eles", "em",
			"enquanto", "entre", "era", "essa", "essas", "esse", "esses",
			"esta", "estamos", "estao", "estas", "estava", "estavam",
			"estavamos", "este", "estes", "estou", "eu", "fazendo", "fazer",
			"feita", "feitas", "feito", "feitos", "foi", "for", "foram",
			"fosse", "fossem", "grande", "grandes", "ha", "isso", "isto", "ja",
			"la", "lhe", "lhes", "lo", "mas", "me", "mesma", "mesmas", "mesmo",
			"mesmos", "meu", "meus", "minha", "minhas", "muita", "muitas",
			"muito", "muitos", "na", "nao", "nas", "nem", "nenhum", "nessa",
			"nessas", "nesta", "nestas", "ninguem", "no", "nos", "nossa",
			"nossas", "nosso", "nossos", "num", "numa", "nunca", "o", "os",
			"ou", "outra", "outras", "outro", "outros", "para", "pela",
			"pelas", "pelo", "pelos", "pequena", "pequenas", "pequeno",
			"pequenos", "per", "perante", "pode", "pude", "podendo", "poder",
			"poderia", "poderiam", "podia", "podiam", "pois", "por", "porem",
			"porque", "posso", "pouca", "poucas", "pouco", "poucos",
			"primeiro", "primeiros", "propria", "proprias", "proprio",
			"proprios", "quais", "qual", "quando", "quanto", "quantos", "que",
			"quem", "sao", "se", "seja", "sejam", "sem", "sempre", "sendo",
			"sera", "serao", "seu", "seus", "si", "sido", "so", "sob", "sobre",
			"sua", "suas", "talvez", "tambem", "tampouco", "te", "tem",
			"tendo", "tenha", "ter", "teu", "teus", "ti", "tido", "tinha",
			"tinham", "toda", "todas", "todavia", "todo", "todos", "tu", "tua",
			"tuas", "tudo", "ultima", "ultimas", "ultimo", "ultimos", "um",
			"uma", "umas", "uns", "vendo", "ver", "vez", "vindo", "vir", "vos" };

	public Locale[] getSupportedLocales() {
		return SUPPORTED_LOCALES;
	}

	public String[] getSeoUnusefulWords() {
		return STOP_WORDS;
	}

}
