#!/usr/bin/env bash

echo "deleting..."
kubectl -n app-quotes delete -f app-quotes-client.yaml
kubectl -n app-quotes delete -f app-quotes-server.yaml
kubectl -n app-quotes delete -f app-quotes-mysql.yaml
kubectl -n app-quotes delete -f app-quotes-secret.yaml