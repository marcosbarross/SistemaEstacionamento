from fastapi import FastAPI, HTTPException
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
    )
""")
conn.commit()

# Classe modelo para os dados do estacionamento
class Estacionamento(BaseModel):
    nome: str
    preco: float
    latitude: float
    longitude: float

# Inicializar a aplicação FastAPI
app = FastAPI()

# Endpoint para salvar um novo estacionamento
@app.post("/estacionamento/")
async def criar_estacionamento(estacionamento: Estacionamento):
    cur.execute("""
        INSERT INTO estacionamentos (nome, preco, latitude, longitude)
        VALUES (%s, %s, %s, %s)
        RETURNING id
    """, (estacionamento.nome, estacionamento.preco, estacionamento.latitude, estacionamento.longitude))
    id = cur.fetchone()[0]
    conn.commit()
    return {"id": id, **estacionamento.dict()}

# Endpoint para retornar todos os estacionamentos salvos
@app.get("/estacionamentos/")
async def listar_estacionamentos():
    cur.execute("SELECT * FROM estacionamentos")
    estacionamentos = []
    for row in cur.fetchall():
        id, nome, preco, latitude, longitude = row
        estacionamentos.append({"id": id, "nome": nome, "preco": preco, "latitude": latitude, "longitude": longitude})
    return estacionamentos
