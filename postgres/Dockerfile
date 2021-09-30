FROM postgres:13.4-alpine

ENV POSTGRES_PASSWORD 12345

COPY tables.sql /docker-entrypoint-initdb.d/tables.sql
