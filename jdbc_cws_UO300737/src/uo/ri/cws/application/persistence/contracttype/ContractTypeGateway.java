package uo.ri.cws.application.persistence.contracttype;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ContractTypeGateway extends Gateway<ContractTypeRecord> {

    /**
     * Finds a contract type by its unique name.
     *
     * @param name the contract type name
     * @return Optional with the record, or empty if not found
     */
    Optional<ContractTypeRecord> findByName(String name);

    /**
     * Counts contracts (any state) linked to this contract type.
     *
     * @param contractTypeId surrogate id
     * @return number of linked contracts
     */
    int countContractsFor(String contractTypeId);

}
