package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import br.com.devdojo.examgenerator.persistence.model.AbstractEntity;

@NoRepositoryBean
public interface CustomJpaRepository<T extends AbstractEntity, ID extends Long> extends JpaRepository<T, ID> {
	@Override
	@Query("select e from #{#entityName} e where e.id = ?1 and e.professor = ?#{principal.professor} and e.enabled = true")
	T getOne(Long id);
	
	@Override
	@Query("select e from #{#entityName} e where e.id = ?1 and e.professor = ?#{principal.professor} and e.enabled = true")
	T getById(Long id);

	@Override
	@Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
	Page<T> findAll(Pageable pageable);
	
	@Override
	@Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
	List<T> findAll();

	@Override
	@Query("select e from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
	List<T> findAll(Sort sort);

	@Override
	@Query("select e from #{#entityName} e where e.id = ?1 and e.professor = ?#{principal.professor} and e.enabled = true")
	Optional<T> findById(Long id);

	@Override
	default boolean existsById(Long id) {
		return findById(id) != null;
	}

	@Override
	@Query("select count(e) from #{#entityName} e where e.professor = ?#{principal.professor} and e.enabled = true")
	long count();

	@Override
	@Transactional
	@Modifying
	@Query("update #{#entityName} e set e.enabled=false where e.id = ?1 and e.professor = ?#{principal.professor}")
	void deleteById(Long id);

	@Override
	@Transactional
	@Modifying
	default void delete(T entity) {
		deleteById(entity.getId());
	}

	@Override
	@Transactional
	@Modifying
	default void deleteAll(Iterable<? extends T> entities) {
		entities.forEach(entity -> deleteById(entity.getId()));
	}

	@Override
	@Transactional
	@Modifying
	@Query("update #{#entityName} e set e.enabled=false where e.professor = ?#{principal.professor}")
	void deleteAll();
	

	
}
