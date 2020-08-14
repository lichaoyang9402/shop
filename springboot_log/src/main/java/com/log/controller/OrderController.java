package com.log.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.log.annotation.CostLogger;
import com.log.annotation.Loggable;

/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午4:31:42  
 * @类描述: 
 *
 */

@Controller
public class OrderController {
	
	
	@CostLogger
	@GetMapping("/id")
	public String get(String id,Integer age,String name){
		
		
		return "id";
	}

}
