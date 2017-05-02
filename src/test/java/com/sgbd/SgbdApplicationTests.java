package com.sgbd;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SgbdApplicationTests {

    @Test
    public void contextLoads() throws UnirestException, IOException {
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.get("http://localhost:9000/estate/getByFilters?city=iasi&offset=0")
                .asJson();

        StringWriter writer = new StringWriter();
        IOUtils.copy(jsonNodeHttpResponse.getRawBody(), writer);
        String theString = writer.toString();

        assertEquals(jsonNodeHttpResponse.getStatus(), 200);
        assertTrue(theString.contains("offset"));
        assertTrue(theString.contains("totalCount"));
        assertTrue(theString.contains("estates"));
    }
}
