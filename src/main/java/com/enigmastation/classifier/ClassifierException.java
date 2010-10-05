package com.enigmastation.classifier;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Mar 5, 2009
 * Time: 8:11:22 PM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public class ClassifierException extends Error {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6041857463765015700L;

	public ClassifierException() {
    }

    public ClassifierException(String message) {
        super(message);
    }

    public ClassifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassifierException(Throwable cause) {
        super(cause);
    }
}
