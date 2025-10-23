package io.github.lazzz.sagittarius.article.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Markdown字数统计工具类
 * 用于准确统计Markdown文本的实际字数，排除Markdown语法标记
 *
 * @author Lazzz
 * @date 2025/10/23 20:54
 **/
public class MarkdownWordCountUtil {

    /**
     * 统计Markdown文本的实际字数（排除Markdown语法标记）
     * @param markdownText Markdown文本内容
     * @return 实际字数统计结果
     */
    public static int countWordsInMarkdown(String markdownText) {
        if (markdownText == null || markdownText.isEmpty()) {
            return 0;
        }

        String plainText = removeMarkdownSyntax(markdownText);
        return countWords(plainText);
    }

    /**
     * 移除Markdown语法标记，保留实际文本内容
     * @param markdownText Markdown文本
     * @return 纯文本内容
     */
    private static String removeMarkdownSyntax(String markdownText) {
        // 移除标题标记 #
        String result = removePattern(markdownText, "^#{1,6}\s+", true);
        
        // 移除粗体和斜体标记 * 和 _
        result = removePattern(result, "[*_]{1,3}", false);
        
        // 移除删除线标记 ~~
        result = removePattern(result, "~~", false);
        
        // 移除链接标记 [text](url)
        result = removePattern(result, "\\[(.*?)\\]\\((.*?)\\)", false, 1);
        
        // 移除图片标记 ![alt](url)
        result = removePattern(result, "!\\[(.*?)\\]\\((.*?)\\)", false, 1);
        
        // 移除代码块标记 ```
        result = removePattern(result, "```[\s\\S]*?```", false);
        
        // 移除行内代码标记 `code`
        result = removePattern(result, "`(.*?)`", false, 1);
        
        // 移除引用标记 >
        result = removePattern(result, "^>\s*", true);
        
        // 移除列表标记（数字、-、*、+）
        result = removePattern(result, "^[\\d.]+\s+", true);
        result = removePattern(result, "^[-+*]\s+", true);
        
        // 移除分割线 --- 和 ***
        result = removePattern(result, "^[-*]{3,}", true);
        
        return result;
    }

    /**
     * 根据正则表达式移除文本中的特定模式
     * @param text 原始文本
     * @param regex 正则表达式
     * @param isMultiLine 是否多行模式
     * @return 处理后的文本
     */
    private static String removePattern(String text, String regex, boolean isMultiLine) {
        int flags = isMultiLine ? Pattern.MULTILINE : 0;
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }

    /**
     * 根据正则表达式移除文本中的特定模式，并保留指定组的内容
     * @param text 原始文本
     * @param regex 正则表达式
     * @param isMultiLine 是否多行模式
     * @param groupToKeep 要保留的捕获组索引
     * @return 处理后的文本
     */
    private static String removePattern(String text, String regex, boolean isMultiLine, int groupToKeep) {
        int flags = isMultiLine ? Pattern.MULTILINE : 0;
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(text);
        
        StringBuilder sb = new StringBuilder();
        int lastEnd = 0;
        
        while (matcher.find()) {
            sb.append(text, lastEnd, matcher.start());
            if (groupToKeep > 0 && matcher.groupCount() >= groupToKeep) {
                sb.append(matcher.group(groupToKeep));
            }
            lastEnd = matcher.end();
        }
        
        sb.append(text.substring(lastEnd));
        return sb.toString();
    }

    /**
     * 统计文本中的字数
     * @param text 文本内容
     * @return 字数统计
     */
    private static int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        
        // 去除多余的空白字符
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        
        // 对于中文等亚洲语言，每个字符都算作一个字
        // 对于英文等西方语言，以空格分隔的单词算作一个字
        
        // 统计中文字符
        Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher chineseMatcher = chinesePattern.matcher(trimmed);
        int chineseCount = 0;
        while (chineseMatcher.find()) {
            chineseCount++;
        }
        
        // 去除中文字符，统计英文单词数
        String nonChineseText = trimmed.replaceAll("[\\u4e00-\\u9fa5]", " ");
        String[] words = nonChineseText.trim().split("\\s+");
        int englishWordCount = words.length;
        
        // 对于纯英文的情况，如果是空数组（只有空白字符），则返回0
        if (chineseCount == 0 && englishWordCount == 1 && words[0].isEmpty()) {
            return 0;
        }
        
        return chineseCount + englishWordCount;
    }
}