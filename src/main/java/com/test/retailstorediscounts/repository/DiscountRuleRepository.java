package com.test.retailstorediscounts.repository;

import com.test.retailstorediscounts.entity.DiscountRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DiscountRuleRepository extends MongoRepository<DiscountRule, String> {

    List<DiscountRule> findByNameInAndActive(List<String> roleNames, boolean active);

}
