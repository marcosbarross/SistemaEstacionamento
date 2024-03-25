from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from typing import List
from datetime import datetime, timedelta
import psycopg2

conn = psycopg2.connect(
    dbname="estacionamentos_api",
    user="postgre",
    password="root",
    host="localhost"
)
cur = conn.cursor()

# Cria a tabela se não existir
cur.execute("""
    CREATE TABLE IF NOT EXISTS usuarios (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(255) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        senha VARCHAR(255) NOT NULL,
        tipo_veiculo VARCHAR(50)
    );

    CREATE TABLE IF NOT EXISTS estacionamentos (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(255) NOT NULL,
        tipo_vaga VARCHAR(50)[],
        horario_abertura varchar(20),
        horario_fechamento varchar(20),
        dias_funcionamento VARCHAR(100)[],
        latitude DECIMAL(10, 8),
        longitude DECIMAL(11, 8),
        preco DECIMAL(10, 2),
        id_usuario INT REFERENCES usuarios(id)
    );
""")
conn.commit()

# Classe modelo para os dados do estacionamento
class Estacionamento(BaseModel):
    nome: str
    tipo_vaga: List[str]
    horario_abertura: str 
    horario_fechamento: str 
    dias_funcionamento: List[str]
    latitude: float
    longitude: float
    preco: float
    id_usuario: int

class Usuario(BaseModel):
    nome: str
    email: str
    senha: str
    tipo_veiculo: str

class UsuarioAuth(BaseModel):
    email: str
    senha: str

# Inicializar a aplicação FastAPI
app = FastAPI()

# Endpoint para salvar um novo estacionamento

@app.post("/AddEstacionamento/")
async def criar_estacionamento(estacionamento: Estacionamento):
    cur.execute("""
        INSERT INTO estacionamentos (nome, tipo_vaga, horario_abertura, horario_fechamento, dias_funcionamento, latitude, longitude, preco, id_usuario)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        RETURNING id
    """, (estacionamento.nome, estacionamento.tipo_vaga, estacionamento.horario_abertura, estacionamento.horario_fechamento, estacionamento.dias_funcionamento, estacionamento.latitude, estacionamento.longitude, estacionamento.preco, estacionamento.id_usuario))
    id = cur.fetchone()[0]
    conn.commit()
    return {"id": id, **estacionamento.dict()}


# Endpoint para retornar todos os estacionamentos salvos
@app.get("/GetEstacionamentos/")
async def listar_estacionamentos():
    cur.execute("SELECT * FROM estacionamentos")
    estacionamentos = []
    for row in cur.fetchall():
        id, nome, tipo_vaga, horario_abertura, horario_fechamento, dias_funcionamento, latitude, longitude, preco, id_usuario = row
        estacionamentos.append({"id": id, "nome": nome, "tipo_vaga": tipo_vaga, "horario_abertura": horario_abertura, "horario_fechamento": horario_fechamento, "dias_funcionamento": dias_funcionamento, "latitude": latitude, "longitude": longitude, "preco": preco, "id_usuario": id_usuario})
    return estacionamentos

# Endpoint para autenticar usuário
@app.post("/AutenticarUsuario/")
async def autenticar_usuario(usuario: UsuarioAuth):
    email = usuario.email
    senha = usuario.senha
    cur.execute("SELECT id, nome, email, senha FROM usuarios WHERE email = %s", (email,))
    usuario = cur.fetchone()
    if usuario is None:
        raise HTTPException(status_code=404, detail="Usuário não encontrado")
    
    id, nome, email_db, senha_db = usuario
    if senha == senha_db:
        return {"status_code": 200, "id_usuario": id}
    
    else:
        raise HTTPException(status_code=401, detail="Credenciais inválidas")

# Endpoint para salvar um novo usuário
@app.post("/AddUsuario/")
async def criar_usuario(usuario: Usuario):
    cur.execute("""
        INSERT INTO usuarios (nome, email, senha, tipo_veiculo)
        VALUES (%s, %s, %s, %s)
        RETURNING id
""", (usuario.nome, usuario.email, usuario.senha, usuario.tipo_veiculo))
    id = cur.fetchone()[0]
    conn.commit()
    return {"id": id, **usuario.dict()}
