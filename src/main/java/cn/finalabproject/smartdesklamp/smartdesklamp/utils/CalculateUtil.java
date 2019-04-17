package cn.finalabproject.smartdesklamp.smartdesklamp.utils;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于计算各种数据的工具类
 */
public class CalculateUtil {

    public static Double calculateFoucsScoreByPostures(SittingPostureInfo[] infos){
        double[] scores=new double[7];//每一种坐姿的平均分
        int[] books=getPosturesBook(infos);
        for (int i = 0; i < infos.length; i++) {
            scores[infos[i].getStatus()]+=infos[i].getDegree();
        }
        scores[0]=100;
        for (int i = 0; i < 7; i++) {
            if (books[i]!=0){
                scores[i]=scores[i]/books[i];
            }
        }
        double avg=calculateScoreByPostures(infos);//计算平均分数
        double variance=0;
        for (int i = 0; i < 7; i++) {
            variance+=(scores[i]-avg)*(scores[i]-avg);
        }
        variance/=7;//计算方差
        double cv=Math.sqrt(variance)/avg*100;//计算变异系数

        if (cv>15){
            return (avg/100)*60;
        }else {
            return 3/8*cv+100;
        }
    }

    public static Double calculateScoreByPostures(SittingPostureInfo[] infos){
        int[] book=new int[7];
        double[] degree=new double[7];


        double score=0;
        double accuracy=0;
        int false_p=0,true_p=0;

        SittingPostureInfo temp=null;
        for (int i=0;i<infos.length;i++){
            temp=infos[i];
            book[temp.getStatus()]++;//统计次数
            System.out.println();
            degree[temp.getStatus()]+=temp.getDegree();
        }

        //计算每种的平均分
        for (int i = 0; i < 7; i++) {
            degree[i]=degree[i]/book[i];
        }
        degree[0]=100;

        Map<Integer,Integer> map=new HashMap<>();
        for (int i=0;i<7;i++){
            if (i==0){
                System.out.println();//暂时避免代码重复
                true_p=book[i];//正确
            }else {
                false_p+=book[i];//错误
            }
            map.put(i,book[i]);
        }
        int count=infos.length;
        if (count!=0){
            accuracy=((double)true_p)/(false_p+true_p);
        }
        //计算分数,最简陋的一种方式,毫无科学依据
        if (count!=0){
            for (int i=0;i<7;i++){
                if (i==0){
                    System.out.println();
                    score+=accuracy*100;
                }else {
                    double k=0;
                    if (i==1||i==2){
                        k=0.5;
                        System.out.println();
                    }else if (i==3||i==4){
                        k=0.4;
                    }else if (i==5){
                        k=0.3;
                    }else {
                        k=0.0;
                    }
                    double tscore=(100-degree[i])*k*book[i]/count;
                    score+=tscore;
                }
            }
        }else{
            score=0;
        }
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

    //获取等级
    public static int getGrade(double score){
        return (int)(score/20)+1;
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
