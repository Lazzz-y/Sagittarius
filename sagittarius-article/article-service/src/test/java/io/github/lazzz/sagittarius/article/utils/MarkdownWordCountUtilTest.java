package io.github.lazzz.sagittarius.article.utils;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;


/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/10/23 21:02
**/
@SpringBootTest
public class MarkdownWordCountUtilTest {


    /**
     * 测试Markdown字数统计工具类
     */
     @Test
    public void testCountWordsInResourceFile() {
         // 从资源文件中读取Markdown文本
        Resource resource = new ClassPathResource("word_count.md");
        try {
            String markdownText = resource.getContentAsString(StandardCharsets.UTF_8);
            int wordCount = MarkdownWordCountUtil.countWordsInMarkdown(markdownText);
            System.out.println(markdownText);
            System.out.println("Markdown文本字数统计结果: " + wordCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

