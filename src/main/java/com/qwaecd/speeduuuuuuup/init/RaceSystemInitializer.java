package com.qwaecd.speeduuuuuuup.init;

import com.qwaecd.speeduuuuuuup.race.RaceEventManager;
import com.qwaecd.speeduuuuuuup.race.RaceMessageListener;

public class RaceSystemInitializer {
    public static void initialize() {
        // 注册消息监听器
        RaceEventManager.addListener(new RaceMessageListener());

        // 你还可以添加其他监听器
        // 比如统计监听器、数据库记录监听器等

        // 示例：添加一个简单的日志监听器
//        RaceEventManager.addListener(event -> {
//            System.out.println("Race Event: " + event.getEventType() +
//                             " - Player: " + event.getRacePlayer().getName() +
//                             " - Track: " + event.getRaceTrack().getName());
//        });
    }
}
