/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package ma.glasnost.orika.benchmark.nested_benchmark.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * This is an update to the GeDA benchmark for the OrikaMapper mapper,
 * where the mapping is performed on an already instantiated object
 * (the same as done by GeDA)
 */
public class OrikaMapper implements Mapper {

    private MapperFacade mapper;
    
    public OrikaMapper() {
        final MapperFactory factory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy())
                .build();
        factory.registerClassMap(factory.classMap(Address.class, AddressDTO.class).
            field("addressLine1", "addressLine1").
            field("addressLine2", "addressLine2").
            field("postCode", "postCode").
            field("city", "city").
            field("country.name", "countryName").
            toClassMap()
        );
        factory.registerClassMap(factory.classMap(Person.class, PersonDTO.class).
            field("name.firstname", "firstName").
            field("name.surname", "lastName").
            field("currentAddress", "currentAddress").
            toClassMap()
        );
        this.mapper = factory.getMapperFacade();
    }

    public Object fromEntity(final Object entity) {
        PersonDTO dto = new PersonDTO();
        mapper.map((Person) entity, dto);
        return dto;
    }

    public Object fromDto(final Object dto) {
        Person entity = new Person();
        mapper.map((PersonDTO) dto, entity);
        return entity;
    }
}
