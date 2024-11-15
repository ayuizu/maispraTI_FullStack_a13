package com.example.api_user.model;
import jakarta.persistence.*;
import lombok.Data;

//Metaprogramacao - anotacoes
@Entity
// Anotação @Entity:
// - Marca a classe como uma **entidade JPA**, o que significa que ela será mapeada para uma tabela no banco de dados.
// - O JPA (Java Persistence API) é um padrão para mapeamento objeto-relacional, permitindo que as classes Java sejam persistidas em um banco de dados relacional.
// - Ao usar @Entity, o Spring Data JPA trata esta classe como um objeto persistente, o que permite realizar operações de CRUD automaticamente.

@Data
// Anotação @Data (Lombok):
// - Faz parte da biblioteca Lombok, que simplifica o código eliminando a necessidade de escrever getters, setters, e outros métodos comuns manualmente.
// - A anotação @Data gera automaticamente os seguintes métodos:
// - **Getters e Setters**: Para todos os campos da classe.
// - **toString()**: Um método que retorna uma string representando o objeto.
// - **equals() e hashCode()**: Para comparar objetos de maneira eficiente.
// - **RequiredArgsConstructor**: Um construtor para os campos obrigatórios.
// - A principal vantagem de usar @Data é manter o código mais limpo, sem ter que escrever explicitamente os métodos de manipulação de atributos.

@Table(name = "users")
// Anotação @Table:
// - Define o nome da tabela no banco de dados que será associada à entidade. Neste caso, a tabela será chamada de `users`.
// - Sem a anotação @Table, o JPA assume que o nome da tabela é o mesmo da classe, mas com a anotação, você pode personalizar o nome da tabela.

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

}
