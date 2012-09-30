package ma.glasnost.orika.benchmark.nested_benchmark.manual;

import ma.glasnost.orika.benchmark.nested_benchmark.Mapper;


/**
 *
 */
public class ManualBasicMapper implements Mapper {

    public Object fromEntity(final Object entity) {

        final Person person = (Person) entity;
        final PersonDTO dto = new PersonDTO();
        if (person.getName() != null) {
            dto.setFirstName(person.getName().getFirstname());
            dto.setLastName(person.getName().getSurname());
        }
        if (person.getCurrentAddress() != null) {
            final Address address = person.getCurrentAddress();
            final AddressDTO addressDTO = new AddressDTO();
            addressDTO.setAddressLine1(address.getAddressLine1());
            addressDTO.setAddressLine2(address.getAddressLine2());
            addressDTO.setCity(address.getCity());
            addressDTO.setPostCode(address.getPostCode());
            if (address.getCountry() != null) {
                addressDTO.setCountryName(address.getCountry().getName());
            }
            dto.setCurrentAddress(addressDTO);
        }
        return dto;
    }

    public Object fromDto(final Object dto) {

        final Person person = new Person();
        final PersonDTO personDTO = (PersonDTO) dto;

        person.setName(new Name(personDTO.getFirstName(), personDTO.getLastName()));

        if (personDTO.getCurrentAddress() != null) {
            final Address address = new Address();
            final AddressDTO addressDTO = personDTO.getCurrentAddress();

            address.setAddressLine1(addressDTO.getAddressLine1());
            address.setAddressLine2(addressDTO.getAddressLine2());
            address.setCity(addressDTO.getCity());
            address.setPostCode(addressDTO.getPostCode());
            address.setCountry(new Country(addressDTO.getCountryName()));

            person.setCurrentAddress(address);
        }
        return person;
    }
}
