package br.com.wancarvalho.todolist.user;

import java.util.UUID;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // biblioteca lombok adiciona os getters e setters a classe
@Entity(name = "tb_users")
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    // @Column(name = "usuario") utiliza Anottation caso queira alterar nome da
    // coluna no banco de dados
    @Column(unique = true) // define coluna como chave única, evitando registros duplicados
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
