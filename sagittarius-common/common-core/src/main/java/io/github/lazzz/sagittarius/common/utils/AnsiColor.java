package io.github.lazzz.sagittarius.common.utils;

/**
 * 终端ANSI颜色工具类
 * 包含基础色、高亮色、256色常用色系
 *
 * @author Lazzz
 * @date 2025/10/18 16:39
 **/
public class AnsiColor {
    // -------------------------- 重置与基础样式 --------------------------
    // 重置所有样式
    public static final String RESET = "\u001B[0m";
    // 加粗
    public static final String BOLD = "\u001B[1m";
    // 下划线
    public static final String UNDERLINE = "\u001B[4m";
    // 斜体（部分终端支持）
    public static final String ITALIC = "\u001B[3m";
    // 反色（前景/背景互换）
    public static final String REVERSE = "\u001B[7m";

    // -------------------------- 标准16色（基础+高亮） --------------------------
    // 基础色（30-37）
    // 黑色
    public static final String BLACK = "\u001B[30m";
    // 红色
    public static final String RED = "\u001B[31m";
    // 绿色
    public static final String GREEN = "\u001B[32m";
    // 黄色
    public static final String YELLOW = "\u001B[33m";
    // 蓝色
    public static final String BLUE = "\u001B[34m";
    // 紫色
    public static final String PURPLE = "\u001B[35m";
    // 青色
    public static final String CYAN = "\u001B[36m";
    // 白色
    public static final String WHITE = "\u001B[37m";

    // 高亮色（90-97）
    // 亮黑（深灰）
    public static final String BRIGHT_BLACK = "\u001B[90m";
    // 亮红
    public static final String BRIGHT_RED = "\u001B[91m";
    // 亮绿
    public static final String BRIGHT_GREEN = "\u001B[92m";
    // 亮黄
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    // 亮蓝
    public static final String BRIGHT_BLUE = "\u001B[94m";
    // 亮紫
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    // 亮青
    public static final String BRIGHT_CYAN = "\u001B[96m";
    // 亮白
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // -------------------------- 256色（38;5;XXX 格式） --------------------------
    // 灰色系（常用中性色，适合辅助文本）
    // 浅灰（接近白色）
    public static final String GRAY_100 = "\u001B[38;5;252m";
    // 中浅灰
    public static final String GRAY_200 = "\u001B[38;5;248m";
    // 中灰
    public static final String GRAY_300 = "\u001B[38;5;240m";
    // 深灰（接近黑色）
    public static final String GRAY_400 = "\u001B[38;5;232m";

    // 红色系（警告、错误）
    // 浅红
    public static final String RED_LIGHT = "\u001B[38;5;203m";
    // 中红
    public static final String RED_MEDIUM = "\u001B[38;5;160m";
    // 深红
    public static final String RED_DARK = "\u001B[38;5;124m";

    // 绿色系（成功、通过）
    // 浅绿
    public static final String GREEN_LIGHT = "\u001B[38;5;118m";
    // 中绿
    public static final String GREEN_MEDIUM = "\u001B[38;5;46m";
    // 深绿
    public static final String GREEN_DARK = "\u001B[38;5;22m";

    // 蓝色系（链接、信息）
    // 浅蓝
    public static final String BLUE_LIGHT = "\u001B[38;5;117m";
    // 中蓝
    public static final String BLUE_MEDIUM = "\u001B[38;5;69m";
    // 深蓝
    public static final String BLUE_DARK = "\u001B[38;5;25m";

    // 黄色系（警告、突出显示）
    // 浅黄
    public static final String YELLOW_LIGHT = "\u001B[38;5;228m";
    // 中黄
    public static final String YELLOW_MEDIUM = "\u001B[38;5;220m";
    // 深黄
    public static final String YELLOW_DARK = "\u001B[38;5;136m";

    // 紫色系（特殊标识、标题）
    // 浅紫
    public static final String PURPLE_LIGHT = "\u001B[38;5;183m";
    // 中紫
    public static final String PURPLE_MEDIUM = "\u001B[38;5;129m";
    // 深紫
    public static final String PURPLE_DARK = "\u001B[38;5;91m";

    // -------------------------- 常用组合样式（减少重复拼接） --------------------------
    // 加粗中黄（突出服务名）
    public static final String BOLD_YELLOW = BOLD + YELLOW_DARK;
    // 加粗中绿（成功状态）
    public static final String BOLD_GREEN = BOLD + GREEN;
    // 中灰（辅助信息，如时间）
    public static final String DIM_DETAIL = GRAY_300;
    // 下划线中蓝（链接）
    public static final String LINK_STYLE = UNDERLINE + BLUE_MEDIUM;

}