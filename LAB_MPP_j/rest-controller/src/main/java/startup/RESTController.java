package startup;

import domain.Meci;
import domain.Vanzare;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.MeciJdbcRepository;
import repository.VanzareJdbcRepository;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rest")
public class RESTController {

    ApplicationContext context = new ClassPathXmlApplicationContext("MainApp.xml");
    MeciJdbcRepository meciRepo = context.getBean(MeciJdbcRepository.class);
    VanzareJdbcRepository repoVanzari = context.getBean(VanzareJdbcRepository.class);

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<RESTDTOMeci> response = new ArrayList<>();
        meciRepo.findAll().forEach(meci -> response.add(new RESTDTOMeci(meci, repoVanzari.getTotalBileteCumparateForMeci(meci))));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") int id){
        return new ResponseEntity<>(meciRepo.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Vanzare sale){
        try{
            sale.setId(repoVanzari.calculateLastFreeId() + 1);
            repoVanzari.save(sale);
//            meciRepo.save(meci);
            return  new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidationException x){
            return new ResponseEntity<>("Salvare esuata!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Meci meci){
        meciRepo.update(id, meci);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        meciRepo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
