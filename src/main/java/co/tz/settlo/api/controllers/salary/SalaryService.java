package co.tz.settlo.api.controllers.salary;

import co.tz.settlo.api.controllers.payslip.Payslip;
import co.tz.settlo.api.controllers.payslip.PayslipRepository;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final StaffRepository staffRepository;
    private final PayslipRepository payslipRepository;

    public SalaryService(final SalaryRepository salaryRepository,
            final StaffRepository staffRepository, final PayslipRepository payslipRepository) {
        this.salaryRepository = salaryRepository;
        this.staffRepository = staffRepository;
        this.payslipRepository = payslipRepository;
    }

    public List<SalaryDTO> findAll() {
        final List<Salary> salaries = salaryRepository.findAll(Sort.by("id"));
        return salaries.stream()
                .map(salary -> mapToDTO(salary, new SalaryDTO()))
                .toList();
    }

    public SalaryDTO get(final UUID id) {
        return salaryRepository.findById(id)
                .map(salary -> mapToDTO(salary, new SalaryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SalaryDTO salaryDTO) {
        final Salary salary = new Salary();
        mapToEntity(salaryDTO, salary);
        return salaryRepository.save(salary).getId();
    }

    public void update(final UUID id, final SalaryDTO salaryDTO) {
        final Salary salary = salaryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(salaryDTO, salary);
        salaryRepository.save(salary);
    }

    public void delete(final UUID id) {
        salaryRepository.deleteById(id);
    }

    private SalaryDTO mapToDTO(final Salary salary, final SalaryDTO salaryDTO) {
        salaryDTO.setId(salary.getId());
        salaryDTO.setAmount(salary.getAmount());
        salaryDTO.setFrequency(salary.getFrequency());
        salaryDTO.setAccountNumber(salary.getAccountNumber());
        salaryDTO.setBankName(salary.getBankName());
        salaryDTO.setBranch(salary.getBranch());
        salaryDTO.setStatus(salary.getStatus());
        salaryDTO.setIsArchived(salary.getIsArchived());
        salaryDTO.setCanDelete(salary.getCanDelete());
        return salaryDTO;
    }

    private Salary mapToEntity(final SalaryDTO salaryDTO, final Salary salary) {
        salary.setAmount(salaryDTO.getAmount());
        salary.setFrequency(salaryDTO.getFrequency());
        salary.setAccountNumber(salaryDTO.getAccountNumber());
        salary.setBankName(salaryDTO.getBankName());
        salary.setBranch(salaryDTO.getBranch());
        salary.setStatus(salaryDTO.getStatus());
        salary.setIsArchived(salaryDTO.getIsArchived());
        salary.setCanDelete(salaryDTO.getCanDelete());
        return salary;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Salary salary = salaryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Staff salaryStaff = staffRepository.findFirstBySalary(salary);
        if (salaryStaff != null) {
            referencedWarning.setKey("salary.staff.salary.referenced");
            referencedWarning.addParam(salaryStaff.getId());
            return referencedWarning;
        }
        final Payslip salaryPayslip = payslipRepository.findFirstBySalary(salary);
        if (salaryPayslip != null) {
            referencedWarning.setKey("salary.payslip.salary.referenced");
            referencedWarning.addParam(salaryPayslip.getId());
            return referencedWarning;
        }
        return null;
    }

}
