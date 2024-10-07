package co.tz.settlo.api.controllers.staff;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.department.DepartmentRepository;
import co.tz.settlo.api.controllers.item_return.ItemReturn;
import co.tz.settlo.api.controllers.item_return.ItemReturnRepository;
import co.tz.settlo.api.controllers.order_item.OrderItem;
import co.tz.settlo.api.controllers.order_item.OrderItemRepository;
import co.tz.settlo.api.controllers.payslip.Payslip;
import co.tz.settlo.api.controllers.payslip.PayslipRepository;
import co.tz.settlo.api.controllers.refund.Refund;
import co.tz.settlo.api.controllers.refund.RefundRepository;
import co.tz.settlo.api.controllers.role.Role;
import co.tz.settlo.api.controllers.role.RoleRepository;
import co.tz.settlo.api.controllers.salary.Salary;
import co.tz.settlo.api.controllers.salary.SalaryRepository;
import co.tz.settlo.api.controllers.shift_log.ShiftLog;
import co.tz.settlo.api.controllers.shift_log.ShiftLogRepository;
import co.tz.settlo.api.controllers.stock_intake.StockIntake;
import co.tz.settlo.api.controllers.stock_intake.StockIntakeRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final BusinessRepository businessRepository;
    private final DepartmentRepository departmentRepository;
    private final SalaryRepository salaryRepository;
    private final OrderItemRepository orderItemRepository;
    private final StockIntakeRepository stockIntakeRepository;
    private final ShiftLogRepository shiftLogRepository;
    private final PayslipRepository payslipRepository;
    private final RefundRepository refundRepository;
    private final ItemReturnRepository itemReturnRepository;

    public StaffService(final StaffRepository staffRepository, final RoleRepository roleRepository,
            final BusinessRepository businessRepository,
            final DepartmentRepository departmentRepository,
            final SalaryRepository salaryRepository, final OrderItemRepository orderItemRepository,
            final StockIntakeRepository stockIntakeRepository,
            final ShiftLogRepository shiftLogRepository, final PayslipRepository payslipRepository,
            final RefundRepository refundRepository,
            final ItemReturnRepository itemReturnRepository) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.businessRepository = businessRepository;
        this.departmentRepository = departmentRepository;
        this.salaryRepository = salaryRepository;
        this.orderItemRepository = orderItemRepository;
        this.stockIntakeRepository = stockIntakeRepository;
        this.shiftLogRepository = shiftLogRepository;
        this.payslipRepository = payslipRepository;
        this.refundRepository = refundRepository;
        this.itemReturnRepository = itemReturnRepository;
    }

    @Transactional(readOnly = true)
    public List<StaffDTO> findAll(final UUID locationId) {
        final List<Staff> staffs = staffRepository.findAllByLocationId(locationId);
        return staffs.stream()
                .map(staff -> mapToDTO(staff, new StaffDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public StaffDTO get(final UUID id) {
        return staffRepository.findById(id)
                .map(staff -> mapToDTO(staff, new StaffDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<StaffDTO> searchAll(SearchRequest request) {
        SearchSpecification<Staff> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Staff> staffPage = staffRepository.findAll(specification, pageable);

        return staffPage.map(staff -> mapToDTO(staff, new StaffDTO()));
    }

    @Transactional
    public UUID create(final StaffDTO staffDTO) {
        final Staff staff = new Staff();
        mapToEntity(staffDTO, staff);
        return staffRepository.save(staff).getId();
    }

    @Transactional
    public void update(final UUID id, final StaffDTO staffDTO) {
        final Staff staff = staffRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(staffDTO, staff);
        staffRepository.save(staff);
    }

    @Transactional
    public void delete(final UUID id) {
        staffRepository.deleteById(id);
    }

    private StaffDTO mapToDTO(final Staff staff, final StaffDTO staffDTO) {
        staffDTO.setId(staff.getId());
        staffDTO.setName(staff.getName());
        staffDTO.setPhone(staff.getPhone());
        staffDTO.setColor(staff.getColor());
        staffDTO.setImage(staff.getImage());
        staffDTO.setPassCode(staff.getPassCode());
        staffDTO.setCanDelete(staff.getCanDelete());
        staffDTO.setIsArchived(staff.getIsArchived());
        staffDTO.setStatus(staff.getStatus());
        staffDTO.setRole(staff.getRole() == null ? null : staff.getRole().getId());
        staffDTO.setBusiness(staff.getBusiness() == null ? null : staff.getBusiness().getId());
        staffDTO.setDepartment(staff.getDepartment() == null ? null : staff.getDepartment().getId());
        staffDTO.setSalary(staff.getSalary() == null ? null : staff.getSalary().getId());
        staffDTO.setLocation(staff.getLocation() == null ? null : staff.getLocation().getId());
        return staffDTO;
    }

    private Staff mapToEntity(final StaffDTO staffDTO, final Staff staff) {
        staff.setName(staffDTO.getName());
        staff.setPhone(staffDTO.getPhone());
        staff.setColor(staffDTO.getColor());
        staff.setImage(staffDTO.getImage());
        staff.setPassCode(staffDTO.getPassCode());
        staff.setCanDelete(staffDTO.getCanDelete());
        staff.setIsArchived(staffDTO.getIsArchived());
        staff.setStatus(staffDTO.getStatus());
        final Role role = staffDTO.getRole() == null ? null : roleRepository.findById(staffDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        staff.setRole(role);
        final Business business = staffDTO.getBusiness() == null ? null : businessRepository.findById(staffDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        staff.setBusiness(business);
        final Department department = staffDTO.getDepartment() == null ? null : departmentRepository.findById(staffDTO.getDepartment())
                .orElseThrow(() -> new NotFoundException("department not found"));
        staff.setDepartment(department);
        final Salary salary = staffDTO.getSalary() == null ? null : salaryRepository.findById(staffDTO.getSalary())
                .orElseThrow(() -> new NotFoundException("salary not found"));
        staff.setSalary(salary);
        return staff;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Staff staff = staffRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final OrderItem staffIdOrderItem = orderItemRepository.findFirstByStaffId(staff);
        if (staffIdOrderItem != null) {
            referencedWarning.setKey("staff.orderItem.staffId.referenced");
            referencedWarning.addParam(staffIdOrderItem.getId());
            return referencedWarning;
        }
        final StockIntake staffStockIntake = stockIntakeRepository.findFirstByStaff(staff);
        if (staffStockIntake != null) {
            referencedWarning.setKey("staff.stockIntake.staff.referenced");
            referencedWarning.addParam(staffStockIntake.getId());
            return referencedWarning;
        }
        final ShiftLog staffShiftLog = shiftLogRepository.findFirstByStaff(staff);
        if (staffShiftLog != null) {
            referencedWarning.setKey("staff.shiftLog.staff.referenced");
            referencedWarning.addParam(staffShiftLog.getId());
            return referencedWarning;
        }
        final Payslip staffPayslip = payslipRepository.findFirstByStaff(staff);
        if (staffPayslip != null) {
            referencedWarning.setKey("staff.payslip.staff.referenced");
            referencedWarning.addParam(staffPayslip.getId());
            return referencedWarning;
        }
        final Refund staffRefund = refundRepository.findFirstByStaff(staff);
        if (staffRefund != null) {
            referencedWarning.setKey("staff.refund.staff.referenced");
            referencedWarning.addParam(staffRefund.getId());
            return referencedWarning;
        }
        final Refund approvedByRefund = refundRepository.findFirstByApprovedBy(staff);
        if (approvedByRefund != null) {
            referencedWarning.setKey("staff.refund.approvedBy.referenced");
            referencedWarning.addParam(approvedByRefund.getId());
            return referencedWarning;
        }
        final ItemReturn staffItemReturn = itemReturnRepository.findFirstByStaff(staff);
        if (staffItemReturn != null) {
            referencedWarning.setKey("staff.itemReturn.staff.referenced");
            referencedWarning.addParam(staffItemReturn.getId());
            return referencedWarning;
        }
        final ItemReturn approvedByItemReturn = itemReturnRepository.findFirstByApprovedBy(staff);
        if (approvedByItemReturn != null) {
            referencedWarning.setKey("staff.itemReturn.approvedBy.referenced");
            referencedWarning.addParam(approvedByItemReturn.getId());
            return referencedWarning;
        }
        return null;
    }

}
