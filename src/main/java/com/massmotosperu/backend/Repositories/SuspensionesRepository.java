package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.SuspensionesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspensionesRepository extends JpaRepository<SuspensionesModel, Integer> {
}
