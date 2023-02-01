package CoffeeOrderWebApp.practice.member.service;

import CoffeeOrderWebApp.practice.member.repository.MemberRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@MockBean(JpaMetamodelMappingContext.class)
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    Gson gson = new Gson();

    @Test
    void findMember() {
    }

    @Test
    void addStampCount() {
    }
}