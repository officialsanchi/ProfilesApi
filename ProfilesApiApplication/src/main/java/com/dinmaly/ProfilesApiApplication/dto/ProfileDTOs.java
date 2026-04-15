package com.dinmaly.ProfilesApiApplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileDTOs {
    public static class CreateProfileRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class ProfileResponse {
        private String id;
        private String name;
        private String gender;

        @JsonProperty("gender_probability")
        private Double genderProbability;

        @JsonProperty("sample_size")
        private Integer sampleSize;

        private Integer age;

        @JsonProperty("age_group")
        private String ageGroup;

        @JsonProperty("country_id")
        private String countryId;

        @JsonProperty("country_probability")
        private Double countryProbability;

        @JsonProperty("created_at")
        private String createdAt;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public Double getGenderProbability() { return genderProbability; }
        public void setGenderProbability(Double genderProbability) { this.genderProbability = genderProbability; }

        public Integer getSampleSize() { return sampleSize; }
        public void setSampleSize(Integer sampleSize) { this.sampleSize = sampleSize; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getAgeGroup() { return ageGroup; }
        public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

        public String getCountryId() { return countryId; }
        public void setCountryId(String countryId) { this.countryId = countryId; }

        public Double getCountryProbability() { return countryProbability; }
        public void setCountryProbability(Double countryProbability) { this.countryProbability = countryProbability; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class ProfileSummary {
        private String id;
        private String name;
        private String gender;
        private Integer age;

        @JsonProperty("age_group")
        private String ageGroup;

        @JsonProperty("country_id")
        private String countryId;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getAgeGroup() { return ageGroup; }
        public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

        public String getCountryId() { return countryId; }
        public void setCountryId(String countryId) { this.countryId = countryId; }
    }

    public static class GenderizeResponse {
        private String name;
        private String gender;
        private Double probability;
        private Integer count;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public Double getProbability() { return probability; }
        public void setProbability(Double probability) { this.probability = probability; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    public static class AgifyResponse {
        private String name;
        private Integer age;
        private Integer count;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
    }

    public static class NationalizeResponse {
        private String name;
        private java.util.List<CountryEntry> country;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public java.util.List<CountryEntry> getCountry() { return country; }
        public void setCountry(java.util.List<CountryEntry> country) { this.country = country; }

        public static class CountryEntry {
            @JsonProperty("country_id")
            private String countryId;
            private Double probability;

            public String getCountryId() { return countryId; }
            public void setCountryId(String countryId) { this.countryId = countryId; }

            public Double getProbability() { return probability; }
            public void setProbability(Double probability) { this.probability = probability; }
        }
    }
}
