package study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.dao.MemberMapper;
import study.model.entity.Member;

@RestController
public class HelloController {

    @Autowired
    MemberMapper memberMapper;

    @GetMapping("/")
    public String helloWorld() {
        return "hello, My name is xavy";
    }

    @GetMapping("/members/{no}")
    public Member helloWorld(@PathVariable("no") long no) {
        return memberMapper.selectMember2(no);
    }
}
