package com.cecgw.cq;

import com.cecgw.cq.util.JedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DzDataprocessApplicationTests {

	@Autowired
    JedisUtil jedisUtil;
	@Test
	public void contextLoads() {
	}

	@Test
	public void lremData(){
		jedisUtil.delListVal("test111","{\"c1\": \"2222\",\"c2\":   \"50010017191235\",\"color\":   \"2\",\"eid\":   \"504459995\",\"id\":   259689302304645130,\"localization\":   \"\",\"nature\":   \"\",\"plate\":   \"02\",\"readerip\":   \"1.1.1.1\",\"time\":   \"Tue Nov 13 11:26:10 CST 2018\",\"vehicle\":   \"2\"}");
	}



}
