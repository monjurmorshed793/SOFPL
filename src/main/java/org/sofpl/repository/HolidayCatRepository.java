package org.sofpl.repository;

import org.sofpl.domain.HolidayCat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HolidayCat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayCatRepository extends JpaRepository<HolidayCat, Long> {

}
