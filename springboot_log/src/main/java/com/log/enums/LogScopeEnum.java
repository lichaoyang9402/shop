package com.log.enums;

/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午8:10:37  
 * @类描述: 
 *
 */
public enum LogScopeEnum {
	ALL, BEFORE, AFTER;

    public boolean contains(LogScopeEnum scope) {
        if (this == ALL) {
            return true;
        } else {
            return this == scope;
        }
    }

    @Override
    public String toString() {
        String str = "";
        switch (this) {
            case ALL:
                break;
            case BEFORE:
                str = "REQUEST";
                break;
            case AFTER:
                str = "RESPONSE";
                break;
            default:
                break;
        }
        return str;
    }
}
