package io.github.lazzz.sagittarius.article.utils;


import org.junit.jupiter.api.Test;

/**
 * TODO
 *
 * @author Lazzz
 * @date 2025/10/31 22:33
 **/
public class MarkDownConvertUtilsTest {

    @Test
    void testMd2Html() {
        String md = """
                一级标题
                ====
                二级标题
                ----
                ### 三级标题
                #### 四级标题
                ##### 五级标题
                ###### 六级标题
                ## 段落
                这是一段普通的段落。
                ## 列表
                ### 无序列表
                - 项目1
                - 项目2
                - 项目3
                ### 有序列表
                1. 项目1
                2. 项目2
                3. 项目3
                ## 链接
                [百度](https://www.baidu.com)
                ## 图片
                ![图片描述](https://www.baidu.com/img/bd_logo1.png)
                ## 表格
                | 表头1 | 表头2 | 表头3 |
                |-------|-------|-------|
                | 单元格1 | 单元格2 | 单元格3 |
                | 单元格4 | 单元格5 | 单元格6 |
                ## 代码块
                ```java
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
                ```
                """;
        String html = MarkdownConvertUtils.md2Html(md);
        System.out.println(html);
    }

    @Test
    void testHtml2Md() {
        String html = """
                <h1>一级标题</h1>
                <h2>二级标题</h2>
                <h3>三级标题</h3>
                <h4>四级标题</h4>
                <h5>五级标题</h5>
                <h6>六级标题</h6>
                <h2>段落</h2>
                <p>这是一段普通的段落。</p>
                <h2>列表</h2>
                <h3>无序列表</h3>
                <ul>
                <li>项目1</li>
                <li>项目2</li>
                <li>项目3</li>
                </ul>
                <h3>有序列表</h3>
                <ol>
                <li>项目1</li>
                <li>项目2</li>
                <li>项目3</li>
                </ol>
                <h2>链接</h2>
                <p><a href="https://www.baidu.com">百度</a></p>
                <h2>图片</h2>
                <p><img src="https://www.baidu.com/img/bd_logo1.png" alt="图片描述" /></p>
                <h2>表格</h2>
                <table>
                <thead>
                <tr><th>表头1</th><th>表头2</th><th>表头3</th></tr>
                </thead>
                <tbody>
                <tr><td>单元格1</td><td>单元格2</td><td>单元格3</td></tr>
                <tr><td>单元格4</td><td>单元格5</td><td>单元格6</td></tr>
                </tbody>
                </table>
                """;
        String md = MarkdownConvertUtils.html2Md(html);
        System.out.println(md);
    }

}

