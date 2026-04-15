package com.dinmaly.ProfilesApiApplication.service;

import com.dinmaly.ProfilesApiApplication.config.UuidV7;
import com.dinmaly.ProfilesApiApplication.dto.ProfileDTOs;
import com.dinmaly.ProfilesApiApplication.exception.ExternalApiException;
import com.dinmaly.ProfilesApiApplication.exception.ProfileNotFoundException;
import com.dinmaly.ProfilesApiApplication.model.Profile;
import com.dinmaly.ProfilesApiApplication.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class ProfileService {
    private static final String GENDERIZE_URL = "https://api.genderize.io?name=";
    private static final String AGIFY_URL = "https://api.agify.io?name=";
    private static final String NATIONALIZE_URL = "https://api.nationalize.io?name=";

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RestTemplate restTemplate;

    public record CreateResult(Profile profile, boolean alreadyExisted) {}

    public CreateResult createProfile(String name) {

        var existing = profileRepository.findByNameIgnoreCase(name);
        if (existing.isPresent()) {
            return new CreateResult(existing.get(), true);
        }

        ProfileDTOs.GenderizeResponse genderize = callGenderize(name);
        ProfileDTOs.AgifyResponse agify = callAgify(name);
        ProfileDTOs.NationalizeResponse nationalize = callNationalize(name);

        if (genderize.getGender() == null || genderize.getCount() == null || genderize.getCount() == 0) {
            throw new ExternalApiException("Genderize");
        }
        if (agify.getAge() == null) {
            throw new ExternalApiException("Agify");
        }
        if (nationalize.getCountry() == null || nationalize.getCountry().isEmpty()) {
            throw new ExternalApiException("Nationalize");
        }

        ProfileDTOs.NationalizeResponse.CountryEntry topCountry = nationalize.getCountry().stream()
                .max(Comparator.comparingDouble(ProfileDTOs.NationalizeResponse.CountryEntry::getProbability))
                .orElseThrow(() -> new ExternalApiException("Nationalize"));

        Profile profile = new Profile();
        profile.setId(UuidV7.generate());
        profile.setName(name.toLowerCase());
        profile.setGender(genderize.getGender());
        profile.setGenderProbability(genderize.getProbability());
        profile.setSampleSize(genderize.getCount());
        profile.setAge(agify.getAge());
        profile.setAgeGroup(classifyAge(agify.getAge()));
        profile.setCountryId(topCountry.getCountryId());
        profile.setCountryProbability(topCountry.getProbability());
        profile.setCreatedAt(Instant.now());

        profileRepository.save(profile);
        return new CreateResult(profile, false);
    }

    public Profile getById(String id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    public List<Profile> getAll(String gender, String countryId, String ageGroup) {
        return profileRepository.findByFilters(
                gender != null ? gender.toLowerCase() : null,
                countryId != null ? countryId.toLowerCase() : null,
                ageGroup != null ? ageGroup.toLowerCase() : null
        );
    }

    public void deleteById(String id) {
        if (!profileRepository.existsById(id)) {
            throw new ProfileNotFoundException("Profile not found");
        }
        profileRepository.deleteById(id);
    }



    public ProfileDTOs.ProfileResponse toFullResponse(Profile p) {
        ProfileDTOs.ProfileResponse r = new ProfileDTOs.ProfileResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setGender(p.getGender());
        r.setGenderProbability(p.getGenderProbability());
        r.setSampleSize(p.getSampleSize());
        r.setAge(p.getAge());
        r.setAgeGroup(p.getAgeGroup());
        r.setCountryId(p.getCountryId());
        r.setCountryProbability(p.getCountryProbability());
        r.setCreatedAt(DateTimeFormatter.ISO_INSTANT.format(p.getCreatedAt()));
        return r;
    }

    public ProfileDTOs.ProfileSummary toSummary(Profile p) {
        ProfileDTOs.ProfileSummary s = new ProfileDTOs.ProfileSummary();
        s.setId(p.getId());
        s.setName(p.getName());
        s.setGender(p.getGender());
        s.setAge(p.getAge());
        s.setAgeGroup(p.getAgeGroup());
        s.setCountryId(p.getCountryId());
        return s;
    }



    private String classifyAge(int age) {
        if (age >= 0 && age <= 12) return "child";
        if (age >= 13 && age <= 19) return "teenager";
        if (age >= 20 && age <= 59) return "adult";
        if (age >= 60) return "senior";
        return "adult";
    }



    private ProfileDTOs.GenderizeResponse callGenderize(String name) {
        try {
            ProfileDTOs.GenderizeResponse response = restTemplate.getForObject(GENDERIZE_URL + name, ProfileDTOs.GenderizeResponse.class);
            if (response == null) throw new ExternalApiException("Genderize");
            return response;
        } catch (RestClientException e) {
            throw new ExternalApiException("Genderize");
        }
    }

    private ProfileDTOs.AgifyResponse callAgify(String name) {
        try {
            ProfileDTOs.AgifyResponse response = restTemplate.getForObject(AGIFY_URL + name, ProfileDTOs.AgifyResponse.class);
            if (response == null) throw new ExternalApiException("Agify");
            return response;
        } catch (RestClientException e) {
            throw new ExternalApiException("Agify");
        }
    }

    private ProfileDTOs.NationalizeResponse callNationalize(String name) {
        try {
            ProfileDTOs.NationalizeResponse response = restTemplate.getForObject(NATIONALIZE_URL + name, ProfileDTOs.NationalizeResponse.class);
            if (response == null) throw new ExternalApiException("Nationalize");
            return response;
        } catch (RestClientException e) {
            throw new ExternalApiException("Nationalize");
        }
    }


}
