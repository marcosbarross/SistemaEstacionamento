from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from typing import List
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
    CREATE TABLE IF NOT EXISTS estacionamentos (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        preco NUMERIC(10, 2) NOT NULL,
        latitude NUMERIC(10, 6) NOT NULL,
        longitude NUMERIC(10, 6) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS usuarios (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL,
        senha VARCHAR(100) NOT NULL
    );
""")
conn.commit()

# Classe modelo para os dados do estacionamento
class Estacionamento(BaseModel):
    nome: str
    preco: float
    latitude: float
    longitude: float

class Usuario(BaseModel):
    nome: str
    email: str
    senha: str

class UsuarioAuth(BaseModel):
    email: str
    senha: str

# Inicializar a aplicação FastAPI
app = FastAPI()

# Endpoint para salvar um novo estacionamento
@app.post("/AddEstacionamento/")
async def criar_estacionamento(estacionamento: Estacionamento):
    cur.execute("""
        INSERT INTO estacionamentos (nome, preco, latitude, longitude)
        VALUES (%s, %s, %s, %s)
        RETURNING id
    """, (estacionamento.nome, estacionamento.preco, estacionamento.latitude, estacionamento.longitude))
    id = cur.fetchone()[0]
    conn.commit()
    return {"id": id, **estacionamento.model_dump()}

# Endpoint para retornar todos os estacionamentos salvos
@app.get("/GetEstacionamentos/")
async def listar_estacionamentos():
    cur.execute("SELECT * FROM estacionamentos")
    estacionamentos = []
    for row in cur.fetchall():
        id, nome, preco, latitude, longitude = row
        estacionamentos.append({"id": id, "nome": nome, "preco": preco, "latitude": latitude, "longitude": longitude})
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
        return {"status_code": 200}
    else:
        raise HTTPException(status_code=401, detail="Credenciais inválidas")


# Endpoint para salvar um novo usuário
@app.post("/AddUsuario/")
async def criar_usuario(usuario: Usuario):
    cur.execute("""
        INSERT INTO usuarios (nome, email, senha)
        VALUES (%s, %s, %s)
        RETURNING id
""", (usuario.nome, usuario.email, usuario.senha))
    id = cur.fetchone()[0]
    conn.commit()
    return {"id": id, **usuario.model_dump()}