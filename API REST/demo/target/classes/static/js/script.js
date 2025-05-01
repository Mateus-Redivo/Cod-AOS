// Configuração base da API
const API_URL = 'http://localhost:8080/api/produtos';

// Estado da aplicação
let editandoProduto = false;

// Carregar produtos ao iniciar a página
document.addEventListener('DOMContentLoaded', carregarProdutos);

// Função para carregar produtos
async function carregarProdutos() {
    try {
        const response = await fetch(API_URL);
        const produtos = await response.json();
        
        const tbody = document.getElementById('produtosTableBody');
        tbody.innerHTML = '';
        
        produtos.forEach(produto => {
            tbody.innerHTML += `
                <tr>
                    <td>${produto.id}</td>
                    <td>${produto.nome}</td>
                    <td>${produto.descricao}</td>
                    <td>R$ ${produto.preco.toFixed(2)}</td>
                    <td>${produto.quantidade}</td>
                    <td>
                        <button class="btn btn-sm btn-warning btn-action" onclick="editarProduto(${produto.id})">
                            Editar
                        </button>
                        <button class="btn btn-sm btn-danger btn-action" onclick="excluirProduto(${produto.id})">
                            Excluir
                        </button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
        mostrarAlerta('Erro ao carregar produtos', 'danger');
    }
}

// Função para salvar produto (criar ou atualizar)
async function salvarProduto() {
    const produto = {
        id: document.getElementById('produtoId').value,
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        preco: parseFloat(document.getElementById('preco').value),
        quantidade: parseInt(document.getElementById('quantidade').value)
    };

    try {
        const url = editandoProduto ? `${API_URL}/${produto.id}` : API_URL;
        const method = editandoProduto ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(produto)
        });

        if (response.ok) {
            mostrarAlerta('Produto salvo com sucesso!', 'success');
            fecharModal();
            carregarProdutos();
        } else {
            throw new Error('Erro ao salvar produto');
        }
    } catch (error) {
        console.error('Erro ao salvar produto:', error);
        mostrarAlerta('Erro ao salvar produto', 'danger');
    }
}

// Função para editar produto
async function editarProduto(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const produto = await response.json();

        document.getElementById('produtoId').value = produto.id;
        document.getElementById('nome').value = produto.nome;
        document.getElementById('descricao').value = produto.descricao;
        document.getElementById('preco').value = produto.preco;
        document.getElementById('quantidade').value = produto.quantidade;

        editandoProduto = true;
        document.getElementById('modalTitle').textContent = 'Editar Produto';
        
        const modal = new bootstrap.Modal(document.getElementById('produtoModal'));
        modal.show();
    } catch (error) {
        console.error('Erro ao carregar produto para edição:', error);
        mostrarAlerta('Erro ao carregar produto', 'danger');
    }
}

// Função para excluir produto
async function excluirProduto(id) {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                mostrarAlerta('Produto excluído com sucesso!', 'success');
                carregarProdutos();
            } else {
                throw new Error('Erro ao excluir produto');
            }
        } catch (error) {
            console.error('Erro ao excluir produto:', error);
            mostrarAlerta('Erro ao excluir produto', 'danger');
        }
    }
}

// Função para fechar o modal e resetar o formulário
function fecharModal() {
    const modal = bootstrap.Modal.getInstance(document.getElementById('produtoModal'));
    modal.hide();
    resetarFormulario();
}

// Função para resetar o formulário
function resetarFormulario() {
    document.getElementById('produtoForm').reset();
    document.getElementById('produtoId').value = '';
    editandoProduto = false;
    document.getElementById('modalTitle').textContent = 'Novo Produto';
}

// Função para mostrar alertas
function mostrarAlerta(mensagem, tipo) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${mensagem}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}

// Listener para resetar o formulário quando o modal for fechado
document.getElementById('produtoModal').addEventListener('hidden.bs.modal', resetarFormulario);