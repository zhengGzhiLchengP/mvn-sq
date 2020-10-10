package com.feiqu.websocket.listener;//package com.feiqu.web.websocket.listener;
//
//import cn.hutool.core.date.DateUtil;
//import com.feiqu.common.utils.SpringUtils;
//import com.feiqu.framwork.util.CommonUtils;
//import com.feiqu.system.model.basicData.WsLog;
//import com.feiqu.system.service.basicData.WsLogService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.tio.core.ChannelContext;
//import org.tio.core.GroupContext;
//import org.tio.core.intf.Packet;
//import org.tio.core.stat.IpStat;
//import org.tio.core.stat.IpStatListener;
//import org.tio.utils.json.Json;
//
//public class SpringBootIpStatListener implements IpStatListener {
//    private static Logger log = LoggerFactory.getLogger(SpringBootIpStatListener.class);
//
//    @Override
//    public void onExpired(GroupContext groupContext, IpStat ipStat) {
////在这里把统计数据入库中或日志
//        log.debug("可以把统计数据入库\r\n{}", Json.toFormatedJson(ipStat));
//        WsLog wsLog = new WsLog();
//        wsLog.setBytespertcpreceive(ipStat.getBytesPerTcpReceive());
//        wsLog.setDecodeerrorcount(ipStat.getDecodeErrorCount().get());
//        wsLog.setDuration(ipStat.getDuration());
//        wsLog.setDurationtype(ipStat.getDurationType().intValue());
//        wsLog.setHandledBytes(ipStat.getHandledBytes().get());
//        wsLog.setFormatedDuration(ipStat.getFormatedDuration());
//        wsLog.setHandledCostsPerPacket((float) ipStat.getHandledCostsPerPacket());
//        wsLog.setHandledPackets(ipStat.getHandledPackets().intValue());
//        wsLog.setIp(ipStat.getIp()+" "+CommonUtils.getRegionByIp(ipStat.getIp()));
//        wsLog.setPacketsPerTcpReceive(ipStat.getPacketsPerTcpReceive());
//        wsLog.setReceivedBytes(ipStat.getReceivedBytes().get());
//        wsLog.setReceivedPackets(ipStat.getReceivedPackets().intValue());
//        wsLog.setSentPackets(ipStat.getSentPackets().intValue());
//        wsLog.setHandledPacketCosts(ipStat.getHandledPacketCosts().intValue());
//        wsLog.setStart(DateUtil.format(ipStat.getStart(), "yyyy-MM-dd HH:mm:ss"));
//        wsLog.setRequestCount(ipStat.getRequestCount().get());
//        wsLog.setReceivedTcps(ipStat.getReceivedTcps().intValue());
//        wsLog.setSentBytes(ipStat.getSentBytes().get());
//        WsLogService wsLogService = SpringUtils.getBean(WsLogService.class);
//        wsLogService.insert(wsLog);
//    }
//
//    @Override
//    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect, IpStat ipStat) throws Exception {
//
//    }
//
//    @Override
//    public void onDecodeError(ChannelContext channelContext, IpStat ipStat) {
//
//    }
//
//    @Override
//    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess, IpStat ipStat) throws Exception {
//
//    }
//
//    @Override
//    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize, IpStat ipStat) throws Exception {
//
//    }
//
//    @Override
//    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes, IpStat ipStat) throws Exception {
//
//    }
//
//    @Override
//    public void onAfterHandled(ChannelContext channelContext, Packet packet, IpStat ipStat, long cost) throws Exception {
//
//    }
//}
