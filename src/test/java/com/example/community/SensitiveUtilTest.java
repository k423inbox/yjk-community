package com.example.community;

import com.example.community.util.SensitiveFiliter;
import com.example.community.util.SensitiveFiliter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SensitiveUtilTest {
    @Autowired
    private SensitiveFiliter sensitiveFiliter;
    @Test
    public void testSensitiveFiliter(){
        String text = "赌!博，吸!毒，!开!票!，嫖娼";
        text=sensitiveFiliter.filter(text);
        System.out.println(text);
    }

}
