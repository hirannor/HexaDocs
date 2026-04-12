package io.github.hirannor.hexadocs.adapter.web.gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ChatPageController {
    ChatPageController() {
    }

    @GetMapping("/")
    public String chatPage() {
        return "chat";
    }
}