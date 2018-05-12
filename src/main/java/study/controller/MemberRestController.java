package study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.model.entity.Member;
import study.service.MemberService;

@RestController
public class MemberRestController {

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "hello, My name is xavy";
    }

    @GetMapping("/members/{no}")
    public Member helloWorld(@PathVariable("no") long no) {
        return memberService.findByNo(no);
    }
}
