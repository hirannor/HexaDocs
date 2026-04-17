package io.github.hirannor.hexadocs.adapter.web.gui.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ChatPageController {
    ChatPageController() {
    }

    @GetMapping("/")
    public String chatPage(final Model model) {
        model.addAttribute("languages", DocumentLanguageModel.values());

        return "chat";
    }
}