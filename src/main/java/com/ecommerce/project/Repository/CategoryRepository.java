package com.ecommerce.project.Repository;

import com.ecommerce.project.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
