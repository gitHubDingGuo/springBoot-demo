package top.javahouse.properties.controller;

import cn.hutool.core.lang.Dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.properties.property.ApplicationProperty;
import top.javahouse.properties.property.DeveloperProperty;

/**
 * <p>
 * 测试Controller
 * </p>
 *
 * @package: com.xkcoding.properties.controller
 * @description: 测试Controller
 * @author: yangkai.shen
 * @date: Created in 2018/9/29 10:49 AM
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@RestController
public class PropertyController {
	private final ApplicationProperty applicationProperty;
	private final DeveloperProperty developerProperty;

	@Autowired
	public PropertyController(ApplicationProperty applicationProperty, DeveloperProperty developerProperty) {
		this.applicationProperty = applicationProperty;
		this.developerProperty = developerProperty;
	}

	@GetMapping("/property")
	public Dict index() {
		return Dict.create().set("applicationProperty", applicationProperty).set("developerProperty", developerProperty);
	}
}
