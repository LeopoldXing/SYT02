package com.hilda.yygh.hosp.repository;

import com.hilda.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    Hospital getHospitalByHoscode(String hoscode);

    List<Hospital> getHospitalByHosnameLike(String hosname);

}
