package io.github.lazzz.sagittarius.article.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * HTML 文本处理工具类：统计去除标签后的有效字数
 *
 * @author Lazzz
 * @date 2025/11/5 21:08
 **/
public class HtmlWordsCountUtil {

    // 常用HTML实体映射（覆盖主流场景）
    private static final Map<String, String> HTML_ENTITY_MAP = new HashMap<>();
    static {
        HTML_ENTITY_MAP.put("&nbsp;", " ");
        HTML_ENTITY_MAP.put("&amp;", "&");
        HTML_ENTITY_MAP.put("&lt;", "<");
        HTML_ENTITY_MAP.put("&gt;", ">");
        HTML_ENTITY_MAP.put("&quot;", "\"");
        HTML_ENTITY_MAP.put("&apos;", "'");
        HTML_ENTITY_MAP.put("&copy;", "©");
        HTML_ENTITY_MAP.put("&reg;", "®");
        HTML_ENTITY_MAP.put("&trade;", "™");
    }

    /**
     * 统计 HTML 去除标签后的有效字数
     * 规则：
     * 1. 去除所有 HTML 标签（包括自闭合标签、注释、脚本/样式标签等）
     * 2. 解码 HTML 实体字符（如 &nbsp; → 空格、&lt; → < 等）
     * 3. 去除多余空白字符（连续空格、换行、制表符等合并为单个空格，首尾空格去除）
     * 4. 统计非空白字符的总个数（中文、英文、数字、符号等均算一个字）
     *
     * @param htmlContent HTML 原始内容（可为 null 或空字符串）
     * @return 有效字数（null/空HTML返回 0）
     */
    public static int countHtmlTextLength(String htmlContent) {
        // 1. 处理 null 和空字符串
        if (htmlContent == null || htmlContent.trim().isEmpty()) {
            return 0;
        }

        // 2. 去除 HTML 标签（原生正则实现，无需Hutool）
        String plainText = removeHtmlTags(htmlContent);

        // 3. 解码 HTML 实体字符（自定义实现，无需Hutool）
        plainText = decodeHtmlEntities(plainText);

        // 4. 去除多余空白字符（连续空格、换行、制表符等统一替换为单个空格，再去除首尾空格）
        String trimmedText = removeExtraWhitespace(plainText);

        // 5. 统计有效字符长度（非空白字符的总个数）
        return trimmedText.length();
    }

    /**
     * 原生正则去除HTML标签（含注释、脚本、样式、自闭合标签）
     */
    private static String removeHtmlTags(String html) {
        if (html == null) {
            return "";
        }
        // 步骤1：去除HTML注释（<!-- 任意内容 -->）
        String noComment = Pattern.compile("<!--.*?-->", Pattern.DOTALL).matcher(html).replaceAll("");
        // 步骤2：去除脚本和样式标签（<script...></script>、<style...></style>）
        String noScriptStyle = Pattern.compile("<(script|style).*?</\\1>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(noComment).replaceAll("");
        // 步骤3：去除所有剩余标签（<任意字符>，包括自闭合标签如 <img/>）
        return Pattern.compile("<[^>]+>").matcher(noScriptStyle).replaceAll("");
    }

    /**
     * 解码常用HTML实体字符
     */
    private static String decodeHtmlEntities(String text) {
        if (text == null) {
            return "";
        }
        String result = text;
        // 替换常用实体字符
        for (Map.Entry<String, String> entry : HTML_ENTITY_MAP.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        // 处理十进制实体（&#数字;）
        result = Pattern.compile("&#(\\d+);").matcher(result).replaceAll(match -> {
            int code = Integer.parseInt(match.group(1));
            return Character.toString((char) code);
        });
        // 处理十六进制实体（&#x数字;）
        result = Pattern.compile("&#x([0-9a-fA-F]+);").matcher(result).replaceAll(match -> {
            int code = Integer.parseInt(match.group(1), 16);
            return Character.toString((char) code);
        });
        return result;
    }

    /**
     * 去除多余空白字符（连续空格、换行、制表符等合并为单个空格，首尾空格去除）
     */
    private static String removeExtraWhitespace(String text) {
        if (text == null) {
            return "";
        }
        // 正则：匹配所有空白字符（空格、制表符、换行、回车等），替换为单个空格
        String singleSpaceText = Pattern.compile("\\s+").matcher(text).replaceAll(" ");
        // 去除首尾空格
        return singleSpaceText.trim();
    }
}