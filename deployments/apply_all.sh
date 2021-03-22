#!/usr/bin/env bash

echo "applying..."
kubectl -n app-quotes apply -f app-quotes-secret.yaml
kubectl -n app-quotes apply -f app-quotes-mysql.yaml
kubectl -n app-quotes apply -f app-quotes-server.yaml
kubectl -n app-quotes apply -f app-quotes-client.yaml
