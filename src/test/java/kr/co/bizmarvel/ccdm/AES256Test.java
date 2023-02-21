package kr.co.bizmarvel.ccdm;

import kr.co.bizmarvel.ccdm.component.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CcdmApplication.class)
public class AES256Test {

    @Test
    void contextLoads() {
        String str = "{\"accessToken\":\"KyhQ7T9L0QhUx3iDe1IEJcXAbqG0eiWM8eN\", \"in_Oxygen\":\"1\",\"in_Carbon\":\"1\",\"in_Methane\":\"1\",\"in_AirCurrent\":\"1\", \"out_Oxygen\":\"1\",\"out_Carbon\":\"1\",\"out_Methane\":\"1\",\"out_AirCurrent\":\"1\", \"st_Temp\":\"1\",\"st_Humi\":\"1\",\"observedDate\":\"20221012111600\"}";

        try {
            System.out.println("=============================");
            String encStr = EncryptUtils.encryptAES256(str);
            System.out.println(encStr);
            System.out.println("-----------------------------");
            String plainStr = EncryptUtils.decryptAES256(encStr);
            System.out.println(plainStr);
            System.out.println("=============================");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
