package top.flobby.live.id.generate;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.id.generate.service.IdGenerateService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-11-20 11:04
 **/

@LiveApplication
@EnableDubbo
public class IdGenerateProvider implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateProvider.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public void run(String... args) throws Exception {
        // HashSet<Long> hashSet = new HashSet<>();
        // for (int i = 0; i < 500; i++) {
        //     Long seqId = idGenerateService.getUnSeqId(2);
        //     Thread.sleep(100);
        //     System.out.print("idid" + seqId + "\t");
        //     hashSet.add(seqId);
        // }
        // System.out.println("长度：" + hashSet.size());
    }
}
