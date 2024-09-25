package co.tz.settlo.api.department;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.expense.ExpenseDTO;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.product.ProductRepository;
import co.tz.settlo.api.shift.Shift;
import co.tz.settlo.api.shift.ShiftRepository;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;
    private final BusinessRepository businessRepository;
    private final StaffRepository staffRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final ShiftRepository shiftRepository;

    public DepartmentService(final DepartmentRepository departmentRepository,
            final LocationRepository locationRepository,
            final BusinessRepository businessRepository, final StaffRepository staffRepository,
            final DiscountRepository discountRepository, final ProductRepository productRepository,
            final ShiftRepository shiftRepository) {
        this.departmentRepository = departmentRepository;
        this.locationRepository = locationRepository;
        this.businessRepository = businessRepository;
        this.staffRepository = staffRepository;
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
        this.shiftRepository = shiftRepository;
    }

    @Transactional(readOnly = true)
    public Page<DepartmentDTO> searchAll(SearchRequest request) {
        SearchSpecification<Department> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Department> departmentsPage = departmentRepository.findAll(specification, pageable);

        return departmentsPage.map(department -> mapToDTO(department, new DepartmentDTO()));
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> findAll() {
        final List<Department> departments = departmentRepository.findAll(Sort.by("id"));
        return departments.stream()
                .map(department -> mapToDTO(department, new DepartmentDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentDTO get(final UUID id) {
        return departmentRepository.findById(id)
                .map(department -> mapToDTO(department, new DepartmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final DepartmentDTO departmentDTO) {
        final Department department = new Department();
        mapToEntity(departmentDTO, department);
        return departmentRepository.save(department).getId();
    }

    @Transactional
    public void update(final UUID id, final DepartmentDTO departmentDTO) {
        final Department department = departmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(departmentDTO, department);
        departmentRepository.save(department);
    }

    @Transactional
    public void delete(final UUID id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentDTO mapToDTO(final Department department, final DepartmentDTO departmentDTO) {
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setColor(department.getColor());
        departmentDTO.setImage(department.getImage());
        departmentDTO.setStatus(department.getStatus());
        departmentDTO.setIsArchived(department.getIsArchived());
        departmentDTO.setCanDelete(department.getCanDelete());
        departmentDTO.setNotificationToken(department.getNotificationToken());
        departmentDTO.setLocation(department.getLocation() == null ? null : department.getLocation().getId());
        departmentDTO.setBusiness(department.getBusiness() == null ? null : department.getBusiness().getId());
        return departmentDTO;
    }

    private Department mapToEntity(final DepartmentDTO departmentDTO, final Department department) {
        department.setName(departmentDTO.getName());
        department.setColor(departmentDTO.getColor());
        department.setImage(departmentDTO.getImage());
        department.setStatus(departmentDTO.getStatus());
        department.setIsArchived(departmentDTO.getIsArchived());
        department.setCanDelete(departmentDTO.getCanDelete());
        department.setNotificationToken(departmentDTO.getNotificationToken());
        final Location location = departmentDTO.getLocation() == null ? null : locationRepository.findById(departmentDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        department.setLocation(location);
        final Business business = departmentDTO.getBusiness() == null ? null : businessRepository.findById(departmentDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        department.setBusiness(business);
        return department;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Department department = departmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Staff departmentStaff = staffRepository.findFirstByDepartment(department);
        if (departmentStaff != null) {
            referencedWarning.setKey("department.staff.department.referenced");
            referencedWarning.addParam(departmentStaff.getId());
            return referencedWarning;
        }
        final Discount departmentDiscount = discountRepository.findFirstByDepartment(department);
        if (departmentDiscount != null) {
            referencedWarning.setKey("department.discount.department.referenced");
            referencedWarning.addParam(departmentDiscount.getId());
            return referencedWarning;
        }
        final Product departmentProduct = productRepository.findFirstByDepartment(department);
        if (departmentProduct != null) {
            referencedWarning.setKey("department.product.department.referenced");
            referencedWarning.addParam(departmentProduct.getId());
            return referencedWarning;
        }
        final Shift departmentShift = shiftRepository.findFirstByDepartment(department);
        if (departmentShift != null) {
            referencedWarning.setKey("department.shift.department.referenced");
            referencedWarning.addParam(departmentShift.getId());
            return referencedWarning;
        }
        return null;
    }

}
