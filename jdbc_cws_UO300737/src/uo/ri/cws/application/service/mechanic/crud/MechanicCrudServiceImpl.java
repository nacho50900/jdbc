package uo.ri.cws.application.service.mechanic.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.DeleteMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.ListAllMechanics;
import uo.ri.cws.application.service.mechanic.crud.commands.ListMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.ListMechanicById;
import uo.ri.cws.application.service.mechanic.crud.commands.ListMechanicByNif;
import uo.ri.cws.application.service.mechanic.crud.commands.ListMechanicsWithValidContracts;
import uo.ri.cws.application.service.mechanic.crud.commands.UpdateMechanic;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceImpl implements MechanicCrudService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public MechanicDto create(MechanicDto dto) throws BusinessException {
	AddMechanic am = new AddMechanic(dto);
	return am.execute();

    }

    @Override
    public void delete(String mechanicId) throws BusinessException {
	DeleteMechanic dm = new DeleteMechanic(mechanicId);
	dm.execute();

    }

    @Override
    public void update(MechanicDto dto) throws BusinessException {
	// new UpdateMechanic(dto).execute();
	executor.execute(new UpdateMechanic(dto));
    }

    @Override
    public Optional<MechanicDto> findById(String id) throws BusinessException {
		ListMechanic lm = new ListMechanicById(id);
		MechanicDto dto = lm.execute();
		if(dto == null) {
			return Optional.empty();
		}
		return Optional.of(lm.execute());
    }

    @Override
    public Optional<MechanicDto> findByNif(String nif)
	throws BusinessException {
	ListMechanic lm = new ListMechanicByNif(nif);
	MechanicDto dto = lm.execute();
	if(dto == null) {
		return Optional.empty();
	}
	return Optional.of(lm.execute());
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
	ListAllMechanics lms = new ListAllMechanics();
	return lms.execute();
    }

	@Override //NOT USED -> DONE THROUGH CONTRACT
	public List<MechanicDto> findMechanicsWithValidContract() 
			throws BusinessException {
		ListMechanicsWithValidContracts lms = 
				new ListMechanicsWithValidContracts();
		return lms.execute();
	}

}
