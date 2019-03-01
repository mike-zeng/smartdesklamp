package cn.finalabproject.smartdesklamp.smartdesklamp.utils;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于计算各种数据的工具类
 */
public class CalculateUtil {

    public static Double calculateFoucsScoreByPostures(int[] book){
        return 0.0;
    }
    //通过坐姿情况计算得分
    public static Double calculateScoreByPostures(int[] book){
        if (book.length!=7){
            return null;
        }
        int count=getCount(book);
        if (count==0){
            return 0.0;
        }
        Double score=100-(100*(book[1]+book[2])/count*0.7+(100*(book[3]+book[4])/count*0.8)+100*book[5]/count*0.9+100*book[6]/count*1);
        return score;
    }

    //计算坐姿总数
    public static int getCount(int[] book){
        int sum=0;
        for (int i=0;i<book.length;i++){
            sum+=book[i];
        }
        return sum;
    }


    public static int getGrade(double score){
        return (int)(score/20)+1;
    }

    //获取id-次数的map
    public static Map<Integer,Integer> getPosturesMap(SittingPostureInfo[] sittingPostureInfos){
        int[] book=getPosturesBook(sittingPostureInfos);
        Map<Integer,Integer> map=getPosturesMap(book);
        return map;
    }

    public static Map<Integer,Integer> getPosturesMap(int[] book){
        if (book.length!=7){
            return null;
        }
        Map<Integer,Integer> map=new HashMap<>();
        for (int i=0;i<book.length;i++){
            map.put(i,book[i]);
        }
        return map;
    }

    public static int[] getPosturesBook(SittingPostureInfo[] sittingPostureInfos){
        if (sittingPostureInfos==null){
            return null;
        }
        int[] book=new int[7];
        for (int i=0;i<sittingPostureInfos.length;i++){
            book[sittingPostureInfos[i].getStatus()]++;
        }
        return book;
    }

    //获取正确率
    public static Double getAccuracy(int[] book){
        if (book.length!=7){
            return null;
        }
        int sum=0;
        for (int i=0;i<book.length;i++){
            sum+=book[i];
        }
        if (sum==0){
            return 0.0;
        }
        return (double) book[0]/sum;
    }
    //获取平均值
    public static Double getAverage(double[] book){
        double sum=0;
        for (int i=0;i<book.length;i++){
            sum+=book[i];
        }
        return sum/book.length;
    }
    //获取方差
    public static Double getVariance(double[] book){
        double average=getAverage(book);
        double num=0;
        for (int i=0;i<book.length;i++){
            num+=(book[i]-average)*(book[i]-average);
        }
        if (book.length==0) {
            return 0.0;
        }
        return num/book.length;
    }

}
