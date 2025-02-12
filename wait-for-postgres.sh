#!/bin/sh
echo "Waiting for PostgreSQL to start..."
while ! nc -z db 5432; do
  sleep 2
done
echo "PostgreSQL started!"
exec "$@"