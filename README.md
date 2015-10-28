# SmoothEmojiKeyBoard
流畅切换表情/输入法的layout  
有许多输入法/表情切换的时候,会先把表情顶上去一下,有一种跳跃感.  
很多开发者都是通过延迟一定时间再来显示表情面板,这样只是减小了跳跃的概率,  
但是键盘的隐藏时间不确定,还是会出现跳跃的情况.  
这时候用户/产品/QA可能就不爽了......  
于是就搞了这个..
****
## Feature 

* 流畅切换 输入法/表情(不闪动)
* 自动适应 输入法高度变化,高度跟随,亦可限制表情栏最小高度,避免输入法高度太小导致表情显示不完整
* 自动适应 动态显示/隐藏navigationbar

* smoothly switch softKeyBoard|EmotionBoard
* autofit dynamic softKeyBoard height change
* autofit dynamic show/hide navigationbar


## PreView
<img width="400" height="720" src="https://github.com/cpoopc/SmoothEmotionKeyBoard/blob/master/preview/keyboard_preview.gif" />

## Usage
```
dependencies{
    compile 'com.github.cpoopc:smoothsoftinputlayout:1.0.0' // jcenter
}
```
0. android:windowSoftInputMode="adjustResize"
1. 继承BaseSoftInputLayout,自定义布局,实现父类定义的几个抽象方法(参考已经实现的2种布局:ChatSoftInputLayout|EditSoftInputLayout)
```
public class ChatSoftInputLayout extends BaseSoftInputLayout {

    @Override
    protected void inflateView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.chat_softinput_layout, this, true);
        ...
        setupEmotionView(layout);
        setupOtherView(layout);
    }

    private void setupEmotionView(View layout) {
        ...
        add2ShowViewList(emotionView);
        add2MappingMap(btnEmotion, SHOW_EMOTION, emotionView);// btnEmotion-(SHOW_EMOTION-emotionView)
    }

    @Override
    protected View getContainer() {
        return container;
    }

    @Override
    protected View getFrame() {
        return frame;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    protected View getBtnKeyBoard() {
        return null;
    }

}
```
 xml中引入控件
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <com.cpoopc.smoothemotionkeyboard.inputboard.ChatSoftInputLayout
        android:id="@+id/softinputLayout"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.cpoopc.smoothemotionkeyboard.inputboard.ChatSoftInputLayout>
</RelativeLayout>
```


