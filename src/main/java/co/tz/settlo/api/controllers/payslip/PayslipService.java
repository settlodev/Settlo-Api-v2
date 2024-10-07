package co.tz.settlo.api.controllers.payslip;

import co.tz.settlo.api.controllers.salary.Salary;
import co.tz.settlo.api.controllers.salary.SalaryRepository;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PayslipService {

    private final PayslipRepository payslipRepository;
    private final StaffRepository staffRepository;
    private final SalaryRepository salaryRepository;

    public PayslipService(final PayslipRepository payslipRepository,
            final StaffRepository staffRepository, final SalaryRepository salaryRepository) {
        this.payslipRepository = payslipRepository;
        this.staffRepository = staffRepository;
        this.salaryRepository = salaryRepository;
    }

    @Transactional(readOnly = true)
    public List<PayslipDTO> findAll(final UUID locationId) {
        final List<Payslip> payslips = payslipRepository.findAllByLocationId(locationId);
        return payslips.stream()
                .map(payslip -> mapToDTO(payslip, new PayslipDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<PayslipDTO> searchAll(SearchRequest request) {
        SearchSpecification<Payslip> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Payslip> payslipsPage = payslipRepository.findAll(specification, pageable);

        return payslipsPage.map(payslip -> mapToDTO(payslip, new PayslipDTO()));
    }

    @Transactional(readOnly = true)
    public PayslipDTO get(final UUID id) {
        return payslipRepository.findById(id)
                .map(payslip -> mapToDTO(payslip, new PayslipDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final PayslipDTO payslipDTO) {
        final Payslip payslip = new Payslip();
        mapToEntity(payslipDTO, payslip);
        return payslipRepository.save(payslip).getId();
    }

    @Transactional
    public void update(final UUID id, final PayslipDTO payslipDTO) {
        final Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(payslipDTO, payslip);
        payslipRepository.save(payslip);
    }

    @Transactional
    public void delete(final UUID id) {
        payslipRepository.deleteById(id);
    }

    private PayslipDTO mapToDTO(final Payslip payslip, final PayslipDTO payslipDTO) {
        payslipDTO.setId(payslip.getId());
        payslipDTO.setBaseSalary(payslip.getBaseSalary());
        payslipDTO.setNetSalary(payslip.getNetSalary());
        payslipDTO.setStartPeriod(payslip.getStartPeriod());
        payslipDTO.setEndPeriod(payslip.getEndPeriod());
        payslipDTO.setStatus(payslip.getStatus());
        payslipDTO.setCanDelete(payslip.getCanDelete());
        payslipDTO.setIsArchived(payslip.getIsArchived());
        payslipDTO.setStaff(payslip.getStaff() == null ? null : payslip.getStaff().getId());
        payslipDTO.setSalary(payslip.getSalary() == null ? null : payslip.getSalary().getId());
        return payslipDTO;
    }

    private Payslip mapToEntity(final PayslipDTO payslipDTO, final Payslip payslip) {
        payslip.setBaseSalary(payslipDTO.getBaseSalary());
        payslip.setNetSalary(payslipDTO.getNetSalary());
        payslip.setStartPeriod(payslipDTO.getStartPeriod());
        payslip.setEndPeriod(payslipDTO.getEndPeriod());
        payslip.setStatus(payslipDTO.getStatus());
        payslip.setCanDelete(payslipDTO.getCanDelete());
        payslip.setIsArchived(payslipDTO.getIsArchived());
        final Staff staff = payslipDTO.getStaff() == null ? null : staffRepository.findById(payslipDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        payslip.setStaff(staff);
        final Salary salary = payslipDTO.getSalary() == null ? null : salaryRepository.findById(payslipDTO.getSalary())
                .orElseThrow(() -> new NotFoundException("salary not found"));
        payslip.setSalary(salary);
        return payslip;
    }

}
