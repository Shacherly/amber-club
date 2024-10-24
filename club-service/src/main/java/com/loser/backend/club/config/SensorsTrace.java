// package com.trading.backend.club.config;
//
// import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.DisposableBean;
// import org.springframework.stereotype.Component;
//
//
// @Slf4j @Component
// public class SensorsTrace implements DisposableBean {
//
//     private final SensorsAnalytics sensorsAnalytics;
//
//     private final SensorsAnalytics batchSensorsAnalytics;
//
//     public SensorsTrace(SensorsAnalytics sensorsAnalytics, SensorsAnalytics batchSensorsAnalytics) {
//         this.sensorsAnalytics = sensorsAnalytics;
//         this.batchSensorsAnalytics = batchSensorsAnalytics;
//     }
//
//     /**
//      * 写入本地日志，使用logAgent来处理
//      *
//      * @param uid
//      * @param event
//      * @param properties
//      */
//     public void trace(String uid, String event, java.util.Map<String, Object> properties) {
//         try {
//             sensorsAnalytics.track(uid, true, event, properties);
//             sensorsAnalytics.flush();
//         } catch (Exception e) {
//             log.error("Sensors trace exception {}", e.getMessage(), e);
//         }
//     }
//
//     /**
//      * 直接发送数据，正式环境不使用
//      *
//      * @param uid
//      * @param event
//      * @param properties
//      */
//     @Deprecated
//     public void trackBatch(String uid, String event, java.util.Map<String, Object> properties) {
//         try {
//             batchSensorsAnalytics.track(uid, true, event, properties);
//             batchSensorsAnalytics.flush();
//         } catch (Exception e) {
//             log.error("Sensors trackBatch exception:{}", e.getMessage());
//         }
//     }
//
//     @Override
//     public void destroy() throws Exception {
//         sensorsAnalytics.shutdown();
//         batchSensorsAnalytics.shutdown();
//     }
// }
