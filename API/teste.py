from math import radians, sin, cos, sqrt, atan2

def calcular_distancia(lat1, lon1, lat2, lon2):
    raio_terra = 6371.0
    
    # Converte graus para radianos
    lat1 = radians(lat1)
    lon1 = radians(lon1)
    lat2 = radians(lat2)
    lon2 = radians(lon2)
    
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    
    # Fórmula de Haversine
    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))
    distancia = raio_terra * c

    print(distancia)
    
    return distancia

calcular_distancia(-7.94490246, -34.86030746, -7.93870489, -34.87889517)