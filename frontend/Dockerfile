# 🐳 Optimized Frontend Dockerfile (Vite + Nginx)
# --------------------------------------------------------

# Stage 1: Build the Static Files
FROM node:20-alpine AS build
WORKDIR /app

# CACHING: Copy package.json first
COPY package*.json ./
RUN npm install

# Copy source and build (passing the API URL argument)
COPY . .
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# Stage 2: Fast Static Serving with Nginx
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html

# SPA ROUTING: Ensure Nginx routes all paths to index.html (for React/Vue/Vite)
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
