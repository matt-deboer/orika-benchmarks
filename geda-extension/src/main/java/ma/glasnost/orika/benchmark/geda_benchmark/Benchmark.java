/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package ma.glasnost.orika.benchmark.geda_benchmark;

import ma.glasnost.orika.benchmark.geda_benchmark.dozer.DozerBasicMapper;
import ma.glasnost.orika.benchmark.geda_benchmark.modelmapper.ModelMapperMapper;
import ma.glasnost.orika.benchmark.geda_benchmark.orika.BoundOrikaMapper;
import ma.glasnost.orika.benchmark.geda_benchmark.orika.OrikaMapper;
import ma.glasnost.orika.benchmark.geda_benchmark.orika.OrikaNonCyclicMapper;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDABasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.manual.ManualBasicMapper;

/**
 * This benchmark is based directly on the benchmark created by
 * the GeDA project to compare mapping libraries with the following
 * modifications:
 * 
 *   * !!!Included xor of the result object's hash code to a result
 *     variable; without this, results can be skewed by the compiler
 *     simply removing operations that return no result from a loop...
 *     Also, a return type is needed (see the Caliper docs), otherwise
 *     compiler can just factor out work done which doesn't produce a 
 *     result.
 *     In this case, it seems like this was only skewing the JAVA_MANUAL
 *     numbers (making them appear 1000x faster than reality)
 *     
 *   * ModelMapper, Dozer, and Orika mappers were all changed so that
 *     they were mapping a pre-instantiated instance of the destination
 *     object; this is how the GeDA mapper was doing it, and it seemed
 *     like a more accurate comparison if the same mapping pattern was
 *     used.
 *     
 *   * This benchmark includes 2 additional mappers for Orika to demonstrate
 *     some of the better performing modes of the benchmark
 *     
 *   * The 'length' parameter was changed to (1,1000,25000) instead of having
 *     5 different measurement points, simply because I didn't want to wait
 *     so long for all of them to finish : )   
 */
public class Benchmark extends SimpleBenchmark {


    public enum Library {

        JAVA_MANUAL(new ManualBasicMapper()),
        ORIKA_NOCYCLES(new OrikaNonCyclicMapper()),
        ORIKA_BOUND(new BoundOrikaMapper()),
        ORIKA(new OrikaMapper()),
        GEDA(new GeDABasicMapper()),
        MODELMAPPER(new ModelMapperMapper()),
        DOZER(new DozerBasicMapper())
;
        
        private Mapper mapper;

        Library(final Mapper mapper) {
            this.mapper = mapper;
        }
    }

    @Param({ "1","1000", "25000"})
    private int length;
    
    @Param
    private Library lib;

    private Person personLoaded;
    private PersonDTO personDTOLoaded;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {

        final Name name = new Name("Sherlock", "Holmes");
        final Country country = new Country("United Kingdom");
        final Address address = new Address("221B Baker Street", null, "London", country, "NW1 6XE");
        final Person entity = new Person(name, address);

        personLoaded = entity;

        final PersonDTO dto = new PersonDTO();
        dto.setFirstName("Sherlock");
        dto.setLastName("Holmes");
        final AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressLine1("221B Baker Street");
        addressDTO.setCity("London");
        addressDTO.setPostCode("NW1 6XE");
        addressDTO.setCountryName("United Kingdom");
        dto.setCurrentAddress(addressDTO);

        personDTOLoaded = dto;

        mapper = lib.mapper;
    }

    public int timeFromDTOToEntity(int reps) {
        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                dummy ^= mapper.fromDto(personDTOLoaded).hashCode();
            }
        }
        return dummy;
    } 
    
    public int timeFromEntityToDTO(int reps) {
        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                dummy ^= mapper.fromEntity(personLoaded).hashCode();
            }
        }
        return dummy;
    }

    public static void main(String[] args) throws Exception {
         Runner.main(Benchmark.class, args);
    }

}
