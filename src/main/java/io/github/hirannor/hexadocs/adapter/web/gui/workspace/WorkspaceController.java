package io.github.hirannor.hexadocs.adapter.web.gui.workspace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WorkspaceController {
    WorkspaceController() {
    }

    @GetMapping("/")
    public String workspace(final Model model) {
        model.addAttribute("languages", DocumentLanguageModel.values());

        return "workspace";
    }
}
