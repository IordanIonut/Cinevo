package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.TV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TVRepository extends JpaRepository<TV, String> {
}
