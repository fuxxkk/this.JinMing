package App.runner;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println(">>>>>>>>>>>>>>>服务初始化执行，执行加载数据等操作<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>>>>>>>服务初始化执行，加载redis<<<<<<<<<<<<<");

        System.out.println(">>>>>>>>>>>>>>>服务器加载数据完成<<<<<<<<<<<<<");
    }
}
