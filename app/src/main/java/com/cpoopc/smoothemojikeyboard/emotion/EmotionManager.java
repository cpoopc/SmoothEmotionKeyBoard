package com.cpoopc.smoothemojikeyboard.emotion;/**
 * Created by Administrator on 2015-09-07.
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.style.ImageSpan;

import com.cpoopc.smoothemojikeyboard.ContextHelper;
import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * User: cpoopc
 * Date: 2015-09-07
 * Time: 21:51
 */
public class EmotionManager {

    private static Map<String, SoftReference<Bitmap>> bitmapMap = new HashMap<>();

    public static ImageSpan getImageSpan(EmotionEntity emotionEntity) {
        Bitmap bitmap = getBitmap(emotionEntity.getSource());
        return new ImageSpan(ContextHelper.instance.getAppContext().getApplicationContext(), bitmap);
    }

    public static Bitmap getBitmap(String source) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapMap.get(source);
        if (bitmapSoftReference != null && bitmapSoftReference.get() != null) {
            return bitmapSoftReference.get();
        }
        AssetManager mngr = ContextHelper.instance.getAppContext().getAssets();
        InputStream in = null;

        try {
            in = mngr.open(source);
        } catch (Exception e){
            e.printStackTrace();
        }
        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
//        DebugLog.e("" + temp);
        bitmapSoftReference = new SoftReference<Bitmap>(temp);
        bitmapMap.put(source, bitmapSoftReference);
        return temp;
    }
}
