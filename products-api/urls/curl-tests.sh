#!/bin/bash

BASE_URL="http://localhost:8080"

# Get All Products
curl -s -X GET "$BASE_URL/products" \
  -H "Accept: application/json" | jq .

# Get Product by ID
curl -s -X GET "$BASE_URL/products/1" \
  -H "Accept: application/json" | jq .

# Create Product
curl -s -X POST "$BASE_URL/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sample Product",
    "description": "A sample product description",
    "value": 29.99,
    "quantity": 100
  }' | jq .

# Update Product
curl -s -X PUT "$BASE_URL/products/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "description": "An updated product description",
    "value": 49.99,
    "quantity": 50
  }' | jq .

# Delete Product
curl -s -X DELETE "$BASE_URL/products/1" -v
