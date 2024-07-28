package ma.emsi.minicrm.web;

import com.opencsv.exceptions.CsvException;
import ma.emsi.minicrm.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    private CSVService csvService;

    @PostMapping("/import")
    public RedirectView importCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please upload a CSV file.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return new RedirectView("/leads");
        }

        try {
            csvService.importCSV(file);
            redirectAttributes.addFlashAttribute("message", "CSV file imported successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");

        } catch (IOException | CsvException e) {
            redirectAttributes.addFlashAttribute("message", "An error occurred while importing the CSV file.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        }
        return new RedirectView("/leads");
    }
}
