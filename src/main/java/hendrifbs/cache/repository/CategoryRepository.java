package hendrifbs.cache.repository;

import hendrifbs.cache.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findAllByParentIsNull();

    List<Category> findAllByParentId(String parentId);

    @Query("from Category c join fetch c.children d where c.parent is null ")
    List<Category> findAllForAPI();

}