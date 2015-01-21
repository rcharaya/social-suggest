package com.yahoo.hack.server.category.impl;

import com.yahoo.hack.server.category.Categorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 10/10/11
 */
public abstract class AbstractLocalCategorizer implements Categorizer {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractLocalCategorizer.class);

    private final Set<String> modelWords;

    public AbstractLocalCategorizer(String modelFile) {
        try {
            InputStream is = AbstractLocalCategorizer.class.getResourceAsStream("/category-models/" + modelFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            Set<String> set = new HashSet<String>();
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.length() == 0) {
                    continue;
                }
                set.add(line);
            }
            modelWords = Collections.unmodifiableSet(set);
        } catch (Exception e) {
            LOG.error("error occured while reading mode file : " + modelFile, e);
            throw new AssertionError(e);
        }
    }


    protected double computScore(String text) {
        text = (text == null ? "" : text.trim()).toLowerCase();
        if (text.length() == 0) {
            return 0;
        }

        Set<String> words = new HashSet<String>();
        String[] tmp = text.split("\\s+");
        for (String w : tmp) {
            w = w.trim().toLowerCase();
            if (w.length() == 0) {
                continue;
            }
            words.add(w);
        }

        double count = 0;

        for (String w : words) {
            if (modelWords.contains(w)) {
                count += 1;
            }
        }

        return count;
    }

}
