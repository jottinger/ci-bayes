package com.enigmastation.extractors.impl;

import com.enigmastation.extractors.HTMLStripper;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.filters.Writer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Mar 20, 2009
 * Time: 1:49:53 PM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public class NekoHTMLStripper implements HTMLStripper {
    static final ElementRemover remover;

    static {
        remover = new ElementRemover();
        remover.removeElement("script");
        remover.removeElement("object");
        remover.removeElement("applet");
    }

    public String stripTags(String sourceText) {
        if (sourceText == null || sourceText.equals("")) {
            return "";
        }
        StringWriter sw = new StringWriter();
        Writer writer = new Writer(sw, null);
        XMLDocumentFilter[] filters = {
                remover, writer,
        };
        XMLParserConfiguration parser = new HTMLConfiguration();
        parser.setProperty("http://cyberneko.org/html/properties/filters", filters);

        XMLInputSource source = new XMLInputSource(null, null, null, new StringReader(sourceText), null);
        try {
            parser.parse(source);
            return sw.getBuffer()
                    .toString()
                    .replaceAll("&apos;", "'")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&lt;", "<")
                    .replaceAll("&quot;", "\"")
                    .replaceAll("&amp;", "&");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
