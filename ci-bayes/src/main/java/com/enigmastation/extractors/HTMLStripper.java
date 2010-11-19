package com.enigmastation.extractors;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Mar 20, 2009
 * Time: 1:49:30 PM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public interface HTMLStripper {
    String stripTags(String sourceText);
}
