#!/bin/bash

set -e

echo "Starting deployment process..."

echo "Pulling latest code..."
git pull origin test

echo "Stopping current containers..."
docker compose down

echo "Building and starting new containers..."
docker compose up -d --build

echo "Cleaning up unused Docker images..."
docker image prune -f

echo "Deployment finished! Current status:"
docker compose ps