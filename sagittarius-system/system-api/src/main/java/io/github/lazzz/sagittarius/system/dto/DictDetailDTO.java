package io.github.lazzz.sagittarius.system.dto;


import lombok.Data;

/**
 * 数据字典详情数据传输对象
 * 
 * @author Lazzz 
 * @date 2025/10/15 21:05
**/
@Data
public class DictDetailDTO {

    /* 字典类型编码 */
    private String typeCode;

    /* 字典名称 */
    private String name;

    /* 字典值 */
    private String value;

}

