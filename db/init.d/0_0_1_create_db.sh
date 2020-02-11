#!/bin/bash
set -e

#    CREATE USER sports_centre_managemnet;
#    CREATE DATABASE sports_centre_managemnet;
#    GRANT ALL PRIVILEGES ON DATABASE sports_centre_managemnet TO sports_centre_managemnet;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SCHEMA sports_management_centre AUTHORIZATION comp2913;
EOSQL
