package br.com.alura.carteira.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CalculadoraDeImpostoService calculadoraDeImpostoService;

	
	public Page<TransacaoDto> listar(Pageable paginacao, Usuario usuario) {
		return transacaoRepository
				.findAllByUsuario(paginacao, usuario)
				.map(t -> modelMapper.map(t, TransacaoDto.class));
		
//		List<TransacaoDto> transacoesDto = new ArrayList<TransacaoDto>();
//		transacoes.forEach(transacao -> {
//			BigDecimal imposto = calculadoraDeImpostoService.calcular(transacao);
//			TransacaoDto dto = modelMapper.map(transacoes, TransacaoDto.class);
//			dto.setImposto(imposto);
//			transacoesDto.add(dto);
//		});
//		return new PageImpl<TransacaoDto>(
//				transacoesDto,
//				transacoes.getPageable(),
//				transacoes.getTotalElements());
	}
		
	
	@Transactional //é para informar ao spring que é u método que vai fazer uma transacao no banco de dados (update, insert, delete ... e no fim um commit) diferente de um select q eh apenas uma consulta
	public TransacaoDto cadastrar(TransacaoFormDto dto, Usuario logado) { // necessário a anotação para o string saber pegar os parametros da request // anotacao valid para o spring realizar a validação das regras que eu coloquei na classe dto nos atributos, (notempty, notnull, size...)	
		Long idUsuario = dto.getUsuarioId();
		
		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			if (!usuario.equals(logado)) {
				lancarErroAcessoNegado();
			}
			
			Transacao transacao = modelMapper.map(dto, Transacao.class);
			transacao.setId(null); //pois o modelMapper esta se confundindo com o usuarioId e preenchendo id para a transacao, entao aqui eu deixo ele null
			transacao.setUsuario(usuario);
			BigDecimal imposto = calculadoraDeImpostoService.calcular(transacao);
			
			transacao.setImposto(imposto);		
			
			transacaoRepository.save(transacao);
			return modelMapper.map(transacao, TransacaoDto.class);
		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException("usuario inexistente");
		}
	
	}

	@Transactional //nao é preciso chamar um update pois a propria jpa faz esse update , ela nota que acabamos de atualizar o resgistro pelo id, e quando acabar a execucao desse metood, o hibernate dispara o update no banco e faz o commit da transacao sozinho
	public TransacaoDto atualizar(@Valid AtualizacaoTransacaoFormDto dto, Usuario logado) {
		Transacao transacao = transacaoRepository.getById(dto.getId());

		if (!transacao.getUsuario().equals(logado)) {
			lancarErroAcessoNegado();
		}
		transacao.atualizarInformacoes(dto.getTicker(), dto.getData(), dto.getPreco(), dto.getQuantidade(), dto.getTipo());
		
		return modelMapper.map(transacao, TransacaoDto.class);
	}


	@Transactional
	public void remover(@NotNull Long id, Usuario logado) {
		Transacao transacao = transacaoRepository.getById(id);
		if (!transacao.getUsuario().equals(logado)) {
			lancarErroAcessoNegado();
		}
		transacaoRepository.deleteById(id);
		
	}


	public TransacaoDetalhadaDto detalhar(@NotNull Long id, Usuario logado) {		
		Transacao transacao = transacaoRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException()); //o findbyId devolve um optional do java8 que é um objeto que pode ser que tenha um registro e pode ser que nao, e ai no caseo de nao ter eum forço uma exception com o elseThrow
		
		if (!transacao.getUsuario().equals(logado)) {
			lancarErroAcessoNegado();
		}
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
	}
	
	private void lancarErroAcessoNegado() {
		throw new AccessDeniedException("Acesso negado");
	}
	
	

}
