package com.cpoopc.smoothemojikeyboard.emotion;/**
 * Created by cpoopc on 2015/9/8.
 */

import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;

/**
 * User: cpoopc
 * Date: 2015-09-08
 * Time: 16:08
 * Ver.: 0.1
 */
public enum EmotionInputEventBus {
    instance;

    public interface EmotionInputEventListener {
        void onEmotionInput(EmotionEntity emotionEntity);
    }

    private EmotionInputEventListener emotionInputEventListener;

    public void setEmotionInputEventListener(EmotionInputEventListener emotionInputEventListener) {
        this.emotionInputEventListener = emotionInputEventListener;
    }

    public void postEmotion(EmotionEntity emotionEntity) {
        if (emotionInputEventListener != null) {
            emotionInputEventListener.onEmotionInput(emotionEntity);
        }
    }

}
