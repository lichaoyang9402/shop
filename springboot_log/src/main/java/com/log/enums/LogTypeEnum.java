package com.log.enums;

/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午8:10:10  
 * @类描述: 
 *
 */
public enum LogTypeEnum {

	WEB("-1"), DUBBO("1"), MQ("2");

    private final String value;

    LogTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
