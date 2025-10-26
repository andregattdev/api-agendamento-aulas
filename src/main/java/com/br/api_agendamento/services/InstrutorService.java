package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.InstrutorRequestDTO;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException;
import com.br.api_agendamento.exception.RegraDeNegocioException;
import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.model.TipoUsuario;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.repositories.InstrutorRepository;
import com.br.api_agendamento.repositories.UsuarioRepository; // NOVO: Para buscar/criar Usuario

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // NOVO: Para criptografar a senha
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // NOVO: Para garantir as operações de duas entidades

import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // INJETADO para CRUD de Usuario

    @Autowired
    private PasswordEncoder passwordEncoder; // INJETADO para criptografar senhas

    // CREATE: Cria o Usuario e o Instrutor associado
    @Transactional
    public Instrutor salvar(InstrutorRequestDTO dto) {
        // Lógica de Negócio: Verificar se E-mail já existe na tabela de USUARIOS (onde fica o login)
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro usuário (Instrutor, Cliente ou Admin).");
        }
        
        // 1. Cria a entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setTelefone(dto.getTelefone()); // Campo adicionado ao DTO
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha())); // Criptografa a senha
        novoUsuario.setTipo(TipoUsuario.INSTRUTOR); // Define o papel corretamente
        
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario); // Salva o Usuario
        
        // 2. Cria a entidade Instrutor e faz a associação
        Instrutor instrutor = new Instrutor();
        // Não precisamos setar nome/email diretamente no Instrutor se usarmos a relação
        instrutor.setEspecialidade(dto.getEspecialidade());
        instrutor.setUsuario(usuarioSalvo); // ASSOCIAÇÃO: Faz a ligação 1:1

        return instrutorRepository.save(instrutor);
    }

    // UPDATE: Atualiza os dados do Instrutor e do Usuario associado
    @Transactional
    public Instrutor atualizar(Long id, InstrutorRequestDTO dto) {
        // 1. Busca o Instrutor existente (lança RecursoNaoEncontradoException se não existir)
        Instrutor instrutorExistente = buscarPorId(id).orElseThrow(
            () -> new RecursoNaoEncontradoException("Instrutor não encontrado com ID: " + id)
        );
        
        Usuario usuarioExistente = instrutorExistente.getUsuario();

        // 2. Lógica de Negócio: Se o email mudou, verificar unicidade na tabela de USUARIOS
        if (!usuarioExistente.getEmail().equals(dto.getEmail())) {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RegraDeNegocioException("Novo e-mail já cadastrado para outro usuário.");
            }
        }
        
        // 3. Atualiza os dados do Usuario
        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setTelefone(dto.getTelefone()); // Campo adicionado ao DTO

        // Se a senha estiver no DTO e for diferente (opcionalmente)
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        usuarioRepository.save(usuarioExistente); // Salva as atualizações do Usuario
        
        // 4. Atualiza os dados do Instrutor
        instrutorExistente.setEspecialidade(dto.getEspecialidade());
        
        return instrutorRepository.save(instrutorExistente);
    }
    
    // READ (Busca Instrutor por ID)
    public Optional<Instrutor> buscarPorId(Long id) {
        return instrutorRepository.findById(id);
    }
    
    // DELETE: Deleta o Instrutor e, por Cascade, o Usuario associado.
    @Transactional
    public void deletarPorId(Long id) {
        Instrutor instrutor = instrutorRepository.findById(id).orElseThrow(
            () -> new RecursoNaoEncontradoException("Instrutor não encontrado com ID: " + id)
        );
        // O cascade DELETE definido no Instrutor.java deve cuidar do Usuario,
        // mas é mais seguro deletar o Instrutor para que ele remova o Usuario associado.
        instrutorRepository.delete(instrutor);
    }
    
    // Outros métodos READ...
    public List<Instrutor> buscarTodos() {
        return instrutorRepository.findAll();
    }
    // ...
}