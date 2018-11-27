package com.cecgw.cq.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.Map;

@Service
public class VehicleFlowService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String T_VEHICLE_FLOW_His_DAY = "T_VEHICLE_FLOW_His_DAY";
	private String CODE_basedatename="T_EPC_DICT";
	private String T_VEHICLE_FLOW_His_HOUR="T_VEHICLE_FLOW_His_HOUR";

public void insert_select(String tablename1,String tablename2,String updatetime,String sql_requirement){
	String weekday="星期日";
	Calendar currentTime=Calendar.getInstance();//读取系统时间

				try{//查询今天是星期几？
					final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
                     
					//Date date = new Date(0);
					//calendar.setTime(date);
					int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK)-1;
					if(dayOfWeek <0)dayOfWeek=0;
					weekday=dayNames[dayOfWeek];
					if(tablename2==T_VEHICLE_FLOW_His_DAY){//如果是“天维度”的数据插入，则将星期向前推一天
						if(dayOfWeek-1 <0) {
                            dayOfWeek=7;
                        }
						weekday=dayNames[dayOfWeek-1];
					}
				}
				catch(Exception e){
					
				}
	String sql_epccode;//查询编码语句		
	String code;//编码

	String sql_insert_s;//插入-查询语句
	String sql_insert="insert into "+tablename2+"(rfidip,TATOL,WORK_DAY,CREATE_TIME";
	String sql_select="select RFIDIP,SUM(TATOL),'"+weekday+"',to_date('"+updatetime+"','YYYY-MM-DD HH24:MI:SS')";
	sql_epccode="SELECT CODE FROM "+CODE_basedatename+" where TYPE='NATURE' OR TYPE='VEHICLE' OR TYPE='PLATE'";
	List<Map<String, Object>> list_epccode = new ArrayList<Map<String, Object>>();//保存所有的车辆分类的类型
	list_epccode=jdbcTemplate.queryForList(sql_epccode);
	for(int code_i=0;code_i<list_epccode.size();code_i++){
		code=(String) list_epccode.get(code_i).get("CODE");//读出code编码，形如B11
		sql_insert+=","+code;
		sql_select+=",SUM("+code+")";
	}
	sql_select+="from "+tablename1;
	sql_insert+=")";
	sql_insert_s=sql_insert+sql_select+sql_requirement+" group by rfidip";//sql结构：insert....selecet...from table where update_time between...and ...group by rfidip
	System.out.println(sql_insert_s);
	jdbcTemplate.execute(sql_insert_s);//把数据存入历史表
	if(tablename2==T_VEHICLE_FLOW_His_HOUR){//如果是“小时维度”的数据插入完成后，则清空数据表
		String delete="delete from T_VEHICLE_FLOW"+sql_requirement;
		jdbcTemplate.execute(delete);
	}
	else if(tablename2=="T_YEELOW_FLOW_His_HOUR"){//如果是“小时维度”的数据插入完成后，则清空数据表
		String delete="delete from T_YEELOW_FLOW"+sql_requirement;
		jdbcTemplate.execute(delete);
	}
	
}

}
