package umm3601.digitalDisplayGarden.Authentication;


import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class TestAuthorizedCookieBody {

    Auth auth;

    @Before
    public void setup() throws NoSuchAlgorithmException{
        auth = new Auth("fakeID", "fakeSecret", "callback");
    }

    @Test
    public void testThatCookieBodyIsAuthorizable(){
        String cookieBody = auth.generateCookieBody(120);
        assertNotNull(cookieBody);
        assertNotEquals("",cookieBody);

        boolean authorized = auth.authorized(cookieBody);
        assertTrue(authorized);
    }

    @Test
    public void testThatCookieBodyIsExpired(){
        String cookieBody = auth.generateCookieBody(0);
        boolean authorized = auth.authorized(cookieBody);
        assertFalse(authorized);
    }

    @Test
    public void testThatInvalidCookieBodyIsNotAuthorized(){
        boolean authorized = auth.authorized("SWvvnS4x1qPOQRUHbuX6s//TrXTk/ir74LDBp");
        assertFalse(authorized);
    }

}
