package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.ContactoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<ContactoModel, Long> {
}
