package kilig.ink.yxy.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class MyFileUtils
{
    /**
     * 复制文件
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copy(File source, File target)
    {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try
        {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0)
            {
                fileOutputStream.write(buffer);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fileInputStream != null)
                {
                    fileInputStream.close();
                }
                if (fileOutputStream != null)
                {
                    fileOutputStream.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void copy(File source, String targetPath)
    {
        File target = new File(targetPath);
    }

    public static void saveFile(File source, String fileName)
    {
//        String imageFileName = "yxy_" + new Random().nextInt(Integer.MAX_VALUE) + ".jpg";
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + "Yxy");
        boolean success = true;
        if (!storageDir.exists())
        {
            success = storageDir.mkdirs();
        }
        if (success)
        {
            File target = new File(storageDir, fileName + ".jpg");
            copy(source, target);
        }
    }

    //保存图片到本地
    public static String saveImage(Bitmap image)
    {
        String savedImagePath = null;
        Random random = new Random();
        String imageFileName = "Yxy_" + random.nextInt(Integer.MAX_VALUE) + ".jpg";
        File storageDir = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                +"/" + "Yxy");
        boolean success = true;
        if (!storageDir.exists())
        {
            success = storageDir.mkdirs();
        }
        if (success)
        {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();   //路径
            try
            {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // Add the image to the system gallery
//            galleryAddPic(savedImagePath);
//            Toast.makeText(context, "图片已保存~", Toast.LENGTH_SHORT).show();
        }
        return savedImagePath;
    }
}
