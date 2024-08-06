package com.course.intercomm;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "User-Service")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/user/service/names", consumes = "application/json")
    List<String> getNamesOfUsers(@RequestParam("idList") List<Long> userIdList);
}
