package dev.sivalabs.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloSecurityApplicationTests {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    void indexWhenUnAuthenticatedThenRedirect()  {
        MvcTestResult testResult = mockMvcTester.get().uri("/").exchange();
        assertThat(testResult).hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser
    void indexWhenAuthenticatedThenOk() {
        MvcTestResult testResult = mockMvcTester.get().uri("/").exchange();
        assertThat(testResult).hasStatusOk();
    }

    //@WithMockUser not working
    @Test
    @WithMockUser
    void helloWhenAuthenticated() {
        MvcTestResult testResult = mockMvcTester.get().uri("/hello").exchange();
        assertThat(testResult).hasStatusOk().body().asString().isEqualTo("user");
    }

    //with(user(..)) working fine
    @Test
    void helloWhenAuthenticatedUsingWithUser() {
        MvcTestResult testResult = mockMvcTester.get().uri("/hello")
                .with(user("user"))
                .exchange();
        assertThat(testResult).hasStatusOk().body().asString().isEqualTo("user");
    }
}
