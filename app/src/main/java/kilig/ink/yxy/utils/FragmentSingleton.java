package kilig.ink.yxy.utils;

import androidx.fragment.app.Fragment;

import kilig.ink.yxy.frament.AblumFragment;
import kilig.ink.yxy.frament.MyFragment;
import kilig.ink.yxy.frament.PublishFragment;

/**
 * 单例模式实践
 */
public class FragmentSingleton {
    private static  class AblumHolder{
        private static final Fragment ABLUMFRAGMENT=new AblumFragment();
    }
    private static class MyHolder{
        private static final Fragment MYFRAGMENT=new MyFragment();
    }
    private static class PublishHolder{
        private static final Fragment PUBLISHFRAGMENT=new PublishFragment();
    }
    private FragmentSingleton() {
    }

    /**
     * 返回单例碎片 保持不用每次都new 一次碎片，便于提高利用率
     * @param fragmentEnum
     * @return
     */
    public static final Fragment getFragment(FragmentEnum fragmentEnum){
        Fragment fragment=null;
        switch (fragmentEnum){
            case MY:
            {
                fragment= MyHolder.MYFRAGMENT;
                break;
            }case ABLUM:{
                fragment= AblumHolder.ABLUMFRAGMENT;
                break;
            }case PUBLISH:{
                fragment =PublishHolder.PUBLISHFRAGMENT;
            }
        }
        return fragment;
    }


}
