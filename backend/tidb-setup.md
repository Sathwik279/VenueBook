# 💾 TiDB Cloud Connection Guide

TiDB Cloud is MySQL-compatible, and connection to it is very similar to standard MySQL, with a few requirements for security.

## 🔗 JDBC Connection URL Format

In your `application-prod.properties` (or as a secret), use the following format:

```properties
spring.datasource.url=jdbc:mysql://<TIDB_HOST>:4000/<DATABASE>?useSSL=true&verifyServerCertificate=true&trustCertificateKeyStoreUrl=file:/app/certs/isrgrootx1.pem&enabledTLSProtocols=TLSv1.2,TLSv1.3
spring.datasource.username=${TIDB_USER}
spring.datasource.password=${TIDB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## 🔒 SSL / CA Certificates

TiDB Cloud  requires SSL for secure connection. Most TiDB clusters use the **ISRG Root X1** certificate (Let's Encrypt).

### Option 1: Bundle with Docker (Recommended)
Add these lines to your `Dockerfile`:

```dockerfile
# Add this after Stage 2 WORKDIR /app
# Download the CA certificate
RUN mkdir -p /app/certs && \
    curl -o /app/certs/isrgrootx1.pem https://letsencrypt.org/certs/isrgrootx1.pem.txt
```

### Option 2: Use Cloud Run Secrets
Alternatively, store the certificate in **Google Secret Manager** and mount it as a volume in Cloud Run.

---

## 🗝️ Environment Variables (GCP Secret Manager)

It is highly recommended to use **Google Secret Manager** to securely store your database credentials.

1.  In the GCP Console, go to **Secret Manager**.
2.  Create secrets for:
    -   `TIDB_USER`
    -   `TIDB_PASSWORD`
    -   `TIDB_HOST`
3.  In your `deploy.yml` (or via Cloud Run console), map these secrets to environment variables.

---

## 🌐 Networking

Ensure your TiDB Cloud cluster's **IP Access List** allows connections from Google Cloud.

- **Option A (Development)**: Allow `0.0.0.0/0` (Use this only during setup/testing).
- **Option B (Production)**: Use a **Static IP** for Cloud Run via a **Serverless VPC Access connector** and Cloud NAT.
- **Option C (Dedicated Clusters)**: Use **VPC Peering** between GCP and TiDB Cloud for private, secure networking without exposing to the public internet.
