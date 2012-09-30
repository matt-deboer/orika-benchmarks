/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package ma.glasnost.orika.benchmark.geda_benchmark.orika;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * This is a configuration of Orika which uses a bound mapper,
 * and  containsCycles==true
 */
public class BoundOrikaMapper implements Mapper {

    private BoundMapperFacade<Person, PersonDTO> mapper;
    
    public BoundOrikaMapper() {
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
        this.mapper = factory.getMapperFacade(Person.class, PersonDTO.class, true);
    }

    public Object fromEntity(final Object entity) {
        PersonDTO dto = new PersonDTO();
        mapper.map((Person) entity, dto);
        return dto;
    }

    public Object fromDto(final Object dto) {
        Person entity = new Person();
        mapper.mapReverse((PersonDTO) dto, entity);
        return entity;
    }
}
