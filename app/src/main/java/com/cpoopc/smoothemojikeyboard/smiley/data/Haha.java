package com.cpoopc.smoothemojikeyboard.smiley.data;/**
 * Created by Administrator on 2015-09-03.
 */

import com.cpoopc.smoothemojikeyboard.smiley.bean.EmotionEntity;
import com.cpoopc.smoothemojikeyboard.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: cpoopc
 * Date: 2015-09-03
 * Time: 13:04
 */
public class Haha {

    public static final String pathBase = "emotions/";

    public static final Pattern PATTERN = Pattern.compile("\\{[^\\}]*\\}");

    public static final String INFO =
            "{[1],1.png},{[2],2.png},{[3],3.png},{[4],4.png},{[5],5.png},{[6],6.png},{[7],7.png},{[8],8.png},{[9],9.png},{[10],10.png}," +
            "{[11],11.png},{[12],12.png},{[13],13.png},{[14],14.png},{[15],15.png},{[16],16.png},{[17],17.png},{[18],18.png},{[19],19.png},{[20],20.png}," +
            "{[21],21.png},{[22],22.png},{[23],23.png},{[24],24.png},{[25],25.png},{[26],26.png},{[27],27.png},{[28],28.png},{[29],29.png},{[30],30.png}," +
            "{[31],31.png},{[32],32.png},{[33],33.png},{[34],34.png},{[35],35.png},{[36],36.png},{[37],37.png},{[38],38.png},{[39],39.png},{[40],40.png}," +
            "{[41],41.png},{[42],42.png},{[43],43.png},{[44],44.png},{[45],45.png},{[46],46.png},{[47],47.png},{[48],48.png},{[49],49.png},{[50],50.png}," +
            "{[51],51.png},{[52],52.png},{[53],53.png},{[54],54.png}";
//    ,{[55],55.png},{[56],56.png},{[57],57.png},{[58],58.png},{[59],59.png}";



    public static final List<EmotionEntity> DATA = initData();

    public static String path(String name) {
        return pathBase + name;
    }

    public static List<EmotionEntity> initData() {
        List<EmotionEntity> emotionEntityList = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(INFO);
        while (matcher.find()) {
            String group = matcher.group();
//            DebugLog.e(matcher.groupCount() + ":" + group);
            StringTokenizer st = new StringTokenizer(group.substring(1, group.length() - 1), ",");
            String code = st.nextToken();
            String name = st.nextToken();
            emotionEntityList.add(EmotionEntity.fromAssert(code, path(name)));
//            DebugLog.e("st:code:" + code);
//            DebugLog.e("st:name:" + name);
        }
        return emotionEntityList;
    }
}