package mystorm;

import org.apache.storm.shade.org.apache.commons.lang.StringUtils;
import org.apache.storm.shade.org.eclipse.jetty.util.BlockingArrayQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleStorm {

    private BlockingQueue sentencesQ = new BlockingArrayQueue(500);
    private BlockingQueue wordQ = new BlockingArrayQueue(500);
    //计数器
    Map<String, Integer> counter = new HashMap<String, Integer>();
    private Random random = new Random();
    //nextTuple ,发送句子
    public void nextTuple() {
        String[] sentences = new String[]{"the cow  over the moon",
                "an apple a day keeps the doctor away",
                "four score and seven years ago",
                "snow white and the seven ", "i am nature"};
        String sentence = sentences[random.nextInt(sentences.length)];
        try {
            sentencesQ.put(sentence);
            System.out.println("发送句子:"+sentence);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //切割句子
    public void split(String sentence) {
        String[] words = sentence.split(" ");
        for (String word :
                words) {
            if (StringUtils.isNotEmpty(word)) {
                try {
                    word = word.toLowerCase();
                    wordQ.add(word);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("split word:" + word);
        }
    }

    //计数器
    public void count(String word) {
        if (counter.containsKey(word)) {
            Integer num = counter.get(word);
            counter.put(word, num + 1);
        } else {
            counter.put(word, 1);
        }
        System.out.println(counter);
    }

    public BlockingQueue getSentencesQ() {
        return sentencesQ;
    }

    public void setSentencesQ(BlockingQueue sentencesQ) {
        this.sentencesQ = sentencesQ;
    }

    public BlockingQueue getWordQ() {
        return wordQ;
    }

    public void setWordQ(BlockingQueue wordQ) {
        this.wordQ = wordQ;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        SimpleStorm simpleStorm = new SimpleStorm();
        executorService.submit(new MySpout(simpleStorm));
        executorService.submit(new MySplitBolt(simpleStorm));
        executorService.submit(new MyCountBolt(simpleStorm));
    }
}

class MySpout implements Runnable {

    private SimpleStorm simpleStorm;
    public MySpout(SimpleStorm simpleStorm) {
        this.simpleStorm=simpleStorm;
    }
    public void run() {
        while (true) {
            System.out.println("MySpout....."+Thread.currentThread().getId());
            simpleStorm.nextTuple();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MySplitBolt implements Runnable{

    private SimpleStorm simpleStorm;
    public MySplitBolt(SimpleStorm simpleStorm) {
        this.simpleStorm=simpleStorm;

    }

    public void run() {
        while (true) {
            try {
                System.out.println("MySplitBolt....."+Thread.currentThread().getId());
                BlockingQueue sentencesQ = simpleStorm.getSentencesQ();
                String sentence = (String) sentencesQ.take();
                simpleStorm.split(sentence);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class MyCountBolt implements Runnable {

    private SimpleStorm simpleStorm;
    public MyCountBolt(SimpleStorm simpleStorm) {
        this.simpleStorm=simpleStorm;
    }
    public void run() {
        while (true) {
            try {
                System.out.println("MyCountBolt....."+Thread.currentThread().getId());
                String word = (String) simpleStorm.getWordQ().take();
                simpleStorm.count(word);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}