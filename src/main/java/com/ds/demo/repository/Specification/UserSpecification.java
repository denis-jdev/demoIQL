package com.ds.demo.repository.Specification;


import com.ds.demo.entity.Email;
import com.ds.demo.entity.Phone;
import com.ds.demo.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> nameLike(String name) {
        return (user, cq, cb) -> cb.like(user.get("name"), name + "%");
    }

    public static Specification<User> emailEqual(String email) {
        return (user, cq, cb) -> {
            Join<Email, User> emails = user.join("emails");
            return cb.equal(emails.get("email"), email);
        };
    }

    public static Specification<User> phoneEqual(String phone) {
        return (user, cq, cb) -> {
            Join<Phone, User> phones = user.join("phones");
            return cb.equal(phones.get("phone"), phone);
        };
    }

    public static Specification<User> greaterThenDateOfBirth(LocalDate date) {
        return (user, cq, cb) -> cb.greaterThan(user.get("dateOfBirth"), date);
    }
}
