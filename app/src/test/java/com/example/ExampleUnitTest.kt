package com.example

import com.example.data.country.CountryRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun allCountries_hasExactly100Countries() {
    val countries = CountryRepository.ALL_COUNTRIES
    assertEquals("Should have exactly 100 countries", 100, countries.size)
  }

  @Test
  fun all100Countries_haveCompleteDivisionsAndDistricts() {
    val countries = CountryRepository.ALL_COUNTRIES
    val uniqueCodes = mutableSetOf<String>()

    for (country in countries) {
      assertFalse("Code should not be blank for ${country.nameEn}", country.code.isBlank())
      assertFalse("NameBn should not be blank for ${country.code}", country.nameBn.isBlank())
      assertFalse("NameEn should not be blank for ${country.code}", country.nameEn.isBlank())
      assertFalse("Flag should not be blank for ${country.code}", country.flag.isBlank())
      assertTrue("PhoneCode should start with + for ${country.code}", country.phoneCode.startsWith("+"))
      assertFalse("Currency should not be blank for ${country.code}", country.currency.isBlank())

      assertTrue("Country code ${country.code} should be unique", uniqueCodes.add(country.code))

      val divisions = country.divisions
      assertTrue("Country ${country.nameEn} (${country.code}) must have non-empty divisions/states", divisions.isNotEmpty())

      for ((divisionName, districtList) in divisions) {
        assertFalse("Division name should not be blank in ${country.nameEn}", divisionName.isBlank())
        assertTrue("Division $divisionName in ${country.nameEn} must have non-empty districts/cities", districtList.isNotEmpty())
        for (district in districtList) {
          assertFalse("District in $divisionName (${country.nameEn}) should not be blank", district.isBlank())
        }
      }
    }
  }
}

