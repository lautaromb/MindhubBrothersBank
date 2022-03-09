package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
//import org.springframework.stereotype.Service;

import java.util.List;


public interface ClientServices {
    public List<ClientDTO> getClients();
    public Client saveClient(Client client);
    public ClientDTO findClientById(Long id);
    public Client findClientByEmail(String email);
}
