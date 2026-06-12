package uo.ri.cws.application.service.contracttype.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContractType implements Command<Void> {

    private final ContractTypeDto dto;
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    public UpdateContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractTypeDto cannot be null");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be blank");
        ArgumentChecks.isTrue(dto.compensationDays >= 0,
                "Compensation days must be >= 0");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractTypeRecord> existing = ctg.findByName(dto.name);
        BusinessChecks.exists(existing, "Contract type does not exist");

        ContractTypeRecord record = existing.get();
        BusinessChecks.hasVersion(dto.version, record.version);

        record.compensationDays = dto.compensationDays;

        try {
            ctg.update(record);
        } catch (PersistenceException pe) {
            throw new BusinessException(pe.getMessage());
        }
        return null;
    }

}
