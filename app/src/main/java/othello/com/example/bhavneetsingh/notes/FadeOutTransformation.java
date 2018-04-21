package othello.com.example.bhavneetsingh.notes;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by bhavneet singh on 20-Apr-18.
 */

public class FadeOutTransformation implements ViewPager.PageTransformer{
    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-position*page.getWidth());

        page.setAlpha(1-Math.abs(position));

    }
}
