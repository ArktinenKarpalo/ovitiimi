package ohtu.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ohtu.database.entities.data.Course;
import ohtu.database.entities.data.Podcast;
import ohtu.database.entities.recommendations.PodcastRecommendation;
import ohtu.database.repositories.CourseRepository;
import ohtu.database.repositories.PodcastRepository;
import ohtu.database.repositories.RecommendationRepository;

@Controller
public class PodcastController {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @InitBinder
    public void allowEmptyDateBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/podcasts")
    public String list(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);

        List<Podcast> podcasts = podcastRepository.findAll();
        model.addAttribute("podcasts", podcasts);
        
        model.addAttribute("podcast", new Podcast());

        return "podcasts";
    }

    @PostMapping("/podcasts")
    public String create(Model model, @Valid PodcastRecommendation podcast, BindingResult result, RedirectAttributes redirectAttributes,
            @RequestParam(value = "selectedCourseId", required = false, defaultValue = "0") Long selectedCourseId) {
        
        if (result.hasErrors()) {
            List<Course> courses = courseRepository.findAll();
            List<Podcast> podcasts = podcastRepository.findAll();

            model.addAttribute("courses", courses);
            model.addAttribute("podcasts", podcasts);
            model.addAttribute("podcast", podcast);

            return "podcasts";
        }

        if (courseRepository.existsById(selectedCourseId)) {
            Course course = courseRepository.getOne(selectedCourseId);
            podcast.addCourse(course);
        }
        
        recommendationRepository.save(podcast);
        
        redirectAttributes.addFlashAttribute("message", "Lisäys onnistui!");
                
        return "redirect:/podcasts";
    }
}
