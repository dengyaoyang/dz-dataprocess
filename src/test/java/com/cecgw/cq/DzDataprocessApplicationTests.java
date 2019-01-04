package com.cecgw.cq;

import com.cecgw.cq.repository.LSpeedRep;
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
	@Autowired
	LSpeedRep lSpeedRep;

//	@Test
//	public void contextLoads() {
//	}

	@Test
	public void lremData(){
//		jedisUtil.delListVal("test111","{\"c1\": \"2222\",\"c2\":   \"50010017191235\",\"color\":   \"2\",\"eid\":   \"504459995\",\"id\":   259689302304645130,\"localization\":   \"\",\"nature\":   \"\",\"plate\":   \"02\",\"readerip\":   \"1.1.1.1\",\"time\":   \"Tue Nov 13 11:26:10 CST 2018\",\"vehicle\":   \"2\"}");
		jedisUtil.delListVal("test111","{\"c2\":\"801394\",\"c2\":\"801394\",\"color\":\"2\",\"eid\":\"2137002\",\"id\": 1546051075163,\"localization\":\"8\",\"nature\":\"A\",\"plate\":\"COL_02\",\"readerip\":\"11.12.10.57\",\"time\": 1544590619000,\"vehicle\":\"K33\"}");
	}

	@Test
	public void removeLSpeed(){
		lSpeedRep.deleteAll();
	}

	@Test
	public void  removeTargetLen(){
		jedisUtil.getListMultValueAfterDel("ttt111");
	}



}
