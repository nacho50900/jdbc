package uo.ri.cws.application.service.contract.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContract implements Command<Void> {

    private final ContractDto dto;
    private final ContractGateway cg  = Factories.persistence.forContract();
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    public UpdateContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractDto cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "Contract id cannot be blank");
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0,
                "Annual base salary must be positive");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractRecord> existing = cg.findById(dto.id);
        BusinessChecks.exists(existing, "Contract does not exist");

        ContractRecord contract = existing.get();
        BusinessChecks.hasVersion(dto.version, contract.version);

        if (!"IN_FORCE".equals(contract.state)) {
            throw new BusinessException(
                    "Only IN_FORCE contracts can be updated");
        }

        // Validate end date for FIXED_TERM
        if (dto.endDate != null) {
            Optional<ContractTypeRecord> contractType =
                    ctg.findById(contract.contractTypeId);
            BusinessChecks.exists(contractType, "Contract type not found");

            if ("FIXED_TERM".equals(contractType.get().name)
                    && !dto.endDate.isAfter(contract.startDate)) {
                throw new BusinessException(
                        "End date must be after start date");
            }
        }

        contract.endDate = dto.endDate;
        contract.annualBaseSalary = dto.annualBaseSalary;

        try {
            cg.update(contract);
        } catch (PersistenceException pe) {
            throw new BusinessException(pe.getMessage());
        }
        return null;
    }

}
