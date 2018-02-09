package mahout;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.util.List;

public class MahoutDemo {
    public static void main(String[] args) throws Exception {
        //文件对象
        File file = new File("D:\\大数据第三期\\课件\\day24\\ml-10M100K\\ratings.dat");
        //数据模型
        GroupLensDataModel datamodel = new GroupLensDataModel(file);
        //相似器--皮尔逊算法
        PearsonCorrelationSimilarity similarity = new PearsonCorrelationSimilarity(datamodel);
        //基于物品的协同过滤算法
        GenericItemBasedRecommender itemSimilarity = new GenericItemBasedRecommender(datamodel, similarity);
        //id为5的用户推荐10个与2398相似的商品
        List<RecommendedItem> recommendedItems = itemSimilarity.recommendedBecause(5, 2398, 10);
        //打印推荐的结果
        System.out.println("使用基于物品的协同过滤算法");
        System.out.println("根据用户5当前浏览的商品2398，推荐10个相似的商品");
        for (RecommendedItem r :
                recommendedItems) {
            System.out.println(r);
        }
    }
}
