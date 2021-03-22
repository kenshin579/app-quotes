#!/usr/bin/env bash

echo "deleting..."
kubectl -n app-quotes apply -f app-quotes-client.yaml
kubectl -n app-quotes apply -f app-quotes-server.yaml
kubectl -n app-quotes apply -f app-quotes-mysql.yaml
kubectl -n app-quotes apply -f app-quotes-secret.yaml
