package uk.bl.wa.nlp.analysers;

/*-
 * #%L
 * warc-nlp
 * %%
 * Copyright (C) 2013 - 2018 The webarchive-discovery project contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import uk.bl.wa.analyser.text.AbstractTextAnalyser;

/**
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */

public class StanfordAnalyserTest {

    @Test
    public void checkServiceLoader() {
        // Get the config:
        Config conf = ConfigFactory.load();

        // create a new provider and call getMessage()
        List<AbstractTextAnalyser> providers = AbstractTextAnalyser
                .getTextAnalysers(conf);
        List<String> providerNames = new ArrayList<String>();
        for (AbstractTextAnalyser provider : providers) {
            System.out.println(provider.getClass().getCanonicalName());
            providerNames.add(provider.getClass().getCanonicalName());
        }

        assertTrue("Additional analyser not found.", providerNames
                .contains("uk.bl.wa.nlp.analysers.StanfordAnalyser"));
        assertTrue("Standard HTML analyser found.", providerNames
                .contains("uk.bl.wa.analyser.text.PostcodeAnalyser"));
    }


}
