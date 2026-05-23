package uo.ri.cws.application.service.profesionalGroup.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.professionalgroup.impl.ProfessionalGroupGatewayImpl;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteProfessionalGroup implements Command<Void> {

    private String groupName;
    private ProfessionalGroupGateway pg =
            Factories.persistence.forProfessionalGroup();

    public DeleteProfessionalGroup(String groupName) {
        ArgumentChecks.isNotNull(groupName, "groupName cannot be null");
        ArgumentChecks.isNotBlank(groupName, "groupName cannot be blank");
        this.groupName = groupName;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ProfessionalGroupRecord> om = pg.findByName(groupName);
        BusinessChecks.exists(om,
                "Cannot remove a professional group that does not exist");

        checkNoContracts(om.get().id);

        pg.remove(om.get().id);
        return null;
    }

    private void checkNoContracts(String groupId) throws BusinessException {
        boolean hasContracts = ((ProfessionalGroupGatewayImpl) pg).hasContracts(groupId);
        if (hasContracts) {
            throw new BusinessException(
                    "Cannot delete a professional group with contracts assigned");
        }
    }

}