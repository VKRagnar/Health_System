package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImgJob(){
        //获取垃圾图片名称集合
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        //遍历删除
        for (String s : sdiff) {
            System.out.println("正在删除："+s);
            //删除七牛上的图片
            QiniuUtils.deleteFileFromQiniu(s);
            //删除redis上对应的图片名
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
        }

/*        Jedis resource = jedisPool.getResource();
        //获取保存到数据库的图片名
        Set<String> setmealPicDbResources = resource.smembers("setmealPicDbResources");
        //获取全部上传的图片名
        Set<String> setmealPicResources = resource.smembers("setmealPicResources");
        //得到垃圾文件名集合
        Set<String> set=new HashSet<>();
        for (String setmealPicResource : setmealPicResources) {
            for (String setmealPicDbResource : setmealPicDbResources) {
                if(setmealPicDbResource.equals(setmealPicResource)){
                    set.add(setmealPicResource);
                }
            }
        }
        System.out.println(set);
        //删除垃圾文件并删除redis中对应的文件名*/
    }
}
