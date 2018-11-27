package com.cecgw.cq.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.cecgw.cq.dao.GenericDao;
import com.cecgw.cq.pojo.EPC_DICT;
import com.cecgw.cq.pojo.T_RFID_STATION;
import com.cecgw.cq.service.FlowCountService;
import com.cecgw.cq.util.JedisUtil;
import com.cecgw.cq.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 照搬逻辑。。。
 * @author
 * @version V1.0
 * @since 2018-11-20
 */
@Service
public class FlowCountServiceImpl implements FlowCountService {

    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    GenericDao genericDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    VehicleFlowService vehicleFlowService;
    private int flag5 = 0;
    private int Flag1=0;

    private Map<String, Integer>map= new HashMap();
    private static final String YEELOW_flowtableD="T_YEELOW_FLOW_D";
    private Map<String,Map<String, Integer>> map_save_m = new HashMap();
    @Override
    public void doyellowcount() {
        List<String> station = jedisUtil.getList("t_rfid_station");
        List<T_RFID_STATION> stationList = JSONArray.parseArray(station.toString(),T_RFID_STATION.class);
        List<String> epcJson =  jedisUtil.getList("t_epc_dict");
        List<EPC_DICT> epcList= JSONArray.parseArray(epcJson.toString(),EPC_DICT.class);
        Calendar time=Calendar.getInstance();
        int currentHour = time.get(Calendar.HOUR_OF_DAY);
        int currentMinute=time.get(Calendar.MINUTE);
        Date date= time.getTime();
        String starttime= new String ();
        String endtime= new String ();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.add(Calendar.MINUTE,-10);
        //					currentTime.add(Calendar.SECOND, 1);//加1s
        date=time.getTime();
        endtime=df.format(date);

        time.add(Calendar.MINUTE,-5);
        date=time.getTime();
        starttime=df.format(date);
        //根据缓存的rfid 查询出黄标车数据
        List<Map<String,Object>> hbList = genericDao.getCurRfid(starttime,endtime);
        List<Map<String,Object>> hbListTotal = genericDao.getCurRfidTotal(starttime,endtime);
        String weekDay = TimeUtil.getEnumTime();
        insertDel(currentHour,currentMinute,flag5,df);

        //写入计算的周期时间，往前推5分钟
        time.add(Calendar.MINUTE,15);
        time.add(Calendar.SECOND,-1);
        date=time.getTime();
        endtime=df.format(date);
        int total_hb=0;
        String ip = "";
        for(int i=0;i<stationList.size();i++)
        {
            ip= stationList.get(i).getReaderip();
            List<String> list_yflow_code=new ArrayList<String>();
            for(int yj=0;yj<hbListTotal.size();yj++){
                if(hbListTotal.get(yj).get("READERIP").equals(ip)){
                    total_hb=Integer.parseInt(hbListTotal.get(yj).get("TATOL").toString()) ;
                    break;
                }
            }
            for(int j=0;j<hbList.size();j++){
                if(hbList.get(j).get("READERIP").equals(ip)){
                    list_yflow_code.add((String) hbList.get(j).get("NATURE"));
                    list_yflow_code.add((String) hbList.get(j).get("VEHICLE"));
                    list_yflow_code.add((String) hbList.get(j).get("PLATE"));
                }
            }
            Set uniqueSet=new HashSet(list_yflow_code);
            for(Object temp:uniqueSet){

                map.put((String) temp, Collections.frequency(list_yflow_code, temp));
            }

            update(YEELOW_flowtableD,map,endtime,total_hb,ip,weekDay,epcList);

            map.put("TATOL",total_hb);
            map_save_m.put(ip,map);
            //     add1(map);//数据累加
            total_hb=0;
            map= new HashMap<String,Integer>();


        }//ip循环 完
        String sql_insertyd="insert into T_YEELOW_FLOW select * from T_YEELOW_FLOW_D";
        jdbcTemplate.update(sql_insertyd);
        countFlag();


    }

    public void insertDel(int currentHour,int currentMinute,int flag5,SimpleDateFormat df){
        if((currentHour==0)&&(currentMinute>10)&&(flag5==0)){
            Calendar currentTime=Calendar.getInstance();
            String starttime2=new String ();
            String endtime2=new String ();
            currentTime.set(Calendar.HOUR_OF_DAY , 0);
            currentTime.set(Calendar.MINUTE, 0);
            currentTime.set(Calendar.SECOND, 0);
            currentTime.set(Calendar.MILLISECOND, 0);
            currentTime.add(Calendar.DAY_OF_MONTH, -1);
            Date date=currentTime.getTime();
            starttime2=df.format(date);
            currentTime.set(Calendar.HOUR_OF_DAY, 23);
            currentTime.set(Calendar.MINUTE, 59);
            currentTime.set(Calendar.SECOND, 59);
            //currentTime.set(Calendar.MILLISECOND, 100);
            date=currentTime.getTime();
            endtime2=df.format(date);
            String sql_requirement=" where CREATE_TIME between to_date('"+starttime2+"','YYYY-MM-DD HH24:MI:SS') and to_date('"+endtime2+"','YYYY-MM-DD HH24:MI:SS')";
            vehicleFlowService.insert_select("T_YEELOW_FLOW_HIS_HOUR", "T_YEELOW_FLOW_HIS_DAY",endtime2, sql_requirement);
            flag5=1;
            String sqldelete ="delete FROM  T_YEELOW_FLOW_HIS_DAY a 　WHERE rowid > ( SELECT min(rowid) 　FROM  T_YEELOW_FLOW_HIS_DAY b 　WHERE b.rfidip = a.rfidip and b.create_time=a.create_time)";
            jdbcTemplate.update(sqldelete);
        }
    }

    private void update(String tablename,Map<String, Integer>map,String updatetime,Integer total_hb,String ip,String weekday,List<EPC_DICT> epcList){
        String key;
        Integer value=0;
        String sql_insert1="INSERT INTO "+tablename+" (RFIDIP,WORK_DAY,TATOL,UPDATE_TIME";
        String sql_insert3="values ('"+ip+"','"+weekday+"','"+total_hb+"',to_date('"+updatetime+"','YYYY-MM-DD HH24:MI:SS') ";

        //update语句拼接

        for(int ui=0;ui<epcList.size();ui++){
            key=(String) epcList.get(ui).getCode();
            if(map.get(key)!=null){
                value=map.get(key);
                sql_insert1+=","+key;
                sql_insert3+=","+value;
            }
            else{

                sql_insert1+=","+key;
                sql_insert3+=",'0'";
            }
        }


        //*********************************2.插入数据******************//

        sql_insert1=sql_insert1+")"+sql_insert3+")";
        jdbcTemplate.update(sql_insert1);

    }

    public void countFlag(){
        Flag1++;
        if((Flag1==12)){
            Flag1=0;flag5=0;
        }
    }
}
