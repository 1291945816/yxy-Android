package kilig.ink.yxy.utils;

import androidx.fragment.app.Fragment;

import kilig.ink.yxy.frament.AlbumFragment;
import kilig.ink.yxy.frament.MineFragment;
import kilig.ink.yxy.frament.SquareFragment;

/**
 * 单例模式实践
 */
public class FragmentSingleton {
    private static  class AblumHolder{
        private static final Fragment ABLUMFRAGMENT=new AlbumFragment();
    }
    private static class MyHolder{
        private static final Fragment MYFRAGMENT=new MineFragment();
    }
    private static class PublishHolder{
        private static final Fragment PUBLISHFRAGMENT=new SquareFragment();
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
                break;
            }
        }
        return fragment;
    }


}
