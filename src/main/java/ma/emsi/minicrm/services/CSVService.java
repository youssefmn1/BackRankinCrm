package ma.emsi.minicrm.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    private LeadRepository leadRepository;

    public void importCSV(MultipartFile file) throws IOException, CsvException {
        List<Lead> leads = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                Lead lead = new Lead();
                lead.setNom(record[0]);
                lead.setPrenom(record[1]);
                lead.setEmail(record[2]);
                lead.setAdresse(record[3]);
                lead.setTelephone(record[4]);
                lead.setSource(record[5]);
                lead.setNote(record[6]);
                lead.setStatut(record[7]);
                leads.add(lead);
            }
        }
        leadRepository.saveAll(leads);
    }
}
