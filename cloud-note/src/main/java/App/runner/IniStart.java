package App.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class IniStart implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");


        System.out.println(">>>>>>>>>>>>>>>服务器加载数据完成<<<<<<<<<<<<<");
    }
}
