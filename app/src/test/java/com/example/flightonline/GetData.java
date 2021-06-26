//package com.example.flightonline;
//
//import com.jdkeji.sdcx.common.exceptions.SdcxException;
//
//import org.apache.commons.lang.StringUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class GetData {
//    /**
//     * 携程 航班爬取数据工具类
//     * @Description:
//     * @author：haoqingshuang
//     * @CreatTime：2017年9月28日
//     *
//     */
//        //    http://flights.ctrip.com/actualtime/fno--CZ6902-20170928-BJS-URC.html
//        public static  Document getDocument(String url) throws SdcxException{
//            try {
//                return Jsoup.connect(url).get();
//            } catch (IOException e) {
//                throw new SdcxException("获取航班信息异常，请自行查询航班数据！");
//            }
//        }
//
//        /**
//         * 根据航班号 爬取航班信息
//         * @description:
//         * @author:haoqingshuang
//         * @CreateDate:2017年9月28日
//         */
//        public static String findFlightByFlightCode(String FlightNumber) throws SdcxException{
//            StringBuilder sBuffer = new StringBuilder();
//            if(StringUtils.isEmpty(FlightNumber)){
//                throw new SdcxException("请输入航班号！");
//            }
//            String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            Document doc = getDocument("http://flights.ctrip.com/actualtime/fno--"+FlightNumber+"-"+nowDate+".html");
//            if(null == doc){
//                throw new SdcxException("网络异常，请稍后再试！");
//            }
//
//            // 航班详情
//            Elements flightDetail = doc.select("[class=detail-m]");
//
//            //起飞详情
//            Elements detailfly = flightDetail.select("[class=detail-fly]");
//            //起飞地信息
//            Elements inldeparture = detailfly.select("[class=inl departure] p");//实际起飞--文字
//            sBuffer.append(commonIsNull(inldeparture)==""?"":commonIsNull(inldeparture)+":");
//            Elements inldepartureTime = detailfly.select("[class=inl departure] [class=time]");//实际起飞--时间
//            sBuffer.append(commonIsNull(inldepartureTime)+",");
//            Elements inldeparturegray = detailfly.select("[class=inl departure] [class=gray]");
//            sBuffer.append(commonIsNull(inldeparturegray)+",");
//            //当前飞机状态
//            Elements inlbetween = detailfly.select("[class=inl between]");
//            sBuffer.append(commonIsNull(inlbetween)==""?"":"当前飞机状态:"+commonIsNull(inlbetween)+",");
//            //目的地信息
//            Elements inlarrive = detailfly.select("[class=inl arrive] p");
//            sBuffer.append(commonIsNull(inlarrive)==""?"":commonIsNull(inlarrive)+":");//预计到达
//            Elements inlarriveTime = detailfly.select("[class=inl arrive] [class=time]");
//            sBuffer.append(commonIsNull(inlarriveTime)+",");//预计到达时间
//            Elements inlarrivegray = detailfly.select("[class=inl arrive] [class=gray]");
//            sBuffer.append(commonIsNull(inlarrivegray)+",");//计划到达 文字+时间
//
//            //路线详情
//            Elements detailroute = flightDetail.select("[class=detail-fly detail-route]");
//            Elements routeinldeparture = detailroute.select("[class=inl departure]");
//            sBuffer.append(commonIsNull(routeinldeparture)==""?"":"出发地信息:"+commonIsNull(routeinldeparture)+",");//出发地信息 机场、天气情况
//            //Elements routegray = routeinldeparture.select("[class=f12] [class=gray ml5]");
//            //sBuffer.append(commonIsNull(routegray)==""?"":"出发地天气:"+commonIsNull(routegray)+",");//出发地 天气
//            Elements routeinlbetween = detailroute.select("[class=inl between]").select("p");//飞行数据
//            sBuffer.append(commonDoubleIsNull(routeinlbetween)==""?"":"飞行数据:"+commonDoubleIsNull(routeinlbetween)+",");
//            Elements routeinlarrive = detailroute.select("[class=inl arrive]");//目的地信息 机场、天气情况
//            sBuffer.append(commonIsNull(routeinlarrive)==""?"":"目的地信息:"+commonIsNull(routeinlarrive)+",");
//
//            //附加信息  值机柜台、登机口、行李转盘
//            Elements additionalDetail = doc.select("[class=detail-info] [class=operation]");
//            Elements operation = additionalDetail.select("[class=item]");
//            sBuffer.append(commonDoubleIsNull(operation)==""?"":"附加信息:"+commonDoubleIsNull(operation));
//
//            if(StringUtils.isNotEmpty(sBuffer.toString().replaceAll(",","").replaceAll(" ","").replaceAll("机场攻略","")
//                    .replaceAll(":",""))){
//                //System.out.println(sBuffer.toString().replaceAll("机场攻略",""));
//                return sBuffer.toString().replaceAll("机场攻略","");
//            }else{
//                return null;
//            }
//        }
//
//        //单个拼接
//        public static String commonIsNull(Elements elements) {
//            try {
//                if(null != elements){
//                    if(null != elements.get(0)){
//                        return elements.get(0).text();
//                    }
//                }
//                return "";
//            } catch (Exception e) {
//                return "";
//            }
//        }
//
//        //多个字符串拼接 适合 多个 class=aaa
//        public static String commonDoubleIsNull(Elements elements) {
//            try {
//                StringBuilder sBuilder = new StringBuilder();
//                if(null != elements){
//                    for (int i = 0; i < elements.size(); i++) {
//                        if(null != elements.get(i)){
//                            sBuilder.append(elements.get(i).text());
//                        }
//                    }
//                }
//                return sBuilder.toString();
//            } catch (Exception e) {
//                return "";
//            }
//        }
//
//        public static void main(String[] args) {
//            try {
//                //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
//                System.out.println(findFlightByFlightCode("CA1815"));;
//                //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
//            } catch (SdcxException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//}
