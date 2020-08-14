package com.log.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午3:25:02  
 * @类描述: 
 *
 */

@Controller
public class UserController {

	@GetMapping("/user")
	public String getUser(String name){
		System.out.println("name: "+name);
		return "user";
	}
	
}
