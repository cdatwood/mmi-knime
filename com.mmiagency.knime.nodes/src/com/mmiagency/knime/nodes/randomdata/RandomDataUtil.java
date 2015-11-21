/*
 * ------------------------------------------------------------------------
 * Copyright by MMI Agency, Houston, Texas, USA
 * Website: http://www.mmiagency.com; Contact: 713-929-6900
 *
 * The MMI KNIME Node is Copyright (C) 2015, MMI Agency The KNIME Nodes 
 * are free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your 
 * option) any later version. 
 * 
 * The KNIME Nodes are distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details. You should have received a 
 * copy of the GNU General Public License along with the KNIME Nodes. If 
 * not, see <http://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 */
package com.mmiagency.knime.nodes.randomdata;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomDataUtil {

    /*
     * The Lorem Ipsum Standard Paragraph
     */
    protected final String standard = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private final String[] words = { "a", "ac", "accumsan", "ad", "adipiscing",
        "aenean", "aliquam", "aliquet", "amet", "ante", "aptent", "arcu",
        "at", "auctor", "augue", "bibendum", "blandit", "class", "commodo",
        "condimentum", "congue", "consectetur", "consequat", "conubia",
        "convallis", "cras", "cubilia", "cum", "curabitur", "curae",
        "cursus", "dapibus", "diam", "dictum", "dictumst", "dignissim",
        "dis", "dolor", "donec", "dui", "duis", "e", "egestas", "eget",
        "eleifend", "elementum", "elit", "enim", "erat", "eros", "est",
        "et", "etiam", "eu", "euismod", "facilisi", "facilisis", "fames",
        "faucibus", "felis", "fermentum", "feugiat", "fringilla", "fusce",
        "gravida", "habitant", "habitasse", "hac", "hendrerit",
        "himenaeos", "iaculis", "id", "imperdiet", "in", "inceptos",
        "integer", "interdum", "ipsum", "justo", "lacinia", "lacus",
        "laoreet", "lectus", "leo", "libero", "ligula", "litora",
        "lobortis", "lorem", "luctus", "maecenas", "magna", "magnis",
        "malesuada", "massa", "mattis", "mauris", "metus", "mi",
        "molestie", "mollis", "montes", "morbi", "mus", "nam", "nascetur",
        "natoque", "nec", "neque", "netus", "nibh", "nisi", "nisl", "non",
        "nostra", "nulla", "nullam", "nunc", "odio", "orci", "ornare",
        "parturient", "pellentesque", "penatibus", "per", "pharetra",
        "phasellus", "placerat", "platea", "porta", "porttitor", "posuere",
        "potenti", "praesent", "pretium", "primis", "proin", "pulvinar",
        "purus", "quam", "quis", "quisque", "rhoncus", "ridiculus",
        "risus", "rutrum", "sagittis", "sapien", "scelerisque", "sed",
        "sem", "semper", "senectus", "sit", "sociis", "sociosqu",
        "sodales", "sollicitudin", "suscipit", "suspendisse", "taciti",
        "tellus", "tempor", "tempus", "tincidunt", "torquent", "tortor",
        "tristique", "turpis", "ullamcorper", "ultrices", "ultricies",
        "urna", "ut", "varius", "vehicula", "vel", "velit", "venenatis",
        "vestibulum", "vitae", "vivamus", "viverra", "volutpat",
        "vulputate"};
    private final String[] punctuation = { ".", "?"};
    private final String _n = "\n";
    private Random random = new Random();
    private int maxWordLength = 0;

    public RandomDataUtil() {
        for (int i = 0;i < words.length;i++) {
            if (words[i].length() > maxWordLength) maxWordLength = words[i].length();
        }
    }


    /**
     * Get a random word
     */
    public String randomWord() {
        return words[random.nextInt(words.length - 1)];
    }

    /**
     * Get a random punctuation mark
     */
    public String randomPunctuation() {
        return punctuation[random.nextInt(punctuation.length - 1)];
    }

    /**
     * Get a string of words
     * 
     * @param count
     *            - the number of words to fetch
     */
    public String words(int count) {
        StringBuilder s = new StringBuilder();
        while (count-- > 0)
            s.append(randomWord()).append(" ");
        return s.toString().trim();
    }

    /**
     * Get a sentence fragment
     */
    public String sentenceFragment() {
        return words(random.nextInt(10) + 3);
    }

    /**
     * Get a sentence
     */
    public String sentence() {
        // first word
        String w = randomWord();
        StringBuilder s = new StringBuilder(w.substring(0, 1).toUpperCase())
                          .append(w.substring(1)).append(" ");
        // commas?
        if (random.nextBoolean()) {
            int r = random.nextInt(3) + 1;
            for (int i = 0; i < r; i++)
                s.append(sentenceFragment()).append(", ");
        }
        // last fragment + punctuation
        return s.append(sentenceFragment()).append(randomPunctuation())
        .toString();
    }

    /**
     * Get multiple sentences
     * 
     * @param count
     *            - the number of sentences
     */
    public String sentences(int count) {
        StringBuilder s = new StringBuilder();
        while (count-- > 0)
            s.append(sentence()).append("  ");
        return s.toString().trim();
    }

    /**
     * Get a paragraph
     * 
     * @useStandard - get the standard Lorem Ipsum paragraph?
     */
    public String paragraph(boolean useStandard) {
        return useStandard ? standard : sentences(random.nextInt(3) + 2);
    }

    public String paragraph() {
        return paragraph(false);
    }

    /**
     * Get multiple paragraphs
     * 
     * @param count
     *            - the number of paragraphs
     * @useStandard - begin with the standard Lorem Ipsum paragraph?
     */
    public String paragraphs(int count, boolean useStandard) {
        StringBuilder s = new StringBuilder();
        while (count-- > 0) {
            s.append(paragraph(useStandard)).append(_n).append(_n);
            useStandard = false;
        }
        return s.toString().trim();
    }

    public String paragraphs(int count) {
        return paragraphs(count, false);
    }

    public String randomWord(int length) {
        List<String> wordsWithLength = new ArrayList<String>();
        for (int i = 0;i < words.length;i++) {
            if (words[i].length() == length) {
                wordsWithLength.add(words[i]);
            }
        }
        int randomIndex = random.nextInt(wordsWithLength.size());
        return wordsWithLength.get(randomIndex);            
    }

    public String randomSentence(int length) {
        if (length < maxWordLength) return randomWord(length);

        StringBuffer currentText = new StringBuffer("");
        while (true) {
            // Capitalize the first word
            if (currentText.length() == 0) {
                String word = randomWord();
                if (word.length() == 1) {
                    word = word.toUpperCase();
                } else {
                    word = word.substring(0, 1).toUpperCase() + word.substring(1);
                }
                currentText.append(word);

                int remainingLength = length - currentText.length();
                if (remainingLength == 1) {
                    currentText.append(".");
                    return currentText.toString();
                } else if (remainingLength == 0) {
                    return currentText.toString();
                }
            } else {
                currentText.append(" ");
                int remainingLength = length - currentText.length();

                if (remainingLength == 1) {
                    currentText.append(".");
                    return currentText.toString();
                } else if (remainingLength <= (maxWordLength + 1)) {
                    currentText.append(randomWord(remainingLength - 1));
                    currentText.append(".");
                    return currentText.toString();
                }
                currentText.append(randomWord());
            }
        }
    }

    public String randomText(int length) {
        if (length <=0 ) return "";
        if (length < maxWordLength) return randomWord(length);

        StringBuffer currentText = new StringBuffer("");
        while (currentText.length() < length) {


            String newSentence = sentence();
            // Add "  " before the sentence if this is the second sentence
            if (currentText.length() > 1) newSentence = "  " + newSentence;

            // If the new length is higher than max length then trim the last sentence
            int newLength = currentText.length() + newSentence.length();
            if ((length - newLength) < 100) {
                int remainingLength = length - currentText.length();
                if (currentText.length() > 1) {
                    currentText.append("  ");
                    remainingLength -= 2;
                }
                currentText.append(randomSentence(remainingLength));
                return currentText.toString();
            } else if (newLength > length) {
                int remainingLength = length - currentText.length();
                if (remainingLength <= (maxWordLength + 1)) {
                    currentText.append(randomWord(remainingLength - 1));
                    currentText.append(".");
                    return currentText.toString();
                }
                currentText.append("  ");
                remainingLength = length - currentText.length();
                currentText.append(randomSentence(remainingLength));
                return currentText.toString();
            }

            currentText.append(newSentence);
        }
        return currentText.toString();
    }
}
