package io.github.lazzz.sagittarius.article.utils;


import cn.hutool.core.util.StrUtil;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.List;


/**
 * Markdown <-> Html
 * 
 * @author Lazzz 
 * @date 2025/10/31 21:52
**/
public class MarkdownConvertUtils {

    private static final FlexmarkHtmlConverter FLEX_MARK_HTML_CONVERTER = FlexmarkHtmlConverter.builder().build();


    /**
     * 将 HTML 转换为 Markdown 格式.
     *
     * @param html 需要转换的 HTML 字符串
     * @return 转换后的 Markdown 字符串，如果输入为空则返回空字符串
     */
    public static String html2Md(String html) {
        return FLEX_MARK_HTML_CONVERTER.convert(html);
    }

    /**
     * 安全转换 HTML 为 Markdown，处理 null 或空字符串的情况.
     *
     * @param html 需要转换的 HTML 字符串
     * @return 转换后的 Markdown 字符串
     */
    public static String safeHtml2Md(String html) {
        if (StrUtil.isNotBlank(html)) {
            return html2Md(html);
        }
        return StrUtil.EMPTY;
    }



    /**
     * 将 Markdown 转换为 HTML 格式.
     *
     * @param md 需要转换的 Markdown 字符串
     * @return 转换后的 HTML 字符串，如果输入为空则返回空字符串
     */
    public static String md2Html(String md) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, List.of(TablesExtension.create()));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        return renderer.render(parser.parse(md));
    }

        /**
     * 安全转换 Markdown 为 HTML，处理 null 或空字符串的情况.
     *
     * @param md 需要转换的 Markdown 字符串
     * @return 转换后的 HTML 字符串
     */
    public static String safeMd2Html(String md) {
        if (StrUtil.isNotBlank(md)) {
            return md2Html(md);
        }
        return StrUtil.EMPTY;
    }

}

