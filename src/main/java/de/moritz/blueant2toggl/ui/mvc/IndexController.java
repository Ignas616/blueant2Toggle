package de.moritz.blueant2toggl.ui.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.moritz.blueant2toggl.service.TogglService;
import de.moritz.blueant2toggl.ui.model.UploadForm;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TogglService togglService;

    @GetMapping
    public ModelAndView createForm(@ModelAttribute UploadForm uploadForm) {
        return new ModelAndView("index", "uploadForm", new UploadForm());
    }

    @PostMapping
    public ModelAndView create(@ModelAttribute UploadForm uploadForm, Errors result, RedirectAttributes redirect) throws Exception {
        if (result.hasErrors()) {
            return new ModelAndView("index", "formErrors", result.getAllErrors());
        }

        double uploadedHours = togglService.callToggl(uploadForm.getUserName(), uploadForm.getPassword(), uploadForm.getUploadFile().getBytes()) / 3600.0;

        redirect.addFlashAttribute("globalMessage", String.format("Successfully uploaded %10.1f hours of work.", uploadedHours));
        return new ModelAndView("redirect:/");
    }
}
