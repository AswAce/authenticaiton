package security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import security.utils.CookieUtils;
import javax.servlet.http.Cookie;

public class CookiesUtilsTest {

    private final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

    @Test
    public void testAddCookieSuccess() {
        CookieUtils.addCookie(mockHttpServletResponse, "test", "testValue", 30);
        Cookie test = mockHttpServletResponse.getCookie("test");

        Assertions.assertEquals(test.getValue(), "testValue");
    }

}
