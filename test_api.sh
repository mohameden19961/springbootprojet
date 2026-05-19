#!/bin/bash

# Configuration
PORT=8081
URL="http://localhost:${PORT}"
ADMIN_AUTH="-u admin:admin123"
USER_AUTH="-u user:user123"

echo "=== Démarrage de l'application Spring Boot sur le port ${PORT} ==="
./mvnw spring-boot:run > spring_boot.log 2>&1 &
SERVER_PID=$!

# Fonction de nettoyage à la fin
cleanup() {
    echo ""
    echo "=== Arrêt de l'application Spring Boot (PID: ${SERVER_PID}) ==="
    kill $SERVER_PID 2>/dev/null
    wait $SERVER_PID 2>/dev/null
    rm -f spring_boot.log
}
trap cleanup EXIT

# Attente du démarrage de l'application
echo "Attente de l'ouverture du port ${PORT}..."
STARTED=false
for i in {1..30}; do
    STATUS_CODE=$(curl -s -o /dev/null -w "%{http_code}" ${URL}/api/categories)
    if [ "$STATUS_CODE" -eq 401 ] || [ "$STATUS_CODE" -eq 200 ]; then
        echo "L'application a démarré avec succès !"
        STARTED=true
        break
    fi
    if ! kill -0 $SERVER_PID 2>/dev/null; then
        echo "Erreur : Le serveur a échoué au démarrage. Journal d'erreurs :"
        cat spring_boot.log
        exit 1
    fi
    sleep 2
done

if [ "$STARTED" = false ]; then
    echo "Erreur : Le serveur a mis trop de temps à démarrer."
    cat spring_boot.log
    exit 1
fi

echo ""
echo "=================================================="
echo "          LANCEMENT DES TESTS D'API"
echo "=================================================="

# 1. Vérification de la Sécurité (401 attendu sans authentification)
echo -n "Test 1 : Accès sans authentification (attendu: 401)... "
CODE=$(curl -s -o /dev/null -w "%{http_code}" ${URL}/api/categories)
if [ "$CODE" -eq 401 ]; then
    echo "OK (Code 401)"
else
    echo "ÉCHEC (Code $CODE)"
fi

# 2. Test de la gestion des Catégories (24139)
echo ""
echo "--- Tests Catégories ---"
echo "Test 2.1 : Création d'une catégorie (POST /api/categories)"
curl -s -X POST ${URL}/api/categories $ADMIN_AUTH \
  -H "Content-Type: application/json" \
  -d '{"name": "Roman"}' | grep -q "Roman" && echo "  -> OK (Catégorie créée)" || echo "  -> ÉCHEC"

echo "Test 2.2 : Liste des catégories (GET /api/categories)"
curl -s -X GET ${URL}/api/categories $USER_AUTH | grep -q "Roman" && echo "  -> OK (Catégorie trouvée dans la liste)" || echo "  -> ÉCHEC"

# 3. Test de la gestion des Livres (24238)
echo ""
echo "--- Tests Livres ---"
echo "Test 3.1 : Création d'un livre (POST /api/books)"
curl -s -X POST ${URL}/api/books $ADMIN_AUTH \
  -H "Content-Type: application/json" \
  -d '{"title": "Harry Potter", "isbn": "9780747532699", "languageCode": "FR", "categoryId": 1, "publisherId": 1}' | grep -q "Harry Potter" && echo "  -> OK (Livre créé)" || echo "  -> ÉCHEC"

echo "Test 3.2 : Liste des livres (GET /api/books)"
curl -s -X GET ${URL}/api/books $USER_AUTH | grep -q "Harry Potter" && echo "  -> OK (Livre trouvé dans la liste)" || echo "  -> ÉCHEC"

# 4. Test de la gestion des Emprunts (24014)
echo ""
echo "--- Tests Emprunts ---"
echo "Test 4.1 : Emprunt d'un exemplaire (POST /api/borrows/checkout)"
curl -s -X POST "${URL}/api/borrows/checkout?memberId=1&barcode=BC001" $ADMIN_AUTH | grep -q "ACTIVE" && echo "  -> OK (Emprunt réussi)" || echo "  -> ÉCHEC"

echo "Test 4.2 : Tentative de ré-emprunt du même exemplaire (devrait échouer)"
curl -s -X POST "${URL}/api/borrows/checkout?memberId=1&barcode=BC001" $ADMIN_AUTH | grep -q "error" && echo "  -> OK (Erreur interceptée correctement)" || echo "  -> ÉCHEC"

echo "Test 4.3 : Renouvellement de l'emprunt (POST /api/borrows/1/renew)"
curl -s -X POST "${URL}/api/borrows/1/renew" $ADMIN_AUTH | grep -q "\"renewalCount\":1" && echo "  -> OK (Renouvellement pris en compte)" || echo "  -> ÉCHEC"

echo "Test 4.4 : Retour de l'exemplaire (POST /api/borrows/1/return)"
curl -s -X POST "${URL}/api/borrows/1/return" $ADMIN_AUTH | grep -q "RETURNED" && echo "  -> OK (Retour enregistré)" || echo "  -> ÉCHEC"

# 5. Test de la gestion des Réservations & File d'attente (24157)
echo ""
echo "--- Tests Réservations & File d'attente ---"
echo "Test 5.1 : Créer une réservation 1 pour le membre 1 sur le livre 1 (POST /api/reservations)"
curl -s -X POST ${URL}/api/reservations $USER_AUTH \
  -H "Content-Type: application/json" \
  -d '{"memberId": 1, "bookId": 1}' | grep -q "\"queuePosition\":1" && echo "  -> OK (Réservation 1 créée, position 1)" || echo "  -> ÉCHEC"

echo "Test 5.2 : Créer une réservation 2 pour le membre 1 sur le livre 1 (POST /api/reservations)"
curl -s -X POST ${URL}/api/reservations $USER_AUTH \
  -H "Content-Type: application/json" \
  -d '{"memberId": 1, "bookId": 1}' | grep -q "\"queuePosition\":2" && echo "  -> OK (Réservation 2 créée, position 2)" || echo "  -> ÉCHEC"

echo "Test 5.3 : Récupérer la file d'attente pour le livre 1 (GET /api/reservations/queue/1)"
curl -s -X GET ${URL}/api/reservations/queue/1 $USER_AUTH | grep -q "queuePosition" && echo "  -> OK (File d'attente renvoyée)" || echo "  -> ÉCHEC"

echo "Test 5.4 : Annuler la réservation 1 (POST /api/reservations/1/cancel)"
curl -s -X POST "${URL}/api/reservations/1/cancel" $USER_AUTH | grep -q "CANCELLED" && echo "  -> OK (Réservation annulée avec succès)" || echo "  -> ÉCHEC"

echo ""
echo "=================================================="
echo "            TOUS LES TESTS SONT TERMINÉS"
echo "=================================================="
