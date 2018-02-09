package mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;

/**
 * 获取推荐模型的评分
 */
public class MyEvaluator {
    public static void main(String[] args) throws Exception{
        File file = new File("D:\\大数据第三期\\课件\\day24\\ml-10M100K\\ratings.dat");
        GroupLensDataModel dataModel = new GroupLensDataModel(file);
        AverageAbsoluteDifferenceRecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        //评估对象
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder(){

            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                //相似器
                PearsonCorrelationSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
                NearestNUserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, similarity,dataModel);
                //获取基于用户协同算法对象
                return new GenericUserBasedRecommender(dataModel,userNeighborhood,similarity);
            }
        };
        double score = evaluator.evaluate(recommenderBuilder, null, dataModel, 0.7, 1.0);
        System.out.println("推荐模型的评分:"+score);
    }
}
