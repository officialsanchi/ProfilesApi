package com.dinmaly.ProfilesApiApplication.repository;

import com.dinmaly.ProfilesApiApplication.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM profiles WHERE " +
            "(:gender IS NULL OR LOWER(gender::text) = LOWER(:gender)) AND " +
            "(:countryId IS NULL OR LOWER(country_id::text) = LOWER(:countryId)) AND " +
            "(:ageGroup IS NULL OR LOWER(age_group::text) = LOWER(:ageGroup))",
            nativeQuery = true)
    List<Profile> findByFilters(
            @Param("gender") String gender,
            @Param("countryId") String countryId,
            @Param("ageGroup") String ageGroup);
}
