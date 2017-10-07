package com.take_iwiw.tonguetwisterteacher

import org.atilika.kuromoji.Tokenizer

/**
 * Created by tak on 2017/10/07.
 */
class SentenceChecker {
    companion object {
        val JUDGE_SIMILAR_THRESHOLD = arrayOf(0.7, 0.8, 0.9)

        fun checkSimilar(strOriginal: String, strRecognized: String): Double {
            var str1 = strOriginal
            var str2 = strRecognized
            if (str1 == null || str1.trim().isEmpty()) {
                return if (str2 == null || str2.trim().isEmpty()) {
                    1.0
                } else {
                    0.0
                }
            } else if (str2 == null || str2.trim().isEmpty()) {
                return 0.0
            }
            str1 = eliminatePunctuation(str1)
            str2 = eliminatePunctuation(str2)

            val distance = computeLevenshteinDistance(str1, str2)
            return 1.0 - distance.toDouble() / Math.max(str2.length, str1.length)
        }

        fun checkSimilarJa(strKanjiMixed: String, strKatakana: String, strRecognized: String): Double {
            /* As for Japanse, Google Speech Recognizer always returns result including kanji (cannot get katakana only result) */
            /* Kanji might be different even if pronunciation is tha same, so I'll check both sentence in Kanji and sentence in Katakana, and use better result */
            var scoreKanji = 0.0
            var scoreKatakana = 0.0

            /* check sentence with kanji */
            scoreKanji = checkSimilar(strKanjiMixed, strRecognized)

            /* convert recognized sentence(including kanji) into katakana */
            val tokenizer = Tokenizer.builder().mode(Tokenizer.Mode.NORMAL).build()
            var tokens = tokenizer.tokenize(strRecognized)
            var recognizedKatakana = ""
            try {
                for (token in tokens) {
                    if (token.reading == null) {
                        /* workaround: tokenize sometimes returns null (it looks it happens when input sentence is all katakana) */
                        recognizedKatakana = strRecognized
                        break;
                    } else {
                        recognizedKatakana += token.reading
                    }
                }
            } catch (e: Exception) {
                Debug.logError(e.toString())
            }
            Debug.logDebug(recognizedKatakana)
            Debug.logDebug(strKatakana)
            scoreKatakana = checkSimilar(strKatakana, recognizedKatakana)

            return Math.max(scoreKanji, scoreKatakana)
        }


        /* refer: http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java */
        private fun computeLevenshteinDistance(str1: String, str2: String): Int {
            val distance = Array(str1.length + 1) { IntArray(str2.length + 1) }

            for (i in 0..str1.length)
                distance[i][0] = i
            for (j in 1..str2.length)
                distance[0][j] = j

            for (i in 1..str1.length)
                for (j in 1..str2.length)
                    distance[i][j] = minimum(
                            distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            distance[i - 1][j - 1] + if (str1[i - 1] == str2[j - 1]) 0 else 1)

            return distance[str1.length][str2.length]
        }

        private fun eliminatePunctuation(str: String): String {
            var str = str
            str = str.toLowerCase()
            str = str.replace(" ", "")
            str = str.replace("\r", "")
            str = str.replace("\n", "")
            str = str.replace(".", "")
            str = str.replace(",", "")
            str = str.replace("!", "")
            str = str.replace("?", "")
            str = str.replace("　", "")
            str = str.replace("、", "")
            str = str.replace("。", "")
            return str
        }

        private fun minimum(a: Int, b: Int, c: Int): Int {
            return Math.min(Math.min(a, b), c)
        }
    }
}