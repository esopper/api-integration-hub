package com.eriksopper.hub.testsupport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public final class JsonSupport {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonSupport() {}

    public static ObjectMapper mapper() { return MAPPER; }

    public static <T> T read(String json, Class<T> type) {
        try { return MAPPER.readValue(json, type); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static <T> T read(String json, TypeReference<T> type) {
        try { return MAPPER.readValue(json, type); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static void assertJsonStrict(String expected, String actual) {
        try { JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT); }
        catch (AssertionError e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static void assertJsonLenient(String expected, String actual) {
        try { JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT); }
        catch (AssertionError e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
