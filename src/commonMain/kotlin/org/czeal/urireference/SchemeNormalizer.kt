package org.czeal.urireference



/**
 * 
 * 
 * *NOTE: This class is intended for internal use only.*
 * 
 * 
 * 
 * 
 * Normalizes the `scheme` component of a URI reference according to
 * [RFC 3986, Section
 * 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
 * 
 * 
 * @see [ RFC 3986,
 * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
 * 
 * 
 * @author Hideki Ikeda
 */
internal class SchemeNormalizer {
    /**
     * Normalizes a `scheme` according to [
     * RFC 3986, Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986#section-6).
     * 
     * @param scheme
     * A scheme to normalize.
     * 
     * @return
     * A new string value representing the normalized scheme.
     * 
     * @see [RFC 3986,
     * Section 6: Normalization and Comparison](https://www.rfc-editor.org/rfc/rfc3986.section-6)
     */
    fun normalize(scheme: String?): String {
        // Convert the scheme value to lower-case based on the following
        // requirement.
        //
        //   RFC 3986, 6.2.2.1. Case Normalization
        //
        //     When a URI uses components of the generic syntax, the
        //     component syntax equivalence rules always apply; namely,
        //     that the scheme and host are case-insensitive and therefore
        //     should be normalized to lowercase.

        return scheme!!.lowercase()
    }
}
